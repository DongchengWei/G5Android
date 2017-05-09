package com.pageutil;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;

public class AppStorePage extends UiAutomatorTestCase {
	
	//应用推荐列表
	UiObject appstoreObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/appstoreTextView"));
	
	//应用卸载列表
	UiObject appsUninstallObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/messageTextView"));
	//全部卸载按钮
	UiObject uninstallAllButtonObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/imageUninstall"));
	//确定卸载全部应用对话框
	UiObject confirmDialogObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/message"));
	//确定卸载按钮
	UiObject confirmButtonObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/positiveButton"));
	//卸载成功后待卸载应用的listview变为testHit
	UiObject uninstallOkObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/textHit"));
	
	//应用推荐列表的apps list
	UiObject appsRecommendListObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/list"));
	//应用卸载列表的apps list
	UiObject appsUninstallListObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/uninstallList"));
	
	//应用卸载列表的apps list
	UiObject uninstallPageDocuentObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mdm:id/textview_document_title"));
	
	public boolean waitForExists(int seconds) {
		if (appstoreObj.waitForExists(seconds * 1000)) {
			return true;
		}else {
			return false;
		}
	}
	
	//进入到应用推荐列表
	public boolean intoAppsStore() throws UiObjectNotFoundException {
		appstoreObj.click();
		if (appsRecommendListObj.waitForExists(10000)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//进入到应用卸载列表
	public boolean intoAppsUninstall() throws UiObjectNotFoundException {
		appsUninstallObj.click();
		if (uninstallPageDocuentObj.waitForExists(10000)) {
			return true;
		} else {
			return false;
		}
	}
	
	//判断待卸载应用列表是否有应用待卸载
	public boolean isTobeUninstall() throws UiObjectNotFoundException {
		
		boolean thereIsApp = false;
		if (appsUninstallListObj.waitForExists(3000)) {
			thereIsApp = true;
		}
		return thereIsApp;
	}
	//卸载应用
	public boolean uninstallAllApp(long timeout) throws UiObjectNotFoundException {
		
		boolean uninstallOk = false;
		
		uninstallAllButtonObj.click();
		if (confirmDialogObj.waitForExists(3000)) {
			confirmButtonObj.click();
			
			//指定时间
			long startMills = SystemClock.uptimeMillis();
			long currentMills = 0;
			while(currentMills <= timeout){
				
				currentMills = SystemClock.uptimeMillis() - startMills;
				if(timeout > 0) {
					SystemClock.sleep(1000);
				}
				if (uninstallOkObj.exists()) {
					uninstallOk = true;
					currentMills = timeout + 1;
				}
			}
		}
		
		return uninstallOk;
	}
	
	//根据应用名滚动到应用页,并进入应用详情页面
	public boolean scrollIntoAppDetailByName(String appName) throws UiObjectNotFoundException {
		
		boolean intoResult = false;
	     UiScrollable appsListScroll = new UiScrollable( new UiSelector().
	        		resourceId("com.thundersoft.mdm:id/list"));
	     UiObject resultAppObj = appsListScroll.getChildByText(new UiSelector()
	    		 .resourceId("com.thundersoft.mdm:id/textListItemTitle"),appName, true);
	     
	     UiObject resultAppNameObj = new UiObject(new UiSelector()
	    		 .resourceId("com.thundersoft.mdm:id/textListItemTitle")
	    		 .text(appName));
	     
	     UiObject appDetailPageAppNameObj = new UiObject(new UiSelector()
	    		 .resourceId("com.thundersoft.mdm:id/textListDetailItemTitle")
	    		 .text(appName));
	     
	     if (resultAppObj.exists() && resultAppNameObj.exists()) {
	    	 resultAppNameObj.click();
	    	 if (appDetailPageAppNameObj.waitForExists(10000)) {
	    		 intoResult =  true;
			}
		}
	     return intoResult;
	}
	
	//在应用详情页点击安装,timeout为安装超时时间,安装完成返回应用列表界面并返回true
	public boolean installAppInDetailPage(long timeout) throws UiObjectNotFoundException {
		
		boolean installOk = false;
		UiObject installButtonObj = new UiObject(new UiSelector()
				.resourceId("com.thundersoft.mdm:id/buttonListItem"));
		if (installButtonObj.waitForExists(3000)) {
			
			String buttonStateStr = installButtonObj.getText();//获取按钮文本
			if (buttonStateStr.equals("安装") || buttonStateStr.equals("Install")) {
				
				if (installButtonObj.click()) {//点击安装
					//指定时间
					long startMills = SystemClock.uptimeMillis();
					long currentMills = 0;
					while(currentMills <= timeout){
						
						currentMills = SystemClock.uptimeMillis() - startMills;
						if(timeout > 0) {
							SystemClock.sleep(1000);
						}
						buttonStateStr = installButtonObj.getText();//获取按钮文本检查是否安装成功
						if (buttonStateStr.equals("打开") || buttonStateStr.equals("Open")) {
							UiDevice.getInstance().pressBack();//按下返回键返回应用列表界面
							if (waitForExists(3000)) {
								installOk = true;
								currentMills = timeout + 1;
							}
						}
					}
				}
				
			}
		}
		return installOk;
	}
	
	
	//找到安装按钮，需要先scrollFindAppByName滚动到该应用
	public UiObject installButtonByAppName(String appName) throws UiObjectNotFoundException {
		
    	UiObject appsListObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mdm:id/list"));
    	int itemsCounter = appsListObj.getChildCount();
    	UiObject appButtonInstallObj = null;
    	for (int i = 0; i < itemsCounter; i++) {
    		//找到item
    		UiObject appTitleObj = appsListObj.getChild(new UiSelector()
    				.className("android.widget.LinearLayout").index(i)
    				.childSelector(new UiSelector()
    						.resourceId("com.thundersoft.mdm:id/textListItemTitle")));
			if (appTitleObj.getText().equals(appName)) {//存在
				//安装按钮
				UiObject appButtonObject = appsListObj.getChild(new UiSelector()
	    				.className("android.widget.LinearLayout").index(i)
	    				.childSelector(new UiSelector()
	    						.resourceId("com.thundersoft.mdm:id/buttonListItem")));
				if (appButtonObject.exists()) {
					Utils.logPrint("installButton:" + appButtonObject.getText() + ",app title:" + appTitleObj.getText());
					appButtonInstallObj = appButtonObject;
				}else {
					appButtonInstallObj = null;
				}
			}
		}
		return appButtonInstallObj;
	}
	
	//确定安装/确定卸载
	public void confirmButton() throws UiObjectNotFoundException {
		new UiObject(new UiSelector()
				.resourceId("com.thundersoft.mdm:id/positiveButton")).click();
	}
	
	//查找已安装的应用
	public boolean scrollFindAppToUninstallByName(String appName) throws UiObjectNotFoundException {
		
	     UiScrollable appsListScroll = new UiScrollable( new UiSelector().
	        		resourceId("com.thundersoft.mdm:id/uninstallList"));
	     UiObject resultAppObj = appsListScroll.getChildByText(new UiSelector()
	    		 .resourceId("com.thundersoft.mdm:id/textItemTitle"),appName, true);
	     if (resultAppObj.exists()) {
			return true;
		}else {
			return false;
		}
	}
	
	//卸载按钮
	public UiObject uninstallButtonAppByAppName(String appName) throws UiObjectNotFoundException {
		
		
    	UiObject appsListObj = new UiObject(new UiSelector().resourceId("com.thundersoft.mdm:id/uninstallList"));
    	int itemsCounter = appsListObj.getChildCount();
    	
    	UiObject uninstallButtonObj = null;
    	for (int i = 0; i < itemsCounter; i++) {
    		//找到item
    		UiObject appTitleObj = appsListObj.getChild(new UiSelector()
    				.className("android.widget.LinearLayout").index(i)
    				.childSelector(new UiSelector()
    						.resourceId("com.thundersoft.mdm:id/textItemTitle")));
			if (appTitleObj.getText().equals(appName)) {//存在
				UiObject appButtonObject = appsListObj.getChild(new UiSelector()
	    				.className("android.widget.LinearLayout").index(i)
	    				.childSelector(new UiSelector()
	    						.resourceId("com.thundersoft.mdm:id/buttonItem")));
				if (appButtonObject.exists()) {
					uninstallButtonObj = appButtonObject;
				}else {
					uninstallButtonObj = null;
				}
			}
		}
		return uninstallButtonObj;
	}
	
	//根据应用名安装应用
	public boolean installAppByName(String appName) throws UiObjectNotFoundException {
		intoAppsStore();
		scrollIntoAppDetailByName(appName);
		UiObject installButtonObj = installButtonByAppName(appName);
		if (installButtonObj!=null && installButtonObj.exists()) {
			String buttonStateStr = installButtonObj.getText();
			if (buttonStateStr.equals("安装") || buttonStateStr.equals("Install")) {
				installButtonObj.click();
				confirmButton();
				return true;
			}else {
				Utils.logPrint(appName + " have been installed...");
				return false;
			}
		}else {
			Utils.logPrint("install button not found,install fail...");
			return false;
		}
	}
	
}
