// Gitee的token凭证
def gitee_auth = "9d4f0e293be28fe72236617b265ddaee"
// 构建的版本
def tag = "1.0.0"
def git_url = "git@gitee.com:yan129/ape-space.git"

node{
    def selectedProjects = "${project_name}".split(",")

    stage('Gitee远程拉取代码'){
        checkout([$class: 'GitSCM', branches: [[name: '*/${branch_name}']],
	    doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
	    userRemoteConfigs: [[credentialsId: "${gitee_auth}", url: "${git_url}"]]])
    }
    stage('编译构建公共模块'){
        echo "====开始编译构建公共模块===="
        sh "mvn -f ape-common clean install package"
        echo "====结束编译构建公共模块===="
    }
    stage('编译构建模块镜像'){
        for(int i = 0; i < selectedProjects.size(); i++){
            //取出每个项目的名称和端口
            def currentProject = selectedProjects[i];
            //项目名称
            def currentProjectName = currentProject.split('@')[0]

            // 调用Linux上的sh文件脚本删除容器和镜像
            sh "/root/softwork/ape-space/deploy.sh ${currentProjectName} ${tag}"

            def imageName = "${currentProjectName}:${tag}"

            echo "====开始${currentProjectName}模块编译构建镜像===="
            sh "mvn -f ${currentProjectName} clean package dockerfile:build -Ddockerfile.buildArgs.JASYPT_PASSWORD=${JASYPT_PASSWORD}"
            // sh "docker tag ${imageName} ape-space/${imageName}"
            echo "====结束${currentProjectName}模块编译构建镜像===="
        }
    }
    stage('启动微服务'){
        for(int i = 0; i < selectedProjects.size(); i++){
            //取出每个项目的名称和端口
            def currentProject = selectedProjects[i];
            //项目名称
            def currentProjectName = currentProject.split('@')[0]
            //项目启动端口
            def currentProjectPort = currentProject.split('@')[1]

            def imageName = "${currentProjectName}:${tag}"
            sh "docker run --restart=always -d --name ${currentProjectName} -p ${currentProjectPort}:${currentProjectPort} ${imageName}"
            sh "docker logs ${currentProjectName}"
        }
    }
}