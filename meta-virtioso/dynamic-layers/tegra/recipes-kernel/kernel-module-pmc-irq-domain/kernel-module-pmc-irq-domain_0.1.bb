SUMMARY = "Dummy PMC IRQ domain driver for seL4 CAmkES VM"
DESCRIPTION = "Creates minimal IRQ domain so Tegra GPIO driver uses hierarchical mode with proper hwirq translation"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE;md5=38fdb46a69013e6a9ce5642709691fc3"

inherit module

SRC_URI = "file://pmc-irq-domain.c \
           file://Makefile \
           file://LICENSE \
          "

S = "${UNPACKDIR}"

# Ensure module is loaded automatically
KERNEL_MODULE_AUTOLOAD += "pmc-irq-domain"
