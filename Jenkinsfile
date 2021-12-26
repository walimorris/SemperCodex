pipeline {
    agent any

    stages {

        stage("build") {
            steps {
                echo 'building the application...'
            }
        }

        stage ("test") {
            when {
                expression {
                    BRANCH_NAME == 'dev' || BRANCH_NAME == 'master'
                }
            }
            steps {
                echo 'running application tests...'
            }
        }
    }

    post {
        always {

        }
        failure {

        }
    }
}