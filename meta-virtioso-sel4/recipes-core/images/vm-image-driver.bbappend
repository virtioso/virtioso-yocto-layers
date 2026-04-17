# SPDX-License-Identifier: Apache-2.0

# vm-image-driver doubles as the host-side QEMU root disk for qemu-backed
# bring-up. Keep the existing nested guest-image installation behavior, but
# also emit the driver image itself in a deployable qcow2 format.
inherit vm-guest-image

IMAGE_INSTALL += "\
    kernel-module-sel4-virt \
    "

set_driver_console_getty() {
    sed -i \
        -e 's|^hvc0:12345:respawn:/usr/sbin/ttyrun hvc0 /bin/start_getty 115200 hvc0 vt102$|TCU0:12345:respawn:/usr/sbin/ttyrun ttyTCU0 /bin/start_getty 115200 ttyTCU0 vt102|' \
        ${IMAGE_ROOTFS}${sysconfdir}/inittab
}

ROOTFS_POSTPROCESS_COMMAND:append = " \
    set_driver_console_getty; \
"
