package com.pageutil;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;



/*
 * 	硬按键键值：
 *  AKEYCODE_NAV_VEHICLE = 260,			导航
 *  AKEYCODE_MEDIA_VEHICLE = 261,		切换source
 *  AKEYCODE_FAVORITE_VEHICLE = 262,	所有应用
 *  AKEYCODE_UP_VEHICLE = 263,			上
 *  AKEYCODE_DOWN_VEHICLE = 264,		下
 *  AKEYCODE_LEFT_VEHICLE = 265,		左
 *  AKEYCODE_RIGHT_VEHICLE = 266,		右
 *  AKEYCODE_OK_VEHICLE = 267   		确定
 * */
public class HardKeyAction extends UiAutomatorTestCase {
	
	public static final int AKEYCODE_NAV_VEHICLE = 260;
	public static final int AKEYCODE_MEDIA_VEHICLE = 261;
	public static final int AKEYCODE_FAVORITE_VEHICLE = 262;
	public static final int AKEYCODE_UP_VEHICLE = 263;
	public static final int AKEYCODE_DOWN_VEHICLE = 264;
	public static final int AKEYCODE_LEFT_VEHICLE = 265;
	public static final int AKEYCODE_RIGHT_VEHICLE = 266;
	public static final int AKEYCODE_OK_VEHICLE = 267;
	
	//按下NVA硬件，进入导航
	public static boolean pressNAV() {
		
		boolean intoOk = false;
		if (UiDevice.getInstance().pressKeyCode(AKEYCODE_NAV_VEHICLE)) {
			intoOk = true;
		}
		
		return intoOk;
	}
	
	public static boolean pressMusic() {
		boolean intoOk = false;
		if (UiDevice.getInstance().pressKeyCode(AKEYCODE_MEDIA_VEHICLE)) {
			intoOk = true;
		}
		return intoOk;
	}
	
}
