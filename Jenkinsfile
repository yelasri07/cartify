pipeline {
    agent {
        node {
            label 'docker-agent'
        }
    }

    triggers {
        pollSCM '* * * * *'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                // sh './build.sh'
            }
        }

        // stage('Test') {
        //     steps {
        //         echo 'Testing..'
        //         sh './test.sh'
        //     }
        // }

        // stage('Deliver') {
        //     steps {
        //         withCredentials([
        //             file(credentialsId: 'buy01-env-file', variable: 'ENV_FILE'),
        //             file(credentialsId: 'ssl-cert', variable: 'SSL_CERT'),
        //             file(credentialsId: 'ssl-key', variable: 'SSL_KEY'),
        //             file(credentialsId: 'ssl-passphrase', variable: 'SSL_PASSPHRASE')
        //         ]) {
        //             echo 'Deliver....'

        //             sh '''
        //                 cp "$ENV_FILE" .env
        //                 cp "$SSL_CERT" frontend/secureCertificate.crt
        //                 cp "$SSL_KEY" frontend/private.key
        //                 cp "$SSL_PASSPHRASE" frontend/securePassphrase
        //             '''

        //             script {
        //                 try {
        //                     sh '''
        //                         docker compose down
        //                         docker compose up -d --build
        //                         docker compose ps
        //                     '''
        //                 } catch (err) {
        //                     if (!env.GIT_PREVIOUS_SUCCESSFUL_COMMIT) {
        //                         error 'Deploy failed and no previous successful commit exists to roll back to. Manual intervention required.'
        //                     }

        //                     echo "Deploy failed — rolling back to ${env.GIT_PREVIOUS_SUCCESSFUL_COMMIT}"
        //                     sh '''
        //                         docker compose down
        //                         git checkout ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}

        //                         cp "$ENV_FILE" .env
        //                         cp "$SSL_CERT" frontend/secureCertificate.crt
        //                         cp "$SSL_KEY" frontend/private.key
        //                         cp "$SSL_PASSPHRASE" frontend/securePassphrase

        //                         ./build.sh
        //                         docker compose up -d --build
        //                     '''
        //                     error "Deployment failed, rolled back to previous successful commit ${env.GIT_PREVIOUS_SUCCESSFUL_COMMIT}"
        //                 } finally {
        //                     sh '''
        //                         rm -f .env
        //                         rm -f frontend/secureCertificate.crt
        //                         rm -f frontend/private.key
        //                         rm -f frontend/securePassphrase
        //                     '''
        //                 }
        //             }
        //         }
        //     }
        // }
    }

    post {
        success {
            emailext(
            subject: "✅ BUILD SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
                <h2>Build Succeeded</h2>
                <p><b>Job:</b> ${env.JOB_NAME}</p>
                <p><b>Build:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Commit:</b> ${env.GIT_COMMIT}</p>
                <p><b>Branch:</b> ${env.GIT_BRANCH}</p>
                <p><a href="${env.BUILD_URL}">View Build Logs</a></p>
            """,
            mimeType: 'text/html',
            to: 'adnane.elmir1@gmail.com'
        )
        }
        failure {
            emailext(
            subject: "❌ BUILD FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
                <h2>Build Failed</h2>
                <p><b>Job:</b> ${env.JOB_NAME}</p>
                <p><b>Build:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Commit:</b> ${env.GIT_COMMIT}</p>
                <p><b>Branch:</b> ${env.GIT_BRANCH}</p>
                <p><a href="${env.BUILD_URL}console">View Console Output</a></p>
            """,
            mimeType: 'text/html',
            to: 'adnane.elmir1@gmail.com'
        )
        }
    }
}
