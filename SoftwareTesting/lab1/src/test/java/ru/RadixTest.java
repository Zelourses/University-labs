package ru;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RadixTest {
    public boolean isSorted(int[] sortingArray){
        int test = Integer.MIN_VALUE;
        for (int i : sortingArray){
            if (test > i)
                return false;
            test = i;
        }
        return true;
    }

    @Test
    void testEmpty(){
        RadixSort sort = new RadixSort();
        Assertions.assertTrue(isSorted(sort.sort().getArray()));
        Assertions.assertThrows(ArrayStoreException.class,() -> sort.setArray(new int[]{}));
        Assertions.assertThrows(ArrayStoreException.class,()-> new RadixSort(new int[]{}));
        Assertions.assertThrows(NullPointerException.class,()-> new RadixSort(null));

    }
    @Test
    void testSort(){
        RadixSort sort = new RadixSort();
        Assertions.assertDoesNotThrow(()-> sort.setArray(new int[]{1,2,3,4,5,6,7,8,9,10}));
        Assertions.assertTrue(isSorted(sort.sort().getArray()));
        Assertions.assertDoesNotThrow(()-> sort.setArray(new int[]{10,9,8,7,6,5,4,3,2,1}));
        Assertions.assertTrue(isSorted(sort.sort().getArray()));
        Assertions.assertDoesNotThrow(() ->{
            sort.setArray(new Random(1001).ints(10,0, Integer.MAX_VALUE).toArray());
        });
        Assertions.assertTrue(()->isSorted(sort.sort().getArray()));
        Assertions.assertDoesNotThrow(()-> sort.setArray(new int[]{1}));
        Assertions.assertTrue(()->isSorted(sort.sort().getArray()));
        Assertions.assertDoesNotThrow(()->new RadixSort(new int[]{1,2,10}));
    }

    @Test
    void testSortNegative(){
        RadixSort sort = new RadixSort();
        Assertions.assertDoesNotThrow(() ->{
            sort.setArray(new Random(1001).ints(10,Integer.MIN_VALUE, -1).toArray());
        });
        Assertions.assertThrows(Exception.class, ()->isSorted(sort.sort().getArray()));
    }
}
