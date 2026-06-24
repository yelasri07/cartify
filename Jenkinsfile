pipeline {
    agent {
        node {
            label 'docker-agent'
        }
    }
    triggers {
        pollSCM '* * * * *'
    }
    tools {
        jdk 'JDK17' // Matches the name you gave it in Global Tools
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh '''
                ./build.sh
                '''
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh '''
                ./test.sh
                '''
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
