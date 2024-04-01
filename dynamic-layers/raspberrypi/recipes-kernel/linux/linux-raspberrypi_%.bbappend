FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://v3d.cfg \
"

include recipes-kernel/linux/linux-virtio.inc
