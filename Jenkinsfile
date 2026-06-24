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
                sh '''
                export JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-amd64
                export PATH=$JAVA_HOME/bin:$PATH
                java -version
                cd user-service
                ./mvnw -version
                '''
                // ./build.sh
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
                echo 'Deliver....'
                sh '''
                echo "doing delivery stuff.."
                '''
            }
        }
    }
}
