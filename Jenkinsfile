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
                // sh '''
                // ./build.sh
                // '''
                // export JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-amd64
                // export PATH=$JAVA_HOME/bin:$PATH
                // java -version
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                // sh '''
                // ./test.sh
                // '''
            }
        }
        stage('Deliver') {
            steps {
                withCredentials([
                    file(credentialsId: 'buy01-env-file', variable: 'ENV_FILE')
                ]) {
                    echo 'Deliver....'
                    sh '''
                    cp $ENV_FILE .env
                    cat .env
                    docker compose down
                    docker compose up -d --build
                    rm -f .env
                    '''
                }
            }
        }
    }
}
