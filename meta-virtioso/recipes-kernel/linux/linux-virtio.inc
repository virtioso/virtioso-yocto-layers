FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://virtio.cfg \
    file://virtio-blk.cfg \
    file://virtio-net.cfg \
    file://virtio-9p.cfg \
    file://virtio-gpu.cfg \
    file://virtio-input.cfg \
    file://pstore.cfg \
    file://swiotlb.cfg \
    file://tap.cfg \
"
