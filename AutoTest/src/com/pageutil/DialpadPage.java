/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-04-27
 */
package com.pageutil;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年4月27日 下午4:46:46 
* 类说明 :拨号界面
*/
/**
 * @author uidq0460
 *
 */
public class DialpadPage extends UiAutomatorTestCase {

	
	/**
	 * 输入框
	 */
	UiObject inputNumObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/edit_input_number"));
	/**
	 * 拨号盘对象
	 */
	UiObject gridNumberObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/grid_number"));
	
	/**
	 * 拨号按钮
	 */
	UiObject callBtnObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/btn_call"));
	
	/**
	 * 拨号出去的联系人名称
	 */
	UiObject callNameObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/call_name"));
	
	/**
	 * 拨号状态
	 */
	UiObject callStateObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.call:id/call_state"));
	
	/**
	 * 数字键选择器 
	 */
	UiSelector numsSel = new UiSelector()
			.resourceId("com.thundersoft.call:id/image_number");
	
	
	/**
	 * 拨打电话后马上挂断
	 * @param callStr
	 * @return
	 * @throws UiObjectNotFoundException
	 * @Date 2017-05-05
	 */
	public boolean dialAndEndImmediately(String callStr) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		
		inputNumbers(callStr);
		String textStr = getInputNumbers();
		Utils.logPrint("inputText:" + textStr);
		if (textStr.equals(callStr)) {//判断输入的是否是正确
			if (dialStart()) {
				if (callNameObj.exists()) {
					endCall();
					if (callNameObj.waitUntilGone(30000)) {
						isOk = true;
					}
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 拨打电话，在接通后通话3秒钟挂断电话
	 * @param callStr 拨打的电话号码
	 * @return 成功返回true
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-28
	 */
	public boolean dialAndEndCallAfterOncall(String callStr) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		
		inputNumbers(callStr);
		String textStr = getInputNumbers();
		Utils.logPrint("inputText:" + textStr);
		if (textStr.equals(callStr)) {//判断输入的是否是正确
			if (dialStart()) {
				if (isOncall(70000)) {//
					SystemClock.sleep(1000);
					if (endCall()) {
						String inputStr = getInputNumbers();
						if (inputStr.isEmpty()) {
							isOk = true;
						}
					}
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 拨打特殊号码退出正在拨号界面返回拨号界面
	 * @param callStr
	 * @return
	 * @throws UiObjectNotFoundException
	 * @Date 2017-05-04
	 */
	public boolean dialAbnormalNumber(String callStr) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		
		inputNumbers(callStr);
		String textStr = getInputNumbers();
		Utils.logPrint("inputText:" + textStr);
		if (textStr.equals(callStr)) {//判断输入的是否是正确
			if (dialStart()) {
				if (isOnDial()) {
					if (waitDialingPageGone(30000)) {
						if (inputNumObj.exists()) {
							isOk = true;
						}
					}
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 判断是否在正在拨号界面
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean isOnDial() {
		if (callNameObj.waitForExists(3000)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 等待正在拨号界面退出
	 * @param timeout
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean waitDialingPageGone(long timeout) {
		if (callNameObj.waitUntilGone(timeout)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 点击挂断按钮结束通话
	 * @return
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-28
	 */
	public boolean endCall() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		//点击成功没有返回true，uiautomator的bug,所以只能判断界面其他控件的情况来判断是否点击成功
		callBtnObj.click();
		if (callNameObj.waitUntilGone(3000)) {
			isOk = true;
		}
		return isOk;
	}
	
	/**
	 * 判断是否进入通话中状态(一般超时时间为70秒)
	 * @param timeout 超时时间
	 * @return 进入通话中则返回true
	 * @throws UiObjectNotFoundException 
	 * @Date 2017-04-28
	 */
	public boolean isOncall(long timeout) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		
		boolean isOk = false;
		
		if (callStateObj.waitForExists(timeout)) {
			if (getCallState() == ONCALL_STATE) {
				isOk = true;
			}
		} 
		 
		return isOk;
	}
	
	/**
	 * 获取通话中联系人名称
	 * @return 联系人名称
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-28
	 */
	public String getCallNameText() throws UiObjectNotFoundException {
		return callNameObj.getText();
	}
	
	public static final int ONCALL_STATE = 1;
	public static final int WAITING_STATE = 2;
	/**
	 * 获取通话状态
	 * @return 
	 * 通话中 INCALL_STATE,
	 * 等待 WAITING_STATE,
	 * 其他
	 * @throws UiObjectNotFoundException 
	 * @Date 2017-04-28
	 */
	public int getCallState() throws UiObjectNotFoundException {
		int stateInt = 0;
		String stateStr = callStateObj.getText();
		if (stateStr.equals("通话中...")) {
			stateInt = ONCALL_STATE;
		} else if (stateStr.equals("等待")) {
			stateInt = WAITING_STATE;
		}
		return stateInt;
	}
	
	/**
	 * 点击拨号盘上的第几个键
	 * @param num
	 * @return
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-27
	 */
	public boolean inputNumber(String numStr) throws UiObjectNotFoundException {
		boolean isOk = false;
		
		UiObject numberObj = new UiObject(numsSel.text(numStr));
		if (numberObj.exists()) {
			if (numberObj.click()) {
				isOk = true;
			}
		}
		return isOk;
	}
	
	/**
	 * 解析字符串后在拨号框输入
	 * @param numStr
	 * @return numStr
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-27
	 */
	public String inputNumbers(String numStr) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		
		char [] numArr = numStr.toCharArray(); //注意返回值是char数组
		for (int i = 0; i < numArr.length; i++) {
			inputNumber(String.valueOf(numArr[i]));
		}
		
		return numStr;
	}
	
	/**
	 * 获取输入框的内容
	 * @return 输入框内的字符串
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-28
	 */
	public String getInputNumbers() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		inputNumObj.waitForExists(2000);
		return inputNumObj.getText();
	}
	
	/**
	 * 点击拨号按钮开始拨号，如果输入框内没有字符串，将自动把最近的一条通话记录的号码输入
	 * @return 点击成功返回true
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-28
	 */
	public boolean dialStart() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		if (callBtnObj.click()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 拨打一个号码
	 * @param numStr 拨打的号码
	 * @return 输入号码并点击拨号按钮成功返回true
	 * @throws UiObjectNotFoundException
	 * @Date 2017-05-04
	 */
	public boolean dialANumber(String numStr) throws UiObjectNotFoundException {
		boolean isOk = false;
		inputNumbers(numStr);
		if (dialStart()) {
			isOk = true;
			Utils.logPrint("dialANumber:true");
		}
		
		return isOk;
	}
	
	/**
	 * 等待拨号界面消失
	 * @param timeout 超时时间
	 * @return 消失返回true，否则false
	 * @Date 2017-05-04
	 */
	public boolean waitInputGone(long timeout) {
		if (inputNumObj.waitUntilGone(timeout)) {//5秒没有消失说明没有进入正在拨号界面
			return true;
		} else {
			return false;
		}
	}
}
