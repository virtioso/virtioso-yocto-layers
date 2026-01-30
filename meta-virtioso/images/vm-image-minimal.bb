SUMMARY = "Minimal VM image for seL4 testing"
DESCRIPTION = "Tiny initramfs with just busybox for VM boot testing. \
No networking, no systemd, no extras - just enough to verify the VM boots."

# Output as cpio.gz for use as initrd
INITRAMFS_FSTYPES = "cpio.gz"

python () {
    d.setVar("IMAGE_FSTYPES", d.getVar("INITRAMFS_FSTYPES"))
}

inherit image

# Device nodes for console - kernel needs /dev/console before running init
# Create via ROOTFS_POSTPROCESS_COMMAND since IMAGE_DEVICE_TABLES has issues
create_console_devices() {
    mkdir -p ${IMAGE_ROOTFS}/dev
    mknod -m 600 ${IMAGE_ROOTFS}/dev/console c 5 1
    mknod -m 666 ${IMAGE_ROOTFS}/dev/null c 1 3
    mknod -m 666 ${IMAGE_ROOTFS}/dev/tty c 5 0
    mknod -m 666 ${IMAGE_ROOTFS}/dev/ttyAMA0 c 204 64
}
ROOTFS_POSTPROCESS_COMMAND:append = " create_console_devices;"

# Avoid circular dependencies
EXTRA_IMAGEDEPENDS = ""

# Just our minimal init and busybox, plus essential modules for Orin AGX
# tegra-bpmp MUST be loaded first - other drivers depend on it for clocks/resets
# pmc-irq-domain creates IRQ domain so GPIO driver uses hierarchical mode
# hyp-ftrace is built into kernel (CONFIG_SEL4_HYP_FTRACE) for early init
# devmem2 for reading hardware registers for debugging
IMAGE_INSTALL = "minimal-init \
    kernel-module-pmc-irq-domain \
    nv-kernel-module-tegra-bpmp \
    kernel-module-phy-tegra194-p2u \
    nv-kernel-module-nvethernet \
    nv-kernel-module-nvpps \
    devmem2 \
"

# No extra features
IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""

# Minimize size
FEED_DEPLOYDIR_BASE_URI = ""
LDCONFIGDEPEND = ""
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Disable runtime dependency on run-postinsts
ROOTFS_BOOTSTRAP_INSTALL = ""

# Strip everything possible
INHIBIT_PACKAGE_STRIP = "0"
