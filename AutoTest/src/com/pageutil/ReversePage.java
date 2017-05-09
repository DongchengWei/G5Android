package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;


public class ReversePage extends UiAutomatorTestCase {

	//隐藏和显示倒车设置的按钮
	UiObject showRvcSettingObj = new UiObject(new UiSelector()
			.resourceId("com.svauto.fastrvc:id/isTabImage"));
	
	//亮度设置
	UiObject lightObj = new UiObject(new UiSelector()
			.resourceId("com.svauto.fastrvc:id/light"));
	
	/*
	 * 函数功能：进入倒车
	 * 参数	      ：none
	 * 返回值    ：boolean,成功返回true
	 * */
	public boolean intoReversePage() {
		boolean isOk = false;
		
		Utils.execCmd("vehicle.fakervcdctl on");//命令行执行进入倒车命令
		if (showRvcSettingObj.waitForExists(5000)) {
			isOk = true;
		}
		
		return isOk;
	}
	
	/*
	 * 函数功能：退出倒车
	 * 参数	      ：none
	 * 返回值    ：boolean,成功返回true
	 * */
	public boolean exitReversePage() {
		boolean isOk = true;
		
		Utils.execCmd("vehicle.fakervcdctl off");//命令行执行进入倒车命令
		SystemClock.sleep(1000);
		return isOk;
	}
	
	/*
	 * 函数功能：显示倒车画面设置菜单
	 * 参数	      ：none
	 * 返回值    ：boolean,成功返回true
	 * */
	public boolean showRvcSettingBar() throws UiObjectNotFoundException {
		boolean isOk = false;
		
		if (lightObj.exists()) {
			isOk = true;
		} else {
			if (showRvcSettingObj.exists()) {
				if (showRvcSettingObj.click()) {
					if (lightObj.exists()) {
						isOk = true;
					}
				}
			}
		}
		return isOk;
	}
	
	/*
	 * 函数功能：隐藏倒车画面设置菜单
	 * 参数	      ：none
	 * 返回值    ：boolean,成功返回true
	 * */
	public boolean hideRvcSettingBar() throws UiObjectNotFoundException {
		boolean isOk = false;
		
		if (lightObj.exists()) {
			if (showRvcSettingObj.click()) {
				if (lightObj.waitUntilGone(3000)) {
					isOk = true;
				}
			}
		} else {
			isOk = true;
		}
		return isOk;
	}
	
}
