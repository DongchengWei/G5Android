package com.autotest;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.AppStorePage;
import com.pageutil.AppsPage;
import com.pageutil.HomePage;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.util.Log;


/* 
 * case: 安装卸载QQ音乐   100次
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.InstallAndUninstallApp
 * 前提：车机已连接上网络（wifi）并且网速不应过慢
 * 步骤：HOME->本地应用->应用商店->qq音乐->安装，
 * 		安装成功后切到应用卸载页面，把qq音乐卸载（安装、卸载重复100次）
 * 期望：安装卸载QQ音乐 100次
 * 注意事项：本次测试会卸载所有已安装应用
 * */
public class InstallAndUninstallApp extends UiAutomatorTestCase {
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.autotest.InstallAndUninstallApp", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();//监视anr
			
			installAndUninstallAppTest();		//压力测试：快捷栏左右滑动压力测试
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private boolean installAndUninstallAppTest() {
		
		boolean testPass = false;
		try {
			HomePage homePage = new HomePage();
			AppsPage appsPage = new AppsPage();
			AppStorePage appStorePage = new AppStorePage();
			homePage.goBackHome();
			if (homePage.intoApps()) {
				Utils.logPrint("Launch Apps Store...");
				appsPage.launchAppStore();
			}
			
			
			long timeout = 3600000;//超时时间
			
			boolean canStartTest = false;
			appStorePage.intoAppsUninstall();
			if (appStorePage.isTobeUninstall()) {
				if (appStorePage.uninstallAllApp(timeout)) {
					canStartTest = true;
				}else {
					canStartTest = false;
				}
			}else {
				canStartTest = true;
			}
			
			
			boolean keepTesting = true;
			int testCounter = 0;
			int passCounter = 0;
			
			if (canStartTest) {
				while(keepTesting){
					testCounter ++;
					
						appStorePage.intoAppsStore();
						String appName = "QQ音樂";
						Utils.logPrint("find app:" + appName);
						appStorePage.scrollIntoAppDetailByName(appName);
						Utils.logPrint("install app:" + appName);
						if (appStorePage.installAppInDetailPage(timeout)) {
							Utils.logPrint("Apps install OK...");
							appStorePage.intoAppsUninstall();
							Utils.logPrint("unstall apps...");
							if (appStorePage.uninstallAllApp(timeout)) {
								passCounter ++;
								Utils.logPrint("Apps unstall OK...");
								Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
							}else {
								Utils.logPrint("Apps unstall fail..." + Utils.getNowTime());
								keepTesting = false;
							}
						} else {
							Utils.logPrint("Apps install fail..." + Utils.getNowTime());
							keepTesting = false;
						}
						if (testCounter == 100) {
							if (passCounter == testCounter) {
								testPass = true;
								Utils.logPrint("Test pass..." + Utils.getNowTime());
								Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
							}else {
								Utils.logPrint("Test fail..." + Utils.getNowTime());
								Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
							}
						}
				}
			}else {
				Utils.logPrint("Apps can not uninstall..." + Utils.getNowTime());
				keepTesting = false;
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObject not found,test fail:" + Utils.getNowTime());
		}
		return testPass;
	}
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("====case:install and uninstall app pressure test==");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","====case:install and uninstall app pressure test==");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
