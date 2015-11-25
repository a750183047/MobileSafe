## 手机安全管家软件 ##
* 这是一个手机安全管家的软件，模拟手机管家软件。

## 2015-11-16
 
 * 创建项目

## 2015-11-16
*	创建splash活动
	
	  
		//获取版本号等信息
        PackageManager packageManager = getPackageManager();  //得到包的信息
        try {
          packageInfo = packageManager.getPackageInfo(getPackageName(),0);  //得到包的信息
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = packageInfo.versionName;  //版本名
* 沉浸任务栏的实现

	    //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
- 沉浸任务栏只需在加载layout之后添加上述代码就可以了。

- 检测更新
	
   	 使用json请求服务器中的versionCode，如果大于本地的，就弹出窗口提示要更新。



	- 开进程进行数据请求

			new Thread(){
            @Override
            public void run() {
                Message message = Message.obtain();
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(UPDATEURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//设置请求方法
                    connection.setConnectTimeout(5000); //设置连接超时
                    connection.setReadTimeout(5000); //设置响应超时
                    connection.connect(); //连接服务器

                    int responseCode =  connection.getResponseCode(); //获得响应码
                    if (responseCode == 200){
                        InputStream inputStream = connection.getInputStream();  //获取输入流


	- 把获得的数据转成string，并进行解析
	
		    String result =  StreamUtils.readFromStream(inputStream); //把输入流转成字符串
        	//解析json
        	JSONObject jsonObject = new JSONObject(result);
        	versionNameFromInternet = jsonObject.getString("versionName");
        	versionCodeFromInternet = jsonObject.getInt("versionCode");
        	descriptionFromInternet = jsonObject.getString("description");
        	downloadUrlFromInternet = jsonObject.getString("downloadUrl");
	- 然后通过Handler更新ui
	
			private Handler handler = new Handler(){
       		@Override
       		public void handleMessage(Message msg) {
           		switch (msg.what){
             	  case 0:
                	   showUpdateDialog();
	- 通过上述过程实现从服务器检查软件的更新信息。

- 使用 MaterialNavigationDrawer 开源框架

	
![](https://github.com/a750183047/MobileSafe/blob/master/image/pic1.png?raw=true)

	- 使用过程
	- 添加支持
	- 新建类继承自 MaterialNavigationDrawer，重写 init 方法
	- setDrawerHeaderImage(R.drawable.title); 设置图片
	- this.addSection(newSection("流量统计", R.drawable.netmanager_home, new FragmentButton()));  
	- 添加一项，里面有多个参数，可以打开活动或者Fragment ，但是第一个必须是一个 Fragment，否则会抛异常
	- this.addBottomSection(newSection("设置中心", R.drawable.ic_settings_black_24dp, new Intent(this, SettingActivity.class)));
	- 添加底部设置按钮。
	- 在style中添加一个style 
	- 
	-  <style name="MyNavigationDrawerTheme"       parent="MaterialNavigationDrawerTheme.Light">
        <item name="colorPrimary">#03a9f4</item>
        <!--<item name="colorPrimaryDark">#558b2f</item>-->
        <item name="colorPrimaryDark">#ff286b8b</item>
        <item name="colorAccent">#ffffff</item>
        <item name="drawerType">@integer/DRAWERTYPE_IMAGE</item>
        </style>

	- 将Activity的主题设置成刚刚添加的主题。
	- 基本步骤如上。。。

## 2015-11-25 ##

完成了手机防盗功能的开发。

* 通过设置安全手机号码，绑定手机卡序列号，收听开机广播等方式，达到开机检测手机卡是否更换，如果更换，则向安全手机号码发送短息，提示手机卡已经更换。
* 接收短信拦截，通过设置比系统短信权限更高的广播接收器，拦截特定的短信，然后阻拦其继续传播，达到接收短信指令的目的（然而4.4已经不能用了）。
* 判断短信指令，通过不同的指令来进行不同的动作。
	* 短信控制报警声音，接收到信息后进行判断，通过  
	
		    //播放报警音乐
            MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
            player.setVolume(1f,1f);    
            player.setLooping(true);
            player.start();

	* 来进行循环播放报警音乐
	* 定位
	* 通过位置服务来获取位置，发送位置给安全号码。
	* 一键锁屏和清除数据。
		* 通过Administered来注册设备，达到控制系统的目的。

