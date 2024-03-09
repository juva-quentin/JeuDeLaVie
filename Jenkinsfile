pipeline {
    agent any

    tools {
        maven 'Maven' // Assurez-vous que cette version de Maven est configurée dans Jenkins
        jdk 'OpenJDK 17'    // Assurez-vous que cette version de JDK est configurée dans Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {

            steps {
                script {
                    // Lance les tests avec Maven
                    def mvnHome = tool 'Maven' // Assurez-vous que cette version de Maven est configurée dans Jenkins
                    sh "${mvnHome}/bin/mvn test"
                }
            }
            post {
                always {
                    // Collecte les résultats des tests pour Jenkins
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build and Deploy') {

            steps {
                script {
                    // Construit l'application avec Maven
                    def mvnHome = tool 'Maven' // Assurez-vous que cette version de Maven est configurée dans Jenkins
                    sh "${mvnHome}/bin/mvn clean package"

                    // Déployer l'application - ajustez selon votre cible de déploiement
                    echo 'Déployer l\'application'
                    // Exemple de commande de déploiement, ajustez en fonction de votre environnement
                    // sh 'scp target/monapplication.jar user@monserveur:/chemin/vers/deploiement/'
                }
            }
        }
    }

    post {
        always {
            echo 'La pipeline est terminée.'
        }
    }
}
