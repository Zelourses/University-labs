package ru.pages.demoPages.AdditionalService;

import org.openqa.selenium.WebDriver;
import ru.pages.DemoPage;
import ru.pages.Page;

public class PromotionToArticles extends DemoPage {
    private static final String URL = "https://promopult.ru/articles.html";
    public PromotionToArticles(WebDriver driver) {
        super(driver, URL);
    }
}
