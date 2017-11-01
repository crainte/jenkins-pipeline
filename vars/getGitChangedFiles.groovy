def call() {
    def cmd = "git diff --diff-filter=d --name-only remotes/origin/${env.CHANGE_TARGET}..HEAD"
    def files = sh(returnStdout: true, script: cmd).trim()
    return files
}
