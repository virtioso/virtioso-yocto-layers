# SPDX-License-Identifier: Apache-2.0

# vm-image-driver doubles as the host-side QEMU root disk for qemu-backed
# bring-up. Make the deployable QCOW2 artifact explicit instead of relying on
# vm-guest-images-install, which only copies nested guest images into the
# driver rootfs.
inherit vm-guest-image
