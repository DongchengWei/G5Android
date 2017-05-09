package com.pageutil;

import com.android.uiautomator.core.UiCollection;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

public class SettingsPage extends UiAutomatorTestCase {
	
	UiObject wifiTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_wifi_id"));
	UiObject btTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_bluetooth_id"));
	UiObject soundTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_sound_id"));
	//显示Tab
	UiObject displayTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_display_id"));
	//显示设置的list
	UiCollection displaySettingListCol = new UiCollection(new UiSelector()
			.resourceId("com.android.settings:id/ts_display_settings_list"));
	//语言设置的list
	UiCollection languageSeleteListCol = new UiCollection(new UiSelector()
			.resourceId("com.android.settings:id/ts_language_settings_list"));
	//显示和语言设置的list的item的ID,ID都是一样
	public final String DISPLAY_LIST_ITEM_ID = "com.android.settings:id/ts_item_txt";
	
	
	//时间Tab
	UiObject timeTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_time_id"));
	//系统
	UiObject systemTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_system_id"));
	
	public boolean waitForExists(int seconds) {
		if (wifiTabObj.waitForExists(seconds * 1000)) {
			return true;
		}else {
			return false;
		}
	}
	
	//进入蓝牙设置
	public boolean intoBtTab() throws UiObjectNotFoundException{
		Utils.getCurrentMethodName();
		if (btTabObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	
	//进入wifi设置
	public boolean intoWifiTab() throws UiObjectNotFoundException{
		Utils.getCurrentMethodName();
		if (wifiTabObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	//进入系统设置
	public boolean intoSystemTab() throws UiObjectNotFoundException{
		Utils.getCurrentMethodName();
		if (systemTabObj.click()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 进入显示页
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean intoDisplayTab() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		if (displayTabObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 进入语言设置
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean intoLanguageSetting() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (displaySettingListCol.waitForExists(3000)) {
			UiObject displaySettingObj = displaySettingListCol.getChildByInstance(new UiSelector()
					.resourceId("com.android.settings:id/ts_item_txt"), 1);//1为语言设置，0为日间模式和亮度设置
			if (displaySettingObj != null) {
				if (displaySettingObj.click()) {
					isOk = true;
				}
			}
		}
		
		return isOk;
	}
	
	/**
	 * 选择中文
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean seleteChinese() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		UiObject languageSeleteObj = languageSeleteListCol.getChildByInstance(new UiSelector()
				.resourceId(DISPLAY_LIST_ITEM_ID), 0);//0为中文，1为英文
		if (languageSeleteObj != null) {
			if (languageSeleteObj.click()) {
				isOk = true;
			}
		}
		
		return isOk;
	}
	/**
	 * 选择英文
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean seleteEnglish() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		UiObject languageSeleteObj = languageSeleteListCol.getChildByInstance(new UiSelector()
				.resourceId(DISPLAY_LIST_ITEM_ID), 1);//0为中文，1为英文
		if (languageSeleteObj != null) {
			if (languageSeleteObj.click()) {
				isOk = true;
			}
		}
		
		return isOk;
	}
}
