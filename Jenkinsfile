pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-17'
        }
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Coverage') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t erik/spring-app:latest .'
            }
        }
    }
}