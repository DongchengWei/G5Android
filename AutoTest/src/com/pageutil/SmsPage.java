package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class SmsPage extends UiAutomatorTestCase {

	//短信中心列表某项
	UiObject smsItemObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/sms_item_layout"));
	//短信会话界面
	UiObject smsChatObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/chat_name"));
	//短信会话列表
	UiObject smsChatListObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/chat_msg_listview"));
	
	
	//进入第一个短信会话
	public boolean intoFirstChatList() throws UiObjectNotFoundException {
		smsItemObj.click();
		if (smsChatObj.exists()) {
			return true;
		} else {
			return false;
		}
	}
	//播放第一条短信内容
	public boolean playFirstSmsChat() throws UiObjectNotFoundException {
		
		boolean playOk = false;
		UiObject smsContentObj = new UiObject(new UiSelector()
				.resourceId("com.thundersoft.call:id/tv_chatcontent"));
		smsContentObj.click();//点击短信内容，会出现语音播放按钮
		UiObject smsVoicePlayObj = new UiObject(new UiSelector()
				.resourceId("com.thundersoft.call:id/sms_chat_tts"));
		if (smsVoicePlayObj.waitForExists(3000)) {
			smsVoicePlayObj.click();//点击语音播放按钮开始播报短信
			if (smsVoicePlayObj.waitUntilGone(120000)) {//2分钟要播报完否则当做失败
				playOk = true;
			} else {
				playOk = false;
			}
		}
		return playOk;
	}
}
