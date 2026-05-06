# Copyright 2026, Normet
#
# SPDX-License-Identifier: Apache-2.0

inherit cargo externalsrc

EXTERNALSRC = "${TOPDIR}/../../sources/isengard-rs"
EXTERNALSRC_BUILD = "${EXTERNALSRC}"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = ""
SUMMARY = "Rust library for the isengard snapshot N-buffer pool (no binaries)"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# External crate dependencies (libc is used for mmap/munmap in shared_nbuf.rs).
SRC_URI += "crate://crates.io/libc/0.2.186"
SRC_URI[libc-0.2.186.sha256sum] = "68ab91017fe16c622486840e4c83c9a37afeff978bd239b5293d61ece587de66"

# isengard-rs is a pure library crate with no installed binaries.
# It is a build-time dependency of isengard-snapshot-watch via cargo path dep.
