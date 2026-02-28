SUMMARY = "Virtioso shared RPC and trace contract headers"
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;md5=5acbc805043b555d80689b28fa9c3fbe"

VIRTIOSO_CONTRACTS_LOCAL_SRC = "${VIRTIOSO_LOCAL_SOURCES_DIR}/virtioso-contracts"
SRC_URI = "file://${VIRTIOSO_CONTRACTS_LOCAL_SRC}/;subdir=git"

# Local directory unpack preserves absolute source path under ${WORKDIR}/git.
S = "${WORKDIR}/git/${@d.getVar('VIRTIOSO_CONTRACTS_LOCAL_SRC').lstrip('/')}"

python () {
    import os
    src = d.getVar("VIRTIOSO_CONTRACTS_LOCAL_SRC")
    if not os.path.isdir(src):
        bb.fatal("Missing required local source directory: %s" % src)
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}/virtioso/rpc
    install -d ${D}${includedir}/virtioso/trace

    install -m 0644 ${S}/include/virtioso/rpc/rpc.h ${D}${includedir}/virtioso/rpc/
    install -m 0644 ${S}/include/virtioso/rpc/rpc_queue.h ${D}${includedir}/virtioso/rpc/
    install -m 0644 ${S}/include/virtioso/trace/trace_contract.h ${D}${includedir}/virtioso/trace/
    install -m 0644 ${S}/include/virtioso/trace/trace.h ${D}${includedir}/virtioso/trace/
    install -m 0644 ${S}/include/virtioso/trace/backend.h ${D}${includedir}/virtioso/trace/
}
