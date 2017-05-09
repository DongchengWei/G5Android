package com.autotest;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.AppStorePage;
import com.pageutil.AppsPage;
import com.pageutil.HomePage;

import android.os.RemoteException;
import android.util.Log;


/*
 * case: 启动/退出app
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.AddAndDeleteShortcut
 * 前提： 1、主机正常开机   2、应用程序正常启动
 * 步骤：多次反复的进入同一个应用程序（25次）
 * 期望：能正常执行反复的进入同一个应用程序（25次）
 * 其他：
 * */
public class AppLaunchAndExit extends UiAutomatorTestCase {
	
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			AppLaunchAndExitTest();		//压力测试：启动/退出app
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/* Case: 启动/退出app(第7个应用为应用商店)
	 * 前提： 1、主机正常开机   2、应用程序正常启动
	 * 操作：多次反复的进入同一个应用程序（25次）
	 * */
	private void AppLaunchAndExitTest() {
		
		HomePage homePage = new HomePage();
		AppsPage appsPage = new AppsPage();
		AppStorePage appStorePage = new AppStorePage();
		
		String startTestTimeStr = "";
		String nowTimeStr = "";
		int passCounter = 0;
		for (int i = 0; i < 3; i++) {
			try {
				homePage.goBackHome();
				homePage.intoApps();
				int totalApps = appsPage.AppsCounter();
				appsPage.LaunchOneApp(4);//第4个应用为应用商店
				if (appStorePage.waitForExists(10)) {
					UiDevice.getInstance().pressBack();
					if (appsPage.waitForExists(10)) {
						passCounter ++;
						Utils.logPrint("Total apps:" + totalApps+ ", Test pass = " + passCounter + ", Total test:25");
					}else {
						Utils.logPrint("back to apps fail,takescreenshort...");
						nowTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
						Utils.logPrint("Save capture to /sdcard/Pictures/BackToAppFail-" + nowTimeStr + ".png");
						File capPicture = new File("/sdcard/Pictures/BackToAppFail-" + nowTimeStr + ".png");
						getUiDevice().takeScreenshot(capPicture);
					}
				}else {
					Utils.logPrint("into App Store fail,takescreenshort...");
					nowTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
					Utils.logPrint("Save capture to /sdcard/Pictures/IntoAppStoreFail-" + nowTimeStr + ".png");
					File capPicture = new File("/sdcard/Pictures/IntoAppStoreFail-" + nowTimeStr + ".png");
					getUiDevice().takeScreenshot(capPicture);
				}
			} catch (UiObjectNotFoundException e) {
				Utils.logPrint("UiObjectNotFoundException");
			}
			
		}
		Utils.logPrint("Start Test:" + startTestTimeStr + ", end test:" + nowTimeStr);
		assertEquals(25, passCounter);
	}
	
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("=========case:App paunch and exit  ===============");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","=========case:App paunch and exit  ===============");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
