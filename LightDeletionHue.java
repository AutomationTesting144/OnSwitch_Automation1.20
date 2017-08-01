package com.example.a310287808.onswitchautomation_sr120;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/28/2017.
 */

public class LightDeletionHue {
    public String IPAddress = "192.168.86.23/api";
    public String HueUserName = "YDU6nYRzaMmKqqzS5lmdb3s9roIfo7R5AZbq9JY4";
    public String HueBridgeParameterType = "lights/44";
    public String HueBridgeParameterTypeGroup = "groups/2";
    public String lightStatusReturned;
    public String finalURL;
    public String ActualResult;
    public String ExpectedResult;
    public String Status;
    public String Comments;
    public String lightName;
    public String newString1;
    Dimension size;

    public void LightDeletionHue (AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException  {

        driver.navigate().back();

        HttpURLConnection connection;

        //Checking whether the group light is ON/OFF
        finalURL = "http://" + IPAddress + "/" + HueUserName + "/" + HueBridgeParameterTypeGroup;
        URL url = new URL(finalURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer br = new StringBuffer();
        String line = " ";
        while ((line = reader.readLine()) != null) {
            br.append(line);
        }

        String output1 = br.toString();
        JSONObject jsonObject = new JSONObject(output1);

        Object ob = jsonObject.get("state");
        String newString = ob.toString();
        JSONObject jsonObject1 = new JSONObject(newString);
        Object ob1 = jsonObject1.get("all_on");

        //If the lights in the group are already ON then turn them off
        if (ob1.toString()=="true")
        {
            URL url1 = new URL("http://192.168.86.23/api/YDU6nYRzaMmKqqzS5lmdb3s9roIfo7R5AZbq9JY4/groups/2/action");
            String content = "{"+"\"on\""+":"+"false"+"}";
            HttpURLConnection httpCon = (HttpURLConnection) url1.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(content);
            out.close();
            httpCon.getInputStream();
            System.out.println(httpCon.getResponseCode());
            TimeUnit.SECONDS.sleep(5);
            System.out.println("Lights are switched off");
            TimeUnit.SECONDS.sleep(5);

        }
        //Opening Hue application
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[24,1380][216,1572]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Clicking on settings button
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[1026,184][1074,232]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Selecting Room setup
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[0,408][152,536]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Clicking on bedroom icon to add the lights
        driver.findElement(By.id("com.philips.lighting.hue2:id/list_item_left_icon")).click();
        TimeUnit.SECONDS.sleep(2);

        driver.findElement(By.xpath("//android.widget.TextView[@text='Light 4']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Saving the light in Bedroom
        driver.findElement(By.id("com.philips.lighting.hue2:id/save")).click();
        TimeUnit.SECONDS.sleep(10);
        //Going back from the application
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,160]']")).click();
        driver.navigate().back();
        Runtime.getRuntime().exec("taskkill /F /FI \"WindowTitle eq OnSwitch\" /T");
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

        //Clicking on the toggle switch for bedroom to turn it ON
        driver.findElement(By.xpath("//android.widget.Button[@bounds='[1039,466][1199,562]']")).click();
        TimeUnit.SECONDS.sleep(2);

        //Going back from the application
        driver.navigate().back();
        driver.navigate().back();

        //getting the status of  group from API
        finalURL = "http://" + IPAddress + "/" + HueUserName + "/" + HueBridgeParameterType;
        URL urlstatus = new URL(finalURL);
        connection = (HttpURLConnection) urlstatus.openConnection();
        connection.connect();
        InputStream streamStatus = connection.getInputStream();
        BufferedReader readerStatus = new BufferedReader(new InputStreamReader(streamStatus));
        StringBuffer brStatus = new StringBuffer();
        String lineStatus = " ";
        while ((lineStatus = readerStatus.readLine()) != null) {
            brStatus.append(lineStatus);
        }

        String outputStatus = brStatus.toString();

        BridgeIndividualLightStateONOFF lOnOff = new BridgeIndividualLightStateONOFF();
        lightStatusReturned = lOnOff.stateONorOFF(outputStatus);

        Object ob2 = jsonObject.get("state");
        newString1 = ob2.toString();
        Object lightNameObject = jsonObject.get("name");
        lightName = lightNameObject.toString();

        br.append(lightName);
        br.append("\n");

        if (lightStatusReturned == "false")

        {
            Status = "1";
            ActualResult = "Light " + lightName + " is deleted from Hue and is not controlled by OnSwitch";
            Comments = "NA";
            ExpectedResult="Light " + lightName + " should be deleted from Hue and should not controlled by OnSwitch";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);
        } else {
            Status = "0";
            ActualResult = "Light " + lightName + " is deleted from Hue but is controlled by OnSwitch";
            Comments = "Light Status of " + lightName + " is : " + newString1;
            ExpectedResult="Light " + lightName + " should be deleted from Hue and should not controlled by OnSwitch";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);
        }

        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult,APIVersion,SWVersion);
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
        r2c2.setCellValue("23");

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
