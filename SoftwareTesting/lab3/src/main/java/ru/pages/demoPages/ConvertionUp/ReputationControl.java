package ru.pages.demoPages.ConvertionUp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.pages.AddProject;
import ru.pages.DemoPage;

public class ReputationControl extends DemoPage {
    private static final String URL = "https://promopult.ru/serm";

    private AddProject project;

    public ReputationControl(WebDriver driver) {
        super(driver, URL);
        project = new AddProject(driver);
    }

    public AddProject project(){
        return this.project;
    }

}
