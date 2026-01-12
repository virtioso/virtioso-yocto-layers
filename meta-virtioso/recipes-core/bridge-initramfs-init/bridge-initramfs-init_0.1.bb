FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

LICENSE = "CLOSED"

SUMMARY = "Unified initramfs for seL4 VM with NFS primary, eMMC fallback"
DESCRIPTION = "Init script with platform detection, TAP bridge setup, \
NFS boot with eMMC fallback, and platform-specific driver loading."

SRC_URI += " \
    file://init \
"

RDEPENDS:${PN} += " \
    busybox \
    bridge-utils \
    tunctl \
    kernel-module-tun \
"

# Platform-specific kernel modules for Orin AGX
# Core drivers (currently used)
RDEPENDS:${PN}:append:vm-jetson-agx-orin = " \
    kernel-module-pmc-irq-domain \
    nv-kernel-module-tegra-bpmp \
    kernel-module-phy-tegra194-p2u \
    nv-kernel-module-nvpps \
    nv-kernel-module-nvethernet \
"

# Additional Tegra modules (from tegra-minimal-initramfs)
# Not used yet, but available for future device passthrough
RDEPENDS:${PN}:append:vm-jetson-agx-orin = " \
    tegra-firmware-xusb \
    kernel-module-nvme \
    kernel-module-pcie-tegra194 \
    kernel-module-tegra-xudc \
    kernel-module-ucsi-ccg \
"

do_install:append() {
    install -m 0755 ${UNPACKDIR}/init ${D}/init

    # Create mount point directories
    install -d ${D}/proc
    install -d ${D}/sys
    install -d ${D}/dev
    install -d ${D}/tmp
    install -d ${D}/run
    install -d ${D}/newroot
    install -d ${D}/mnt/emmc
    install -d ${D}/etc/udhcpc.d
}

FILES:${PN} = "/init /proc /sys /dev /tmp /run /newroot /mnt /etc"
