package ru.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddProject {
    WebDriver driver;

    private static final By ADD_PROJECT_PATH = By.xpath("//input[@class='btn btn-orange']");

    private static final By ADD_URL_PATH = By.xpath("//input[@class='standart_input']");
    private static final By ADD_BUTTON_PATH = By.xpath("//input[@class='btn_or_12d']");
    private static final By ERROR_FIELD_PATH = By.xpath("//div[@class='lightbox_errors']");
    private static final By ADD_PROJECT_CANCEL_BUTTON = By.xpath("//div[@class='lightbox_buttons']/input[@class='btn_gr_8d']");

    private static final By SYSTEM_ERROR_PATH = By.xpath("//p[@class='lightbox_operation']/span");
    private static final By SYSTEM_ERROR_OK_BUTTON = By.xpath("//p[@class='lightbox_operation']/span/div/input");

    public static final String ERROR_WRONG_URL = "Неверный формат адресса сайта.";
    public static final String SYSTEM_ERROR_TEXT ="Ошибка записи данных";

    public AddProject(WebDriver driver){
        this.driver = driver;
    }

    public void tryToAddProject(String urlToWrite){
        driver.findElement(ADD_PROJECT_PATH).click();
        driver.findElement(ADD_URL_PATH).sendKeys(urlToWrite);
        driver.findElement(ADD_BUTTON_PATH).click();
    }
    public String getAddProjectError(){
        return driver.findElement(ERROR_FIELD_PATH).getText();
    }
    public String getSystemErrorText(){
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.elementToBeClickable(SYSTEM_ERROR_OK_BUTTON));
        return driver.findElement(SYSTEM_ERROR_PATH).getText();
    }
    public void closeAddProjectWindow(){
        driver.findElement(ADD_PROJECT_CANCEL_BUTTON).click();
    }
    public void closeErrorWindow(){
        driver.findElement(SYSTEM_ERROR_OK_BUTTON).click();
    }
}
