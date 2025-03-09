pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'develop', url: 'https://github.com/GIICIT/store.git'
            }
        }

        stage('Build Project') {
            steps {
                script {
                    powershell './gradlew clean build'
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
                    powershell "docker stop store-service || true"
                    powershell "docker rm store-service || true"

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
                powershell "docker rmi store-service:latest || true"
            }
        }
    }
}
