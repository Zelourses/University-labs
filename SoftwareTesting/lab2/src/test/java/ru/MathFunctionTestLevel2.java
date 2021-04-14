package ru;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class MathFunctionTestLevel2 {
    private final static double EPSILON = 0.00001;
    private final static double PERIOD = Math.PI*2;
    private final static double DELTA = 0.001;

    private static MathFunction func;

    @BeforeAll
    static void initMock(){
        func = new MathFunction(new MathThings(new BasicMath()));
    }

    @Test
    void testZeroX(){
        //Test zero
        Assertions.assertEquals(Double.NaN,func.lab2_func(0.0));
        Assertions.assertEquals(0.0001,func.lab2_func(0.0-EPSILON), DELTA);
    }

    @Test
    void checkNegativeExtremumFunctions() {
        //Check first negative point
        Assertions.assertEquals(12.998,func.lab2_func(-1.393),DELTA);
        Assertions.assertEquals(12.998,func.lab2_func(-1.393-EPSILON),DELTA);
        Assertions.assertEquals(12.998,func.lab2_func(-1.393+EPSILON),DELTA);
        Assertions.assertEquals(12.998,func.lab2_func(-1.393-PERIOD),DELTA);

        //Check third negative point
        Assertions.assertEquals(0.465,func.lab2_func(-3.528),DELTA);
        Assertions.assertEquals(0.465,func.lab2_func(-3.528+EPSILON),DELTA);
        Assertions.assertEquals(0.465,func.lab2_func(-3.528-EPSILON),DELTA);
        Assertions.assertEquals(0.506,func.lab2_func(-3.528-PERIOD),DELTA);

        //Check fourth negative point
        Assertions.assertEquals(1.11,func.lab2_func(-3.838),DELTA);
        Assertions.assertEquals(1.11,func.lab2_func(-3.838+EPSILON),DELTA);
        Assertions.assertEquals(1.11,func.lab2_func(-3.838-EPSILON),DELTA);
        //Assertions.assertEquals(0.72,func.lab2_func(-3.838-PERIOD),DELTA);

        //Check sixth negative point
        Assertions.assertEquals(-0.504,func.lab2_func(-6.15),DELTA);
        Assertions.assertEquals(-0.504,func.lab2_func(-6.15+EPSILON),DELTA);
        Assertions.assertEquals(-0.504,func.lab2_func(-6.15-EPSILON),DELTA);
        //Assertions.assertEquals(-0.504,func.lab2_func(-6.15-PERIOD),DELTA);

    }

    @Test
    void checkNegativeZeroes(){
        //Check second negative point
        Assertions.assertEquals(0.0,func.lab2_func(-2.314),DELTA);
        Assertions.assertEquals(0.0,func.lab2_func(-2.314+EPSILON),DELTA);
        Assertions.assertEquals(0.0,func.lab2_func(-2.314-EPSILON),DELTA);
        Assertions.assertEquals(0.0,func.lab2_func(-2.314-PERIOD),DELTA);

        //Check fifth negative point
        Assertions.assertEquals(0.004,func.lab2_func(-4.016),DELTA);
        Assertions.assertEquals(0.004,func.lab2_func(-4.016+EPSILON),DELTA);
        Assertions.assertEquals(0.004,func.lab2_func(-4.016-EPSILON),DELTA);
        Assertions.assertEquals(0.378,func.lab2_func(-4.016-PERIOD),DELTA);

        //Check sixth negative point
        Assertions.assertEquals(0.0,func.lab2_func(-5.626),DELTA);
        Assertions.assertEquals(0.0,func.lab2_func(-5.626+EPSILON),DELTA);
        Assertions.assertEquals(0.0,func.lab2_func(-5.626-EPSILON),DELTA);
        //
        // Assertions.assertEquals(0.0,func.lab2_func(-5.626-PERIOD),DELTA);

    }

    @Test
    void checkPositiveZeroes(){
        //Check third positive point
        Assertions.assertEquals(0,func.lab2_func(4),DELTA);

        //Check fourth positive point
        Assertions.assertEquals(0,func.lab2_func(5),DELTA);
    }

    @Test
    void checkPositivePoints(){

        //Check first positive point
        Assertions.assertEquals(Double.NaN,func.lab2_func(1),DELTA);

        //Check second positive point
        Assertions.assertEquals(-1.526,func.lab2_func(2.1),DELTA);
    }

}
