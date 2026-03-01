@Library('jenkins-shared-library') _

pipeline {
    agent any

    tools {
        maven 'M3' // Must match the Maven installation in Jenkins
    }

    stages {
        stage('Build with Shared Library') {
            steps {
                script {
                    // Call the shared library function
                    mavenPipeline(
                        branch: 'main',
                        gitUrl: 'https://github.com/sakhareram8877-lang/jenkins-shared-library.git'
                    )
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build completed successfully!'
        }
        failure {
            echo '❌ Build failed. Check Jenkins logs for details.'
        }
    }
}
