@Library('jenkins-shared-library') _

pipeline {
    agent any

    // Use Maven installed in Jenkins (must match Global Tool Configuration)
    tools {
        maven 'M3'
    }

    stages {
        stage('Checkout & Build with Shared Library') {
            steps {
                // Call your shared library function
                mavenPipeline(
                    branch: 'main',
                    gitUrl: 'https://github.com/sakhareram8877-lang/jenkins-shared-library.git'
                )
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
