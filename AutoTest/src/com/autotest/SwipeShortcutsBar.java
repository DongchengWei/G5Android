package com.autotest;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;

import android.os.RemoteException;
import android.util.Log;


/* Case: Shortcuts Bar左右滑动1000次
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.SwipeShortcutsBar
 * 前提：
 * 操作：HOME页面Shortcuts Bar向左滑动，再向右滑动，左右滑动循环1000次
 * */
public class SwipeShortcutsBar extends UiAutomatorTestCase {
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			shortcutsBarSwipeTest();		//压力测试：快捷栏左右滑动压力测试
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void shortcutsBarSwipeTest() {
		try {
			HomePage homePage = new HomePage();
			homePage.goBackHome();
			homePage.goBackFirstShortcutBarPage();
			
			boolean keepTesting = true;
			int testCounter = 0;
			int passCounter = 0;
			while(keepTesting){
				testCounter ++;
				
				Utils.logPrint("exit press settings ...");
				try {
					Utils.logPrint("Swipe left...");
					if (homePage.findAddShortcutBarPage()) {
						Utils.logPrint("Swipe right...");
						if (homePage.findFirstShortcutBarPage()) {
							passCounter ++;
							Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
						} else {
							Utils.logPrint("find first shortcut bar page fail:" + Utils.getNowTime());
						}
					}else {
						Utils.logPrint("find add shortcut bar page fail:" + Utils.getNowTime());
					}
				} catch (UiObjectNotFoundException e) {
					if (homePage.isHomePageExists() == false) {
						keepTesting = false; //退出测试
						Utils.logPrint("You exit test by yourself!");
						Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + (testCounter-1));
					}else {
						Utils.logPrint("UiObject not found,test fail:" + Utils.getNowTime());
					}
				}
			}
			
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObject not found,test fail:" + Utils.getNowTime());
		}
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("====case:Shortcuts Bar swipe pressure test========");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","====case:Shortcuts Bar swipe pressure test========");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
