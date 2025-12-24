# Copyright 2022, Technology Innovation Institute
#
# SPDX-License-Identifier: Apache-2.0

inherit core-image

# Development images: empty root password, root login allowed
IMAGE_FEATURES += "allow-empty-password allow-root-login empty-root-passwd"

FEATURE_PACKAGES_qemu-virtualization = "\
    qemu \
    qemu-rnd-helper \
    qemu-rnd-helper-gui \
    kernel-module-vhost \
    kernel-module-vhost-net \
    kernel-module-tap \
"

FEATURE_PACKAGES_benchmark = " \
    screen \
    tii-benchmark \
"

GUI_BENCHMARK_TOOLS = " \
    x11perf \
    xrestop \
    xwininfo \
    xprop \
"

GUI_GL_BENCHMARK_TOOLS = " \
    mesa-demos \
"

FEATURE_PACKAGES_gui-benchmark = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', "${GUI_BENCHMARK_TOOLS}", "", d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 opengl', "${GUI_GL_BENCHMARK_TOOLS}", "", d)} \
"

GUI_GL_TEST_TOOLS = " \
    piglit \
"

FEATURE_PACKAGES_gui-test = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 opengl', "${GUI_GL_TEST_TOOLS}", "", d)} \
"
