package ru.pages;

import org.openqa.selenium.WebDriver;
import ru.pages.demoPages.DemoWindowUI;

public class DemoPage extends Page{
    private final DemoWindowUI ui;
    public DemoPage(WebDriver driver, String url) {
        super(driver, url);
        ui = new DemoWindowUI(driver);
    }

    public DemoWindowUI ui() {
        return ui;
    }
}
