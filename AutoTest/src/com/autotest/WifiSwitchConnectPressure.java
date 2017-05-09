package com.autotest;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.SettingsPage;
import com.pageutil.WifiTabPage;

import android.os.RemoteException;
import android.util.Log;

/* Case: wifi打开-自动连接-切换连接-关闭	100次
 * 前提：1. wifi有已保存两个热点以上，当前wifi处于关闭状态。
 * 操作：多次反复的执行 wifi打开-自动连接-切换连接-关闭
 * */
public class WifiSwitchConnectPressure extends UiAutomatorTestCase {
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			WifiSwitchConnectPressureTest();		//压力测试：启动/退出app
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean WifiSwitchConnectPressureTest() {
		boolean isTestPass = false;
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		WifiTabPage wifiPage = new WifiTabPage();
		
		homePage.goBackHome();
		try {
			homePage.intoSettings();
			settingsPage.intoWifiTab();
			boolean keepTesting = true;
			int testCounter = 0;
			int passCounter = 0;
			while(keepTesting){
				testCounter ++;
				
				if (! wifiPage.isWifiOn()) {
					Utils.logPrint("turn on wifi");
					wifiPage.turnOnWifi();
					
					if (wifiPage.isWifiConnected(30000)) {
						Utils.logPrint("wifi Connected:" + wifiPage.getConnectedWifiName());
						if (wifiPage.switchWifiAP(30000)) {
							Utils.logPrint("switch connect " + wifiPage.getConnectedWifiName() + " OK");
							Utils.logPrint("turn off wifi,exit test press home in 10s...");
							wifiPage.turnOffWifi();
							//打开wifi后等待十秒让wifi连接上,十秒内切换到其他界面则退出测试
							if (wifiPage.waitUntilGone(10000)) {
								keepTesting = false;
							}
							if (!wifiPage.isWifiOn()) {
								passCounter ++;
								Utils.logPrint("turn off OK");
								Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
							}else {
								Utils.logPrint("turn off fail,exit test...");
								keepTesting = false;
							}
						}else {
							Utils.logPrint("wifi switch AP fail,exit test");
							keepTesting = false;
						}
					}else {
						Utils.logPrint("wifi connect fail,exit test");
						keepTesting = false;
					}
				}else {
					wifiPage.turnOffWifi();
					sleep(10000);
					wifiPage.turnOnWifi();
					if (wifiPage.isWifiConnected(30000)) {
						Utils.logPrint("wifi Connected:" + wifiPage.getConnectedWifiName());
						if (wifiPage.switchWifiAP(30000)) {
							Utils.logPrint("switch connect " + wifiPage.getConnectedWifiName() + "OK");
							Utils.logPrint("turn off wifi,exit test press home in 10s...");
							wifiPage.turnOffWifi();
							//打开wifi后等待十秒让wifi连接上,十秒内切换到其他界面则退出测试
							if (wifiPage.waitUntilGone(10000)) {
								keepTesting = false;
							}
							if (!wifiPage.isWifiOn()) {
								passCounter ++;
								Utils.logPrint("turn off OK");
								Utils.logPrint("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
							}else {
								Utils.logPrint("turn off fail");
								keepTesting = false;
								
							}
						}else {
							Utils.logPrint("wifi switch AP fail");
							keepTesting = false;
						}
					}else {
						Utils.logPrint("wifi connect fail");
						keepTesting = false;
					}
				}
				if (testCounter == 100) {
					if (testCounter == 1000) {
						if (passCounter == testCounter) {
							isTestPass = true;//测试通过
						}
						keepTesting = false;
						Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
					}
				}
			}
		} catch (UiObjectNotFoundException e1) {
			Utils.logPrint("Settings UiObject not found");
		}
		
		return isTestPass;
		
	}
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("===case:Wifi on & Auto connect & swithc connect===");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","===case:Wifi on & Auto connect & swithc connect===");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
