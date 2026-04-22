SUMMARY = "Relocatable x86_64 host QEMU runtime artifact"
DESCRIPTION = "Produces a deployable runtime tarball for the Yocto-built qemu-system-x86_64 host runtime."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit nopackages

DEPENDS = "qemu-system-native"

SRC_URI = "file://create_virtioso_qemu_runtime.py"

S = "${UNPACKDIR}"

PACKAGE_ARCH = "${BUILD_ARCH}"
COMPATIBLE_HOST = "(x86_64.*-linux)"

VIRTIOSO_QEMU_RUNTIME_ARTIFACT = "virtioso-qemu-runtime-x86_64"
VIRTIOSO_QEMU_RUNTIME_DEPLOY_DIR = "${DEPLOY_DIR}/virtioso-qemu-runtime"
VIRTIOSO_QEMU_RUNTIME_TAR = "${VIRTIOSO_QEMU_RUNTIME_DEPLOY_DIR}/${VIRTIOSO_QEMU_RUNTIME_ARTIFACT}.tar.zst"
VIRTIOSO_QEMU_RUNTIME_LATEST = "${VIRTIOSO_QEMU_RUNTIME_DEPLOY_DIR}/latest-x86_64.tar.zst"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"

do_deploy[depends] += "qemu-system-native:do_populate_sysroot"
do_deploy() {
    install -d "${VIRTIOSO_QEMU_RUNTIME_DEPLOY_DIR}"
    python3 "${WORKDIR}/create_virtioso_qemu_runtime.py" \
        --artifact-name "${VIRTIOSO_QEMU_RUNTIME_ARTIFACT}" \
        --qemu-binary "${WORKDIR}/../qemu-system-native/${PV}/build/qemu-system-x86_64" \
        --support-usr "${WORKDIR}/../qemu-system-native/${PV}/recipe-sysroot-native/usr" \
        --pc-bios-dir "${WORKDIR}/../qemu-system-native/${PV}/build/pc-bios" \
        --interpreter "${UNINATIVE_LOADER}" \
        --uninative-root "${TMPDIR}/sysroots-uninative" \
        --output-tar "${VIRTIOSO_QEMU_RUNTIME_TAR}" \
        --build-id "${PF}"
    ln -sfn "${@os.path.basename(d.getVar('VIRTIOSO_QEMU_RUNTIME_TAR'))}" "${VIRTIOSO_QEMU_RUNTIME_LATEST}"
}

addtask deploy after do_install before do_build
