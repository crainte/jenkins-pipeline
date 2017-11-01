def call() {
    def cmd = "git diff --diff-filter=d --name-only HEAD~0..HEAD~1"
    def files = sh(returnStdout: true, script: cmd).trim()
    return files
}
