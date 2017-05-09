package com.pageutil;

import com.android.uiautomator.core.UiCollection;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;


public class AppsPage extends UiAutomatorTestCase {

	//com.android.launcher:id/ts_apps_grid
	UiSelector appsGridViewSel = new UiSelector()
			.resourceId("com.android.launcher:id/ts_apps_grid");
	UiCollection appsGridViewCol = new UiCollection(new UiSelector()
			.resourceId("com.android.launcher:id/ts_apps_grid"));
	
	UiObject appsGridViewObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/ts_apps_grid"));
	
	public boolean waitForExists(int seconds) {
		if (appsGridViewObj.waitForExists(seconds * 1000)) {
			return true;
		}else {
			return false;
		}
	}
	
	//获取apps数量
	public int AppsCounter() throws UiObjectNotFoundException {
			return appsGridViewCol.getChildCount();
	}
	
	//进入某个app,counter的形式
	public void LaunchOneApp(int appcounter) throws UiObjectNotFoundException {
		
		UiSelector layoutApps = appsGridViewSel.childSelector(new UiSelector()
				.className("android.widget.FrameLayout").index(appcounter-1));
		UiObject targetAppObj = new UiObject(layoutApps);
		targetAppObj.click();
	}
	
	//判断某个app是否存在，通过app name
	public boolean isAppExists(String nameStr) throws UiObjectNotFoundException {
		boolean isOk = false;
		
		UiObject appsNameObj = new UiObject(new UiSelector()
				.resourceId("com.android.launcher:id/ts_app_name").text(nameStr));
		if (appsNameObj.waitForExists(3000)) {
			isOk = true;
		}
		return isOk;
	}
	//进入某个app,app name的形式
	public boolean launchOneAppByName(String nameStr) throws UiObjectNotFoundException {
		boolean isOk = false;
		
		UiObject appsNameObj = new UiObject(new UiSelector()
				.resourceId("com.android.launcher:id/ts_app_name").text(nameStr));
		if (appsNameObj.click()) {
			isOk = true;
		}
		return isOk;
	}
	
	//进入App Store
	public void launchAppStore() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		UiObject appsNameCnObj = new UiObject(new UiSelector()
				.resourceId("com.android.launcher:id/ts_app_name").text("应用商店"));
		UiObject appsNameEnObj = new UiObject(new UiSelector()
				.resourceId("com.android.launcher:id/ts_app_name").text("APP STORE"));
		if (appsNameCnObj.exists()) {
			appsNameCnObj.click();
		}else if (appsNameEnObj.exists()) {
			appsNameEnObj.click();
		}
	}
}
