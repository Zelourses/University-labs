package ru;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.*;
import ru.pages.demoPages.MainDemoPage;
import ru.pages.MainPage;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MainPageTest {

    private WebDriver driver;
    private static final String MAIN_URL = "https://seopult.ru/";

    private static final By DemoWindowTextPath = By.xpath("//div[@class='visible']//div/div[2]/div");
    private static final String DemoWindowText = "Демонстрационный режим PromoPult. Вы можете посмотреть интерфейсы системы всех модулей у запущенного проекта";

    private static final By HINTS_TEXT_PATH = By.xpath("//body[@onload]/div[1]/div[4]/div[@style]");
    private static final String HINTS_TEXT = "Знакомство с интерфейсом Системы";

    private static final String FAILED_RECAPTCHA = "Вы не прошли проверку recaptcha";


    @ParameterizedTest
    @ProvideWebDrivers
    public void checkRegistrationWindow(List<WebDriver> drivers){
        drivers.stream().parallel().forEach((driver)-> {
            this.driver = driver;
            driver.get(MAIN_URL);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

            Assertions.assertDoesNotThrow(() -> {
                new MainPage(driver);
            });

            MainPage mainPage = new MainPage(driver);
            Assertions.assertThrows(IllegalStateException.class, mainPage::typeIntoEmailField);
            Assertions.assertThrows(IllegalStateException.class, mainPage::typeIntoPasswordField);
            Assertions.assertThrows(IllegalStateException.class, mainPage::typeIntoPhoneField);
            Assertions.assertThrows(IllegalStateException.class, mainPage::typeIntoNameField);
            Assertions.assertThrows(IllegalStateException.class, mainPage::getEmailErrorFieldValue);
            Assertions.assertThrows(IllegalStateException.class, mainPage::clickOnTryRegisterButton);

            mainPage.clickOnRegistrationButton(); //and now...

            Assertions.assertDoesNotThrow(mainPage::typeIntoEmailField);
            Assertions.assertDoesNotThrow(mainPage::typeIntoPasswordField);
            Assertions.assertDoesNotThrow(mainPage::typeIntoPhoneField);
            Assertions.assertDoesNotThrow(mainPage::typeIntoNameField);
            Assertions.assertDoesNotThrow(mainPage::clickOnTryRegisterButton);

            Assertions.assertTrue(mainPage.getEmailErrorFieldValue().contains(FAILED_RECAPTCHA));
        });
    }

    @ParameterizedTest
    @ProvideWebDrivers
    public void checkOnStartAndEndOfDemoVersion(List<WebDriver> drivers) {
        drivers.stream().parallel().forEach((driver)-> {
            this.driver = driver;
            driver.get(MAIN_URL);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

            Assertions.assertDoesNotThrow(() -> {
                new MainPage(driver);
            });

            MainPage mainPage = new MainPage(driver);
            mainPage.clickOnTryDemoWindow();

            Assertions.assertDoesNotThrow(() -> {
                MainDemoPage.getMainDemoPage(driver);
            });

            MainDemoPage demoPage = MainDemoPage.getMainDemoPage(driver);
            Assertions.assertTrue(driver.findElement(DemoWindowTextPath).getText().contains(DemoWindowText));
            Assertions.assertTrue(driver.findElement(HINTS_TEXT_PATH).getText().contains(HINTS_TEXT));
            demoPage.clickOnCLoseHints();
            Assertions.assertFalse(driver.findElement(HINTS_TEXT_PATH).getText().contains(HINTS_TEXT));
            demoPage.ui().clickOnExitFromDemoWindow();
        });
    }



    @AfterEach
    public void Quit(){
        driver.quit();
    }
}
