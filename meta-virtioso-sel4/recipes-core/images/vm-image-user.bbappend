# SPDX-License-Identifier: Apache-2.0

SRC_URI += "file://virtioso-diag-init"

SERIAL_CONSOLES = "115200;hvc0"
SERIAL_CONSOLES:vm-jetson-agx-orin = "115200;ttyAMA0"

install_orin_user_diag_init() {
    install -d ${IMAGE_ROOTFS}${base_sbindir}
    install -m 0755 ${UNPACKDIR}/virtioso-diag-init \
        ${IMAGE_ROOTFS}${base_sbindir}/virtioso-diag-init
}

set_orin_user_console_getty() {
    sed -i \
        -e 's|^hvc0:12345:respawn:/usr/sbin/ttyrun hvc0 /bin/start_getty 115200 hvc0 vt102$|AMA0:12345:respawn:/usr/sbin/ttyrun ttyAMA0 /bin/start_getty 115200 ttyAMA0 vt102|' \
        ${IMAGE_ROOTFS}${sysconfdir}/inittab
}

ROOTFS_POSTPROCESS_COMMAND:append:vm-jetson-agx-orin = " \
    install_orin_user_diag_init; \
    set_orin_user_console_getty; \
"
