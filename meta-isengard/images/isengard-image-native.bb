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
    isengard-snapshot-watch \
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

fix_network_interfaces() {
    # busybox ifup calls udhcpc without -s; compiled-in default is
    # /etc/udhcpc.d/default.script which doesn't exist — pass script explicitly
    printf 'auto lo\niface lo inet loopback\nauto eth0\niface eth0 inet dhcp\n\tscript /etc/udhcpc.d/50default\n' \
        > ${IMAGE_ROOTFS}/etc/network/interfaces
}
ROOTFS_POSTPROCESS_COMMAND:append = " fix_network_interfaces;"

fix_udhcpc_mask() {
    # udhcpc sets $subnet (dotted) not $mask (CIDR); ip addr add accepts both
    sed -i 's|\$ip/\$mask|\$ip/\$subnet|g' ${IMAGE_ROOTFS}/etc/udhcpc.d/50default
}
ROOTFS_POSTPROCESS_COMMAND:append = " fix_udhcpc_mask;"

export IMAGE_BASENAME = "isengard-rootfs"
