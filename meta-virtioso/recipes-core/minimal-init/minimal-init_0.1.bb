SUMMARY = "Minimal init for seL4 VM testing"
DESCRIPTION = "Tiny init script that mounts filesystems and starts a shell"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "file://init"

# Only need busybox - provides sh, mount, mknod, cat, uname, hostname, setsid, cttyhack
RDEPENDS:${PN} = "busybox"

do_install() {
    install -m 0755 ${UNPACKDIR}/init ${D}/init

    # Create mount point directories
    install -d ${D}/dev
    install -d ${D}/proc
    install -d ${D}/sys
    install -d ${D}/tmp
    install -d ${D}/run

    # Create /dev/console - kernel needs this before init runs
    mknod -m 600 ${D}/dev/console c 5 1
    mknod -m 666 ${D}/dev/null c 1 3
}

FILES:${PN} = "/init /dev /proc /sys /tmp /run"
