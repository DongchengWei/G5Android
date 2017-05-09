package com.update;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.PropertiesUtil;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.PhonePage;
import com.pageutil.SettingsPage;
import com.pageutil.SysTabPage;
import com.pageutil.UpdatePage;
import com.runutils.RunTestCase;

import android.os.RemoteException;

/* 
 * case: 重复从U盘进行系统升级
 * 命令：把test.properties拷贝到u盘根目录后启动系统，或者执行
 * 		 uiautomator runtest AutoTest.jar -c com.update.ContinusUpdate
 * 前提：当前在任意界面
 * 步骤：系统不断用本地升级包进行升级
 * 期望：能正常升级 
 * 其他：
 * Date:2017-04-01
 * */
public class ContinusUpdate extends UiAutomatorTestCase {

	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.update.ContinusUpdate", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:continus update test=================");
		System.out.println("==================================================");
	}
	
	PropertiesUtil propUtil = new PropertiesUtil();
	int testCounterTotal = 0;
	int testPassCounter = 0;
	
	public void testContinusUpdate(){
		try{
			boolean isTestingBoolean = false;
			File screenShotFile = new File("/storage/usb0/test.properties");
			if (screenShotFile.exists()) {
				Map<String, String> mapTest = propUtil.getProperties("/storage/usb0/test.properties");
				String isTestValueStr = "0";
				for (Map.Entry<String, String> entry : mapTest.entrySet()) {
					//Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
					//entry.getKey() ;entry.getValue(); entry.setValue();
					//map.entrySet()  返回此映射中包含的映射关系的 Set视图。
					System.out.println("key= " + entry.getKey() + " and value= "
							+ entry.getValue());
					if (entry.getKey().equals("isTesting")) {//是否正在测试的标志
						isTestValueStr = entry.getValue();
						if (isTestValueStr.equals("1")) {
							isTestingBoolean = true;
						} else {
							isTestingBoolean = false;
						}
					} else if (entry.getKey().equals("testCounter")) {
						testCounterTotal = Integer.parseInt(entry.getValue());
					} else if (entry.getKey().equals("testPass")) {
						testPassCounter = Integer.parseInt(entry.getValue());
					}
				}
			}
			
			
			if (isTestingBoolean) {
				CaseInfo();
				Utils.logPrint("ContinusUpdate is true...");
				getUiDevice().wakeUp();
				Utils.stopRunningWatcher();
				
				assertEquals(true, continusUpdateTest());
				
				UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
			} else {
				Utils.logPrint("ContinusUpdate is false...");
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean continusUpdateTest() {
		boolean isOk = false;
				
		UpdatePage updatePage = new UpdatePage();
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		SysTabPage sysTabPage = new SysTabPage();
		PhonePage phonePage = new PhonePage();
		
			try {
				if (homePage.waitForExists(0)) {//如果当前在home界面
					testCounterTotal ++;
					propUtil.setProperties("/storage/usb0/test.properties", "testCounter", testCounterTotal + "", true);
					Utils.logPrint("Test pass:" + testPassCounter + ", Total Test:" + testCounterTotal);
					if (homePage.intoSettings()) {//进入设置
						if (settingsPage.intoSystemTab()) {//系统
							if (sysTabPage.intoSystemUpdatePage()) {//进入系统升级，会弹出升级成功对话框
								updatePage.clickConfirmUpdateOk();//确定
								if (updatePage.clickCheckUpdateBtn()) {//点击检查升级按钮
									if (updatePage.clickConfirmToUpdate()) {//确定升级
										if (updatePage.clickConfirmIfDialogExists(5000)) {//弹出确定框
											if (updatePage.clickUpdateNow()) {//立即升级
												Utils.logPrint("update now...");
												if (updatePage.clickConfirmIfDialogExists(5000)) {//确定
													isOk = true;
													testPassCounter ++;
													String testPassStr = testPassCounter + "";
													propUtil.setProperties("/storage/usb0/test.properties", "testPass", testPassStr, true);
													Utils.logForResult("Test Pass:" + testPassStr + " times,Total Test:" + testCounterTotal);
												} else {
													Utils.logPrint("confirm update now fail...");
												}
											} else {
												Utils.logPrint("update now fail...");
											}
										} else {
											Utils.logPrint("updating1 fail...");
										}
									} else {
										Utils.logPrint("updating fail...");
									}
								} else {
									Utils.logPrint("check updating fail...");
								}
							} else {
								Utils.logPrint("into system update fail...");
							}
						} else {
							Utils.logPrint("intoSystemTab fail...");
						}
					} else {
						Utils.logPrint("intoSettings fail...");
					}
					//如果出错则直接退出设置且设置配置文件为不再继续测试
					if (isOk == false) {
						propUtil.setProperties("/storage/usb0/test.properties", "isTesting", "0", true);
						if (phonePage.waitForExists(0)) {//退出到通讯录界面则退出测试
							Utils.logPrint("Exit test by yourself...");
						} else {
							Utils.logPrint("other fail...");
						}
					}
				} else {
					if (updatePage.waitForConfirmBtnExists(60000)) {
						testCounterTotal ++;
						propUtil.setProperties("/storage/usb0/test.properties", "testCounter", testCounterTotal + "", true);
						Utils.logPrint("Test pass:" + testPassCounter + ", Total Test:" + testCounterTotal);
						
						
						sleep(1000);
						updatePage.clickConfirmIntoSys();
						Utils.logPrint("into system ok...");
						if (homePage.waitForExists(5)) {//5秒
							if (homePage.intoSettings()) {//进入设置
								if (settingsPage.intoSystemTab()) {//系统
									if (sysTabPage.intoSystemUpdatePage()) {//进入系统升级，会弹出升级成功对话框
										updatePage.clickConfirmUpdateOk();//确定
										if (updatePage.clickCheckUpdateBtn()) {//点击检查升级按钮
											if (updatePage.clickConfirmToUpdate()) {//确定升级
												if (updatePage.clickConfirmIfDialogExists(5000)) {//弹出确定框
													if (updatePage.clickUpdateNow()) {//立即升级
														Utils.logPrint("update now...");
														if (updatePage.clickConfirmIfDialogExists(5000)) {//确定
															isOk = true;
															testPassCounter ++;
															String testPassStr = testPassCounter + "";
															propUtil.setProperties("/storage/usb0/test.properties", "testPass", testPassStr, true);
															Utils.logForResult("Test Pass:" + testPassStr + " times,Total Test:" + testCounterTotal);
														} else {
															Utils.logPrint("confirm update now fail...");
														}
													} else {
														Utils.logPrint("update now fail...");
													}
												} else {
													Utils.logPrint("updating1 fail...");
												}
											} else {
												Utils.logPrint("updating fail...");
											}
										} else {
											Utils.logPrint("check updating fail...");
										}
									} else {
										Utils.logPrint("into system update fail...");
									}
								} else {
									Utils.logPrint("intoSystemTab fail...");
								}
							} else {
								Utils.logPrint("intoSettings fail...");
							}
						} else {
							Utils.logPrint("homePage not exists fail...");
						}
						//如果出错则直接退出设置且设置配置文件为不再继续测试
						if (isOk == false) {
							propUtil.setProperties("/storage/usb0/test.properties", "isTesting", "0", true);
							if (phonePage.waitForExists(0)) {//退出到通讯录界面则退出测试
								Utils.logPrint("Exit test by yourself...");
							} else {
								Utils.logPrint("other fail...");
							}
						}
					} else {
						if (phonePage.waitForExists(0)) {//退出到通讯录界面则退出测试
							propUtil.setProperties("/storage/usb0/test.properties", "isTesting", "0", true);
							Utils.logPrint("Exit test by yourself...");
						} else {
							Utils.logPrint("confirm button not exists fail...");
						}
					}
					
				}
			} catch (UiObjectNotFoundException e) {
				if (phonePage.waitForExists(0)) {//退出到通讯录界面则退出测试
					propUtil.setProperties("/storage/usb0/test.properties", "isTesting", "0", true);
					Utils.logPrint("Exit test by yourself...");
				} else {
					Utils.logPrint("UiObjectNotFoundException fail...");
				}
			}
		return isOk;
	}
	
	
}
