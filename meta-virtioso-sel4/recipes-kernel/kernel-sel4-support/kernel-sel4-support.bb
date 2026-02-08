FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SUMMARY = "seL4 linux kernel support headers/libs/etc"
SECTION = "kernel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d9916c659cd59cc3dea0299cd895bf0e"

SEL4_KERNEL_SUPPORT_LOCAL_SRC = "${VIRTIOSO_LOCAL_SOURCES_DIR}/sel4-linux-kernel-support"
SRC_URI = "file://${SEL4_KERNEL_SUPPORT_LOCAL_SRC}/;subdir=git"

# Local directory unpack preserves absolute source path under ${WORKDIR}/git.
S = "${WORKDIR}/git/${@d.getVar('SEL4_KERNEL_SUPPORT_LOCAL_SRC').lstrip('/')}"

python () {
    import os
    src = d.getVar("SEL4_KERNEL_SUPPORT_LOCAL_SRC")
    if not os.path.isdir(src):
        bb.fatal("Missing required local source directory: %s" % src)
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install:append() {
    case ${TARGET_ARCH} in
        aarch64) ;;
        *) bbfatal "Currently unsupported arch: ${TARGET_ARCH}, please add" ;;
    esac

    install -d ${D}${includedir}/sel4/
    install -m 0644 ${S}/include/*.h ${D}${includedir}/sel4/
    install -m 0644 ${S}/include/arch/${TARGET_ARCH}/*.h ${D}${includedir}/sel4/
}
