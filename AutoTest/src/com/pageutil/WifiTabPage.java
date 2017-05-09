package com.pageutil;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;

public class WifiTabPage extends UiAutomatorTestCase {
	
	//wifi连接按钮
	UiObject confirmConnectObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/dialog_confirm"));
	//wifi开关
	UiObject wifiSwitchObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/switch_view"));
	//wifi列表
	UiObject wifiListObj = new UiObject(new UiSelector()
			.resourceId("android:id/list"));
	
	//wifi列表选择器
	UiSelector listViewSel = new UiSelector()
			.resourceId("android:id/list");
	
	UiObject wifiTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_wifi_id"));
	
	//快捷应用弹窗标题
	UiObject appsShortcutPageObj = new UiObject(new UiSelector()
			.resourceId("com.android.systemui:id/ts_apps_dialog_title"));
	
	//锁屏
	UiObject lockScreenChObj = new UiObject(new UiSelector()
			.resourceId("com.android.systemui:id/ts_app_name").text("锁屏"));
	UiObject lockScreenEnObj = new UiObject(new UiSelector()
			.resourceId("com.android.systemui:id/ts_app_name").text("Lock screen"));
	//锁屏界面（显示时钟）
	UiObject lockScreenPageObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/textTime"));
	
	//等待wifi界面消失
	public boolean waitUntilGone(int millSecond) {
		boolean isGone = false;
		if (wifiTabObj.waitUntilGone(millSecond)) {
			isGone = true;
		}else {
			isGone = false;
		}
		return isGone;
	}
	//获取wifi列表中每一项的object,0为第一项
	private UiObject getWifiItemObject(int itemIndex) {
		UiObject wifiItemsObj;
		UiSelector wifiAPItemSel = listViewSel
				.childSelector(new UiSelector()
						.className("android.widget.LinearLayout").index(itemIndex));
		wifiItemsObj = new UiObject(wifiAPItemSel);
		return wifiItemsObj;
	}
	//判断wifi开关是否处于打开状态
	public boolean isWifiOn() throws UiObjectNotFoundException {
		boolean wifiChecked = false;
		if (wifiSwitchObj.isChecked()) {
			wifiChecked = true;
		}
		return wifiChecked;
	}
	
	//打开wifi开关
	public boolean turnOnWifi() throws UiObjectNotFoundException {
		boolean isOk = false;
		if (isWifiOn()) {
			isOk = true;
		} else {
			if (wifiSwitchObj.click()) {
				if (isWifiOn()) {
					isOk = true;
				}
			}
		}
		return isOk;
	}
	
	//关闭wifi开关
	public boolean turnOffWifi() throws UiObjectNotFoundException {
		boolean isOk = false;
		if (isWifiOn() == false) {
			isOk = true;
		} else {
			if (wifiSwitchObj.click()) {
				if (isWifiOn() == false) {
					isOk = true;
				}
			}
		}
		return isOk;
	}
	
	//判断是否已连接上wifi，已连接返回true,30秒
	public boolean isWifiConnected(long timeout) {
		
		boolean isConnected = false;
		
		try {
			wifiListObj.waitForExists(30000);
			UiObject wifiItemsObj = getWifiItemObject(0);
			wifiItemsObj.waitForExists(30000);
			UiObject connectedStateObj = wifiItemsObj
					.getChild(new UiSelector()
							.resourceId("android:id/summary"));
			
			String wifiStateStr = connectedStateObj.getText();
			long startMills = SystemClock.uptimeMillis();
			long currentMills = 0;
			while(currentMills <= timeout){
				
				currentMills = SystemClock.uptimeMillis() - startMills;
				if(timeout > 0) {
					SystemClock.sleep(1000);
				}
				
				wifiStateStr = connectedStateObj.getText();
				if (wifiStateStr.equals("已连接") || wifiStateStr.equals("Connected")) {
					isConnected = true;
					currentMills = timeout + 1;
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("wifi state object not found");
		}
		return isConnected;
	}
	
	//获取wifi名称（前提是wifi已连接成功）
	public String getConnectedWifiName() {
		
		String wifiTitleStr = "";
		try {
			UiObject connectedTitleObj = getWifiItemObject(0)
			.getChild(new UiSelector()
					.resourceId("android:id/title"));
			wifiTitleStr = connectedTitleObj.getText();
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("wifi title object not found");
		}
		
		return wifiTitleStr;
	}
	
	//切换连接第二个已保存的wifi，连接成功返回true
	public boolean switchWifiAP(long timeout) {
		
		boolean isSwitchOk = false;
		UiObject switchToObj = getWifiItemObject(1);
		String switchToStr;
		try {
			//获取待切换到的wifi名称
			switchToStr = switchToObj
					.getChild(new UiSelector()
					.resourceId("android:id/title"))
					.getText();
			
			switchToObj.click();		//点击待切换到的wifi
			confirmConnectObj.click();	//点击弹出的窗口的连接按钮
			
			sleep(10000);     			//等待10s连接成功
			UiObject afterSwitchItemObj = getWifiItemObject(0);
			String afterSwitchTitleStr = afterSwitchItemObj
					.getChild(new UiSelector()
					.resourceId("android:id/title"))
					.getText();
			if (afterSwitchTitleStr.equals(switchToStr)) {//点击连接后该热点会升到第一行
				
				//指定时间连接
				long startMills = SystemClock.uptimeMillis();
				long currentMills = 0;
				while(currentMills <= timeout){
					
					currentMills = SystemClock.uptimeMillis() - startMills;
					if(timeout > 0) {
						SystemClock.sleep(1000);
					}
					String afterSwitchStateStr =  afterSwitchItemObj
							.getChild(new UiSelector()
									.resourceId("android:id/summary"))
							.getText();
					if (afterSwitchStateStr.equals("已连接") || afterSwitchStateStr.equals("Connected")) {
						isSwitchOk = true;
						currentMills = timeout + 1;
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObject not found");
		}
		return isSwitchOk;
	}
	
	//进入快捷方式页
	public boolean intoShortcutPage() {
		UiDevice.getInstance().click(76, 625);
		if (appsShortcutPageObj.waitForExists(3000)) {
			return true;
		} else {
			return false;
		}
	}
	
	//锁屏
	public boolean lockScreenInWifiTab() throws UiObjectNotFoundException {
		
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
			lockOk = false;
		}
		return lockOk;
	}
	
	//判断是否正在锁屏页面
	public boolean isLockScreenPageExists() {
		if (lockScreenPageObj.exists()) {
			return true;
		} else {
			return false;
		}
	}
	//解锁
	public boolean unlockScreenInWifiTab() throws UiObjectNotFoundException {
		
		boolean unlockOk = false;
		UiDevice.getInstance().click(76, 625);//点击屏幕
		if (lockScreenPageObj.waitUntilGone(3000)) {
			unlockOk = true;
		} 
		return unlockOk;
	}
	
}
