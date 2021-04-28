package ru.DemoPagesTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.WebDriver;
import ru.ProvideWebDrivers;
import ru.pages.AddProject;
import ru.pages.MainPage;
import ru.pages.demoPages.ConvertionUp.ReputationControl;
import ru.pages.demoPages.MainDemoPage;

import java.util.List;
import java.util.concurrent.TimeUnit;


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ReputationControlTest {
    private static final String MAIN_URL = "https://seopult.ru/";
    private WebDriver driver;



    @ParameterizedTest
    @ProvideWebDrivers
    public void testAddProjectButton(List<WebDriver> drivers){
        drivers.stream().parallel().forEach((driver)-> {
            ReputationControl control = initReputationControl(driver);
            Assertions.assertDoesNotThrow(() -> {
                control.project().tryToAddProject("Samara");
            });
            Assertions.assertTrue(control.project().getAddProjectError().contains(AddProject.ERROR_WRONG_URL));
            Assertions.assertDoesNotThrow(control.project()::closeAddProjectWindow);

            Assertions.assertDoesNotThrow(() -> {
                control.project().tryToAddProject("Perm.ru");
            });
            Assertions.assertEquals(AddProject.SYSTEM_ERROR_TEXT, control.project().getSystemErrorText());
            Assertions.assertDoesNotThrow(control.project()::closeErrorWindow);

            Assertions.assertThrows(Exception.class, control.project()::getAddProjectError);
            Assertions.assertThrows(Exception.class, control.project()::getSystemErrorText);
        });
    }


    private ReputationControl initReputationControl(WebDriver driver){
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);

        driver.get(MAIN_URL);

        Assertions.assertDoesNotThrow(()->{new MainPage(driver);});
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnTryDemoWindow();

        Assertions.assertDoesNotThrow(()->{
            MainDemoPage.getMainDemoPage(driver).clickOnCLoseHints();
            MainDemoPage.getMainDemoPage(driver).ui().clickToControlReputation();});

        return new ReputationControl(driver);
    }



    @AfterEach
    public void Quit(){
        driver.quit();
    }
}
