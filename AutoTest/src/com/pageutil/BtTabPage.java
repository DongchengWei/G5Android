package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;

public class BtTabPage extends UiAutomatorTestCase {
	
	public static final String HFP_ICON_ID = "com.android.settings:id/item_bt_icon_phone";
	//蓝牙开关
	UiObject btSwitchObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/switch_view"));
	//蓝牙标题
	UiObject btTabObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_tab_bluetooth_id"));
	
	//已匹配列表
	UiObject pairedDeviceListObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_bluetooth_pair_dev_list"));
	
	//已连接HFP成功图标
	UiObject hfpConnectedObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_bt_icon_phone"));
	
	//已连接A2DP成功图标
	UiObject a2dpConnectedObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_bt_icon_music"));
	
	//已匹配的第一个列表设备
	UiObject firstDeviceObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/ts_bluetooth_device_name"));
	
	//已连接状态的显示框
	UiObject profileStatusObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_profile_layout"));
	//点击切换协议时弹出的窗口中的listview
	UiObject profileListObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/profile_check_list")) ;
	//弹出选择协议窗口的确定键
	UiObject profileConfirmBtnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/profile_dialog_confirm"));
	UiObject confirmBtnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/dialog_confirm"));
	
	UiObject hfpA2dpItemCnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_name").text("电话及音频"));
	UiObject hfpA2dpItemEnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_name").text("Tel & media profiles"));
	UiObject a2dpItemCnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_name").text("仅音频"));
	UiObject a2dpItemEnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_name").text("Only media profiles"));
	UiObject hfpItemCnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_name").text("仅电话"));
	UiObject hfpItemEnObj = new UiObject(new UiSelector()
			.resourceId("com.android.settings:id/item_name").text("Only tel profiles"));
	
	
	/**
	 * 在点击切换协议时弹出的窗口中的listview中选择蓝牙协议进行连接
	 * @param status 切换到的协议	 
	 * HFP_ONLY:仅连接HFP协议,
	 * A2DP_ONLY：仅连接A2DP协议,
	 * ALL_CONNECT：连接全部协议,
	 * @return boolean 是否切换并点击确定成功
	 * */
	public boolean changeToStatus(int status) throws UiObjectNotFoundException {
		boolean isOk = false;
		
		UiObject connectObj = null;
		
		if (status == ALL_CONNECT) {
			if (hfpA2dpItemCnObj.exists()) {
				connectObj = hfpA2dpItemCnObj;
			} else if (hfpA2dpItemEnObj.exists()) {
				connectObj = hfpA2dpItemEnObj;
			}
		} else if (status == HFP_ONLY) {
			if (hfpItemCnObj.exists()) {
				connectObj = hfpItemCnObj;
			} else if (hfpItemEnObj.exists()) {
				connectObj = hfpItemEnObj;
			}
		} else if (status == A2DP_ONLY) {
			if (a2dpItemCnObj.exists()) {
				connectObj = a2dpItemCnObj;
			} else if (a2dpItemEnObj.exists()) {
				connectObj = a2dpItemEnObj;
			}
		}
		if (connectObj != null && connectObj.waitForExists(3000)) {
			Utils.logPrint("connectObj == exists");
			if (connectObj.click()) {
				Utils.logPrint("connectObj == click");
				if (profileConfirmBtnObj.click()) {
					if (profileConfirmBtnObj.waitUntilGone(3000)) {
						isOk = true;
					}
				}
			}
		}
		return isOk;
	}
	
	public static final int NONE_CONNECT = 0;
	public static final int HFP_ONLY = 1;
	public static final int A2DP_ONLY = 2;
	public static final int ALL_CONNECT = 3;
	/**
	 * 判断某个蓝牙设备的连接状态,
	 * NONE_CONNECT:没有任何连接,
	 * HFP_ONLY:仅连接HFP协议,
	 * A2DP_ONLY：仅连接A2DP协议,
	 * ALL_CONNECT：连接全部协议,
	 * @param deviceNameStr 蓝牙设备名称包含的字符串，可以不完全匹配
	 * */
	public int getBtConnectStatus(String deviceNameStr) throws UiObjectNotFoundException {
		int status = 0;
		UiObject deviceObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/ts_bluetooth_device_name").textContains(deviceNameStr));
		//连接状态的控件
		UiObject statusObj = deviceObj.getFromParent(new UiSelector()
				.resourceId("com.android.settings:id/item_profile_layout"));
		int counter = statusObj.getChildCount();//根据这个控件的子控件数量判断连接状态
		if (counter == 1) {			//都没有连接
			status = NONE_CONNECT;
		} else if (counter == 3) {	//连接了全部协议
			status = ALL_CONNECT;
		} else {					//只连接了一个协议，需要判断连接的是HFP还是A2DP
			UiObject hfpObj = statusObj.getChild(new UiSelector()
					.instance(0)
					.resourceId(HFP_ICON_ID));
			if (hfpObj.exists()) {
				status = HFP_ONLY;
			} else {
				status = A2DP_ONLY;
			}
		}
		return status;
	}
	
	/**
	 * 判断设备是否已连接上指定的蓝牙协议
	 * @param deviceNameStr 设备名称，可以不完全匹配
	 * @param status 连接的协议，HFP或者A2DP或者连接全部协议
	 * @param timeout 超时时间
	 * HFP_ONLY:仅连接HFP协议,
	 * A2DP_ONLY：仅连接A2DP协议,
	 * ALL_CONNECT：连接全部协议,
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean isDeviceConnected(String deviceNameStr, int status, long timeout) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		//指定时间
		long startMills = SystemClock.uptimeMillis();
		long currentMills = 0;
		while(currentMills <= timeout){
			
			currentMills = SystemClock.uptimeMillis() - startMills;
			if(timeout > 0) {
				SystemClock.sleep(1000);
			}
			if (getBtConnectStatus(deviceNameStr) == status) {
				isOk = true;
				currentMills = timeout + 1;
			}
		}
		return isOk;
	}
	/**
	 * 连接蓝牙设备
	 * @param devicesNameStr 设备名称，可以不完全匹配
	 * @param status 连接的协议，HFP或者A2DP或者连接全部协议
	 * HFP_ONLY:仅连接HFP协议,
	 * A2DP_ONLY：仅连接A2DP协议,
	 * ALL_CONNECT：连接全部协议,
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean connectBtDevice(String deviceNameStr, int status) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		//TODO
		boolean isOk = false;
		UiObject deviceObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/ts_bluetooth_device_name").textContains(deviceNameStr));
		
		int connectStatus = getBtConnectStatus(deviceNameStr);
		if (connectStatus == status) {//已连接指定协议则直接退出
			isOk = true;
		} else {					//没有连接指定协议则判断然后连接
			if (status == ALL_CONNECT) {//要连接全部协议
				if (connectStatus == NONE_CONNECT) {//当前没有连接任何协议则可以直接点击设备名称连接全部协议
					if (deviceObj.click()) {//点击设备连接全部协议
						if (isDeviceConnected(deviceNameStr, status, 60000)) {
							isOk = true;
						}
					}
				} else {//当前已连接两个协议中的一个协议，则需要切换连接
					if (profileStatusObj.click()) {//弹出选择协议对话框
						if (changeToStatus(status)) {
							if (isDeviceConnected(deviceNameStr, status, 60000)) {
								isOk = true;
							}
						}
					}
				}
			} else if (status == HFP_ONLY) {//要连接HFP协议，只能选择协议连接
				if (profileStatusObj.click()) {//弹出选择协议对话框
					if (changeToStatus(HFP_ONLY)) {
						if (isDeviceConnected(deviceNameStr, status, 60000)) {
							isOk = true;
						}
					}
				}
			} else if (status == A2DP_ONLY) {//要连接A2DP协议，只能选择协议连接
				if (profileStatusObj.click()) {//弹出选择协议对话框
					if (changeToStatus(A2DP_ONLY)) {
						if (isDeviceConnected(deviceNameStr, status, 60000)) {
							isOk = true;
						}
					}
				}
			} else if (status == NONE_CONNECT) { //断开所有协议
				if (deviceObj.click()) {		//点击设备弹出断开确认对话框
					if (confirmBtnObj.click()) {//点击确定
						if (isDeviceConnected(deviceNameStr, status, 60000)) {
							isOk = true;
						}
					}
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 等待已匹配列表出现
	 * */
	public boolean waitPairedListExists(int millSecond) {
		if (pairedDeviceListObj.waitForExists(millSecond)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 等待BT界面消失
	 * */
	public boolean waitUntilGone(int millSecond) {
		boolean isGone = false;
		if (btTabObj.waitUntilGone(millSecond)) {
			isGone = true;
		}else {
			isGone = false;
		}
		return isGone;
	}
	
	//判断BT开关是否处于打开状态,打开状态返回true
	public boolean isBTOn() throws UiObjectNotFoundException {
		if (btSwitchObj.isChecked()) {
			return true;
		}else {
			return false;
		}
	}
	
	//打开BT开关
	public boolean turnOnBT(long timeout) throws UiObjectNotFoundException {
		boolean isOk = false;
		while(! btSwitchObj.click());
//		if (btSwitchObj.click()) {
			sleep(3000);
//			//指定时间
//			long startMills = SystemClock.uptimeMillis();
//			long currentMills = 0;
//			while(currentMills <= timeout){
//				
//				currentMills = SystemClock.uptimeMillis() - startMills;
//				if(timeout > 0) {
//					SystemClock.sleep(1000);
//				}
				if (btSwitchObj.isChecked()) {
					isOk = true;
//					currentMills = timeout + 1;
				}
//			}
//		}
		return isOk;
	}
	
	//关闭BT开关
	public boolean turnOffBT(long timeout) throws UiObjectNotFoundException {
		boolean isOk = false;
		while(! btSwitchObj.click());
//		if (btSwitchObj.click()) {
//			//指定时间
//			long startMills = SystemClock.uptimeMillis();
//			long currentMills = 0;
//			while(currentMills <= timeout){
//				
//				currentMills = SystemClock.uptimeMillis() - startMills;
//				if(timeout > 0) {
//					SystemClock.sleep(1000);
//				}
				if (! btSwitchObj.isChecked()) {
					isOk = true;
//					currentMills = timeout + 1;
				}
//			}
//		}
		return isOk;
	}
	
	//判断是否已连接上蓝牙全部协议，已连接返回true
	public boolean isBTConnectedAll(long timeout) {
		
		boolean isConnected = false;
		
		//指定时间
		long startMills = SystemClock.uptimeMillis();
		long currentMills = 0;
		while(currentMills <= timeout){
			
			currentMills = SystemClock.uptimeMillis() - startMills;
			if(timeout > 0) {
				SystemClock.sleep(1000);
			}
			if (hfpConnectedObj.exists() && a2dpConnectedObj.exists()) {
				isConnected = true;
				currentMills = timeout + 1;
			}
		}
		
		return isConnected;
	}
	
	//连接所有协议,前提是列表第一个设备，且未连接任何协议的情况下
	public boolean btConnectFirstDeviceAllByName(String deviceName,long timeout) throws UiObjectNotFoundException {
		
		boolean connectOk = false;
		UiObject firstDeviceByNameObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/ts_bluetooth_device_name").text(deviceName));
		if (firstDeviceByNameObj.exists()) {
			
			while(! firstDeviceByNameObj.click() ){
				
			}
			
			if (isBTConnectedAll(timeout)) {
				connectOk = true;
			}
		}
		return connectOk;
	}
	//断开所有协议,前提是连接的是列表中第一个设备名称
	public boolean btDisconnectFirstDeviceAllByName(String deviceName,long timeout) throws UiObjectNotFoundException {
		
		boolean disconnectOk = false;
		UiObject firstDeviceByNameObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/ts_bluetooth_device_name").text(deviceName));
		UiObject confirmDialogObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/dialog_summary1"));
		UiObject confirmButtonObj = new UiObject(new UiSelector()
				.resourceId("com.android.settings:id/dialog_confirm"));
		if (isBTConnectedAll(0)) {
			if (firstDeviceByNameObj.waitForExists(10000)) {
				firstDeviceByNameObj.click();
				if (confirmDialogObj.waitForExists(10000)) {
					confirmButtonObj.click();
					
					//指定时间
					long startMills = SystemClock.uptimeMillis();
					long currentMills = 0;
					while(currentMills <= timeout){
						
						currentMills = SystemClock.uptimeMillis() - startMills;
						if(timeout > 0) {
							SystemClock.sleep(1000);
						}
						if (hfpConnectedObj.waitUntilGone(100) && a2dpConnectedObj.waitUntilGone(100)) {
							disconnectOk = true;
							currentMills = timeout + 1;
						}
					}
				}
			} 
		}
		return disconnectOk;
	}
}
