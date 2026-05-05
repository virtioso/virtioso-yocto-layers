# Copyright 2026, Normet
#
# SPDX-License-Identifier: Apache-2.0

SUMMARY = "Isengard minimal initramfs: loop-mounts rootfs from stock Jetson Linux eMMC"

# Some BSPs use IMAGE_FSTYPES_<machine override> which would override
# an assignment to IMAGE_FSTYPES so we need anon python
python () {
    d.setVar("IMAGE_FSTYPES", d.getVar("INITRAMFS_FSTYPES"))
}

inherit image

# avoid circular dependencies
EXTRA_IMAGEDEPENDS = ""
KERNELDEPMODDEPEND = ""

IMAGE_INSTALL = " \
    isengard-initramfs-init \
"

IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""
FEED_DEPLOYDIR_BASE_URI = ""
LDCONFIGDEPEND = ""
IMAGE_ROOTFS_EXTRA_SPACE = "0"
ROOTFS_BOOTSTRAP_INSTALL = ""

export IMAGE_BASENAME = "isengard-initramfs"
IMAGE_NAME_SUFFIX = ""

create_console_devices() {
    mkdir -p ${IMAGE_ROOTFS}/dev
    mknod -m 600 ${IMAGE_ROOTFS}/dev/console c 5 1
    mknod -m 666 ${IMAGE_ROOTFS}/dev/null c 1 3
    mknod -m 666 ${IMAGE_ROOTFS}/dev/tty c 5 0
}
ROOTFS_POSTPROCESS_COMMAND:append = " create_console_devices;"
