package ru.DemoPagesTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.WebDriver;
import ru.ProvideWebDrivers;
import ru.pages.MainPage;
import ru.pages.demoPages.ConvertionUp.ReputationControl;
import ru.pages.demoPages.ConvertionUp.Widgets;
import ru.pages.demoPages.MainDemoPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class WidgetsTest {
    private static final String MAIN_URL = "https://seopult.ru/";
    private static final String REDIRECTED_URL = "https://getsale.io/#/cabinet/wizard";

    private WebDriver driver;

    @ParameterizedTest
    @ProvideWebDrivers
    public void testWidgetsAddProjectButton(List<WebDriver> drivers){
        drivers.stream().parallel().forEach((driver)-> {
            Widgets widget = initWidgets(driver);
            widget.clickOnAddProjectButton();
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            Assertions.assertEquals(2, tabs.size());
        });
    }

    private Widgets initWidgets(WebDriver driver){
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);

        driver.get(MAIN_URL);

        Assertions.assertDoesNotThrow(()->{new MainPage(driver);});
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnTryDemoWindow();

        Assertions.assertDoesNotThrow(()->{
            MainDemoPage.getMainDemoPage(driver).clickOnCLoseHints();
            MainDemoPage.getMainDemoPage(driver).ui().clickToWidgetsAndConsults();});
        return new Widgets(driver);
    }

    @AfterEach
    public void Quit(){
        driver.quit();
    }
}
