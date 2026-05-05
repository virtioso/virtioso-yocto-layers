# Copyright 2026, Normet
#
# SPDX-License-Identifier: Apache-2.0

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

LICENSE = "CLOSED"

SUMMARY = "Isengard initramfs init: loop-mount rootfs from stock Jetson Linux eMMC"

SRC_URI += " \
    file://init \
"

RDEPENDS:${PN} += " \
    busybox \
"

do_install:append() {
    install -m 0755 ${UNPACKDIR}/init ${D}/init
    sed -i 's#@DEFAULT_ROOTFS_IMAGE@#var/lib/virtioso-vm-images/isengard-rootfs.ext4#g' ${D}/init

    install -d ${D}/proc
    install -d ${D}/sys
    install -d ${D}/dev
    install -d ${D}/tmp
    install -d ${D}/run
    install -d ${D}/newroot
    install -d ${D}/mnt/emmc
}

FILES:${PN} = "/init /proc /sys /dev /tmp /run /newroot /mnt /etc"
