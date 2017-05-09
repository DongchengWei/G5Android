package com.pageutil;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class ShortcutPage extends UiAutomatorTestCase {
	
	//快捷应用弹窗标题
	UiObject appsShortcutPageObj = new UiObject(new UiSelector()
			.resourceId("com.android.systemui:id/ts_apps_dialog_title"));
	//快捷应用弹窗窗体，操作滑动事件
	UiScrollable shortcutPageScroll = new UiScrollable(new UiSelector()
			.resourceId("com.android.systemui:id/ts_apps_gird_page"));
	
	
	//快捷方式当前页的第一个app(默认时锁屏)
	UiObject firstAppObj = new UiObject(new UiSelector()
			.resourceId("com.android.systemui:id/ts_dialog_app_0"));
	//锁屏后显示的时钟文字
	UiObject lockScreenShowTimeObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/textTime"));
	
	//快捷方式页的app名字
	UiObject appNameObj = new UiObject(new UiSelector()
			.resourceId("com.android.systemui:id/ts_app_name"));
	
	//进入快捷方式页
	public boolean intoShortcutPage() {
		UiDevice.getInstance().click(76, 625);//快捷方式坐标
		if (appsShortcutPageObj.waitForExists(3000)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 在锁屏页锁屏
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean lockScreen() throws UiObjectNotFoundException {
		boolean isOk = false;
		if (firstAppObj.click()) {
			if (lockScreenShowTimeObj.waitForExists(3000)) {
				isOk = true;
			}
		}
		return isOk;
	}
	
	/**
	 * 在锁屏页解锁
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean unlockScreen() throws UiObjectNotFoundException {
		boolean isOk = false;
		if (lockScreenShowTimeObj.click()) {
			if (lockScreenShowTimeObj.waitUntilGone(3000)) {
				isOk = true;
			}
		}
		return isOk;
	}
	
	//获取当前快捷方式页第一个app名字
	public String getFirstAppNameBeforeSwipe() throws UiObjectNotFoundException {
		String returnNameStr = "";
		if (appNameObj.exists()) {
			returnNameStr = appNameObj.getText();
		}
		return returnNameStr;
	}
	//滑动到下一页并获取第一个app名字
	public String swipeToNextPageAndGetFirstAppName() throws UiObjectNotFoundException {
		
		String returnNameStr = "";
		if (shortcutPageScroll.swipeLeft(10)) {
			if (appNameObj.exists()) {
				returnNameStr = appNameObj.getText();
			}
		}
		return returnNameStr;
	}
	//滑动到上一页并获取第一个app名字
	public String swipeToPrevPageAndGetFirstAppName() throws UiObjectNotFoundException {
		
		String returnNameStr = "";
		if (shortcutPageScroll.swipeRight(10)) {
			if (appNameObj.exists()) {
				returnNameStr = appNameObj.getText();
			}
		}
		return returnNameStr;
	}

}
