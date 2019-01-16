import groovy.json.JsonOutput

def call(String ref, String target, String owner, String repo, String description=null) {
    withCredentials([[$class: 'StringBinding', credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN']]) {
        def message
        def id

        if( description ) {
            message = description
        } else {
            message = "Deploying ${ref} to ${target}"
        }

        def payload = JsonOutput.toJson(["ref": "${ref}", "environment": "${target}", "auto_merge": false, "description": "${message}"])
        def apiURL = "${env.GITHUB_API}/repos/${owner}/${repo}/deployments"

        // Create new Deployment using the GitHub Deployment API 
        def response = httpRequest  customHeaders: [[name: 'Authorization', value: "Token ${env.GITHUB_TOKEN}"]], httpMode: 'POST', requestBody: payload, responseHandle: 'STRING', url: apiURL
        if(response.status != 201) {
            error("Deployment API Create Failed: " + response.status)
        }

        // Get the ID of the GitHub Deployment just created
        def responseJson = readJSON text: response.content
        id = responseJson.id
        if(id == "") {
            error("Could not extract id from Deployment response")
        }
    }
    return id
}
