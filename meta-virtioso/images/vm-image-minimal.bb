SUMMARY = "Minimal VM image for seL4 testing"
DESCRIPTION = "Tiny initramfs with just busybox for VM boot testing. \
No networking, no systemd, no extras - just enough to verify the VM boots."

# Output as cpio.gz for use as initrd
INITRAMFS_FSTYPES = "cpio.gz"

python () {
    d.setVar("IMAGE_FSTYPES", d.getVar("INITRAMFS_FSTYPES"))
}

inherit image

# Avoid circular dependencies
EXTRA_IMAGEDEPENDS = ""
KERNELDEPMODDEPEND = ""

# Just our minimal init and busybox
IMAGE_INSTALL = "minimal-init"

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
