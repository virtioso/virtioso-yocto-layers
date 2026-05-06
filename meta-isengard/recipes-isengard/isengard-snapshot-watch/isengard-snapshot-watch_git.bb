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
LIC_FILES_CHKSUM = "file://LICENSE;md5=0215f7e684a6a05e27d4cf5e15d9cfcf"
SUMMARY = "isengard_snapshot_watch: cross-process N-buffer snapshot dashboard"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# External crate dependencies.
SRC_URI += "crate://crates.io/libc/0.2.186"
SRC_URI[libc-0.2.186.sha256sum] = "68ab91017fe16c622486840e4c83c9a37afeff978bd239b5293d61ece587de66"
