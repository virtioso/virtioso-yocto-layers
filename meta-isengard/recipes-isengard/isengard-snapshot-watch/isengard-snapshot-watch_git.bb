# Copyright 2026, Normet
#
# SPDX-License-Identifier: Apache-2.0

inherit cargo externalsrc

# Source is the standalone Rust binary crate in isengard-app.
# Its path dependency `isengard = { path = "../../isengard-rs" }` resolves to
# sources/isengard-rs, which cargo builds automatically — no separate
# DEPENDS on isengard-rs is required.
EXTERNALSRC = "${TOPDIR}/../../sources/isengard-app/isengard_snapshot_watch"
EXTERNALSRC_BUILD = "${EXTERNALSRC}"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = ""
SUMMARY = "isengard_snapshot_watch: cross-process N-buffer snapshot dashboard"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# External crate dependencies.
SRC_URI += "crate://crates.io/libc/0.2.186"
