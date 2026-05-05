# Copyright 2026, Normet
#
# SPDX-License-Identifier: Apache-2.0

inherit externalsrc

EXTERNALSRC = "${TOPDIR}/../../sources/isengard-app"
EXTERNALSRC_BUILD = "${EXTERNALSRC}"

LICENSE = "CLOSED"
SUMMARY = "Isengard application layer: CANopenNODE service + FUSE snapshot export"

DEPENDS = "fuse3"

EXTRA_OEMAKE = "CC='${CC}' CFLAGS='${CFLAGS}' LDFLAGS='${LDFLAGS}'"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://isengard-demo-start \
    file://isengard-demo-watch \
"

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${EXTERNALSRC}/build/isengard_app   ${D}${bindir}/isengard_app
    install -m 0755 ${EXTERNALSRC}/build/isengard_objfs ${D}${bindir}/isengard_objfs
    install -m 0755 ${UNPACKDIR}/isengard-demo-start    ${D}${bindir}/isengard-demo-start
    install -m 0755 ${UNPACKDIR}/isengard-demo-watch    ${D}${bindir}/isengard-demo-watch
}

RDEPENDS:${PN} = "fuse3 kernel-module-fuse can-utils"
