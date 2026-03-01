DEPENDS:append = " virtioso-contracts"

do_configure:append() {
    install -d ${B}/virtioso-include
    ln -sfn ${RECIPE_SYSROOT}/usr/include/virtioso ${B}/virtioso-include/virtioso
}

EXTRA_OEMAKE:append = " KCFLAGS=-I${B}/virtioso-include"
