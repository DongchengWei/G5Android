package com.pageutil;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

public class HomePage extends UiAutomatorTestCase {
	
	UiObject nvaObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_home_button_item_nav_id"));
	UiObject saveDrivingObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_home_button_item_safe_id"));
	UiObject multiMediaObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_home_button_item_media_id"));
	UiObject appsObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_home_button_item_apps_id"));
	UiObject phoneObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_home_button_item_call_id"));
	UiObject settingObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_home_button_item_settings_id"));
	
	//进入媒体后的媒体标题对象
	UiObject mediaTitleObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/media_title"));
	
	//锁屏
	UiObject lockScreenChObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_app_name").text("锁屏"));
	UiObject lockScreenEnObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_app_name").text("Lock screen"));
	//锁屏界面（显示时钟）
	UiObject lockScreenPageObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/textTime"));
	
	//添加快捷方式
	UiObject addEnObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_app_name").text("Add"));
	UiObject addCnObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_app_name").text("添加"));
	
	//主界面右下角的快捷栏
	UiScrollable shortcutsBarScroll = new UiScrollable(new UiSelector()
			.className("android.support.v4.view.ViewPager"));
	
	//快捷栏第一页
	UiObject firstshortcutBarPageObj = new UiObject(new UiSelector()
			.className("android.widget.ImageView").instance(0));
	
	//home界面是否仍存在
	public boolean isHomePageExists() {
		if (settingObj.exists()) {
			return true;
		} else {
			return false;
		}
	}
	//等待home界面消失
	public boolean waitUntilGone(int millSecond) {
		if (shortcutsBarScroll.waitUntilGone(5000)) {
			return true;
		}else {
			return false;
		}
	}
	//判断当前是否在快捷栏第一页，是返回true
	public boolean isFirstShortcutBarPage() {
		if (lockScreenChObj.exists() || lockScreenEnObj.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	//是否添加快捷页
	public boolean isAddShortcutBarPage() {
		if (addCnObj.exists() || addEnObj.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	//滑动找到第一快捷页
	public boolean findFirstShortcutBarPage() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		int findCouter = 0;
		boolean foundTarget = false;
		while(findCouter < 10){
			if (isFirstShortcutBarPage()) {
				foundTarget = true;
				findCouter = 11;
			}else {
				shortcutsBarScrollPrev();
				findCouter ++;
			}
		}
		return foundTarget;
	}
	//滑动找到添加快捷页
	public boolean findAddShortcutBarPage() throws UiObjectNotFoundException {
		int findCouter = 0;
		boolean foundTarget = false;
		while(findCouter < 10){
			if (isAddShortcutBarPage()) {
				foundTarget = true;
				findCouter = 11;
			}else {
				shortcutsBarScrollNext();
				findCouter ++;
			}
		}
		return foundTarget;
	}
	
	//判断是否正在锁屏页面
	public boolean isLockScreenPageExists() {
		if (lockScreenPageObj.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	//锁屏
	public boolean lockScreenInHomePage() throws UiObjectNotFoundException {
		boolean lockOk = false;
		if (lockScreenChObj.exists()) {
			lockScreenChObj.click();
			if (isLockScreenPageExists()) {
				lockOk = true;
			}
		} else if (lockScreenEnObj.exists()) {
			lockScreenEnObj.click();
			if (isLockScreenPageExists()) {
				lockOk = true;
			}
		} else {
			return false;
		}
		
		return lockOk;
	}
	
	//解锁
	public boolean unlockScreenInHomePage() throws UiObjectNotFoundException {
		boolean isOk = false;
		
		if (lockScreenPageObj.click()) {
			if (lockScreenPageObj.waitUntilGone(3000)) {
				isOk = true;
			}
		}
		
		return isOk;
	}
	
	//返回第一页
	public boolean goBackFirstShortcutBarPage() throws UiObjectNotFoundException {
		firstshortcutBarPageObj.click();
		if (isFirstShortcutBarPage()) {
			return true;
		} else {
			return false;
		}
	}
	//滑动shortcutsBar到下一页
	public void shortcutsBarScrollNext() throws UiObjectNotFoundException {
		shortcutsBarScroll.swipeLeft(10);
	}
	
	//滑动shortcutsBar到上一页
	public void shortcutsBarScrollPrev() throws UiObjectNotFoundException {
		shortcutsBarScroll.swipeRight(10);
	}
	//返回home界面
	public void goBackHome() {
		Utils.getCurrentMethodName();
		UiDevice.getInstance().pressHome();
	}
	
	//等待home页面出现
	public boolean waitForExists(int seconds) {
		if (settingObj.waitForExists(seconds * 1000)) {
			return true;
		}else {
			return false;
		}
	}
	
	//进入多媒体
	public boolean intoMultimedia() throws UiObjectNotFoundException{
		Utils.getCurrentMethodName();
		boolean intoOk = false;
		if (multiMediaObj.click()) {
			intoOk = true;
		}
		return intoOk;
	}
	
	//进入电话通讯
	public boolean intoPhone() throws UiObjectNotFoundException{
		Utils.getCurrentMethodName();
		if (phoneObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	
	//进入设置
	public boolean intoSettings() throws UiObjectNotFoundException{
		Utils.getCurrentMethodName();
		if (settingObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	
	//进入车机所有应用
	public boolean intoApps() throws UiObjectNotFoundException{
		Utils.getCurrentMethodName();
		if (appsObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	
	//根据应用名添加该应用快捷方式
	public boolean addShortcutByAppName(String appName) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		if (addCnObj.exists()) {
			addCnObj.click();
		} else if (addEnObj.exists()) {
			addEnObj.click();
		}
		
		boolean addState = false;
		UiObject appNameObj = new UiObject(new UiSelector()
				.resourceId("com.android.launcher:id/ts_app_name")
				.text(appName));
		if (appNameObj.exists()) {
			appNameObj.click();//点击该应用名
			UiDevice.getInstance().click(10, 10);//点击到快捷页外
			if (settingObj.waitForExists(3000)) {
				if (appNameObj.exists()) {
					addState = true;
				}
			}
		} 
		return addState;
	}
	
	//根据应用名删除该应用快捷方式
	public boolean deleteShortcutByAppName(String appName) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		if (addCnObj.exists()) {
			addCnObj.click();
		} else if (addEnObj.exists()) {
			addEnObj.click();
		}
		
		boolean addState = false;
		UiObject appNameObj = new UiObject(new UiSelector()
				.resourceId("com.android.launcher:id/ts_app_name")
				.text(appName));
		if (appNameObj.exists()) {
			appNameObj.click();//点击该应用名
			UiDevice.getInstance().click(10, 10);//点击到快捷页外
			if (settingObj.waitForExists(3000)) {
				if (appNameObj.waitForExists(1000)) {
					addState = false;
				} else {
					addState = true;
				}
			}
		} 
		return addState;
	}
	
	
}
