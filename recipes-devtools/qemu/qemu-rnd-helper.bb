# Copyright 2022, Technology Innovation Institute
#
# SPDX-License-Identifier: Apache-2.0

SUMMARY = "QEMU R&D helper"
DESCRIPTION = "Helper utilities for QEMU"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

SRC_URI = "\
    file://qemu-rnd-helper \
    file://qemu-rnd-helper-gui \
    "

S = "${UNPACKDIR}"

do_configure() {
    :
}

do_compile() {
    :
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 \
        ${UNPACKDIR}/qemu-rnd-helper \
        ${UNPACKDIR}/qemu-rnd-helper-gui \
        ${D}${bindir}
}

PACKAGES = "${PN} ${PN}-gui"

FILES:${PN} = "${bindir}/qemu-rnd-helper"
RDEPENDS:${PN} = "\
    busybox \
    qemu \
    "

FILES:${PN}-gui = "${bindir}/qemu-rnd-helper-gui"
RDEPENDS:${PN}-gui = "\
    ${PN} (= ${EXTENDPKGV}) \
    xdpyinfo \
    "
