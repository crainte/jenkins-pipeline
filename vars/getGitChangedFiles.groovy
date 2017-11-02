def call() {
    if ( env.CHANGE_TARGET == null ) {
        println "Changed files skipped. No target found."
        return
    }
    def cmd = "git diff --diff-filter=d --name-only remotes/origin/${env.CHANGE_TARGET}..HEAD"
    def files = sh(returnStdout: true, script: cmd).trim()
    return files
}
