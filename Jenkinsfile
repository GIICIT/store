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
                    sh './gradlew clean build'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t store-service:latest ."
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    sh "docker stop store-service || true"
                    sh "docker rm store-service || true"

                    sh "docker run -d --name store-service -p 9090:8080 -e 'SPRING_PROFILES_ACTIVE=local' store-service:latest"
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build and container run successful!'
        }
        failure {
            echo '❌ Build or container run failed!'
        }
        always {
            script {
                echo '🧹 Cleaning up old Docker images...'
                sh "docker rmi store-service:latest || true"
            }
        }
    }
}
