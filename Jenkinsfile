// Gitee的token凭证
def gitee_auth = "534edba8f682ec4968c5d4840aa0eaff"
// 构建的版本
def tag = "1.0.0"

node{
    stage('Gitee远程拉取代码'){
        checkout([$class: 'GitSCM', branches: [[name: '*/${branch_name}']], 
	    doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
	    userRemoteConfigs: [[credentialsId: "534edba8f682ec4968c5d4840aa0eaff", url: 'git@gitee.com:yan129/ape-space.git']]])
    }
    stage('编译构建公共模块'){
        sh ''mvn -f ape-common clean install''
    }
    stage('编译构建${project_name}模块镜像'){
        def imageName = "${project_name}:${tag}"

        sh ''mvn -f ${project_name} clean package dockerfile:build''
        sh ''docker tag ${imageName} ape-space/${imageName}''
    }
}