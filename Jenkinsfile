pipeline {
    agent {
        node {
            label 'docker-agent'
        }
    }
    
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh './build.sh'
                sh '''
                cd frontend
                npm i
                ng build
                '''
            }
        }

        stage('Test') {
            steps {
                echo 'Testing..'
                sh './test.sh'
                sh 'cd frontend && ng test --watch=false'
            }
        }

        stage('Deliver') {
            steps {
                withCredentials([
                    file(credentialsId: 'buy01-env-file', variable: 'ENV_FILE'),
                    file(credentialsId: 'ssl-cert', variable: 'SSL_CERT'),
                    file(credentialsId: 'ssl-key', variable: 'SSL_KEY'),
                    file(credentialsId: 'ssl-passphrase', variable: 'SSL_PASSPHRASE')
                ]) {
                    echo 'Deliver....'

                    sh '''
                        cp "$ENV_FILE" .env
                        cp "$SSL_CERT" frontend/secureCertificate.crt
                        cp "$SSL_KEY" frontend/private.key
                        cp "$SSL_PASSPHRASE" frontend/securePassphrase
                    '''

                    script {
                        try {
                            sh '''
                                docker compose down
                                docker compose up -d --build
                                docker compose ps
                            '''
                        } catch (err) {
                            if (!env.GIT_PREVIOUS_SUCCESSFUL_COMMIT) {
                                error 'Deploy failed and no previous successful commit exists to roll back to. Manual intervention required.'
                            }

                            echo "Deploy failed — rolling back to ${env.GIT_PREVIOUS_SUCCESSFUL_COMMIT}"
                            sh '''
                                docker compose down
                                git checkout ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}

                                cp "$ENV_FILE" .env
                                cp "$SSL_CERT" frontend/secureCertificate.crt
                                cp "$SSL_KEY" frontend/private.key
                                cp "$SSL_PASSPHRASE" frontend/securePassphrase

                            ./build.sh
                            docker compose up -d --build
                        '''
                            error "Deployment failed, rolled back to previous successful commit ${env.GIT_PREVIOUS_SUCCESSFUL_COMMIT}"
                    } finally {
                            sh '''
                            rm -f .env
                            rm -f frontend/secureCertificate.crt
                            rm -f frontend/private.key
                            rm -f frontend/securePassphrase
                        '''
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                mail(
                to: 'adnane.elmir1@gmail.com, elasriyoussef604@gmail.com',
                subject: "✅ SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build passed.\nLogs: ${env.BUILD_URL}"
            )
            }
        }
        failure {
            script {
                mail(
                to: 'adnane.elmir1@gmail.com, elasriyoussef604@gmail.com',
                subject: "❌ FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build failed.\nLogs: ${env.BUILD_URL}console"
            )
            }
        }
    }
}
