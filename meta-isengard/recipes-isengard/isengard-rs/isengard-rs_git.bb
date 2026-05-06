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

# isengard-rs is a pure library crate with no installed binaries.
# It is a build-time dependency of isengard-snapshot-watch via cargo path dep.
