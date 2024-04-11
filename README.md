# autoSignApk
通过配置文件，可以自动签名apk，直接拖入apk文件即可

# 序言
因为360加固，自动签名需要开通VIP，每次加固完了都得手动签名。所以写了个工具。实现通过配置文件配置，拖拽APK自动签名。

支持：V1 V2 V3 V4 签名。通过分析清单文件，自动选择版本。

![在这里插入图片描述](https://img-blog.csdnimg.cn/639548b7c04f4e719e661d5618b34f62.png)

# 2024.4.11 发布1.1
支持了对齐功能，如果apk没有对齐，在targetSdkVersion>30的情况下会出现无法按照的情。
需要在配置文件中配置对齐工具的地址。属性为alignToolsPath，值可以参考。"D:\\Android\\SDK（前面是sdk的地址）\\build-tools\\33.0.2(最新的sdk版本)\\zipalign.exe"

# 效果
![在这里插入图片描述](https://img-blog.csdnimg.cn/e7b4d90c860046478e8494781777a537.gif#pic_center)
# 使用

##  1.下载jar包
[autoSign-1.0.jar](https://github.com/zhuguohui/autoSignApk/blob/master/jar/autoSign-1.0.jar)
## 2.编写配置文件

```json
{
	"signToolsPath": "D:\\Android\\SDK\\build-tools\\33.0.2\\apksigner.bat",
  "alignToolsPath":"D:\\Android\\SDK\\build-tools\\33.0.2\\zipalign.exe",
	"signConfigs": [{
		"appId": "com.aaa.bbb.ccc",
		"storePath": ".\\keysotres\\abc.jks",
		"storePassword": "abc",
		"keyAlias": "abc",
		"aliasPassword": "abc"
	}]
}
```
### 参数说明
| 字段名称 |  作用|
|--|--|
| signToolsPath | 签名工具地址，使用的是apksigner，在sdk下build-tools中。<br>例如： D:\\Android\\SDK\\build-tools\\33.0.2\\apksigner.bat |
|signConfigs|用于配置单个的签名配置项|
|appId|应用id |
|storePath|签名文件地址|
|storePassword|签名文件密码|
|keyAlias|别名|
|aliasPassword|别名密码|
## 3.使用bat启动
将以下命令放置在一个bat文件中。即可。其中 **-configFilePath** 后面接着的是上面的配置文件的地址。
```java
start /min "cmd" java -jar autoSign-1.1.jar -configFilePath .\signConfig.json
```
## 直接拖入
拖入apk以后会自动解析出包名，然后通过配置文件签名。最后在apk原来的位置生成一个名字为 xxx-signed.apk
还会有一个idsig文件，这是V4签名生成的。可以不用
![在这里插入图片描述](https://img-blog.csdnimg.cn/49183027d52c4722a48a4e87511c8e79.png)
# 签名工具
使用的是 **apksigner**
文档地址 
[Android 开发者 apksigner](https://developer.android.google.cn/studio/command-line/apksigner?hl=zh-cn)
![在这里插入图片描述](https://img-blog.csdnimg.cn/3da511f0c598434583217bf30074b607.png)
## 签名版本
通过  --min-sdk-version 和 --max-sdk-version 的值来决定何时采用此签名方案 
![在这里插入图片描述](https://img-blog.csdnimg.cn/750bc2c2feb9460cb8b762848e6c3e32.png)
而这两个值，通过解析apk文件获得
![在这里插入图片描述](https://img-blog.csdnimg.cn/3215fe9f57954cc49fc3e26ccb806487.png)
所以兼容性没问题。

