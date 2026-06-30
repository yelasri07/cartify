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
                    file(credentialsId: 'buy01-env-file', variable: 'ENV_FILE'),
                    file(credentialsId: 'ssl-cert', variable: 'SSL_CERT'),
                    file(credentialsId: 'ssl-key', variable: 'SSL_KEY'),
                    file(credentialsId: 'ssl-passphrase', variable: 'SSL_PASSPHRASE')
                ]) {
                    echo 'Deliver....'
                    sh '''
                    cp $ENV_FILE .env

                    cp $SSL_CERT angular-app/secureCertificate.crt
                    cp $SSL_KEY angular-app/private.key
                    cp $SSL_PASSPHRASE angular-app/securePassphrase

                    docker compose down
                    docker compose up -d --build
                    rm -f .env
                    rm -f angular-app/secureCertificate.crt
                    rm -f angular-app/private.key
                    rm -f angular-app/securePassphrase
                    '''
                }
            }
        }
    }
}
