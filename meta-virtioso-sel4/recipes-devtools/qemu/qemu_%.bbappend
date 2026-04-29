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

QEMU_TARGETS = "aarch64 x86_64"

QEMU_SEL4_DEPS = ""
QEMU_SEL4_DEPS:class-target = "kernel-module-sel4-virt virtioso-contracts"
DEPENDS += "${QEMU_SEL4_DEPS}"

CFLAGS:append:class-target = " -I${STAGING_INCDIR} ${QEMU_SEL4_RPC_WAIT_THREAD_CFLAGS}"

python __anonymous () {
    import os
    layers_root = d.getVar("LAYERS_ROOT") or os.path.abspath(os.path.join(d.getVar("TOPDIR"), ".."))
    config_file = os.path.abspath(os.path.join(layers_root, "..", ".config"))
    cflags = ""
    if os.path.exists(config_file):
        with open(config_file, encoding="utf-8") as config:
            for line in config:
                if line.strip() == "CONFIG_QEMU_SEL4_RPC_WAIT_THREAD=y":
                    cflags = "-DVIRTIO_QEMU_SEL4_RPC_WAIT_THREAD=1"
                    break
    d.setVar("QEMU_SEL4_RPC_WAIT_THREAD_CFLAGS", cflags)
}

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
SRC_URI += "file://${QEMU_LOCAL_SRC}/;subdir=${BPN}-${PV}"
SRC_URI:remove = "file://fix-strerrorname_np.patch"

# Local directory unpack preserves absolute source path under ${WORKDIR}/${BPN}-${PV}.
S = "${WORKDIR}/${BPN}-${PV}/${@d.getVar('QEMU_LOCAL_SRC').lstrip('/')}"

python () {
    import os
    src = d.getVar("QEMU_LOCAL_SRC")
    if not os.path.isdir(src):
        bb.fatal("Missing required local source directory: %s" % src)
}

# Git source doesn't bundle meson subprojects like the tarball does
# Allow meson to download them during configure
EXTRA_OECONF:remove = "--disable-download"

# Ensure the built system QEMU binaries include the SJA1000-based PCI CAN
# devices such as kvaser_pci. On Linux hosts, can-host-socketcan is compiled
# automatically when CAN bus support is enabled, so there is no separate
# socketcan meson option to toggle here.
enable_qemu_can_support() {
    for cfg in \
        ${S}/configs/devices/aarch64-softmmu/default.mak \
        ${S}/configs/devices/x86_64-softmmu/default.mak; do
        grep -q '^CONFIG_CAN_SJA1000=y$' "$cfg" || echo 'CONFIG_CAN_SJA1000=y' >> "$cfg"
        grep -q '^CONFIG_CAN_PCI=y$' "$cfg" || echo 'CONFIG_CAN_PCI=y' >> "$cfg"
    done
}

do_configure:prepend() {
    enable_qemu_can_support
}
