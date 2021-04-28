package ru.DemoPagesTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Text;
import ru.ProvideWebDrivers;
import ru.pages.AddProject;
import ru.pages.MainPage;
import ru.pages.demoPages.AdditionalService.TextOrder;
import ru.pages.demoPages.AdditionalService.TrafficCheck;
import ru.pages.demoPages.MainDemoPage;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TextOrderTest {
    private static final String MAIN_URL = "https://seopult.ru/";

    private WebDriver driver;

    @ParameterizedTest
    @ProvideWebDrivers
    public void testAddProjectButton(List<WebDriver> drivers){
        drivers.stream().parallel().forEach((driver)-> {
            TextOrder textOrder = initTextOrder(driver);
            Assertions.assertDoesNotThrow(() -> {
                textOrder.project().tryToAddProject("Perm");
            });
            Assertions.assertTrue(textOrder.project().getAddProjectError().contains(AddProject.ERROR_WRONG_URL));
            Assertions.assertDoesNotThrow(textOrder.project()::closeAddProjectWindow);

            Assertions.assertDoesNotThrow(() -> {
                textOrder.project().tryToAddProject("Urupinsk.ru");
            });
            Assertions.assertEquals(AddProject.SYSTEM_ERROR_TEXT, textOrder.project().getSystemErrorText());
            Assertions.assertDoesNotThrow(textOrder.project()::closeErrorWindow);

            Assertions.assertThrows(Exception.class, textOrder.project()::getAddProjectError);
            Assertions.assertThrows(Exception.class, textOrder.project()::getSystemErrorText);
        });
    }


    private TextOrder initTextOrder(WebDriver driver){
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);

        driver.get(MAIN_URL);

        Assertions.assertDoesNotThrow(()->{new MainPage(driver);});
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnTryDemoWindow();

        Assertions.assertDoesNotThrow(()->MainDemoPage.getMainDemoPage(driver));
        MainDemoPage p = MainDemoPage.getMainDemoPage(driver);
        Assertions.assertDoesNotThrow(()->{
            p.clickOnCLoseHints();
            p.ui().clickToTextOrder();});
        return new TextOrder(driver);
    }
    @AfterEach
    public void Quit(){
        driver.quit();
    }
}
