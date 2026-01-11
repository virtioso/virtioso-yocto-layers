# Fixes for nvethernet driver in seL4 VM guests
#
# 0001: Fix MGBE power domain sequencing - the driver accesses MAC registers
#       before enabling runtime PM, causing hangs when power domain is not
#       pre-initialized by the hypervisor.
#
# 0002: Fix NULL pointer bug in of_get_mac_address() call - the driver passed
#       a NULL pointer instead of the mac_addr buffer, causing ethernet
#       initialization failures.

FILESEXTRAPATHS:prepend := "${THISDIR}:"

SRC_URI += "file://0002-nvethernet-Fix-NULL-pointer-in-of_get_mac_address.patch"
