package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class FmAmPage extends UiAutomatorTestCase {
	
	//FM界面
	UiObject fmTitleObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/media_title").text("FM/AM"));
	//手动/自动扫描开关
	UiObject autoScanSwitchObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/switch_view").instance(0));
	//FM/AM转换开关
	UiObject fmAmSwitchObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/switch_view").instance(1));
	//开始扫描按钮
	UiObject scanStartObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/ts_auto_scan"));
	//当前频率
	UiObject freqObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/ib_ts_radio_freq_text"));
	
	//等待FM界面消失
	public boolean waitUntilGone(int millSecond) {
		if (fmTitleObj.waitUntilGone(millSecond)) {
			return true;
		}else {
			return false;
		}
	}
	
	//是否是自动扫描模式，是返回true,否则是手动返回false
	public boolean isAutoScan() throws UiObjectNotFoundException {
		if (autoScanSwitchObj.isChecked()) {
			return true;
		}else {
			return false;
		}
	}
	//是否是FM模式，是返回true,否则是手动返回false
	public boolean isFmMode() throws UiObjectNotFoundException {
		if (fmAmSwitchObj.isChecked()) {
			return false;
		}else {
			return true;
		}
	}
	
	//切换到自动扫描模式，切换成功返回true否则false
	public boolean switchToAutoScan() throws UiObjectNotFoundException{
		if (isAutoScan()) {
			return true;
		}else {
			autoScanSwitchObj.click();
			sleep(1000);
			if (isAutoScan()) {
				return true;
			}else {
				return false;
			}
		}
	}	
	
	//切换到FM模式，切换成功返回true否则false
	public boolean switchToFmMode() throws UiObjectNotFoundException{
		if (isFmMode()) {
			return true;
		}else {
			fmAmSwitchObj.click();
			sleep(1000);
			if (isFmMode()) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	//切换到手动扫描模式，切换成功返回true否则false
	public boolean switchToManualScan() throws UiObjectNotFoundException{
		if (isAutoScan()) {
			autoScanSwitchObj.click();
			sleep(1000);
			if (isAutoScan()) {
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
	}
	//切换到AM模式，切换成功返回true否则false
	public boolean switchToAmMode() throws UiObjectNotFoundException{
		if (isFmMode()) {
			fmAmSwitchObj.click();
			sleep(1000);
			if (isFmMode()) {
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
	}
	
	//获取当前FM/AM显示频率值
	public String getFrequencyText() throws UiObjectNotFoundException {
		return freqObj.getText();
	}
	
	//自动扫描开始
	public void scanStart() throws UiObjectNotFoundException {
		scanStartObj.click();
	}
	
	public boolean isNowFrequencyButtonExists(String freqStr) {
		//当前频率按键
		UiObject freqButtonObj = new UiObject(new UiSelector()
				.resourceId("android.widget.Button").text(freqStr + "MHZ"));
		if (freqButtonObj.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
