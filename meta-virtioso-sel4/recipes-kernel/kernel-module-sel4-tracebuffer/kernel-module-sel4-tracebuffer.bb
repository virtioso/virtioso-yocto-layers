SUMMARY = "Kernel module to manage canonical vio-trace guest tracebuffer access"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS += " kernel-sel4-support "

inherit module externalsrc

EXTERNALSRC = "/home/hlyytine/tii-sel4/sources/kmod-vio-trace"
EXTERNALSRC_BUILD = "${EXTERNALSRC}"

EXTRA_OEMAKE += "EXTRA_CFLAGS=-I${STAGING_DIR_TARGET}${includedir}"

MODULES_INSTALL_TARGET = "modules_install"
MODULE_NAME = "vio-trace"
KERNEL_MODULE_AUTOLOAD += "${MODULE_NAME}"
RPROVIDES:kernel-module-${MODULE_NAME} += "kernel-module-sel4-tracebuffer"
RREPLACES:kernel-module-${MODULE_NAME} += "kernel-module-sel4-tracebuffer"
RCONFLICTS:kernel-module-${MODULE_NAME} += "kernel-module-sel4-tracebuffer"

# Workaround for do_rootfs spdx generation error:
# If the module recipe name is prefixed with 'kernel-module-", the spdx
# generation fails as kernel-module-split.bbclass attempts to create virtual
# package for the actual package i.e.
# 'kernel-module-<name>-<kernel-version>' -> 'kernel-module-<name> but this
# already exists and there is a separate spdx.json.
KERNEL_MODULE_PROVIDE_VIRTUAL = "0"
