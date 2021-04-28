package ru.DemoPagesTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import ru.pages.MainPage;
import ru.pages.demoPages.AdditionalService.PromotionToArticles;
import ru.pages.demoPages.AdditionalService.TextOrder;
import ru.pages.demoPages.MainDemoPage;

import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PromotionToArticlesTest {

    private static final String MAIN_URL = "https://seopult.ru/";

    private WebDriver driver;

    private PromotionToArticles initPromotionToArticles(WebDriver driver){
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);

        driver.get(MAIN_URL);

        Assertions.assertDoesNotThrow(()->{new MainPage(driver);});
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnTryDemoWindow();

        Assertions.assertDoesNotThrow(()-> MainDemoPage.getMainDemoPage(driver));
        MainDemoPage p = MainDemoPage.getMainDemoPage(driver);
        Assertions.assertDoesNotThrow(()->{
            p.clickOnCLoseHints();
            p.ui().clickToPromotionToArticles();});
        return new PromotionToArticles(driver);
    }
    @AfterEach
    public void Quit(){
        driver.quit();
    }
}
