package com.example.a310287808.onswitchautomation_sr120;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.openqa.selenium.By;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/28/2017.
 */

public class GroupRenameOnSwitch {
    public String Status;
    public String Comments;
    public String ActualResult;
    public String ExpectedResult;
    public String RoomName;
    public List RoomListHue;
    public MobileElement RoomlistItem2;
    public int notToTalkCounter=0;
    public MobileElement listItem;
    public String LastRoom;

    public void GroupRenameOnSwitch(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException {

        driver.navigate().back();
        //Opening OnSwitch App
        driver.findElement(By.xpath("//android.widget.TextView[@text='OnSwitch']")).click();
        TimeUnit.SECONDS.sleep(10);

        //Go to the groups tab.
        driver.findElement(By.xpath("//android.widget.TextView[@text='GROUPS']")).click();
        TimeUnit.SECONDS.sleep(5);

        //clicking on Edit button
        driver.findElement(By.id("com.getonswitch.onswitch:id/action_edit")).click();
        TimeUnit.SECONDS.sleep(5);

        //Locate all drop down list elements
        List dropList = driver.findElements(By.id("com.getonswitch.onswitch:id/groupName"));
        //Extract text from each element of drop down list one by one.
        for(int i=1; i< dropList.size()-1; i++){
            listItem = (MobileElement) dropList.get(i);
            LastRoom=listItem.getText();
        }

        //Clicking on group to rename
        driver.findElement(By.xpath("//*[@text='Edit' and ./following-sibling::*[@text='"+LastRoom+"']]")).click();
        TimeUnit.SECONDS.sleep(5);

        //Writing the name for the group
        Random rand = new Random();
        int n = rand.nextInt() + 1;
        RoomName = "Room" + String.valueOf(n);
        System.out.println(RoomName);

        //Entering the new name in the text field
        driver.findElement(By.id("com.getonswitch.onswitch:id/editGroupName")).click();
        driver.findElement(By.id("com.getonswitch.onswitch:id/editGroupName")).clear();
        driver.findElement(By.id("com.getonswitch.onswitch:id/editGroupName")).sendKeys(RoomName);
        TimeUnit.SECONDS.sleep(5);
        driver.hideKeyboard();

        //Saving the group name
        driver.findElement(By.id("com.getonswitch.onswitch:id/action_save")).click();
        TimeUnit.SECONDS.sleep(5);

        //going back from the application
        driver.navigate().back();
        driver.navigate().back();

        //Opening Hue applictaion
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[24,1380][216,1572]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Clicking on settings button
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[1026,184][1074,232]']")).click();
        TimeUnit.SECONDS.sleep(2);

        //Selecting Room setup
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[0,408][152,536]']")).click();
        TimeUnit.SECONDS.sleep(2);

        //getting the list of rooms available in the application and inserting it in Hashmap
        driver.findElement(By.id("com.philips.lighting.hue2:id/hue_play_toolbar")).isDisplayed();

        RoomListHue = (driver.findElements(By.id("com.philips.lighting.hue2:id/list_item_title")));

        for (int j = 0; j < RoomListHue.size(); j++)
        {
            RoomlistItem2 = (MobileElement) RoomListHue.get(j);
            Boolean result = (RoomlistItem2.getText()).equals(RoomName);
            if (result == true) {
                notToTalkCounter++;
            } else {
                continue;
            }
        }

        if (notToTalkCounter == 1) {
            Status = "1";
            ActualResult = "Room: "+LastRoom+" is renamed to : " + RoomName+" by OnSwitch is reflected by Hue app also";
            Comments = "NA";
            ExpectedResult ="Room: "+LastRoom+" is renamed to : " + RoomName+" by OnSwitch should be reflected by Hue app";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);
        } else {
            Status = "0";
            ActualResult = "Room: "+LastRoom+" is renamed to : " + RoomName+" by OnSwitch ai reflected by Hue app also";
            Comments = "Fail: Group type created by OnSwitch is not ROOM/ Room name is not updated";
            ExpectedResult ="Room: "+LastRoom+" is renamed to : " + RoomName+" by OnSwitch should be reflected by Hue app";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);
        }
        driver.navigate().back();
        driver.navigate().back();
        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult, APIVersion, SWVersion);

    }
    public String CurrentdateTime;
    public int nextRowNumber;
    public void storeResultsExcel (String excelStatus, String excelActualResult, String excelComments, String resultFileName, String ExcelExpectedResult, String resultAPIVersion, String resultSWVersion) throws IOException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentdateTime = sdf.format(cal.getTime());
        FileInputStream fsIP = new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        HSSFWorkbook workbook = new HSSFWorkbook(fsIP);
        nextRowNumber = workbook.getSheetAt(0).getLastRowNum();
        nextRowNumber++;
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row2 = sheet.createRow(nextRowNumber);
        HSSFCell r2c1 = row2.createCell((short) 0);
        r2c1.setCellValue(CurrentdateTime);

        HSSFCell r2c2 = row2.createCell((short) 1);
        r2c2.setCellValue("19");

        HSSFCell r2c3 = row2.createCell((short) 2);
        r2c3.setCellValue(excelStatus);

        HSSFCell r2c4 = row2.createCell((short) 3);
        r2c4.setCellValue(excelActualResult);

        HSSFCell r2c5 = row2.createCell((short) 4);
        r2c5.setCellValue(excelComments);

        HSSFCell r2c6 = row2.createCell((short) 5);
        r2c6.setCellValue(resultAPIVersion);

        HSSFCell r2c7 = row2.createCell((short) 6);
        r2c7.setCellValue(resultSWVersion);

        fsIP.close();
        FileOutputStream out =new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();

    }

}
