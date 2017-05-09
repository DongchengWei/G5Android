package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.os.SystemClock;

public class UpdatePage extends UiAutomatorTestCase {

	//系统升级或恢复出厂设置后进入的确认使用系统界面的确认按钮
	UiObject confirmStartSysObj = new UiObject(new UiSelector()
			.resourceId("com.android.launcher:id/contirm_btn"));
	//系统升级成功确认按钮
	UiObject updateOkConfirmObj = new UiObject(new UiSelector()
			.resourceId("com.thunderst.update:id/dialog_cancel"));
	//当前版本
	UiObject currentVersionObj = new UiObject(new UiSelector()
			.resourceId("com.thunderst.update:id/current_version"));
	//检查更新
	UiObject checkUpdateObj = new UiObject(new UiSelector()
			.resourceId("com.thunderst.update:id/btn_check_update"));
	
	//检测到升级包弹出的对话框
	UiObject dialogObj = new UiObject(new UiSelector()
			.resourceId("com.thunderst.update:id/dialog_summary"));
	UiObject dialogConfirmObj = new UiObject(new UiSelector()//对话框的确定按钮
			.resourceId("com.thunderst.update:id/dialog_confirm"));
	//立即更新
	UiObject updateNowObj = new UiObject(new UiSelector()//对话框的确定按钮
			.resourceId("com.thunderst.update:id/dl_update"));
	
	
	/*
	 * 函数功能：等待确定进入系统的按钮存在
	 * 参          数：timeout,超时时间
	 * 返回值     ：boolean,存在返回true
	 * */
	public boolean waitForConfirmBtnExists(long timeout) {
		boolean isOk = false;
		
		//指定时间
		long startMills = SystemClock.uptimeMillis();
		long currentMills = 0;
		while(currentMills <= timeout){
			
			currentMills = SystemClock.uptimeMillis() - startMills;
			if(timeout > 0) {
				SystemClock.sleep(1000);
			}
			if (confirmStartSysObj.exists()) {
				isOk = true;
				currentMills = timeout + 1;
			} else {
				
			}
		}
		return isOk;
	}
	
	/*
	 * 函数功能：点击确定进入系统的按钮存在
	 * 参          数：
	 * 返回值     ：boolean,点击成功返回true
	 * */
	public boolean clickConfirmIntoSys() throws UiObjectNotFoundException {
		if (confirmStartSysObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	/*
	 * 函数功能：点击确定升级成功（升级成功后再次进入系统升级页面会弹出）
	 * 参          数：
	 * 返回值     ：boolean,点击成功返回true
	 * */
	public void clickConfirmUpdateOk() throws UiObjectNotFoundException {
		if (updateOkConfirmObj.exists()) {
			updateOkConfirmObj.click();
		}
	}
	
	/*
	 * 函数功能：点击确定升级成功（升级成功后再次进入系统升级页面会弹出）
	 * 参          数：
	 * 返回值     ：boolean,点击成功返回true
	 * */
	public boolean clickCheckUpdateBtn() throws UiObjectNotFoundException {
		if (checkUpdateObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	/*
	 * 函数功能：点击确定升级（检查升级后弹出检查到更新包）
	 * 参          数：
	 * 返回值     ：boolean,点击成功返回true
	 * */
	public boolean clickConfirmToUpdate() throws UiObjectNotFoundException {
		boolean isOk = false;
		
		if (dialogObj.waitForExists(30000)) {
			if (dialogConfirmObj.click()) {
				isOk =  true;
			} 
		}
		
		return isOk;
	}
	/*
	 * 函数功能：在timeout时间内只要弹出确认对话框就点击确定
	 * 参          数：
	 * 返回值     ：boolean,点击成功返回true
	 * */
	public boolean clickConfirmIfDialogExists(long timeout) throws UiObjectNotFoundException {
		boolean isOk = false;
		
		//指定时间
		long startMills = SystemClock.uptimeMillis();
		long currentMills = 0;
		while(currentMills <= timeout){
			
			currentMills = SystemClock.uptimeMillis() - startMills;
			if(timeout > 0) {
				SystemClock.sleep(2000);
			}
			if (dialogObj.exists()) {
				dialogConfirmObj.click();
			} else {
				isOk = true;
				currentMills = timeout + 1;
			}
		}
		return isOk;
	}
	/*
	 * 函数功能：点击立即更新
	 * 参          数：
	 * 返回值     ：boolean,点击成功返回true
	 * */
	public boolean clickUpdateNow() throws UiObjectNotFoundException {
		boolean isOk = false;
		if (updateNowObj.click()) {
			isOk =  true;
		} 
		return isOk;
	}
	
}
