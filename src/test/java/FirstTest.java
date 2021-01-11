import io.appium.java_client.MobileBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.GsmCallActions;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FirstTest {

    private static final String APP = "https://github.com/Megued/qaAppium/raw/main/app-debug.apk";
    private static final String APPIUM = "http://localhost:4723/wd/hub";
    private static final int timeOut = 5;
    private static final String phoneNumber = "1234567890";

    private static AndroidDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    static void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "11");
        caps.setCapability("deviceName", "emulator-5554");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("app", APP);
        driver = new AndroidDriver(new URL(APPIUM), caps);
        wait = new WebDriverWait(driver, timeOut);

        // used to check if application loaded
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("co.teltech.callblocker:id/counterBlockedCalls")));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Enable blocking calls")
    @Order(1)
    public void enableBlock() {

        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("co.teltech.callblocker:id/switchBlockCalls"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("com.android.permissioncontroller:id/permission_allow_button"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("com.android.permissioncontroller:id/permission_allow_button"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("co.teltech.callblocker:id/radioOptionNotContacts"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("com.android.permissioncontroller:id/permission_allow_button"))).click();
    }

    @Test
    @DisplayName("Make call from fake number")
    @Order(2)
    public void callFromFakeNumber() throws IOException, InterruptedException {
        // wait for block to be enabled
        wait.until(ExpectedConditions.attributeToBe(MobileBy.id("co.teltech.callblocker:id/radioOptionNotContacts"),"checked", "true"));
        driver.makeGsmCall(phoneNumber, GsmCallActions.CALL);
        Thread.sleep(2000);

        WebElement call = driver.findElement(MobileBy.id("co.teltech.callblocker:id/counterBlockedCalls"));

        if(call.isDisplayed())
            call.click();

        File scrFile =  ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("incomingCall.png"));
    }

    @Test
    @DisplayName("Check last call was blocked")
    @Order(3)
    public void checkBlockedCall(){
        wait.until(ExpectedConditions.attributeToBe(MobileBy.id("co.teltech.callblocker:id/counterBlockedCalls"),"text", "1"));
    }

    @Test
    @DisplayName("Add number to contacts")
    @Order(4)
    public void addNumberToContacts() {
        driver.startActivity(new Activity("com.android.dialer", "main.impl.MainActivity"));
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("com.android.dialer:id/call_log_tab"))).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("com.android.dialer:id/primary_action_view"))).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("com.android.dialer:id/create_new_contact_action"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Name")));

        List<WebElement> contact = driver.findElements(MobileBy.className("android.widget.EditText"));
        contact.get(0).sendKeys("TestName");
        driver.findElement(MobileBy.id("com.android.contacts:id/editor_menu_save_button")).click();
    }

    @Test
    @DisplayName("Check if contact number is not blocked")
    @Order(5)
    public void checkIfContactNumberIsBlocked() throws InterruptedException {
        driver.startActivity(new Activity("co.teltech.callblocker", "activities.SplashActivity"));
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("co.teltech.callblocker:id/counterBlockedCalls")));
        driver.makeGsmCall(phoneNumber, GsmCallActions.CALL);

        // wait for call to go trough
        Thread.sleep(2000);

        WebElement call = driver.findElement(MobileBy.id("co.teltech.callblocker:id/counterBlockedCalls"));

        if(call.isDisplayed())
            call.click();

        wait.until(ExpectedConditions.attributeToBe(MobileBy.id("co.teltech.callblocker:id/counterBlockedCalls"),"text", "1"));
    }

    @Test
    @DisplayName("Enable blacklisting")
    @Order(6)
    public void enableBlacklisting() throws InterruptedException {

        driver.findElement(MobileBy.id("co.teltech.callblocker:id/radioOptionBlacklist")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("co.teltech.callblocker:id/buttonEditBlacklist"))).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("co.teltech.callblocker:id/buttonAddContact"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("TestName"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Navigate up"))).click();

        driver.makeGsmCall(phoneNumber, GsmCallActions.CALL);

        // wait for call to go trough
        Thread.sleep(2000);

        WebElement call = driver.findElement(MobileBy.id("co.teltech.callblocker:id/counterBlockedCalls"));

        if(call.isDisplayed())
            call.click();

        wait.until(ExpectedConditions.attributeToBe(MobileBy.id("co.teltech.callblocker:id/counterBlockedCalls"),"text", "2"));
    }

    @Test
    @DisplayName("Remove number from contact")
    @Order(7)
    public void removeFromContact() throws InterruptedException {
        driver.startActivity(new Activity("com.android.dialer", "main.impl.MainActivity"));
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("com.android.dialer:id/contacts_tab"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Quick contact for TestName"))).click();

        // use sleep for popup to open, rather than wait by xpath
        Thread.sleep(2000);

        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("More options"))).click();

        // use sleep for popup to open, rather than wait by xpath
        Thread.sleep(2000);

        List<WebElement> optionsList = driver.findElements(MobileBy.className("android.widget.LinearLayout"));
        optionsList.get(2).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("android:id/button1"))).click();
    }
}