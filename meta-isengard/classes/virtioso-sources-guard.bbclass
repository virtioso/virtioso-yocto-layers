# Overrides meta-virtioso-sel4's virtioso-sources-guard for isengard builds.
# isengard-agx-orin targets are plain Linux and do not depend on the seL4
# source repos (qemu, kmod-sel4-virt, etc.) that the seL4 guard checks.
# meta-isengard (priority 32) takes precedence over meta-virtioso-sel4 (30)
# only when this layer is in BBLAYERS, i.e. only for isengard MACHINE builds.
# seL4 builds do not include meta-isengard, so the real guard still runs there.
