FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Include virtio driver configuration for VM guests
include recipes-kernel/linux/linux-virtio.inc
