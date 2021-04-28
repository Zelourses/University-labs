package ru;

import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

@MethodSource("ru.WebDriverUtils#provideWebDrivers")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvideWebDrivers {
}
