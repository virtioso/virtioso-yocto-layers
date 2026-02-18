FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://dd.cfg \
    file://devmem.cfg \
    file://init-console.cfg \
    file://losetup-switchroot.cfg \
    file://xxd.cfg \
    file://udhcpc.cfg \
"
