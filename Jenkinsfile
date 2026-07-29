
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
    }
}
