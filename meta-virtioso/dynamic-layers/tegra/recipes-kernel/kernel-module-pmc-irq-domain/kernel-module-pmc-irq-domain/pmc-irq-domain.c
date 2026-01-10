// SPDX-License-Identifier: GPL-2.0-only
/*
 * Dummy PMC IRQ domain driver for seL4 CAmkES VM
 *
 * This module creates a minimal IRQ domain that allows the Tegra GPIO driver
 * to operate in hierarchical mode. In hierarchical mode, the GPIO driver
 * properly translates DT GPIO numbers (e.g., TEGRA234_MAIN_GPIO(Y,3) = 147)
 * to linear hwirq values (e.g., 125).
 *
 * Without PMC, GPIO uses simple IRQ domain with 1:1 xlate, causing hwirq
 * mismatch between IRQ request time and interrupt delivery time.
 *
 * This module does NOT implement actual PMC wake functionality - it simply
 * disconnects from the IRQ hierarchy, allowing GPIO's parent_handler to
 * handle interrupts directly via the shared GIC SPIs.
 */

#include <linux/module.h>
#include <linux/platform_device.h>
#include <linux/of.h>
#include <linux/of_irq.h>
#include <linux/irqdomain.h>
#include <linux/irq.h>

struct dummy_pmc {
	struct device *dev;
	struct irq_domain *domain;
};

static int dummy_pmc_irq_translate(struct irq_domain *domain,
				   struct irq_fwspec *fwspec,
				   unsigned long *hwirq,
				   unsigned int *type)
{
	/* Accept both 2-cell and 3-cell formats from GPIO driver */
	if (fwspec->param_count < 2)
		return -EINVAL;

	*hwirq = fwspec->param[0];
	*type = fwspec->param[1];

	return 0;
}

static int dummy_pmc_irq_alloc(struct irq_domain *domain, unsigned int virq,
			       unsigned int num_irqs, void *data)
{
	/*
	 * We don't actually wire up to the GIC hierarchy.
	 * GPIO's parent_handler (tegra186_gpio_irq) handles the actual
	 * hardware interrupts via shared GIC SPIs and demultiplexes
	 * to the correct GPIO hwirq.
	 *
	 * Just disconnect from hierarchy like real PMC does for non-wake events.
	 */
	return irq_domain_disconnect_hierarchy(domain, virq);
}

static const struct irq_domain_ops dummy_pmc_irq_domain_ops = {
	.translate = dummy_pmc_irq_translate,
	.alloc = dummy_pmc_irq_alloc,
};

static int dummy_pmc_probe(struct platform_device *pdev)
{
	struct dummy_pmc *pmc;
	struct device_node *np = pdev->dev.of_node;
	struct irq_domain *parent_domain;
	struct device_node *parent_np;

	pmc = devm_kzalloc(&pdev->dev, sizeof(*pmc), GFP_KERNEL);
	if (!pmc)
		return -ENOMEM;

	pmc->dev = &pdev->dev;

	/* Find GIC as parent domain */
	parent_np = of_irq_find_parent(np);
	if (parent_np) {
		parent_domain = irq_find_host(parent_np);
		of_node_put(parent_np);
	} else {
		parent_domain = NULL;
	}

	/* Create hierarchical IRQ domain */
	pmc->domain = irq_domain_add_hierarchy(parent_domain, 0, 0,
					       np, &dummy_pmc_irq_domain_ops,
					       pmc);
	if (!pmc->domain) {
		dev_err(&pdev->dev, "failed to create IRQ domain\n");
		return -ENOMEM;
	}

	platform_set_drvdata(pdev, pmc);

	dev_info(&pdev->dev, "dummy PMC IRQ domain registered (parent: %s)\n",
		 parent_domain ? parent_domain->name : "none");

	return 0;
}

static int dummy_pmc_remove(struct platform_device *pdev)
{
	struct dummy_pmc *pmc = platform_get_drvdata(pdev);

	if (pmc->domain)
		irq_domain_remove(pmc->domain);

	return 0;
}

static const struct of_device_id dummy_pmc_match[] = {
	{ .compatible = "nvidia,tegra234-pmc" },
	{ .compatible = "nvidia,tegra194-pmc" },
	{ .compatible = "nvidia,tegra186-pmc" },
	{ }
};
MODULE_DEVICE_TABLE(of, dummy_pmc_match);

static struct platform_driver dummy_pmc_driver = {
	.driver = {
		.name = "tegra-pmc-irq-domain",
		.of_match_table = dummy_pmc_match,
	},
	.probe = dummy_pmc_probe,
	.remove = dummy_pmc_remove,
};
module_platform_driver(dummy_pmc_driver);

MODULE_AUTHOR("TII");
MODULE_DESCRIPTION("Dummy Tegra PMC IRQ domain for seL4 CAmkES VM");
MODULE_LICENSE("GPL");
