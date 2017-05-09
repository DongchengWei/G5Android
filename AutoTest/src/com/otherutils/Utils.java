package com.otherutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiWatcher;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.util.Log;


/**
 * 常用工具
 * 1.把播放器的播放时间转换成秒
 * 2.执行cmd命令，保存log
 * 3.获取当前方法名
 * 4.获取设备当前时间
 * 5.注册监视器，发生anr或crash停止测试并截图
 * 6.注册监视器，发生anr或crash截图后继续测试
 * 7.截图到某个路径
 * 8.打印log，输出到控制台和logcat
 * */
public class Utils extends UiAutomatorTestCase {
	
	public static String failStepStr = "";
	public static final String rootLogPathStr = "/sdcard/AutoTest/";//log保存根目录
	public static final boolean IS_SAVE_LOGS = true;
	
	/**
	 * 向logcat中打入Mark标记
	 * |--- Mark:failStep --- sync --- Date: 2017-05-08 10:09:12.06 ---|
	 * |--- Mark:01 --- sync --- Date: 08.05.2017 --- Time: 10:09:12.06 ---|
	 * @param detailStr mark详情（如fail的步骤，方法名）
	 * @Date 2017-05-08
	 */
	public static void markInsert(String detailStr) {
		String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		logPrint("|--- Mark:" + detailStr + " --- sync --- Date: " + nowTimeStr + " ---|");
	}
	
	/**
	 * 把播放时间转换成秒，如01:20转成80,仅限分钟和秒的情况
	 * */
	public static int timeStringToSecond(String str) {
		
		String[] timeStrArr = str.split(":");
		int secondsLong = 0;
		secondsLong = Integer.parseInt(timeStrArr[0])*60 + Integer.parseInt(timeStrArr[1]);
		return secondsLong;
	}
	
	/**
	 * 捉取log命令，保存在/data/logs路径下。
	 * 命令：su 0 svbugreport 1
	 * */
	public static void getLogs() {
		execCmd("su 0 svbugreport 1");
	}
	
	/**
	 * 打Mark并截图
	 * @param markStr 打入的mark详情
	 * @param pathStr 截图保存路径/sdcard/AutoTest
	 * @Date 2017-05-08
	 */
	public static void markAndCapture(String markStr, String pathStr) {
		markInsert(markStr);
		String nowTimeStr = getNowTime();
		takeScreenshotToPath(pathStr, nowTimeStr + ".png");
	}
	
	/**
	 * 保存logs:拷贝logs文件夹到某个目录下,如果没有这个目录会自动创建
	 * @param markStr mark详情
	 * @param pathStr 要拷贝到的目录（/sdcard/AutoTest/xxx/time）
	 * @Date 2017-05-08
	 */
	public static void markAndSaveLogs(boolean isOk,String markStr, String pathStr) {
		if ((isOk==false) && IS_SAVE_LOGS) {
			Date date = new Date();
			String markTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
			String saveTimeStr = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(date);
			logPrint("|--- Mark:" + markStr + " --- sync --- Date: " + markTimeStr + " ---|");
			
			execCmdNoReturn("su");						//获取su权限
			logPrint("su finished,start getlogs...");
			execCmdLine("su 0 svbugreport 1");			//抓取log
			
			String savePathStr = rootLogPathStr + pathStr + "/" + saveTimeStr;//拼接保存目录
			logPrint("savePathStr:" + savePathStr);
			File file = new File(savePathStr);
			if (!file.exists()) {
				file.mkdirs();
			} 
			logPrint("getlogs finished, copy logs to " + savePathStr);
			execCmdNoReturn("cp -rf /data/logs/ " + savePathStr + "/");//保存到指定目录
		}
	}
	
	/**
	 * 直接执行命令，不等待命令结束
	 * @param cmd 要执行的命令
	 * @Date 2017-05-08
	 */
	public static void execCmdNoReturn(String cmd) {
		System.out.println("------execute command:  " + cmd);
		Log.d("UiAutomator", "------execute command:  " + cmd);
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 需求：执行cmd命令，且输出信息到控制台
	 * @param cmd
	 */
	public static void execCmd(String cmd) {
		System.out.println("------execute command:  " + cmd);
		Log.d("UiAutomator", "------execute command:  " + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStream input = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			int code = 0;
			while ((code = reader.read()) != -1) {
				System.out.print((char) code);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 命令行输入命令并通过读行判断是否执行完毕
	 * @param cmd
	 * @Date 2017-05-08
	 */
	public static void execCmdLine(String cmd) {
		System.out.println("------execute command:  " + cmd);
		Log.d("UiAutomator", "------execute command:  " + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStream input = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.print(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前方法名，并打印当前的方法名
	 * @return String 方法名，不带括弧的
	 * */
	public static String getCurrentMethodName() {
		//具体使用数组的那个元素和JVM的实现有关，在SUN JDK1.8下面测试的是第三个元素,
		//具体说明可以查看Thread.getStackTrace方法的javadoc
		//数组下标为0为方法：getStackTrace
		//数组下标为1为方法：getMethodName
		//数组下标为2为当前方法：getCurrentMethodName
		//数组下标为3为调用getCurrentMethodName方法的方法
		String methodNameStr = Thread.currentThread().getStackTrace()[3].getMethodName();
		failStepStr = methodNameStr;
		Utils.logPrint(methodNameStr + "()");
		return methodNameStr;
	}
	
	/**
	 * 获取设备当前时间
	 * */
	public static String getNowTime(){
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	}
	
	/**
	 * 发生anr或者crash停止测试并截图，
	 * 出现第三方弹框则关闭第三方弹窗后继续测试
	 * UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
	 * */
	public static void stopRunningWatcher(){
		//先要注册监听器
		UiDevice.getInstance().registerWatcher("stopRunning",new UiWatcher(){
			@Override
			public boolean checkForCondition()
			{
				UiObject messageDialogObj = new UiObject(new UiSelector().resourceId("android:id/message"));
				UiObject confirmBtnObj = new UiObject(new UiSelector().resourceId("android:id/button1"));
				UiObject sogouDialogObj = new UiObject(new UiSelector()
						.resourceId("com.sohu.inputmethod.sogou:id/custom_dialog_title"));
				UiObject closeSogouObj = new UiObject(new UiSelector()
						.resourceId("com.sohu.inputmethod.sogou:id/close_dialog"));
				UiObject closeSogouBrowserObj = new UiObject(new UiSelector()//搜狗广告
						.resourceId("com.sohu.inputmethod.sogou:id/hotwords_app_popup_negative_button"));
				if(messageDialogObj.exists())
				{
					try {
						logPrint("UiWatcher trigger: " + messageDialogObj.getText());
						if (confirmBtnObj.exists()) {
							logPrint("UiWatcher trigger: get button text=" + confirmBtnObj.getText());
							Utils.logPrint("Save capture to /sdcard/Pictures/" + "UiWatcher.png");
							File capPicture = new File("/sdcard/Pictures/" + "UiWatcher.png");
							UiDevice.getInstance().takeScreenshot(capPicture);
						}else {
							logPrint("UiWatcher trigger: confirm button not exists");
						}
						return true;
					} catch (UiObjectNotFoundException e) {
						logPrint("UiWatcher trigger: UiObjectNotFoundException" + e.toString());
						return false;
					}
				}else if (sogouDialogObj.exists()) {
					try {
						logPrint("UiWatcher trigger: " + sogouDialogObj.getText());
						Utils.logPrint("Save capture to /sdcard/Pictures/" + "SougouWatcher.png");
						File sougouPicture = new File("/sdcard/Pictures/" + "SougouWatcher.png");
						UiDevice.getInstance().takeScreenshot(sougouPicture);
						
						if (closeSogouObj.exists()) {
							closeSogouObj.click();//点击关闭窗口
						}
						return true;
					} catch (UiObjectNotFoundException e) {
						logPrint("UiWatcher trigger: UiObjectNotFoundException" + e.toString());
						return false;
					}
				}else if (closeSogouBrowserObj.exists()) {
					try {
						closeSogouBrowserObj.click();//关闭窗口
						return true;
					} catch (UiObjectNotFoundException e) {
						logPrint("closeSogouBrowserObj UiWatcher trigger: UiObjectNotFoundException-" + e.toString());
						return false;
					}
				}else {
					return false;
				}
			}
		});
	}
	
	/**
	 * 监视器，出现anr或crash则点击确定继续测试，不停止测试
	 * 出现第三方弹框则点击关闭弹窗口继续测试
	 * UiDevice.getInstance().removeWatcher("confirmStopRunning"); //移除监视器
	 * */
	public static void clickConfirmWatcher(){
		//先要注册监听器
		UiDevice.getInstance().registerWatcher("confirmStopRunning",new UiWatcher(){
			@Override
			public boolean checkForCondition()
			{
				UiObject messageDialogObj = new UiObject(new UiSelector().resourceId("android:id/message"));
				UiObject confirmBtnObj = new UiObject(new UiSelector().resourceId("android:id/button1"));
				UiObject sogouDialogObj = new UiObject(new UiSelector()
						.resourceId("com.sohu.inputmethod.sogou:id/custom_dialog_title"));
				UiObject closeSogouObj = new UiObject(new UiSelector()
						.resourceId("com.sohu.inputmethod.sogou:id/close_dialog"));
				UiObject closeSogouBrowserObj = new UiObject(new UiSelector()//搜狗广告
						.resourceId("com.sohu.inputmethod.sogou:id/hotwords_app_popup_negative_button"));
				if(messageDialogObj.exists())
				{
					try {
						logPrint("UiWatcher trigger: " + messageDialogObj.getText());
						if (confirmBtnObj.exists()) {
							logPrint("UiWatcher trigger: get button text=" + confirmBtnObj.getText());
							//String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
							Utils.logPrint("Save capture to /sdcard/Pictures/" + "UiWatcher.png");
							File capPicture = new File("/sdcard/Pictures/" + "UiWatcher.png");
							UiDevice.getInstance().takeScreenshot(capPicture);
							confirmBtnObj.click();//点击确定
						}else {
							logPrint("UiWatcher trigger: confirm button not exists");
						}
						return true;
					} catch (UiObjectNotFoundException e) {
						logPrint("UiWatcher trigger: UiObjectNotFoundException" + e.toString());
						return false;
					}
				}else if (sogouDialogObj.exists()) {
					try {
						logPrint("UiWatcher trigger: " + messageDialogObj.getText());
						Utils.logPrint("Save capture to /sdcard/Pictures/" + "SougouWatcher.png");
						File sougouPicture = new File("/sdcard/Pictures/" + "SougouWatcher.png");
						UiDevice.getInstance().takeScreenshot(sougouPicture);
						
						if (closeSogouObj.exists()) {
							closeSogouObj.click();//点击关闭窗口
						}
						return true;
					} catch (UiObjectNotFoundException e) {
						logPrint("UiWatcher trigger: UiObjectNotFoundException" + e.toString());
						return false;
					}
				}else if (closeSogouBrowserObj.exists()) {
					try {
						closeSogouBrowserObj.click();//关闭窗口
						return true;
					} catch (UiObjectNotFoundException e) {
						logPrint("closeSogouBrowserObj UiWatcher trigger: UiObjectNotFoundException-" + e.toString());
						return false;
					}
				}else {
					return false;
				}
			}
		});
	}
	
	/**
	 * 保存截图到某个路径（包括文件名/sdcard/Pictures/）
	 * @param pathStr 如/sdcard/Pictures/
	 * @param fileNameStr 文件名“xxx.png”
	 * */
	public static void takeScreenshotToPath(String pathStr, String fileNameStr) {
		Utils.logPrint("Save capture to " + pathStr + fileNameStr);
		File capPicture = new File(pathStr + fileNameStr);
		UiDevice.getInstance().takeScreenshot(capPicture);
	}
	
	/**
	 * 打印结果，打印到控制台和logcat
	 * */
	public static void logForResult(String str) {
		System.out.println("=========>>>>" + str + "<<");
		Log.d("UiAutomator",str);
	}
	
	
	/**
	 * 打印log，打印到控制台和logcat
	 * */
	public static void logPrint(String str){
		System.out.println("=========" + str);
		Log.d("UiAutomator",str);
	}
	
	/**
	 * 输出版本信息
	 * */
	public static void PrintInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android Bluetooth AutoTest v0.0.1======");
		System.out.println("=========       2016-12-30       =================");
		System.out.println("=========        DesaySV         =================");
		System.out.println("==================================================");
		Log.d("UiAutomator","=========G5Android Bluetooth AutoTest v0.0.1======");
		Log.d("UiAutomator","=========       2016-12-30       =================");
		Log.d("UiAutomator","=========        DesaySV         =================");
		Log.d("UiAutomator","==================================================");
	}

}
