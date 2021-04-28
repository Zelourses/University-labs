package ru.pages.demoPages.ConvertionUp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.pages.DemoPage;
import ru.pages.Page;

public class Widgets extends DemoPage {
    private static final String URL ="https://promopult.ru/getsale/projects.html";

    private static final By ADD_PROJECT_BUTTON = By.xpath("//a[@class='btn btn-orange']");
    public Widgets(WebDriver driver) {
        super(driver,URL);
    }

    public void clickOnAddProjectButton(){
        driver.findElement(ADD_PROJECT_BUTTON).click();
    }
}
