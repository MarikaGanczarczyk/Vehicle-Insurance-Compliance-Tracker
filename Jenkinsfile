pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build, Test & Coverage') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'chmod +x mvnw'
                        sh './mvnw clean verify'
                    } else {
                        bat 'mvnw.cmd clean verify'
                    }
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/site/jacoco/**,target/jacoco.exec', allowEmptyArchive: true
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

        stage('Docker Build') {
            steps {
                script {
                    if (isUnix()) {
                        def dockerAvailable = sh(
                                script: 'command -v docker >/dev/null 2>&1',
                                returnStatus: true
                        ) == 0

                        if (dockerAvailable) {
                            sh 'docker build -t erik/spring-app:latest .'
                        } else {
                            echo 'Skipping Docker Build: docker command not found on this Jenkins agent.'
                        }
                    } else {
                        def dockerAvailable = bat(
                                script: 'where docker >nul 2>nul',
                                returnStatus: true
                        ) == 0

                        if (dockerAvailable) {
                            bat 'docker build -t erik/spring-app:latest .'
                        } else {
                            echo 'Skipping Docker Build: docker command not found on this Jenkins agent.'
                        }
                    }
                }
            }
        }
    }
}
