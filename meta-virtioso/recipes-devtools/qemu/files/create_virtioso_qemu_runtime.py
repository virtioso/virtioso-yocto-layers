#!/usr/bin/env python3

from __future__ import annotations

import argparse
import json
import os
import re
import shutil
import subprocess
import tempfile
from pathlib import Path


def shell_env_with_support_usr(support_usr: Path) -> dict[str, str]:
    env = os.environ.copy()
    parts = []
    for rel in ("lib", "lib64", "libexec"):
        path = support_usr / rel
        if path.exists():
            parts.append(str(path))
    if env.get("LD_LIBRARY_PATH"):
        parts.append(env["LD_LIBRARY_PATH"])
    env["LD_LIBRARY_PATH"] = ":".join(parts)
    return env


def runtime_lib_files(binary: Path, support_usr: Path) -> set[Path]:
    env = shell_env_with_support_usr(support_usr)
    proc = subprocess.run(["ldd", str(binary)], check=True, capture_output=True, text=True, env=env)
    libs: set[Path] = set()
    pattern = re.compile(r"=>\s+(\S+)")
    for line in proc.stdout.splitlines():
        match = pattern.search(line)
        if match:
            lib_path = Path(match.group(1))
            if lib_path.exists() and support_usr in lib_path.parents:
                libs.add(lib_path.resolve())
            continue
        stripped = line.strip()
        if "=>" in stripped or not stripped.startswith("/"):
            continue
        lib_path = Path(stripped.split()[0])
        if lib_path.exists() and support_usr in lib_path.parents:
            libs.add(lib_path.resolve())
    return libs


def copy_tree(src: Path, dst: Path) -> None:
    if src.is_dir():
        shutil.copytree(src, dst, dirs_exist_ok=True)
    else:
        dst.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(src, dst)


def copy_file_preserve_rel(src: Path, root: Path, dst_root: Path) -> None:
    copy_tree(src, dst_root / src.relative_to(root))


def program_interpreter(binary: Path) -> str | None:
    proc = subprocess.run(["readelf", "-l", str(binary)], check=True, capture_output=True, text=True)
    marker = "Requesting program interpreter:"
    for line in proc.stdout.splitlines():
        if marker in line:
            return line.split(marker, 1)[1].strip().rstrip("]").strip()
    return None


def sanitize_pc_bios_tree(pc_bios_dir: Path) -> None:
    shutil.rmtree(pc_bios_dir / "descriptors", ignore_errors=True)
    for path in pc_bios_dir.rglob("*"):
        if not path.is_file():
            continue
        if path.name == "Makefile":
            path.unlink()
            continue
        if path.suffix in {".d", ".o"}:
            path.unlink()
            continue
        if path.suffix == ".mak" and path.name.startswith("config"):
            path.unlink()


def write_manifest(root: Path, artifact_name: str, qemu_version: str, includes_uninative: bool, build_id: str) -> None:
    manifest = {
        "schema_version": 1,
        "artifact": artifact_name,
        "host_arch": "x86_64",
        "qemu_binary": "usr/bin/qemu-system-x86_64",
        "qemu_version": qemu_version,
        "launcher_model": "relative-runtime-tree",
        "includes_uninative": includes_uninative,
        "entry_hint": "usr/bin/qemu-system-x86_64",
        "required_host_tools": ["bash", "tar", "ps", "setsid"],
        "required_host_capabilities": ["x86_64-linux", "kvm"],
        "build_id": build_id,
    }
    (root / "manifest.json").write_text(json.dumps(manifest, indent=2) + "\n")


def write_readme(root: Path) -> None:
    (root / "README.runtime.md").write_text(
        "# Virtioso QEMU Runtime\n\n"
        "This tree contains a relocatable host QEMU runtime for x86_64.\n\n"
        "It is intended to be consumed by workspace bundle composers and by local\n"
        "Intel/AMD developers who do not build the Yocto native QEMU runtime\n"
        "themselves.\n"
    )


def qemu_version(binary: Path, support_usr: Path) -> str:
    env = shell_env_with_support_usr(support_usr)
    proc = subprocess.run([str(binary), "--version"], check=True, capture_output=True, text=True, env=env)
    first = proc.stdout.splitlines()[0].strip()
    parts = first.split()
    return parts[-1] if parts else "unknown"


def build_runtime_tree(args: argparse.Namespace, root: Path) -> None:
    usr = root / "usr"
    copy_tree(args.qemu_binary, usr / "bin" / args.qemu_binary.name)
    for lib_path in sorted(runtime_lib_files(args.qemu_binary, args.support_usr)):
        copy_file_preserve_rel(lib_path, args.support_usr, usr)

    for share_name in ("qemu", "qemu-firmware"):
        share_dir = args.support_usr / "share" / share_name
        if share_dir.exists():
            copy_file_preserve_rel(share_dir, args.support_usr, usr)

    if args.pc_bios_dir and args.pc_bios_dir.exists():
        copy_tree(args.pc_bios_dir, root / "pc-bios")
        sanitize_pc_bios_tree(root / "pc-bios")

    if args.interpreter:
        interpreter = Path(args.interpreter)
        try:
            rel = interpreter.relative_to(args.uninative_root)
        except ValueError:
            rel = None
        if rel is not None:
            copy_tree(interpreter.parent, root / "uninative" / rel.parent)

    write_manifest(
        root,
        artifact_name=args.artifact_name,
        qemu_version=qemu_version(args.qemu_binary, args.support_usr),
        includes_uninative=bool(args.interpreter),
        build_id=args.build_id,
    )
    write_readme(root)


def create_tarball(staging_root: Path, output_tar: Path) -> None:
    output_tar.parent.mkdir(parents=True, exist_ok=True)
    subprocess.run(
        ["tar", "--zstd", "-cf", str(output_tar), "-C", str(staging_root.parent), staging_root.name],
        check=True,
    )


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument("--artifact-name", required=True)
    parser.add_argument("--qemu-binary", required=True, type=Path)
    parser.add_argument("--support-usr", required=True, type=Path)
    parser.add_argument("--pc-bios-dir", type=Path)
    parser.add_argument("--interpreter", default="")
    parser.add_argument("--uninative-root", required=True, type=Path)
    parser.add_argument("--output-tar", required=True, type=Path)
    parser.add_argument("--build-id", required=True)
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    with tempfile.TemporaryDirectory(prefix=f"{args.artifact_name}.") as tmp:
        staging_root = Path(tmp) / args.artifact_name
        build_runtime_tree(args, staging_root)
        create_tarball(staging_root, args.output_tar)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
