SUMMARY = "Minimal init for seL4 VM with eMMC rootfs support"
DESCRIPTION = "Init script that mounts eMMC, loop-mounts a rootfs image, and switch_roots to it"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "file://init"

# busybox provides: sh, mount, mknod, cat, uname, setsid, cttyhack, losetup, switch_root
# Ensure busybox is configured with CONFIG_LOSETUP and CONFIG_SWITCH_ROOT
RDEPENDS:${PN} = "busybox"

do_install() {
    install -m 0755 ${UNPACKDIR}/init ${D}/init

    # Create mount point directories
    install -d ${D}/proc
    install -d ${D}/sys
    install -d ${D}/dev
    install -d ${D}/tmp
    install -d ${D}/run
    install -d ${D}/mnt/emmc
    install -d ${D}/mnt/rootfs
}

FILES:${PN} = "/init /proc /sys /dev /tmp /run /mnt"
