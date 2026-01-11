# Use nvidia-oot BPMP module instead of built-in for VM guests
# The nvidia-oot module has hypervisor IPC support
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://bpmp-module.cfg"
SRC_URI += "file://printk-debug.cfg"
