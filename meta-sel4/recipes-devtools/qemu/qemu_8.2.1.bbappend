FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PATCHTOOL = "git"

SRC_URI += " \
    file://0001-Do-not-load-DTB-on-seL4-guest.patch \
    file://0001-Add-meson-subprojects.patch \
    "

