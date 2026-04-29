# SPDX-License-Identifier: Apache-2.0

SERIAL_CONSOLES = "115200;hvc0"
SERIAL_CONSOLES:vm-jetson-agx-orin = "115200;ttyAMA0"

set_orin_user_console_getty() {
    sed -i \
        -e 's|^hvc0:12345:respawn:/usr/sbin/ttyrun hvc0 /bin/start_getty 115200 hvc0 vt102$|AMA0:12345:respawn:/usr/sbin/ttyrun ttyAMA0 /bin/start_getty 115200 ttyAMA0 vt102|' \
        ${IMAGE_ROOTFS}${sysconfdir}/inittab
}

set_orin_user_9p_noauto() {
    sed -i \
        -e 's|^shared /mnt/shared    9p      trans=virtio,version=9p2000.L   0 0$|shared /mnt/shared    9p      noauto,trans=virtio,version=9p2000.L   0 0|' \
        ${IMAGE_ROOTFS}${sysconfdir}/fstab
}

ROOTFS_POSTPROCESS_COMMAND:append:vm-jetson-agx-orin = " \
    set_orin_user_console_getty; \
    set_orin_user_9p_noauto; \
"
