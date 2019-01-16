import groovy.json.JsonOutput

def call(String ref, String target, String description=null) {
    withCredentials([[$class: 'StringBinding', credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN']]) {
        def message

        if( description ) {
            message = description
        } else {
            message = "Deploying ${ref} to ${target}"
        }

        def payload = JsonOutput.toJson(["ref": "${ref}", "environment": "${target}", "auto_merge": "false", "description": "${message}"])
        def apiURL = "${env.GITHUB_API}/repos/${env.GIT_REMOTE_ORIGIN}/deployments"

        // Create new Deployment using the GitHub Deployment API 
        def response = httpRequest  customHeaders: [[name: 'Authorization', value: "Token ${env.GITHUB_TOKEN}"]], httpMode: 'POST', requestBody: payload, responseHandle: 'STRING', url: apiURL
        if(response.status != 201) {
            error("Deployment API Create Failed: " + response.status)
        }

        // Get the ID of the GitHub Deployment just created
        def responseJson = readJSON text: response.content
        def id = responseJson.id
        if(id == "") {
            error("Could not extract id from Deployment response")
        }

        return id
    }
}
