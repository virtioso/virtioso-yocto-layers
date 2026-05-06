# SPDX-License-Identifier: Apache-2.0

# vm-image-driver doubles as the host-side QEMU root disk for qemu-backed
# bring-up. Keep the existing nested guest-image installation behavior, but
# also emit the driver image itself in a deployable qcow2 format.
inherit vm-guest-image

IMAGE_INSTALL += "\
    kernel-module-sel4-virt \
    kernel-module-vio-trace \
    "

set_driver_console_getty() {
    sed -i \
        -e 's|^hvc0:12345:respawn:/usr/sbin/ttyrun hvc0 /bin/start_getty 115200 hvc0 vt102$|TCU0:12345:respawn:/usr/sbin/ttyrun ttyTCU0 /bin/start_getty 115200 ttyTCU0 vt102|' \
        ${IMAGE_ROOTFS}${sysconfdir}/inittab
}

set_driver_console_getty:vm-jetson-agx-orin() {
    sed -i \
        -e 's|^hvc0:12345:respawn:/usr/sbin/ttyrun hvc0 /bin/start_getty 115200 hvc0 vt102$|AMA0:12345:respawn:/usr/sbin/ttyrun ttyAMA0 /bin/start_getty 115200 ttyAMA0 vt102|' \
        ${IMAGE_ROOTFS}${sysconfdir}/inittab
}

set_x86_driver_console_getty() {
    # sysvinit-inittab is machine-scoped, so qemux86-64's ttyS1 getty is
    # shared across images. The outer QEMU-backed driver-vm only exposes ttyS0,
    # so normalize the driver image rootfs here rather than splitting the
    # machine or changing inner guest console policy.
    sed -i \
        -e '/^S1:12345:respawn:\/usr\/sbin\/ttyrun ttyS1 \/bin\/start_getty 115200 ttyS1 vt102$/d' \
        ${IMAGE_ROOTFS}${sysconfdir}/inittab
}

ROOTFS_POSTPROCESS_COMMAND:append = " \
    set_driver_console_getty; \
"

ROOTFS_POSTPROCESS_COMMAND:append:qemux86-64 = " \
    set_x86_driver_console_getty; \
"
