package ru.pages.demoPages.ForProfessionals;

import org.openqa.selenium.WebDriver;
import ru.pages.DemoPage;
import ru.pages.Page;

public class ManualLinksMode extends DemoPage {
    private static final String URL = "https://promopult.ru/filters.html";
    public ManualLinksMode(WebDriver driver) {
        super(driver, URL);
    }
}
