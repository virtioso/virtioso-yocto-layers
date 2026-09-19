SUMMARY = "Virtioso shared RPC and trace contract headers"
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

VIRTIOSO_CONTRACTS_LOCAL_SRC = "${VIRTIOSO_LOCAL_SOURCES_DIR}/virtioso-contracts"
SRC_URI = "file://${VIRTIOSO_CONTRACTS_LOCAL_SRC}/;subdir=git"

# Local directory unpack preserves absolute source path under ${UNPACKDIR}/git.
S = "${UNPACKDIR}/git/${@d.getVar('VIRTIOSO_CONTRACTS_LOCAL_SRC').lstrip('/')}"

python () {
    import os
    src = d.getVar("VIRTIOSO_CONTRACTS_LOCAL_SRC")
    if not os.path.isdir(src):
        bb.fatal("Missing required local source directory: %s" % src)
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"

# The contract is the include tree as the repository ships it; a list of
# names here went stale silently (four of seven no longer existed).
do_install() {
    install -d ${D}${includedir}
    cp -R --no-dereference --preserve=mode,timestamps ${S}/include/virtioso ${D}${includedir}/
}
