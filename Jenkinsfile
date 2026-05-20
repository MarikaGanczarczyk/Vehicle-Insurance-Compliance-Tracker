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

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }
    }
}