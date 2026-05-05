# Copyright 2026, Normet
#
# SPDX-License-Identifier: Apache-2.0

require recipes-core/images/core-image-minimal.bb

SUMMARY = "Isengard native Linux rootfs for Orin AGX bare-metal boot"

IMAGE_INSTALL:append = " \
    can-utils \
    iproute2 \
    kmod \
    kernel-modules \
    "

export IMAGE_BASENAME = "isengard-rootfs"
