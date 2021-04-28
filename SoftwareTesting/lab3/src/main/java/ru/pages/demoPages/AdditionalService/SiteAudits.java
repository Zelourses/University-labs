package ru.pages.demoPages.AdditionalService;

import org.openqa.selenium.WebDriver;
import ru.pages.DemoPage;
import ru.pages.Page;

public class SiteAudits extends DemoPage {
    private static final String URL = "https://promopult.ru/support.html";
    public SiteAudits(WebDriver driver) {
        super(driver, URL);
    }
}
