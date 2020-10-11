pipeline {
    agent {
        kubernetes {
            //cloud 'kubernetes'
            label 'kaniko'
            yaml """
kind: Pod
metadata:
  name: kaniko
spec:
  containers:
  - name: maven
    image: maven:3.6.3-openjdk-16
    command:
    - cat
    tty: true
    volumeMounts:
      - name: m2
        mountPath: /root/.m2
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
    volumeMounts:
      - name: jenkins-docker-cfg
        mountPath: /kaniko/.docker
  volumes:
  - name: jenkins-docker-cfg
    projected:
      sources:
      - secret:
          name: registry-credentials
          items:
            - key: .dockerconfigjson
              path: config.json
  - name: m2
    hostPath:
      path: /root/.m2
"""
        }
    }
    stages {
        stage('Checkout') {
            steps {
                git url:'https://github.com/subhakar-microservices/spring-boot-dept-service.git',branch: 'main'
            }
        }
        
        stage('Build') {
            steps {
                container(name: 'maven') {
                    sh 'mvn package'
                }
            }
        }
     
        stage('Make Image') {
            environment {
                PATH        = "/busybox:$PATH"
                REGISTRY    = 'index.docker.io' // Configure your own registry
                REPOSITORY  = 'subhakarkotta'
                IMAGE       = 'spring-boot-dept-service:1.0'
            }
            steps {
                container(name: 'kaniko', shell: '/busybox/sh') {
                    sh '''#!/busybox/sh
                    /kaniko/executor -f `pwd`/Dockerfile -c `pwd` --cache=true --destination=${REGISTRY}/${REPOSITORY}/${IMAGE}
                    '''
                }
            }
        }
    }
}