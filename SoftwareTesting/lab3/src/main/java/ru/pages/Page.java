package ru.pages;

import org.openqa.selenium.WebDriver;

public class Page {
    protected WebDriver driver;

    public Page(WebDriver driver, String url){
        this.driver = driver;
        if (!driver.getCurrentUrl().equals(url)){
            throw new IllegalStateException("Problem with URL: "+url);
        }
    }

}
