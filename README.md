ape-auth-server: 9530
ape-article: 9527
ape-sms: 9525
ape-user: 9526
ape-gateway: 8001

使用keytool生成RSA证书jwt.jks，复制到resource目录下，在JDK的bin目录下使用如下命令即可
keytool -genkey -alias ape-space -keyalg RSA -keysize 1024 -keystore jwt.jks
密钥口令：1291248490
````
Keytool –genkey -alias my_alias  -keyalg RSA  –keystore  C:\mykeystore.keystore  -validity 1830  -keysize 1024  -storepass  123456 -keypass pass -dname "CN=Jack,OU=腾讯,O=腾讯,L=深圳市,ST=广东省,C=Tecent"
 -genkey    表示要创建新的密钥
 -alias   别名，每个keystore都关联这一个独一无二的alias，这个alias通常不区分大小写
 -keystore    keystore文件的存储位置，文件扩展名为".keystore"
 -keyalg       指定密钥的算法 (如 RSA  DSA（如果不指定默认采用DSA）)
 -validity     指定有效期(天)，默认90天
 -keysize      指定密钥长度
 -storepass   指定密钥库的访问密码(获取keystore信息所需的密码)
 -keypass      指定别名条目的密码(私钥的密码)
  -dname       指定证书拥有者信息 例如：  "CN=名字与姓氏,OU=组织单位名称,O=组织名称,L=城市或区域名称,ST=州或省份名称,C=单位的两字母国家代码"
````
GitHub回调地址：http://localhost:9526/ape-space/oauth2/github/callback
Homepage URL 用户使用github登陆成功后跳转的页面：http://localhost:9526/ape-space

第三方登录接口：
GitHub: http://localhost:9526/oauth2/github/login
