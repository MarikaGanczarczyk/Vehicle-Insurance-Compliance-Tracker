pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Agent Diagnostics') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            set +e
                            echo "User: $(whoami)"
                            echo "Working directory: $(pwd)"
                            echo "PATH=$PATH"
                            uname -a
                            command -v java
                            java -version
                            command -v javac
                            javac -version
                            command -v curl
                            command -v wget
                            command -v unzip
                            ls -la
                            ls -la .mvn .mvn/wrapper
                            head -1 mvnw
                            sed -n '1,20p' .mvn/wrapper/maven-wrapper.properties
                            curl -I https://repo.maven.apache.org/maven2/ || true
                            exit 0
                        '''
                    } else {
                        bat '''
                            echo User: %USERNAME%
                            echo Working directory: %CD%
                            echo PATH=%PATH%
                            where java
                            java -version
                            where javac
                            javac -version
                            where curl
                            dir
                            dir .mvn
                            dir .mvn\\wrapper
                            type .mvn\\wrapper\\maven-wrapper.properties
                            curl -I https://repo.maven.apache.org/maven2/
                            exit /b 0
                        '''
                    }
                }
            }
        }

        stage('Build, Test & Coverage') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'java -version'
                        sh 'chmod +x mvnw'
                        sh 'MVNW_VERBOSE=true ./mvnw -B clean verify'
                    } else {
                        bat 'java -version'
                        bat 'set MVNW_VERBOSE=true && mvnw.cmd -B clean verify'
                    }
                }
            }
            post {
                always {
                    archive includes: 'target/surefire-reports/*.xml,target/site/jacoco/**,target/jacoco.exec'
                    publishHTML(target: [
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage Report'
                    ])
                }
            }
        }
    }
}
