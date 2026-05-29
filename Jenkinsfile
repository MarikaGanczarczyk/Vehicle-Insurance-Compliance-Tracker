pipeline {
    agent any

    parameters {
        booleanParam(name: 'PUSH_DOCKER_IMAGE', defaultValue: false, description: 'Push the Docker image to Docker Hub after a successful build')
    }

    environment {
        DOCKER_IMAGE_REPOSITORY = 'vehicle-insurance-compliance-tracker'
    }

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

        stage('Docker Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            set -e
                            docker version
                            docker build -t "${DOCKER_IMAGE_REPOSITORY}:${BUILD_NUMBER}" -t "${DOCKER_IMAGE_REPOSITORY}:latest" .
                        '''
                    } else {
                        bat '''
                            @echo off
                            docker version
                            if errorlevel 1 exit /b 1
                            docker build -t "%DOCKER_IMAGE_REPOSITORY%:%BUILD_NUMBER%" -t "%DOCKER_IMAGE_REPOSITORY%:latest" .
                            if errorlevel 1 exit /b 1
                        '''
                    }
                }
            }
        }

        stage('Docker Push') {
            when {
                expression {
                    return params.PUSH_DOCKER_IMAGE
                }
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-token', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_TOKEN')]) {
                        if (isUnix()) {
                            sh '''
                                set -e
                                docker tag "${DOCKER_IMAGE_REPOSITORY}:${BUILD_NUMBER}" "${DOCKER_USERNAME}/${DOCKER_IMAGE_REPOSITORY}:${BUILD_NUMBER}"
                                docker tag "${DOCKER_IMAGE_REPOSITORY}:latest" "${DOCKER_USERNAME}/${DOCKER_IMAGE_REPOSITORY}:latest"
                                echo "${DOCKER_TOKEN}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
                                docker push "${DOCKER_USERNAME}/${DOCKER_IMAGE_REPOSITORY}:${BUILD_NUMBER}"
                                docker push "${DOCKER_USERNAME}/${DOCKER_IMAGE_REPOSITORY}:latest"
                                docker logout
                            '''
                        } else {
                            bat '''
                                @echo off
                                docker tag "%DOCKER_IMAGE_REPOSITORY%:%BUILD_NUMBER%" "%DOCKER_USERNAME%/%DOCKER_IMAGE_REPOSITORY%:%BUILD_NUMBER%"
                                if errorlevel 1 exit /b 1
                                docker tag "%DOCKER_IMAGE_REPOSITORY%:latest" "%DOCKER_USERNAME%/%DOCKER_IMAGE_REPOSITORY%:latest"
                                if errorlevel 1 exit /b 1
                                echo %DOCKER_TOKEN% | docker login -u "%DOCKER_USERNAME%" --password-stdin
                                if errorlevel 1 exit /b 1
                                docker push "%DOCKER_USERNAME%/%DOCKER_IMAGE_REPOSITORY%:%BUILD_NUMBER%"
                                if errorlevel 1 exit /b 1
                                docker push "%DOCKER_USERNAME%/%DOCKER_IMAGE_REPOSITORY%:latest"
                                if errorlevel 1 exit /b 1
                                docker logout
                            '''
                        }
                    }
                }
            }
        }

        stage('Merge develop to master') {
            when {
                expression {
                    def branchName = env.BRANCH_NAME ?: env.GIT_BRANCH ?: ''
                    return (
                        branchName == 'develop'
                        || branchName == 'origin/develop'
                        || branchName == 'refs/heads/develop'
                        || branchName == '*/develop'
                    )
                }
            }
            steps {
                script {
                    echo 'Merging origin/develop into master after successful build'
                    withCredentials([usernamePassword(credentialsId: 'github-push-token', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_TOKEN')]) {
                        if (isUnix()) {
                            sh '''
                                set -e
                                git config user.name "Jenkins CI"
                                git config user.email "jenkins-ci@example.com"
                                git fetch origin +refs/heads/develop:refs/remotes/origin/develop +refs/heads/master:refs/remotes/origin/master
                                git checkout -B master origin/master
                                git merge --no-ff origin/develop -m "Merge develop into master after successful Jenkins build"
                                git push "https://${GIT_USERNAME}:${GIT_TOKEN}@github.com/MarikaGanczarczyk/Vehicle-Insurance-Compliance-Tracker.git" master
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
                                git push "https://%GIT_USERNAME%:%GIT_TOKEN%@github.com/MarikaGanczarczyk/Vehicle-Insurance-Compliance-Tracker.git" master
                                if errorlevel 1 exit /b 1
                            '''
                        }
                    }
                }
            }
        }
    }
}
