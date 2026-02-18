SUMMARY = "Hypervisor ftrace control helper for VM guests"
DESCRIPTION = "Provides /usr/bin/hyp-ftrace-ctl to arm and dump hyp-ftrace MMIO control."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://hyp-ftrace-ctl"

S = "${UNPACKDIR}"

do_configure() {
    :
}

do_compile() {
    :
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/hyp-ftrace-ctl ${D}${bindir}/hyp-ftrace-ctl
}

FILES:${PN} = "${bindir}/hyp-ftrace-ctl"
RDEPENDS:${PN} = "busybox"
