package com.autotest;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.SettingsPage;
import com.pageutil.WifiTabPage;

import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;


/* 
 * case: 系统BT及WiFi已连接状态下进入锁屏状态
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.WifiBtConnectedLockAndUnlock
 * 前提：车机蓝牙和wifi已连接
 * 步骤：系统BT及WiFi已连接状态下进入锁屏状态，1小时后点击屏幕唤醒
 * 期望：在1小时后点击屏幕唤醒，无异常现象
 * 其他：
 * */
public class WifiBtConnectedLockAndUnlock extends UiAutomatorTestCase {
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			wifiBtConnectedLockAndUnlockTest();		//压力测试：启动/退出app
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private void wifiBtConnectedLockAndUnlockTest() {
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		WifiTabPage wifiPage = new WifiTabPage();
		
		homePage.goBackHome();
		try {
			homePage.intoSettings();
			settingsPage.intoWifiTab();
			if (wifiPage.intoShortcutPage()) {
				if (wifiPage.lockScreenInWifiTab()) {
					Utils.logPrint("lock ok,lock time: " + Utils.getNowTime());
					SystemClock.sleep(60000);//锁屏时间
					if (wifiPage.unlockScreenInWifiTab()) {
						Utils.logPrint("unlock ok,unlock time: "  + Utils.getNowTime());
						if (wifiPage.isWifiConnected(30000)) {
							Utils.logForResult("wifiBtConnectedLockAndUnlock test pass");
						}
					}
				}
			}
			
			
			
		} catch (UiObjectNotFoundException e1) {
			Utils.logPrint("Settings UiObject not found");
		}
	}
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("====case:Wifi&Bt connected lock and unlock =======");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","====case:Wifi&Bt connected lock and unlock =======");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
