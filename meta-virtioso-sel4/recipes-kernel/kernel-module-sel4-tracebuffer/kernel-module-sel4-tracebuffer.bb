SUMMARY = "Kernel module to manage canonical vio-trace guest tracebuffer access"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS += "kernel-sel4-support virtioso-contracts"

inherit module

VIO_TRACE_LOCAL_SRC = "${VIRTIOSO_LOCAL_SOURCES_DIR}/kmod-vio-trace"
SRC_URI = "file://${VIO_TRACE_LOCAL_SRC}/;subdir=git"

# Local directory unpack preserves absolute source path under ${WORKDIR}/git.
S = "${WORKDIR}/git/${@d.getVar('VIO_TRACE_LOCAL_SRC').lstrip('/')}"

python () {
    import os
    src = d.getVar("VIO_TRACE_LOCAL_SRC")
    if not os.path.isdir(src):
        bb.fatal("Missing required local source directory: %s" % src)
}

EXTRA_OEMAKE += "CONTRACTS_INCLUDE_DIR=${STAGING_INCDIR} EXTRA_CFLAGS=-I${STAGING_DIR_TARGET}${includedir}"

MODULES_INSTALL_TARGET = "modules_install"
MODULE_NAME = "vio_trace"
KERNEL_MODULE_AUTOLOAD += "${MODULE_NAME}"

# Keep canonical install name while this recipe PN/path is still legacy.
RPROVIDES:${PN} += "kernel-module-vio-trace"
RREPLACES:${PN} += "kernel-module-vio-trace"
RCONFLICTS:${PN} += "kernel-module-vio-trace"

# Transitional compatibility for older manifests/install lists.
RPROVIDES:${PN} += "kernel-module-sel4-tracebuffer"
RREPLACES:${PN} += "kernel-module-sel4-tracebuffer"
RCONFLICTS:${PN} += "kernel-module-sel4-tracebuffer"

# Workaround for do_rootfs spdx generation error:
# If the module recipe name is prefixed with 'kernel-module-", the spdx
# generation fails as kernel-module-split.bbclass attempts to create virtual
# package for the actual package i.e.
# 'kernel-module-<name>-<kernel-version>' -> 'kernel-module-<name> but this
# already exists and there is a separate spdx.json.
KERNEL_MODULE_PROVIDE_VIRTUAL = "0"
