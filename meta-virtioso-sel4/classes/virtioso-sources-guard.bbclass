python () {
    import os
    import subprocess

    src_root = d.getVar("VIRTIOSO_LOCAL_SOURCES_DIR")
    if not src_root:
        bb.fatal("VIRTIOSO_LOCAL_SOURCES_DIR is not set")

    repo_list = (d.getVar("YOCTO_REQUIRED_CLEAN_REPOS") or "").split()
    if not repo_list:
        bb.fatal("YOCTO_REQUIRED_CLEAN_REPOS is empty")

    qemu_repo_dir = d.getVar("VIRTIOSO_QEMU_REPO_DIR") or "qemu"

    for repo in repo_list:
        repo_path = os.path.join(src_root, repo)
        if not os.path.isdir(repo_path):
            bb.fatal("Required local source directory is missing: %s" % repo_path)

        git_dir = os.path.join(repo_path, ".git")
        if not (os.path.isdir(git_dir) or os.path.isfile(git_dir)):
            bb.fatal("Required local source directory is not a git repository: %s" % repo_path)

        status_cmd = [
            "git",
            "-C",
            repo_path,
            "status",
            "--porcelain",
            "--untracked-files=all",
            "--ignore-submodules=all",
        ]
        status = subprocess.run(status_cmd, check=False, capture_output=True, text=True)
        if status.returncode != 0:
            bb.fatal(
                "Failed to read git status for %s:\n%s"
                % (repo_path, (status.stderr or status.stdout).strip())
            )

        if status.stdout.strip():
            bb.fatal(
                "Repository %s has uncommitted/untracked changes. "
                "Before any bitbake command, review changes with a human and either commit or discard."
                % repo_path
            )

        if repo == qemu_repo_dir:
            submodule_cmd = ["git", "-C", repo_path, "submodule", "status", "--recursive"]
            submodules = subprocess.run(submodule_cmd, check=False, capture_output=True, text=True)
            if submodules.returncode != 0:
                bb.fatal(
                    "Failed to query qemu submodule status for %s:\n%s"
                    % (repo_path, (submodules.stderr or submodules.stdout).strip())
                )

            uninitialized = [
                line.strip()
                for line in submodules.stdout.splitlines()
                if line.startswith("-")
            ]
            if uninitialized:
                details = "\n".join(uninitialized)
                bb.fatal(
                    "QEMU submodules are not initialized in %s. "
                    "Run 'git -C %s submodule update --init --recursive' before bitbake.\n%s"
                    % (repo_path, repo_path, details)
                )
}
