import groovy.json.JsonOutput

def call(String context, String message, String state) {
    def display = context.replaceAll(" ", "-")
    def fullcontext = "continuous-integration/jenkins/${display}"

    withCredentials([[$class: 'StringBinding', credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN']]) {
        def payload = JsonOutput.toJson(["state": "${state}", "description":"${message}", "context": "${fullcontext}", "target_url": "${env.BUILD_URL}display/redirect"])
        def apiURL = "${env.GITHUB_API}/repos/${env.GIT_REMOTE_ORIGIN}/statuses/${env.GIT_COMMIT}"
        sh """curl -sSi --retry-max-time 0 --max-time 10 --connect-timeout 10 --retry 15 --retry-delay 0 --retry-connrefused -H "Authorization: Token ${env.GITHUB_TOKEN}" -H "Accept: application/json" -H "Content-type: application/json" -X POST -d'${payload}' ${apiURL}"""
    }
}
