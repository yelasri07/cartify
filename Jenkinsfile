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
                sh './build.sh'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing..'
                sh './test.sh'
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
                                sleep 60

                                # 1. Check container states (works even with no exposed ports)
                                FAILED=$(docker compose ps --format json | grep -E '"State":"exited"|"Health":"unhealthy"' || true)
                                if [ -n "$FAILED" ]; then
                                    echo "Container health check failed"
                                    docker compose ps
                                    exit 1
                                fi

                                # 2. Check discovery-service, the one service we know is exposed
                                curl -f http://localhost:8761/actuator/health || exit 1

                                echo "Health check passed"
                            '''
                        } catch (err) {
                            if (!env.GIT_PREVIOUS_SUCCESSFUL_COMMIT) {
                                error "Deploy failed and no previous successful commit exists to roll back to. Manual intervention required."
                            }

                            echo "Deploy failed — rolling back to ${env.GIT_PREVIOUS_SUCCESSFUL_COMMIT}"
                            sh '''
                                docker compose down
                                git checkout ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}

                                # Re-copy secrets since checkout may affect untracked files
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
            echo 'Pipeline succeeded! Build, tests, and deployment all passed.'
            // TODO: add email
        }
        failure {
            echo 'Pipeline failed! Check logs above for build, test, or deployment errors.'
            // TODO: add email
        }
    }
}