package ru.pages.demoPages.AdditionalService;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.pages.AddProject;
import ru.pages.DemoPage;
import ru.pages.Page;

public class TrafficCheck extends DemoPage {
    private static final String URL = "https://promopult.ru/seotraf.html";

    private final AddProject project;

    public TrafficCheck(WebDriver driver) {
        super(driver, URL);
        project = new AddProject(driver);
    }

    public AddProject project(){
        return this.project;
    }
}
