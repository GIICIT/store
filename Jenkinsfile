pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'develop', url: 'https://github.com/GIICIT/store.git'
            }
        }

        stage('Apply Spotless') {
            steps {
                script {
                    powershell './gradlew spotlessApply'
                }
            }
        }

        stage('Build Project') {
            steps {
                script {
                    powershell './gradlew clean build'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    powershell './gradlew test'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    powershell "docker build -t store-service:latest ."
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    powershell "docker stop store-service; docker rm store-service"
                    powershell "docker run -d --name store-service -p 9090:8080 -e 'SPRING_PROFILES_ACTIVE=local' store-service:latest"
                }
            }
        }
    }

    post {
        success {
            echo 'Build and container run successful!'
        }
        failure {
            echo 'Build or container run failed!'
        }
        always {
            script {
                echo 'Cleaning up old Docker images...'
                powershell "docker rmi store-service:latest; true"
            }
        }
    }
}