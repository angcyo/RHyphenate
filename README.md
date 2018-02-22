# RHyphenate
环信SDK接入

### 2018-2-22

环信版本 V3.3.9 2018-02-11

文档接入地址: http://docs.easemob.com/im/200androidclientintegration/10androidsdkimport



## 使用说明
SDK 中相关异步/同步处理方法介绍
同步方法：SDK 里大部分方法都为同步方法，即这个方法执行完毕，才会走后面的代码。
异步方法：带有 callback 以及 API 注释里明确写明异步方法的方法，即不需要等这个方法走完，后边的代码就已经在执行了，通过 callback 得到方法执行的结果。
取本地log
以Demo为例，获取本地的log

adb pull /sdcard/Android/data/com.hyphenate.chatuidemo/easemob-demo#chatdemoui/core_log/easemob.log
其中com.hyphenate.chatuidemo是packagename, easemob-demo#chatdemoui是appkey，需要替换成自己对应的路径。

## 初始化 SDK
要求在 application 的oncreate方法中做初始化，初始化的时候需要传入设置好的 options。

EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
options.setAutoDownloadThumbnail(true);
...
//初始化
EMClient.getInstance().init(applicationContext, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
EMClient.getInstance().setDebugMode(true);
注：如果你的 APP 中有第三方的服务启动，请在初始化 SDK（EMClient.getInstance().init(applicationContext, options)）方法的前面添加以下相关代码（相应代码也可参考 Demo 的 application），使用 EaseUI 库的就不用理会这个。

appContext = this;
int pid = android.os.Process.myPid();
String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

if (processAppName == null ||!processAppName.equalsIgnoreCase(appContext.getPackageName())) {
    Log.e(TAG, "enter the service process!");

    // 则此application::onCreate 是被service 调用的，直接返回
    return;
}
如何获取processAppName请参考以下方法。

private String getAppName(int pID) {
    String processName = null;
    ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
    List l = am.getRunningAppProcesses();
    Iterator i = l.iterator();
    PackageManager pm = this.getPackageManager();
    while (i.hasNext()) {
        ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
        try {
            if (info.pid == pID) {
                processName = info.processName;
                return processName;
            }
        } catch (Exception e) {
            // Log.d("Process", "Error>> :"+ e.toString());
        }
    }
    return processName;
}