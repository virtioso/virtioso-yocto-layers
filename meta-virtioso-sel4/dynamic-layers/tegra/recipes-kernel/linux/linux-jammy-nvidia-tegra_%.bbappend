# Use nvidia-oot BPMP module instead of built-in for VM guests
# The nvidia-oot module has hypervisor IPC support
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://bpmp-module.cfg"
SRC_URI += "file://printk-debug.cfg"

# seL4 hypervisor ftrace control - built-in driver that initializes early
SRC_URI += "file://0001-platform-sel4-Add-hypervisor-ftrace-control-driver.patch"
SRC_URI += "file://hyp-ftrace.cfg"

# Debug prints for BPMP/HSP
SRC_URI += "file://0002-firmware-tegra-Add-debug-prints-for-BPMP-and-HSP.patch"

# Enable seL4 platform drivers in defconfig
SRC_URI += "file://0003-arm64-configs-Enable-seL4-hypervisor-platform-driver.patch"
