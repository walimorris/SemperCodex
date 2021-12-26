pipeline {
    agent any

    stages {

        stage("build") {
            when {
                expression {
                    BRANCH_NAME == 'dev' || BRANCH_NAME == 'master'
                }
            }
            steps {
                echo 'building the application...'
            }
        }

        stage ("test") {
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