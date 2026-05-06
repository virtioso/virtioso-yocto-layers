# Copyright 2026, Technology Innovation Institute
#
# SPDX-License-Identifier: Apache-2.0

inherit cargo externalsrc

EXTERNALSRC = "${TOPDIR}/../../sources/isengard-rs"
EXTERNALSRC_BUILD = "${EXTERNALSRC}"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = ""
SUMMARY = "Rust wrapper for the isengard snapshot N-buffer pool and watch binary"

# CARGO_MANIFEST_PATH is derived from S = EXTERNALSRC by cargo_common.bbclass:
#   CARGO_MANIFEST_PATH ??= "${S}/${CARGO_SRC_DIR}/Cargo.toml"
# No extra path assignment needed.

# The crate has no external dependencies — Cargo.lock records only the
# package itself.  No crate:// SRC_URI entries are needed.

# Override PACKAGE_ARCH so the recipe is rebuilt for each target arch.
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install:append() {
    # The cargo_do_install base installs all executables from the target dir.
    # Nothing extra needed; isengard_snapshot_watch is picked up automatically.
    :
}
