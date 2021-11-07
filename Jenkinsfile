// Gitee的token凭证
def gitee_auth = "534edba8f682ec4968c5d4840aa0eaff"
// 构建的版本
def tag = "1.0.0"
def git_url = "git@gitee.com:yan129/ape-space.git"

node{
    stage('Gitee远程拉取代码'){
        checkout([$class: 'GitSCM', branches: [[name: '*/${branch_name}']], 
	    doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
	    userRemoteConfigs: [[credentialsId: "${gitee_auth}", url: "${git_url}"]]])
    }
    stage('编译构建公共模块'){
        echo "====开始编译构建公共模块===="
        sh "mvn -f ape-common clean install"
        echo "====结束编译构建公共模块===="
    }
    stage('编译构建`${project_name}`模块镜像'){
        // 查询容器是否存在，存在则删除
        sh '''
            #! /bin/sh
            containerId = `docker ps -a | grep -w ${project_name}`
            if [[ -n "${containerId}" ]] ; then
                // 停掉容器
                docker stop "${containerId}"
                // 删除容器
                docker rm "${containerId}"
                echo "成功删除${project_name}容器"
            fi
            // 查询镜像是否存在，存在则删除
            imageId = `docker images | grep -w $project_name`
            if [ "${imageId}" != "" ] ; then
                // 删除镜像
                docker rmi -f ${imageId}
                echo "成功删除${project_name}镜像"
        '''

        def imageName = "${project_name}:${tag}"

        echo "====开始${project_name}模块编译构建镜像===="
        sh "mvn -f ${project_name} clean package dockerfile:build"
        // sh "docker tag ${imageName} ape-space/${imageName}"
        echo "====结束${project_name}模块编译构建镜像===="
    }
    stage('启动`${project_name}`服务'){
        def imageName = "${project_name}:${tag}"
        sh "docker run --restart=always -d --name ${project_name} -p 7001:7001 ${imageName}"
    }
}