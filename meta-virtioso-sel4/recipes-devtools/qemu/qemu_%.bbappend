# QEMU with seL4 accelerator support
#
# This bbappend enables the seL4 QEMU accelerator from virtioso-qemu.
# The accelerator allows QEMU to provide virtio device backends for VMs
# running on the seL4 hypervisor.
#
# Source: https://github.com/virtioso/virtioso-qemu (branch: virtioso/9.2.0)
#
# QEMU 9.x API Changes:
# ---------------------
# In QEMU 9.x, the NIC initialization API changed. NICs are no longer
# auto-created from nb_nics/nd_table. Instead, use explicit -device options:
#
# Old (QEMU 8.x):
#   qemu-system-aarch64 -net nic,model=virtio -net user
#
# New (QEMU 9.x):
#   qemu-system-aarch64 -device virtio-net-pci,netdev=net0 -netdev user,id=net0
#
# The seL4 virt machine has been updated to work with this change.

QEMU_TARGETS = "aarch64"

QEMU_SEL4_DEPS = ""
QEMU_SEL4_DEPS:class-target = "kernel-module-sel4-virt"
DEPENDS += "${QEMU_SEL4_DEPS}"

PACKAGECONFIG[sel4] = "--enable-sel4,--disable-sel4,,"
PACKAGECONFIG:class-target = " \
    fdt \
    pie \
    kvm \
    sel4 \
    virtfs \
    sdl \
    vhost \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virglrenderer epoxy', '', d)} \
"

SRC_URI:remove = "https://download.qemu.org/${BPN}-${PV}.tar.xz"
QEMU_LOCAL_SRC = "${VIRTIOSO_LOCAL_SOURCES_DIR}/${VIRTIOSO_QEMU_REPO_DIR}"
SRC_URI += "file://${QEMU_LOCAL_SRC};subdir=${BPN}-${PV}"

python () {
    import os
    src = d.getVar("QEMU_LOCAL_SRC")
    if not os.path.isdir(src):
        bb.fatal("Missing required local source directory: %s" % src)
}

# Git source doesn't bundle meson subprojects like the tarball does
# Allow meson to download them during configure
EXTRA_OECONF:remove = "--disable-download"
