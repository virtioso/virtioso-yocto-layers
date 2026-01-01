FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

LICENSE = "CLOSED"

S = "${UNPACKDIR}"

SRC_URI += " \
    file://collect-traces.sh \
"

RDEPENDS:${PN} += " \
    sel4-trace-support \
"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/collect-traces.sh ${D}${bindir}
}

FILES:${PN} = "\
    ${bindir} \
"
