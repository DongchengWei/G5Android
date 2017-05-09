package com.autotest;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;

import android.os.RemoteException;
import android.util.Log;


/*
 * case: Shortcuts Bar添加删除快捷启动应用500次
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.AddAndDeleteShortcut
 * 前提：高德地图未在HOME页面Shortcuts Bar中
 * 步骤：HOME页面Shortcuts Bar向左滑动，点击“+”添加按钮，
 * 		点击高德地图，点击快捷应用页外围（退出快捷应用页），
 * 		添加高德地图为快捷应用成功，再次点击Shortcuts Bar的“+“按钮，
 * 		点击高德地图，退出快捷应用页，删除高德地图为快捷应用成功。
 * 		连续添加删除500次
 * 期望：能正常执行添加删除高德地图快捷图标到Shortcuts Bar 500次
 * 其他：仅在中文下测试
 * */
public class AddAndDeleteShortcut extends UiAutomatorTestCase {
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			addAndDeleteShortcutTest();		//压力测试：快捷栏左右滑动压力测试
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}	
	
	private void addAndDeleteShortcutTest() {
		HomePage homePage = new HomePage();
		homePage.goBackHome();
		
		
		boolean keepTesting = true;
		int testCounter = 0;
		int testPassCounter = 0;
		
		while(keepTesting){
			testCounter ++;
			Utils.logPrint("exit press settings ...");
			try {
				Utils.logPrint("find add button...");
				if (homePage.findAddShortcutBarPage()) {
					Utils.logPrint("Add and delete a shortcut...");
					if (homePage.addShortcutByAppName(" 高德地图 ")) {
						if (homePage.deleteShortcutByAppName(" 高德地图 ")) {
							testPassCounter ++;
							Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
						} else {
							Utils.logPrint("Delete shortcut fail:" + Utils.getNowTime());
							keepTesting = false;
						}
					} else {
						Utils.logPrint("Add shortcut fail:" + Utils.getNowTime());
						keepTesting = false;
					}
				}else {
					Utils.logPrint("find add shortcut bar page fail:" + Utils.getNowTime());
					keepTesting = false;
				}
			} catch (UiObjectNotFoundException e) {
				if (homePage.isHomePageExists() == false) {
					keepTesting = false; //退出测试
					Utils.logPrint("You exit test by yourself!");
					Utils.logForResult("Test Pass:" + testPassCounter + " ,Total Test:" + (testCounter-1));
				}else {
					Utils.logPrint("UiObject not found,test fail:" + Utils.getNowTime());
				}
			}
		}
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("====case:Shortcuts add and delete pressure test===");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","====case:Shortcuts add and delete pressure test===");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
