package ru;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Resources;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class SecantTest {
    @Test
    void testAsymptotes() {
        Assertions.assertEquals(Double.POSITIVE_INFINITY, Secant.sec(Math.PI/2), 1e-8);
        Assertions.assertEquals(Double.POSITIVE_INFINITY, Secant.sec(-Math.PI/2),1e-8);
    }
    @Test
    void testLocalMinAndMax(){
        Assertions.assertEquals(-1.0d, Secant.sec(-Math.PI), 1e-8);
        Assertions.assertEquals(1.0d, Secant.sec(0),1e-8);
        Assertions.assertEquals(-1.0d, Secant.sec(Math.PI),1e-8);
        Assertions.assertEquals(1.0d, Secant.sec(2*Math.PI), 1e-3);
    }
    @Test
    void testOnInvalidInput(){
        Assertions.assertEquals(Double.NaN, Secant.sec(Double.NaN));
        Assertions.assertEquals(Double.NaN,Secant.sec(Double.POSITIVE_INFINITY));
        Assertions.assertEquals(Double.NaN,Secant.sec(Double.NEGATIVE_INFINITY));
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/secantTest.csv", delimiter = ';')
    void testParams(double value, double expected){
        Assertions.assertEquals(expected, Secant.sec(value), 1e-3);
    }
}
