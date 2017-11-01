// Requires Jenkins Script approval for split()
def call() {
    def cmd = "git config --get remote.origin.url"
    def url = sh(returnStdout: true, script: cmd).trim().replaceAll(".git","").split(':')[1]
    return url
}
