package ru.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainPage extends Page {
    private String PAGE_URL = "https://seopult.ru/";

    private static String SECOND_URL = "https://promopult.ru/";
    private By DEMO_WINDOW = new By.ByXPath("//div[@class='lightbox_hint']//div[@class='lightbox_hint_button']");

    private static final By REGISTRATION_PATH =By.xpath("//div/ul/li[2]/a");

    private static final By EMAIL_INPUT = By.xpath("//input[@name='email' and @class='box']");
    private static final By EMAIL_ERROR_FIELD = By.xpath("//div[@class='auth-content-tab auth-content-tab-register active']/form/div[@class='auth-form-row']/div[@class='auth-form-row-error' and position()=1]");
    private static final By PASSWORD_INPUT = By.xpath("//div[@class='auth-content-tab auth-content-tab-register active']//input[@type='password']");
    private static final By PHONE_INPUT = By.xpath("//input[@name='reg_phone']");
    private static final By NAME_INPUT = By.xpath("//input[@name='firstname']");
    private static final  By REGISTRATION_BUTTON_PATH = By.xpath("//div[@class='auth-form-row']/input[@value='Зарегистрировать']");

    public MainPage(WebDriver driver){
        super(driver, SECOND_URL);
    }

    public void clickOnTryDemoWindow() {
        new WebDriverWait(driver,10)
                .until(ExpectedConditions.elementToBeClickable(DEMO_WINDOW));
        driver.findElement(DEMO_WINDOW).click();
    }

    public void clickOnRegistrationButton(){
        WebElement element = driver.findElement(REGISTRATION_PATH);
        driver.manage().window().maximize();
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)",element);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        element.click();
    }

    public void typeIntoPhoneField(){
        if (driver.findElements(PHONE_INPUT).size() == 0)
            throw new IllegalStateException("You must first call clickOnRegistrationButton()");

        driver.findElement(PHONE_INPUT).sendKeys("89999999999");
    }
    public void typeIntoEmailField(){
        if (driver.findElements(EMAIL_INPUT).size() == 0)
            throw new IllegalStateException("You must first call clickOnRegistrationButton()");

        driver.findElement(EMAIL_INPUT).sendKeys("abcdefg@abcd.abc");
    }
    public void typeIntoPasswordField(){
        if (driver.findElements(PASSWORD_INPUT).size() == 0)
            throw new IllegalStateException("You must first call clickOnRegistrationButton()");

        driver.findElement(PASSWORD_INPUT).sendKeys("abcdefg@abcd.abc");
    }
    public void typeIntoNameField(){
        if (driver.findElements(NAME_INPUT).size() == 0)
            throw new IllegalStateException("You must first call clickOnRegistrationButton()");

        driver.findElement(NAME_INPUT).sendKeys("JamesBond");
    }
    public void clickOnTryRegisterButton(){
        if (driver.findElements(REGISTRATION_BUTTON_PATH).size() == 0)
            throw new IllegalStateException("You must first call clickOnRegistrationButton()");

        driver.findElement(REGISTRATION_BUTTON_PATH).click();
    }
    public String getEmailErrorFieldValue(){
        if (driver.findElements(EMAIL_ERROR_FIELD).size() == 0)
            throw new IllegalStateException("You must first call clickOnRegistrationButton()");

        //Need to be here, because site makes about 0.5 seconds delay between response that you failed captcha
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(REGISTRATION_BUTTON_PATH));

        return driver.findElements(EMAIL_ERROR_FIELD).get(0).getText();
    }
}
