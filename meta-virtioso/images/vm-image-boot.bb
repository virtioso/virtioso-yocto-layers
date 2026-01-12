SUMMARY = "Unified initramfs for seL4 VM"
DESCRIPTION = "This image provides platform-aware boot with NFS primary, eMMC fallback, \
TAP bridge setup, and platform-specific driver loading."

INITRAMFS_FSTYPES += "cpio.gz.u-boot"

# Some BSPs use IMAGE_FSTYPES_<machine override> which would override
# an assignment to IMAGE_FSTYPES so we need anon python
python () {
    d.setVar("IMAGE_FSTYPES", d.getVar("INITRAMFS_FSTYPES"))
}

inherit image

# avoid circular dependencies
EXTRA_IMAGEDEPENDS = ""
KERNELDEPMODDEPEND = ""

IMAGE_INSTALL = " \
    bridge-initramfs-init \
"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

IMAGE_LINGUAS = ""

FEED_DEPLOYDIR_BASE_URI = ""
LDCONFIGDEPEND = ""
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# disable runtime dependency on run-postinsts -> update-rc.d
ROOTFS_BOOTSTRAP_INSTALL = ""

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
