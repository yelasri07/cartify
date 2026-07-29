
pipeline {
    agent {
        node {
            label 'docker-agent'
        }
    }

    stages {
        stage('Build & Test') {
            steps {
                echo 'Building..'
                sh './scripts/deploy_to_nexus.sh'
            }
        }

        stage('Deliver') {
            when {
                not {
                    changeRequest()
                }
            }
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
                            docker compose -p cartify down
                            docker compose -p cartify up -d --build
                            docker compose ps
                        '''
                    } catch (err) {
                            if (!env.GIT_PREVIOUS_SUCCESSFUL_COMMIT) {
                                error 'Deploy failed and no previous successful commit exists to roll back to. Manual intervention required.'
                            }
                            echo "Deploy failed — rolling back to ${env.GIT_PREVIOUS_SUCCESSFUL_COMMIT}"
                            sh '''
                            docker compose -p cartify down
                            git checkout ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}
                            cp "$ENV_FILE" .env
                            cp "$SSL_CERT" frontend/secureCertificate.crt
                            cp "$SSL_KEY" frontend/private.key
                            cp "$SSL_PASSPHRASE" frontend/securePassphrase
                            ./scripts/build.sh
                            docker compose -p cartify up -d --build
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
}
