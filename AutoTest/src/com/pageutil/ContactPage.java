package com.pageutil;

import com.android.uiautomator.core.UiCollection;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;


public class ContactPage extends UiAutomatorTestCase {
	//联系人页面
	UiObject contactsPageObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/img_search"));
	//联系人列表
	UiObject contactsListObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/list_phonebook_contacts"));
	//联系人项
	UiObject contactsItemObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/rel_layout"));
	
	//联系人列表集合
	UiCollection contactsListCol = new UiCollection(new UiSelector()
			.resourceId("com.thundersoft.call:id/list_phonebook_contacts"));
	UiScrollable contactsListScrol = new UiScrollable(new UiSelector()
			.resourceId("com.thundersoft.call:id/list_phonebook_contacts"));
	
	
	public boolean waitUntilGone(long timeout) {
		if (contactsPageObj.waitUntilGone(timeout)) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getConactsCounter() throws UiObjectNotFoundException {
		
		int contactsCounter = 0;
		if (contactsListCol.waitForExists(3000)) {
			contactsCounter = contactsListCol.getChildCount();
		}
		
		return contactsCounter;
	}
	
	//等待联系人同步完成出现联系人项目
	public boolean contactsComeout(long timeout) {
		boolean contactComeoutOk = false;
		
		if (contactsItemObj.waitForExists(timeout)) {
			contactComeoutOk = true;
		}
		
		return contactComeoutOk;
	}
	
	
}
