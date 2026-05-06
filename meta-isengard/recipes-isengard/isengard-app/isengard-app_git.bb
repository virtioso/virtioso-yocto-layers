# Copyright 2026, Normet
#
# SPDX-License-Identifier: Apache-2.0

inherit externalsrc pkgconfig

EXTERNALSRC = "${TOPDIR}/../../sources/isengard-core/isengard-app"
EXTERNALSRC_BUILD = "${EXTERNALSRC}"

LICENSE = "CLOSED"
SUMMARY = "Isengard application layer: CANopenNODE service + FUSE snapshot export"

DEPENDS = "fuse3"

EXTRA_OEMAKE = "CC='${CC}' EXTRA_CFLAGS='${CFLAGS}' LDFLAGS='${LDFLAGS}'"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://isengard-demo-start \
    file://isengard-demo-watch \
    file://isengard-demo-watch-rust \
"

do_compile() {
    F3C=$(pkg-config --cflags fuse3)
    F3L=$(pkg-config --libs fuse3)
    oe_runmake FUSE3_CFLAGS="${F3C}" FUSE3_LIBS="${F3L}"
    oe_runmake snapshot-demo FUSE3_CFLAGS="${F3C}" FUSE3_LIBS="${F3L}"
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${EXTERNALSRC}/build/isengard_app                   ${D}${bindir}/isengard_app
    install -m 0755 ${EXTERNALSRC}/build/isengard_objfs                 ${D}${bindir}/isengard_objfs
    install -m 0755 ${EXTERNALSRC}/build/isengard_snapshot_memfd_demo   ${D}${bindir}/isengard_snapshot_memfd_demo
    install -m 0755 ${UNPACKDIR}/isengard-demo-start        ${D}${bindir}/isengard-demo-start
    install -m 0755 ${UNPACKDIR}/isengard-demo-watch        ${D}${bindir}/isengard-demo-watch
    install -m 0755 ${UNPACKDIR}/isengard-demo-watch-rust   ${D}${bindir}/isengard-demo-watch-rust
    # isengard_snapshot_watch is installed by the isengard-snapshot-watch recipe
}

RDEPENDS:${PN} = "fuse3 kernel-module-fuse can-utils"
