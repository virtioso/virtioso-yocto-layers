FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS:append:raspberrypi4 = " \
    bootscripts \
    "

IMAGE_FSTYPES:append:raspberrypi4 = " \
    rpi-sdimg \
    "

BOOT_SPACE:raspberrypi4 = "65536"
