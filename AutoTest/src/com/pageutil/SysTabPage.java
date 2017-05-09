package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;

public class SysTabPage extends UiAutomatorTestCase {
	
	UiObject multiWinSwitchObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/switch_view"));
	
	UiObject multiWinListObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_system_list"));
	
	//系统升级
	UiObject updateItemCnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_item_txt").text("系统升级"));
	UiObject updateItemEnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_item_txt").text("System Upgrade"));
	
	
	//进入系统升级
	public boolean intoSystemUpdatePage() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (updateItemCnObj.exists()) {
			if (updateItemCnObj.click()) {
				isOk = true;
			} else if (updateItemEnObj.exists()) {
				if (updateItemEnObj.click()) {
					isOk = true;
				}
			}
		}
		return isOk;
	}
	
	//判断多窗口开关是否打开
	public boolean isMultiWinOn() throws UiObjectNotFoundException {
		
		if (multiWinSwitchObj.isChecked()) {
			return true;
		} else {
			return false;
		}
	}
	
	//打开多窗口开关,等待多久
	public boolean turnOnMultiWin(long timeout) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (isMultiWinOn()) {//已经是打开状态
			isOk = true;
		} else {			//关闭状态
			if (multiWinSwitchObj.click()) {//点击开关
				//指定时间
				long startMills = SystemClock.uptimeMillis();
				long currentMills = 0;
				while(currentMills <= timeout){
					
					currentMills = SystemClock.uptimeMillis() - startMills;
					if(timeout > 0) {
						SystemClock.sleep(1000);
					}
					if (isMultiWinOn()) {
						isOk = true;
						currentMills = timeout + 1;
					}
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 关闭多窗口开关,等待timeout
	 * */
	public boolean turnOffMultiWin(long timeout) throws UiObjectNotFoundException {
		
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (!isMultiWinOn()) {//已经是打开状态
			isOk = true;
		} else {			//关闭状态
			if (multiWinSwitchObj.click()) {//点击开关
				//指定时间
				long startMills = SystemClock.uptimeMillis();
				long currentMills = 0;
				while(currentMills <= timeout){
					
					currentMills = SystemClock.uptimeMillis() - startMills;
					if(timeout > 0) {
						SystemClock.sleep(1000);
					}
					if (! isMultiWinOn()) {
						isOk = true;
						currentMills = timeout + 1;
					}
				}
			}
		}
		return isOk;
	}
	
}
