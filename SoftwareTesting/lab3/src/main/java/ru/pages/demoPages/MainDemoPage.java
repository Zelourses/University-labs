package ru.pages.demoPages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.pages.DemoPage;
import ru.pages.Page;

public class MainDemoPage extends DemoPage {

    private static String URL = "https://promopult.ru/items.html?ok&u=947058";
    private static final String mainURL = "https://promopult.ru/items.html?ok&u=947058";
    private static final String additionalURL = "https://promopult.ru/items.html";

    private static final By HINTS_BUTTON = By.xpath("//body[@onload]/div[1]/div[4]/span[1]");


    private MainDemoPage(WebDriver driver) {
            super(driver,URL);
    }

    public static MainDemoPage getMainDemoPage(WebDriver driver){
        if (driver.getCurrentUrl().equals(additionalURL)){
            URL = additionalURL;
        }else {
            URL = mainURL;
        }
        return new MainDemoPage(driver);
    }
    public void clickOnCLoseHints(){
        driver.findElement(HINTS_BUTTON).click();
    }



}
