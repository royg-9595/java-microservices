pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'jayanthroyg'
        DOCKERHUB_TOKEN = credentials('docker-hub-pat')
        IMAGE_NAME = 'jayanthroyg/java-microservices'
        IMAGE_TAG = 'latest'
        K8S_NAMESPACE = 'default'
    }

    stages {
        stage('Clone Git Repo') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-token', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_TOKEN')]) {
                        sh 'git clone https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$GITHUB_USER/java-microservices.git'
                    }
                }
            }
        }

        stage('Build and Test') {
            when {
                anyOf {
                    branch 'feature/*'  // trigger for feature branches
                    branch 'develop'    // trigger for develop branch
                }
            }
            steps {
                script {
                     // clean and compile and run against the testcases
                    sh 'mvn clean install'
                }
            }
        }

        stage('Docker Build and Push') {
            when {
                branch 'develop' // Docker build and push only for the develop branch
            }
            steps {
                script {
                    // Log in to DockerHub
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-pat', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                    }
                    // Build Docker image
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                    // Push image to DockerHub
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                branch 'develop' // Deployment happens for develop branch
            }
            steps {
                script {
                    // Apply Kubernetes manifests
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        sh "kubectl apply -f k8s/deployment.yaml"
                        sh "kubectl apply -f k8s/service.yaml"
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up
            cleanWs()
        }
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}
