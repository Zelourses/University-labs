package ru.pages.demoPages.ForProfessionals;

import org.openqa.selenium.WebDriver;
import ru.pages.DemoPage;
import ru.pages.Page;

public class BlackListOfDonors extends DemoPage {
    private static final String URL = "https://promopult.ru/blacklist.html";
    public BlackListOfDonors(WebDriver driver) {
        super(driver, URL);
    }
}
