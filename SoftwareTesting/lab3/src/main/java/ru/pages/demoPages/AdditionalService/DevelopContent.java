package ru.pages.demoPages.AdditionalService;

import org.openqa.selenium.WebDriver;
import ru.pages.AddProject;
import ru.pages.DemoPage;
import ru.pages.Page;

public class DevelopContent extends DemoPage {
    private static final String URL = "https://promopult.ru/optimization.html?module=development";

    private final AddProject project;

    public DevelopContent(WebDriver driver) {
        super(driver, URL);
        project = new AddProject(driver);
    }
    public AddProject project(){
        return this.project;
    }
}
