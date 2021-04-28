package ru;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WebDriverUtils {
    private static final String SETTINGS_FILE = "/settings.properties";
    private static Properties props = new Properties();
    @SuppressWarnings("unused")
    public static Stream<List<WebDriver>> provideWebDrivers(){
        try {
            props.load(WebDriverUtils.class.getResourceAsStream(SETTINGS_FILE));
        }catch (Exception e){
            System.out.println("Failed to load settings from "+SETTINGS_FILE);
        }
        System.setProperty("webdriver.gecko.driver",props.getProperty("browsers.firefox.driver"));
        System.setProperty("webdriver.chrome.driver", props.getProperty("browsers.chrome.driver"));

        List<String> browsers = Arrays.stream(props.getProperty("bot.user_browsers").split(",")).map(String::trim).collect(Collectors.toList());
        Stream.Builder<List<WebDriver>> builder = Stream.builder();
        List<WebDriver> list = new ArrayList<>();
        if (browsers.contains("chrome")){
            list.add(makeChromeDriver());
        }
        if (browsers.contains("firefox")){
            list.add(makeFirefoxDriver());
        }
        builder.add(list);
        return builder.build();
    }

    private static ChromeDriver makeChromeDriver() {
        var options = new ChromeOptions();
        options.setBinary(props.getProperty("browsers.chrome.binary"));
        return new ChromeDriver(options);
    }

    private static FirefoxDriver makeFirefoxDriver() {
        var options = new FirefoxOptions();
        options.setBinary(new FirefoxBinary(new File(props.getProperty("browsers.firefox.binary"))));
        options.setLogLevel(FirefoxDriverLogLevel.ERROR);
        return new FirefoxDriver(options);
    }
}
