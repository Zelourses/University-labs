package ru.pages.demoPages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DemoWindowUI {

    WebDriver driver;

    private static final By EXIT_FROM_DEMO_BUTTON = By.xpath("//div[@class='visible']//div/div[2]/div/a[@href]");

    private static final By USERNAME_PATH = By.xpath("//div[@class='uname  warning']");

    private static final By CONTROL_REPUTATION_PATH = By.xpath("//a[@data-tip='Управление репутацией']");
    private static final By WIDGETS_PATH = By.xpath("//a[@data-tip='Виджеты, рассылки, консультант']");

    private static final By TRAFFIC_CHECK_PATH = By.xpath("//a[@data-tip='Трафиковое продвижение']");
    private static final By NATURAL_LINKS_PATH = By.xpath("//a[@data-tip='Естественные ссылки']");
    private static final By TEXT_ORDER_PATH = By.xpath("//a[@data-tip='Разовые заказы текстов']");
    private static final By DEVELOP_CONTENT_PATH = By.xpath("//a[@data-tip='Регулярное развитие контента']");
    private static final By PROMOTION_TO_ARTICLES_PATH = By.xpath("//a[@data-tip='Продвижение статьями']");
    private static final By SITE_AUDITS_PATH = By.xpath("//a[@data-tip='Аудиты сайтов']");

    private static final By INSTRUMENTS_PATH = By.xpath("//a[@data-tip='Инструменты']");
    private static final By MANUAL_LINK_MODE_PATH = By.xpath("//a[@data-tip='Ручные режимы ссылок']");
    private static final By BLACK_LIST_OF_DONORS_PATH = By.xpath("//a[@data-tip='Черный список доноров']");


    private static final By BILLING_PATH = By.xpath("//a[@class='rm-btn balance rm-balance-btn']");
    private static final By ACCOUNT_DETAILS_PATH = By.xpath("//a[@class='rm-btn rm-user ']");
    private static final By FEEDBACK_PATH = By.xpath("//a[@class='rm-btn rm-feedback-btn new-messages' and @href='/feedback.html']");
    private static final By NOTIFICATIONS_PATH = By.xpath("//a[@class='rm-btn rm-feedback-btn new-messages' and @href='/notifications.html']");

    public DemoWindowUI(WebDriver driver){
        this.driver = driver;
    }

    public boolean checkUserName(String username){
        return driver.findElement(USERNAME_PATH).getText().trim().split("\n")[0].equals(username);
    }

    public void clickOnExitFromDemoWindow(){
        tryToClick(EXIT_FROM_DEMO_BUTTON);
    }

    public void clickToControlReputation(){ tryToClick(CONTROL_REPUTATION_PATH); }
    public void clickToWidgetsAndConsults(){ tryToClick(WIDGETS_PATH);}

    //Additional service

    public void clickToTrafficCheck(){tryToClick(TRAFFIC_CHECK_PATH);}
    public void clickToNaturalLinks(){tryToClick(NATURAL_LINKS_PATH);}
    public void clickToTextOrder(){tryToClick(TEXT_ORDER_PATH);}
    public void clickToDevelopContent(){tryToClick(DEVELOP_CONTENT_PATH);}
    public void clickToPromotionToArticles(){tryToClick(PROMOTION_TO_ARTICLES_PATH);}
    public void clickToSiteAudits(){tryToClick(SITE_AUDITS_PATH);}

    //For professionals section

    public void clickToInstruments(){tryToClick(INSTRUMENTS_PATH);}
    public void clickToManualLinkModes(){tryToClick(MANUAL_LINK_MODE_PATH);}
    public void clickToBlackListOfDonors(){tryToClick(BLACK_LIST_OF_DONORS_PATH);}


    public void clickToBilling(){tryToClick(BILLING_PATH);}
    public void clickToAccountDetails(){tryToClick(ACCOUNT_DETAILS_PATH);} //little pop-up
    public void clickToFeedback(){tryToClick(FEEDBACK_PATH);} //Messages
    public void clickToNotifications(){tryToClick(NOTIFICATIONS_PATH);}


    private void tryToClick(By toClickOn){
        try {
            WebElement element = driver.findElement(toClickOn);
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)",element);
            element.click();
        }catch (Exception e){
            throw new IllegalStateException("First you need to call clickOnCLoseHints");
        }
    }
}
