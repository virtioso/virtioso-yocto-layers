SUMMARY = "Kernel module to manage seL4 guest VMs"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit module

DEPENDS += "virtioso-contracts"

SEL4_VIRT_LOCAL_SRC = "${VIRTIOSO_LOCAL_SOURCES_DIR}/kmod-sel4-virt"
SRC_URI = "file://${SEL4_VIRT_LOCAL_SRC}/;subdir=git"

# Local directory unpack preserves absolute source path under ${WORKDIR}/git.
S = "${WORKDIR}/git/${@d.getVar('SEL4_VIRT_LOCAL_SRC').lstrip('/')}"

python () {
    import os
    src = d.getVar("SEL4_VIRT_LOCAL_SRC")
    if not os.path.isdir(src):
        bb.fatal("Missing required local source directory: %s" % src)
}

python __anonymous () {
    import os
    layers_root = d.getVar("LAYERS_ROOT") or os.path.abspath(os.path.join(d.getVar("TOPDIR"), ".."))
    config_file = os.path.abspath(os.path.join(layers_root, "..", ".config"))
    enabled = "0"
    stats_enabled = "0"
    if os.path.exists(config_file):
        with open(config_file, encoding="utf-8") as config:
            for line in config:
                stripped = line.strip()
                if stripped == "CONFIG_VIRTIO_VM_DEBUG=y":
                    enabled = "1"
                if stripped == "CONFIG_KMOD_SEL4_VIRT_STATS=y":
                    stats_enabled = "1"
    d.setVar("VIRTIO_VM_DEBUG", enabled)
    d.setVar("VIRTIO_VM_STATS", stats_enabled)
}

EXTRA_OEMAKE += "INSTALL_HDR_PATH=${D} CONTRACTS_INCLUDE_DIR=${STAGING_INCDIR} VIRTIO_VM_DEBUG=${VIRTIO_VM_DEBUG} VIRTIO_VM_STATS=${VIRTIO_VM_STATS}"
MODULES_INSTALL_TARGET = "modules_install headers_install"
MODULE_NAME = "sel4_virt"

KERNEL_MODULE_AUTOLOAD += "${MODULE_NAME}"

# Workaround for do_rootfs spdx generation error:
# If the module recipe name is prefixed with 'kernel-module-", the spdx
# generation fails as kernel-module-split.bbclass attempts to create virtual
# package for the actual package i.e.
# 'kernel-module-<name>-<kernel-version>' -> 'kernel-module-<name> but this
# already exists and there is a separate spdx.json.
KERNEL_MODULE_PROVIDE_VIRTUAL = "0"
