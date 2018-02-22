def call() {
  env.GIT_BRANCH = getGitBranch()
  if (env.BRANCH_NAME.startsWith('PR-')) {
    env.GIT_COMMIT = sh([returnStdout: true, script: 'git rev-parse ORIG_HEAD']).trim()
  } else {
    env.GIT_COMMIT = sh([returnStdout: true, script: 'git rev-parse HEAD']).trim()
  }
  env.GIT_COMMIT_SHORT = env.GIT_COMMIT.take(6)
  env.GIT_REMOTE_ORIGIN = getGitRemoteOrigin()
  env.GIT_CHANGED_FILES = getGitChangedFiles()
}
