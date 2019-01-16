import groovy.json.JsonOutput

def call(Integer id, Integer status, String owner, String repo) {
    withCredentials([[$class: 'StringBinding', credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN']]) {

        // Record new Deployment Status based on output
        result = (status) ? 'failure' : 'success'
        def payload = JsonOutput.toJson(["state": "${result}"])

        def apiURL = "${env.GITHUB_API}/repos/${owner}/${repo}/deployments/${id}/statuses"
        def response = httpRequest  customHeaders: [[name: 'Authorization', value: "Token ${env.GITHUB_TOKEN}"]], httpMode: 'POST', requestBody: payload, responseHandle: 'STRING', url: apiURL
        if(response.status != 201) {
            error("Deployment Status API Update Failed: " + deployStatusResponse.status)
        }
    }
}
