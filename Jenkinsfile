pipeline {
    agent any

    tools {
        jdk 'JDK25'
        maven 'Maven3'
    }

    environment {
        // Default configuration
        ENVIRONMENT = "${params.ENVIRONMENT ?: 'qa'}"
        BROWSER = "${params.BROWSER ?: 'chrome'}"
        EXECUTION_MODE = "${params.EXECUTION_MODE ?: 'LOCAL'}"
        GRID_HUB_URL = "${params.GRID_HUB_URL ?: 'http://localhost:4444/wd/hub'}"
        THREAD_COUNT = "${params.THREAD_COUNT ?: '4'}"
        HEADLESS = "${params.HEADLESS ?: 'true'}"
        ALLURE_REPORT_DIR = 'target/allure-report'
    }

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['qa', 'stage', 'prod', 'dev'],
            description: 'Test environment'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Browser for test execution'
        )
        choice(
            name: 'EXECUTION_MODE',
            choices: ['LOCAL', 'GRID'],
            description: 'Execution mode (LOCAL or GRID)'
        )
        string(
            name: 'GRID_HUB_URL',
            defaultValue: 'http://localhost:4444/wd/hub',
            description: 'Selenium Grid Hub URL (only used in GRID mode)'
        )
        string(
            name: 'THREAD_COUNT',
            defaultValue: '4',
            description: 'Number of parallel threads'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run in headless mode'
        )
        string(
            name: 'TEST_SUITE',
            defaultValue: 'testng.xml',
            description: 'TestNG suite XML file'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile -q'
            }
        }

        stage('Run Tests') {
            steps {
                sh """
                    mvn clean test \
                        -DsuiteXmlFile=${TEST_SUITE} \
                        -Denvironment=${ENVIRONMENT} \
                        -Dbrowser=${BROWSER} \
                        -Dexecution.mode=${EXECUTION_MODE} \
                        -Dgrid.hubUrl=${GRID_HUB_URL} \
                        -Dexecution.threadCount=${THREAD_COUNT} \
                        -Dexecution.headless=${HEADLESS} \
                        -Dconfig.path=src/main/resources/config/application.yaml
                """
            }
            post {
                always {
                    junit allowEmptyResults: true,
                        testResults: 'target/surefire-reports/*.xml'

                    // Archive screenshots
                    archiveArtifacts artifacts: 'target/screenshots/**/*.png',
                        allowEmptyArchive: true
                }
            }
        }

        stage('Generate Allure Report') {
            when {
                expression { fileExists('target/allure-results') }
            }
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn allure:report'
                    } else {
                        bat 'mvn allure:report'
                    }
                }
            }
            post {
                success {
                    publishHTML(target: [
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/allure-report',
                        reportFiles: 'index.html',
                        reportName: 'Allure Report'
                    ])

                    archiveArtifacts artifacts: 'target/allure-report/**/*',
                        allowEmptyArchive: true
                }
            }
        }
    }

    post {
        always {
            // Clean up
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    notFailBuild: true)
        }
        failure {
            emailext(
                subject: "[FAILED] ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: """
                    <h2>Test Execution Failed</h2>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build:</b> <a href="${env.BUILD_URL}">${env.BUILD_NUMBER}</a></p>
                    <p><b>Environment:</b> ${ENVIRONMENT}</p>
                    <p><b>Browser:</b> ${BROWSER}</p>
                    <p><b>Check Allure Report:</b> <a href="${env.BUILD_URL}Allure_20Report">Allure Report</a></p>
                """,
                to: "${env.CHANGE_AUTHOR_EMAIL ?: 'team@example.com'}",
                mimeType: 'text/html'
            )
        }
        success {
            emailext(
                subject: "[SUCCESS] ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: """
                    <h2>Test Execution Passed</h2>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build:</b> <a href="${env.BUILD_URL}">${env.BUILD_NUMBER}</a></p>
                    <p><b>Environment:</b> ${ENVIRONMENT}</p>
                    <p><b>Browser:</b> ${BROWSER}</p>
                    <p><b>Allure Report:</b> <a href="${env.BUILD_URL}Allure_20Report">View Report</a></p>
                """,
                to: "${env.CHANGE_AUTHOR_EMAIL ?: 'team@example.com'}",
                mimeType: 'text/html'
            )
        }
    }
}
