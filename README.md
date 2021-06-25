ape-auth-server: 9530
ape-article: 9527
ape-sms: 9525
ape-user: 9526
ape-gateway: 8001

使用keytool生成RSA证书jwt.jks，复制到resource目录下，在JDK的bin目录下使用如下命令即可
keytool -genkey -alias ape-space -keyalg RSA -keysize 1024 -keystore jwt.jks
密钥口令：1291248490
