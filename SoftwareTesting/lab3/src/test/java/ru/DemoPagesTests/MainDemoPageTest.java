package ru.DemoPagesTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.WebDriver;
import ru.ProvideWebDrivers;
import ru.pages.demoPages.MainDemoPage;
import ru.pages.MainPage;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MainDemoPageTest {
    private WebDriver driver;

    private static final String MAIN_URL = "https://seopult.ru/";
    private static final String USERNAME = "Платон";


    @ParameterizedTest
    @ProvideWebDrivers
    public void testExitFromDemoWindow(List<WebDriver> drivers){
        drivers.stream().parallel().forEach((driver)->{
            MainDemoPage demoPage = initMainDemoPage(driver);
            driver.manage().window().maximize();
            demoPage.ui().clickOnExitFromDemoWindow();
        });
    }

    @ParameterizedTest
    @ProvideWebDrivers
    public void testClickOnCloseHints(List<WebDriver> drivers){
        drivers.stream().parallel().forEach((driver)->{
            MainDemoPage demoPage = initMainDemoPage(driver);
            driver.manage().window().maximize();
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToControlReputation);
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToWidgetsAndConsults);

            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToTrafficCheck);
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToNaturalLinks);
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToTextOrder);
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToDevelopContent);
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToPromotionToArticles);
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToSiteAudits);

            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToInstruments);
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToManualLinkModes);
            Assertions.assertThrows(IllegalStateException.class, demoPage.ui()::clickToBlackListOfDonors);

            demoPage.clickOnCLoseHints();

            Assertions.assertDoesNotThrow(demoPage.ui()::clickToControlReputation);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToWidgetsAndConsults);

            Assertions.assertDoesNotThrow(demoPage.ui()::clickToTrafficCheck);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToNaturalLinks);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToTextOrder);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToDevelopContent);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToPromotionToArticles);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToSiteAudits);

            Assertions.assertDoesNotThrow(demoPage.ui()::clickToInstruments);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToManualLinkModes);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToBlackListOfDonors);

            Assertions.assertDoesNotThrow(demoPage.ui()::clickToBilling);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToAccountDetails);

            Assertions.assertDoesNotThrow(demoPage.ui()::clickToFeedback);
            Assertions.assertDoesNotThrow(demoPage.ui()::clickToNotifications);
        });

    }

    @ParameterizedTest
    @ProvideWebDrivers
    public void testUserName(List<WebDriver> drivers){
        drivers.stream().parallel().forEach((driver)->{
            MainDemoPage demoPage = initMainDemoPage(driver);
            Assertions.assertTrue(demoPage.ui().checkUserName(USERNAME));
        });
    }

    private MainDemoPage initMainDemoPage(WebDriver driver){
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);

        driver.get(MAIN_URL);

        Assertions.assertDoesNotThrow(()->{new MainPage(driver);});
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnTryDemoWindow();

        Assertions.assertDoesNotThrow(()->{MainDemoPage.getMainDemoPage(driver);});
        return MainDemoPage.getMainDemoPage(driver);
    }

    @AfterEach
    public void Quit(){
        driver.quit();
    }

}
