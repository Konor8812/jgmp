package com.illia.algorithm;


public class MergeSortAlgorithm {

  public static void mergeSort(int[] input) {
    var inputLength = input.length;
    if (inputLength <= 1) {
      return;
    }

    var center = inputLength / 2;
    var left = new int[center];
    System.arraycopy(input, 0, left, 0, left.length);
    var right = new int[inputLength - center];
    System.arraycopy(input, inputLength - center, right, 0, right.length);

    mergeSort(left);
    mergeSort(right);
    merge(input, left, right);
  }

  private static void merge(int[] input, int[] left, int[] right) {
    int leftArrayPointer = 0;
    int rightArrayPointer = 0;
    int inputArrayIndex = 0;
    while (leftArrayPointer < left.length && rightArrayPointer < right.length) {
      if (left[leftArrayPointer] >= right[rightArrayPointer]) {
        input[inputArrayIndex++] = right[rightArrayPointer++];
      } else {
        input[inputArrayIndex++] = left[leftArrayPointer++];
      }
    }
    while (leftArrayPointer < left.length) {
      input[inputArrayIndex++] = left[leftArrayPointer++];
    }
    while (rightArrayPointer < right.length) {
      input[inputArrayIndex++] = right[rightArrayPointer++];
    }
  }


}
