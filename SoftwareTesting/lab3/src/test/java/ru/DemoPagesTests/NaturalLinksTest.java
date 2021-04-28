package ru.DemoPagesTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.WebDriver;
import ru.ProvideWebDrivers;
import ru.pages.AddProject;
import ru.pages.MainPage;
import ru.pages.demoPages.AdditionalService.NaturalLinks;
import ru.pages.demoPages.ConvertionUp.ReputationControl;
import ru.pages.demoPages.MainDemoPage;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class NaturalLinksTest {
    private static final String MAIN_URL = "https://seopult.ru/";

    private WebDriver driver;

    @ParameterizedTest
    @ProvideWebDrivers
    public void testAddProjectButton(List<WebDriver> drivers){
        drivers.stream().parallel().forEach((driver)-> {
            NaturalLinks link = initNaturalLinks(driver);

            Assertions.assertDoesNotThrow(() -> {
                link.project().tryToAddProject("Samara");
            });
            Assertions.assertTrue(link.project().getAddProjectError().contains(AddProject.ERROR_WRONG_URL));
            Assertions.assertDoesNotThrow(link.project()::closeAddProjectWindow);

            Assertions.assertDoesNotThrow(() -> {
                link.project().tryToAddProject("Perm.ru");
            });
            Assertions.assertEquals(AddProject.SYSTEM_ERROR_TEXT, link.project().getSystemErrorText());
            Assertions.assertDoesNotThrow(link.project()::closeErrorWindow);

            Assertions.assertThrows(Exception.class, link.project()::getAddProjectError);
            Assertions.assertThrows(Exception.class, link.project()::getSystemErrorText);
        });
    }

    private NaturalLinks initNaturalLinks(WebDriver driver){
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);

        driver.get(MAIN_URL);

        Assertions.assertDoesNotThrow(()->{new MainPage(driver);});
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnTryDemoWindow();

        Assertions.assertDoesNotThrow(()->{
            MainDemoPage.getMainDemoPage(driver).clickOnCLoseHints();
            MainDemoPage.getMainDemoPage(driver).ui().clickToNaturalLinks();});

        return new NaturalLinks(driver);
    }

    @AfterEach
    public void Quit(){
        driver.quit();
    }
}
