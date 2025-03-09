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
                    powershell """
                        if (docker ps -aq -f name=store-service) {
                            echo 'Stopping and removing existing container...'
                            docker stop store-service
                            docker rm -f store-service
                        }
                    """
                    powershell "docker run -d --name store-service -p 9090:8080 -e 'SPRING_PROFILES_ACTIVE=local' store-service:latest"
                }
            }
        }

    }

    post {
        always {
            echo 'Cleaning up old Docker images and containers...'
            try {
                // Add debug logging
                echo "Removing Docker containers and images"
                sh 'docker ps -aq --filter name=store-service | xargs docker rm -f'
                sh 'docker images -q --filter dangling=true | xargs docker rmi -f'
            } catch (Exception e) {
                echo "Error during cleanup: ${e.getMessage()}"
                throw e  // Rethrow the error to fail the build
            }
        }
    }

}