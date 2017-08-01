package com.example.a310287808.onswitchautomation_sr120;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/28/2017.
 */

public class GroupDeletionHue {
    public int lightCounter=0;
    public String ActualResult;
    public String ExpectedResult;
    public String Status;
    public String Comments;
    public MobileElement RoomlistItem1;
    public String DeletedToBeRoom;
    Dimension size;

    public void GroupDeletionHue (AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException  {

        driver.navigate().back();

        //Opening Hue application
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[24,1380][216,1572]']")).click();
        TimeUnit.SECONDS.sleep(2);

        //Clicking on settings button
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[1026,184][1074,232]']")).click();
        TimeUnit.SECONDS.sleep(2);

        //Selecting Room setup
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[0,408][152,536]']")).click();
        TimeUnit.SECONDS.sleep(2);

        //getting the list of rooms available in the application
        driver.findElement(By.id("com.philips.lighting.hue2:id/hue_play_toolbar")).isDisplayed();
        List RoomList = driver.findElements(By.id("com.philips.lighting.hue2:id/list_item_title"));
        int Total=RoomList.size();
        int TotalRooms=Total-1;
        String DeletedRoom= ((MobileElement) RoomList.get(TotalRooms)).getText();
        int bound=192*TotalRooms;
        int bound1=196+bound;
        int bound2=316+bound;

        String FinalBound="[1080,"+bound1+"][1200,"+bound2+"]";

        //Click on the i bubble in front of room to be deleted
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='"+FinalBound+"']")).click();

        TimeUnit.SECONDS.sleep(2);

        //getting the name of the room to be deleted
        DeletedToBeRoom = driver.findElement(By.id("com.philips.lighting.hue2:id/form_field_text")).getText();

        //Clicking on the DELETE button
        driver.findElement(By.id("com.philips.lighting.hue2:id/delete")).click();
        TimeUnit.SECONDS.sleep(2);

        //Clicking on confirmation  box
        driver.findElement(By.id("android:id/button1")).click();
        TimeUnit.SECONDS.sleep(2);

        //Going back from the application
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,160]']")).click();
        driver.navigate().back();
        TimeUnit.SECONDS.sleep(5);
        driver.pressKeyCode(187);
        TimeUnit.SECONDS.sleep(2);

        //Get the size of screen.
        size = driver.manage().window().getSize();

        //Find swipe start and end point from screen's with and height.
        //Find startx point which is at right side of screen.
        int startx = (int) (size.width * 0.70);
        //Find endx point which is at left side of screen.
        int endx = (int) (size.width * 0.30);
        //Find vertical point where you wants to swipe. It is in middle of screen height.
        int starty = size.height / 2;

        //Swipe from Right to Left.
        driver.swipe(startx, starty, endx, starty, 3000);
        Thread.sleep(2000);

        driver.swipe(startx, starty, endx, starty, 3000);
        Thread.sleep(2000);

        driver.swipe(startx, starty, endx, starty, 3000);
        Thread.sleep(2000);


        //Opening OnSwitch App
        System.out.println("Clicking application");
        driver.findElement(By.xpath("//android.widget.TextView[@text='OnSwitch']")).click();
        TimeUnit.SECONDS.sleep(10);

        //Go to the groups tab.
        driver.findElement(By.xpath("//android.widget.TextView[@text='GROUPS']")).click();
        TimeUnit.SECONDS.sleep(5);

        //getting the list of rooms available in the application and inserting it in Hashmap
        HashMap<Object, Integer> Rooms = new HashMap<>();

        RoomList = driver.findElements(By.id("com.getonswitch.onswitch:id/groupName"));

        for(int i=1; i< RoomList.size()-1; i++)
        {
            RoomlistItem1 = (MobileElement) RoomList.get(i);
            Rooms.put(RoomlistItem1.getText(),i);
            Boolean result = RoomlistItem1.getText().equals(DeletedToBeRoom);
            if (result == true) {
                lightCounter++;

            } else {
                continue;
            }
        }
        if (lightCounter == 0)
        {
            Status = "1";
            ActualResult = "Room: " +DeletedToBeRoom+" is deleted from Hue app and also from On Switch";
            Comments = "NA";
            ExpectedResult = "Room: " +DeletedToBeRoom+" should be deleted from Hue app and also from On Switch";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);

        }
        else
        {
            Status = "0";
            ActualResult = "Room: " +DeletedToBeRoom+" is deleted from Hue app but not from On Switch";
            Comments = "FAIL: Room is not deleted from On Switch but deleted from Hue app";
            ExpectedResult = "Room: " +DeletedToBeRoom+" should be deleted from Hue app and also from On Switch";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);
        }

        driver.navigate().back();
        driver.navigate().back();
        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult,APIVersion,SWVersion);
    }

    public String CurrentdateTime;
    public int nextRowNumber;
    public void storeResultsExcel(String excelStatus, String excelActualResult, String excelComments, String resultFileName, String ExcelExpectedResult
            ,String resultAPIVersion, String resultSWVersion) throws IOException {

        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sdf  = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentdateTime = sdf.format(cal.getTime());
        FileInputStream fsIP = new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        HSSFWorkbook workbook = new HSSFWorkbook(fsIP);
        nextRowNumber=workbook.getSheetAt(0).getLastRowNum();
        nextRowNumber++;
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row2 = sheet.createRow(nextRowNumber);
        HSSFCell r2c1 = row2.createCell((short) 0);
        r2c1.setCellValue(CurrentdateTime);

        HSSFCell r2c2 = row2.createCell((short) 1);
        r2c2.setCellValue("17");

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
        FileOutputStream out =
                new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();
    }

}
