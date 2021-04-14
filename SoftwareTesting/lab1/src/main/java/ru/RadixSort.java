package ru;

public class RadixSort {
    private int amountOfIntsInNumber;
    private int[] sortingArray;


    public RadixSort(int[] array){
        if (array == null)
            throw new NullPointerException("Array is null");
        if (array.length == 0)
            throw new ArrayStoreException("Array must be more than zero elements");
        this.sortingArray = array;
        findMaxNumber();
    }
    public RadixSort(){
        sortingArray = new int[]{};
    }
    private void findMaxNumber(){
        int max = Integer.MIN_VALUE;
        for (int i : sortingArray)
            if (i > max)
                max = i;
        int amount;
        for (amount = 0; max != 0; max = max / 10)
            amount++;
        amountOfIntsInNumber = amount;
    }

    public void setArray(int[] array){
        if (array.length == 0)
            throw new ArrayStoreException("Array must be more than zero elements");
        sortingArray = array;
        findMaxNumber();
    }

    public RadixSort sort(){
        if (sortingArray.length ==1 ){
            return this;
        }

        for (int i = 0; i < amountOfIntsInNumber; i++) {
            sorter(i);
        }
        return this;
    }

    private void sorter(int numberToGet){
        int[] outputArray = new int[sortingArray.length];
        int[] numbers = new int[10];
        int exp = (int)Math.pow(10,numberToGet);
        for (int i=0; i<sortingArray.length;i++) {
            numbers[(sortingArray[i]/exp) % 10]++;
        }
        for (int i=1;i<numbers.length;i++){
            numbers[i] += numbers[i-1];
        }
        for (int i = sortingArray.length-1; i >= 0; i-- ){
            outputArray[numbers[(sortingArray[i] / exp)%10]-1] = sortingArray[i];
            numbers[(sortingArray[i] / exp) % 10]--;
        }

        for (int i=0; i<sortingArray.length;i++){
            sortingArray[i] = outputArray[i];
        }
    }



    public int[] getArray(){
        return sortingArray;
    }

}
