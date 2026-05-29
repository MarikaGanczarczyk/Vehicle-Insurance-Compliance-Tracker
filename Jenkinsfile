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

        stage('Merge develop to master') {
            when {
                expression {
                    return (
                        env.BRANCH_NAME == 'develop'
                        || env.GIT_BRANCH == 'develop'
                        || env.GIT_BRANCH == 'origin/develop'
                        || env.GIT_BRANCH == 'refs/heads/develop'
                    )
                }
            }
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            set -e
                            git config user.name "Jenkins CI"
                            git config user.email "jenkins-ci@example.com"
                            git fetch origin +refs/heads/develop:refs/remotes/origin/develop +refs/heads/master:refs/remotes/origin/master
                            git checkout -B master origin/master
                            git merge --no-ff origin/develop -m "Merge develop into master after successful Jenkins build"
                            git push origin master
                        '''
                    } else {
                        bat '''
                            @echo off
                            git config user.name "Jenkins CI"
                            if errorlevel 1 exit /b 1
                            git config user.email "jenkins-ci@example.com"
                            if errorlevel 1 exit /b 1
                            git fetch origin +refs/heads/develop:refs/remotes/origin/develop +refs/heads/master:refs/remotes/origin/master
                            if errorlevel 1 exit /b 1
                            git checkout -B master origin/master
                            if errorlevel 1 exit /b 1
                            git merge --no-ff origin/develop -m "Merge develop into master after successful Jenkins build"
                            if errorlevel 1 exit /b 1
                            git push origin master
                            if errorlevel 1 exit /b 1
                        '''
                    }
                }
            }
        }
    }
}
