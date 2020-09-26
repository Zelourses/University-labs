import beans.MainBean;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class MainTests {
		
    @Test
    public void testBorders(){
			
        assertTrue(MainBean.check(-1.6,1.2,2));
        assertTrue(MainBean.check(-1.2,1.6,2));
        assertTrue(MainBean.check(-4,0,4));
        assertTrue(MainBean.check(-4,-1,4));
        assertTrue(MainBean.check(-4,-2,4));
        assertTrue(MainBean.check(-2,-2,4));
        assertTrue(MainBean.check(0,-2,4));
        assertTrue(MainBean.check(0,0,4));
        assertTrue(MainBean.check(4,0,4));
        assertTrue(MainBean.check(2,1,4));
        assertTrue(MainBean.check(0,2,4));
        assertTrue(MainBean.check(0,4,4));
    }
    @Test
    public void testInside(){
        assertTrue(MainBean.check(-2,2,4));
        assertTrue(MainBean.check(1,1,4));
        assertTrue(MainBean.check(-2,-1,4));
        assertTrue(MainBean.check(0,0,4));
    }
    @Test
    public void testOutside(){
        assertFalse(MainBean.check(-3.442,2.05,4));
        assertFalse(MainBean.check(-4.0001,0,4));
        assertFalse(MainBean.check(0.0001,-0.0001,4));
        assertFalse(MainBean.check(2,-1,4));
        assertFalse(MainBean.check(-10,10,4));
        assertFalse(MainBean.check(-10,-10,4));
        assertFalse(MainBean.check(10,-10,4));
        assertFalse(MainBean.check(10,10,4));
        assertFalse(MainBean.check(4.0001,0,4));
        assertFalse(MainBean.check(4,-0.0001,4));
    }

}
