def call(Map config) {

    pipeline {
        agent any

        tools {
            maven 'Maven-3'
        }

        environment {
            DOCKER_IMAGE = "my-app:${env.BUILD_NUMBER}"  // Tag Docker image with build number
        }

        stages {

            stage('Checkout') {
                steps {
                    git branch: config.branch,
                        url: config.gitUrl,
                        credentialsId: config.credentialsId
                }
            }

            stage('Build') {
                steps {
                    sh 'mvn clean package'
                }
            }

            stage('Test') {
                steps {
                    sh 'mvn test'
                }
            }

            stage('Docker Build') {
                steps {
                    sh 'docker build -t $DOCKER_IMAGE .'
                }
            }

            stage('Docker Run') {
                steps {
                    sh '''
                        docker stop my-app-container || true
                        docker rm my-app-container || true
                        docker run -d --name my-app-container -p 8080:8080 $DOCKER_IMAGE
                    '''
                }
            }

            stage('Archive Artifacts') {
                steps {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        post {
            success {
                echo "Build and Docker Deployment Successful ✅"
            }
            failure {
                echo "Build or Deployment Failed ❌"
            }
        }
    }
}
