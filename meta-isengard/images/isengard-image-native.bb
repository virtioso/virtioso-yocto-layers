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
    isengard-app \
    fuse3 \
    kernel-module-vcan \
    kernel-module-fuse \
    "

EXTRA_IMAGE_FEATURES += "ssh-server-dropbear allow-root-login empty-root-password"

install_authorized_keys() {
    if [ -f "${HOME}/.ssh/id_rsa.pub" ]; then
        install -d -m 0700 ${IMAGE_ROOTFS}/root/.ssh
        install -m 0600 ${HOME}/.ssh/id_rsa.pub ${IMAGE_ROOTFS}/root/.ssh/authorized_keys
    fi
}
ROOTFS_POSTPROCESS_COMMAND:append = " install_authorized_keys;"

export IMAGE_BASENAME = "isengard-rootfs"
