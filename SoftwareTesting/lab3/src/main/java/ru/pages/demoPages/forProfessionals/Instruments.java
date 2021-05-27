package ru.pages.demoPages.ForProfessionals;

import org.openqa.selenium.WebDriver;
import ru.pages.DemoPage;
import ru.pages.Page;

public class Instruments extends DemoPage {
    private static final String URL = "https://promopult.ru/professional_tools.html";
    public Instruments(WebDriver driver) {
        super(driver, URL);
    }
}
