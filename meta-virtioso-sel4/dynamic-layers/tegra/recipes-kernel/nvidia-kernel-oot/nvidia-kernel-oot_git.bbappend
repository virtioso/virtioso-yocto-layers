FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-nvethernet-Fix-MGBE-power-domain-sequencing-in-probe.patch;patchdir=nvidia-oot"

