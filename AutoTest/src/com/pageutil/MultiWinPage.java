package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class MultiWinPage extends UiAutomatorTestCase {
	
	UiObject smsMultiWinObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/ts_call_menu_sms_text"));
	//第一条短信会话
	UiObject smsItemMultiWinObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/sms_item_layout"));
	
	//第一条短信会话
	UiObject smsFirstContentMultiWinObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/tv_chatContent_smallWindow"));
	//第一条短信会话
	UiObject smsTtsMultiWinObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/sms_chat_tts_smallWindow"));
	
	//进入短信
	public boolean intoSmsMultiWinPage() throws UiObjectNotFoundException {
		boolean isOk = false;
		
		if (smsMultiWinObj.click()) {
			isOk = true;
		}
		
		return isOk;
	}
	//进入第一条会话
	public boolean intoFirstSmsChar() throws UiObjectNotFoundException {
		boolean isOk = false;
		
		if (smsItemMultiWinObj.click()) {
			isOk = true;
		}
		
		return isOk;
	}
	//播报短信
	public boolean playFirstSmsVoice() throws UiObjectNotFoundException {
		boolean isOk = false;
		if (smsFirstContentMultiWinObj.click()) {
			if (smsTtsMultiWinObj.click()) {//播放
				if (smsTtsMultiWinObj.waitUntilGone(120000)) {//2分钟要播报完否则当做失败
					isOk = true;
				} else {
					isOk = false;
				}
			}
		}
		
		return isOk;
	}
}
