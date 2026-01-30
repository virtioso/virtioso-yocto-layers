# Use nvidia-oot BPMP module instead of built-in for VM guests
# The nvidia-oot module has hypervisor IPC support
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://bpmp-module.cfg"
SRC_URI += "file://printk-debug.cfg"

# seL4 hypervisor ftrace control - built-in driver that initializes early
SRC_URI += "file://0001-Add-seL4-hyp-ftrace-driver.patch"
SRC_URI += "file://hyp-ftrace.cfg"
