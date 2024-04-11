FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SUMMARY = ""
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit cmake

SRC_URI = "git://github.com/tiiuae/sel4-trace-support.git;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

EXTRA_OECMAKE = ""
