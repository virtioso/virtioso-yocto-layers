# Copyright 2026, Normet
#
# SPDX-License-Identifier: Apache-2.0

require recipes-core/images/core-image-minimal.bb

SUMMARY = "Isengard native Linux image for Orin AGX bare-metal boot"

IMAGE_INSTALL:append = " \
    can-utils \
    iproute2 \
    kmod \
    kernel-modules \
    "

IMAGE_FSTYPES += "cpio.gz"

export IMAGE_BASENAME = "isengard-image-native"

# Remove the .rootfs suffix so the deployed cpio matches the filename that
# do_bundle_initramfs looks for (INITRAMFS_IMAGE_NAME = image-machine, no suffix).
IMAGE_NAME_SUFFIX = ""
