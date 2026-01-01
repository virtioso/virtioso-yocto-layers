SUMMARY = "Minimal init for seL4 VM testing"
DESCRIPTION = "Tiny init script that mounts filesystems and starts a shell"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "file://init"

# Only need busybox - provides sh, mount, mknod, cat, uname, hostname
RDEPENDS:${PN} = "busybox"

do_install() {
    install -m 0755 ${UNPACKDIR}/init ${D}/init
}

FILES:${PN} = "/init"
