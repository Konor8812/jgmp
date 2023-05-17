package com.illia.algorithm;

public class InsertionSortAlgorithm {

  public static void insertionSort(int[] input) {

    for (int i = 0; i < input.length - 1; i++) {

      int currentLowest = input[i];
      int index = i;
      for (int j = i + 1; j < input.length; j++) {
        if (input[j] < currentLowest) {
          currentLowest = input[j];
          index = j;
        }
      }
      input[index] = input[i];
      input[i] = currentLowest;

    }
  }

}
