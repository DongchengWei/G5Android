package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

public class PhonePage extends UiAutomatorTestCase {
	
	//电话通讯页面标题
	UiObject phonePageTitleObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/txt_title"));
	
	//拨号控件
	UiObject phonePageDialPadObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/ts_call_menu_key_id"));
	/**
	 * 拨号盘对象
	 */
	UiObject gridNumberObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/grid_number"));
	
	//拨号给客服控件
	UiObject phonePageServiceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/ts_call_menu_service_id"));
	
	//联系人控件
	UiObject phonePageContactsObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/ts_call_menu_book_id"));
	//联系人页面
	UiObject contactsPageObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/img_search"));
	
	//通话记录
	UiObject recentsObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/ts_call_menu_record_id"));
	//所有记录
	UiObject recentsAllObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/btn_record_all"));
	
	
	
	//短信控件
	UiObject phonePageSmsObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/ts_call_menu_sms_id"));
	//短信中心列表
	UiObject phonePageSmsCenterObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/sms_center_title_tv"));
	
	//设置控件
	UiObject phonePageSettingObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/ts_call_menu_setting_id"));
	
	//挂断按钮
	UiObject endCallObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/btn_call"));
	
	//判断是否存在
	public boolean waitForExists(long timeout) {
		boolean	isOk = false;
		
		if (phonePageTitleObj.waitForExists(timeout)) {
			isOk = true;
		}
		
		return isOk;
	}
	//进入短信,进入成功返回true
	public boolean intoMessaging() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		phonePageSmsObj.click();
		if (phonePageSmsCenterObj.waitForExists(3000)) {
			return true;
		} else {
			return false;
		}
	}
	
	//进入联系人,进入成功返回true
	public boolean intoContact() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		phonePageContactsObj.click();
		if (contactsPageObj.waitForExists(3000)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Function:进入通话记录
	 * @return
	 * @throws UiObjectNotFoundException
	 * Date: 2017-04-27
	 */
	public boolean intoRecents() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		recentsObj.click();
		if (recentsAllObj.waitForExists(3000)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 进入拨号界面
	 * @return 进入成功返回true
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-27
	 */
	public boolean intoDialPad() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		phonePageDialPadObj.click();
		if (gridNumberObj.waitForExists(3000)) {
			return true;
		} else {
			return false;
		}
	}
	
	//挂断电话
	public boolean endCall() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		
		if (endCallObj.waitForExists(5000)) {
			if (endCallObj.click()) {
				if (endCallObj.waitUntilGone(5000)) {
					isOk = true;
				}
			}
		}
		
		return isOk;
	}
	
	//拨打客服电话
	public boolean dialService() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (phonePageServiceObj.click()) {
			if (endCallObj.waitForExists(3000)) {
				isOk = true;
			}
		}
		
		return isOk;
	}
	
}
