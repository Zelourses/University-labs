package ru.pages.demoPages.AdditionalService;

import org.openqa.selenium.WebDriver;
import ru.pages.AddProject;
import ru.pages.DemoPage;
import ru.pages.Page;

public class TextOrder extends DemoPage {
    private static final String URL = "https://promopult.ru/optimization.html";

    private final AddProject project;
    public TextOrder(WebDriver driver) {
        super(driver, URL);
        project = new AddProject(driver);
    }
    public AddProject project(){
        return this.project;
    }

    //Here must be things for textOrder without creating project, but it's not working on site
}
