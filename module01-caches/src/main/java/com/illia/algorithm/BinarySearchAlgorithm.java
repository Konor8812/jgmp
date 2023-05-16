package com.illia.algorithm;

public class BinarySearchAlgorithm {

  public static int searchRecursively(int[] input, int numberToSearch, int lowBound,
      int highBound) {
    var currentElementIndex = (highBound + lowBound) / 2;
    var currElement = input[currentElementIndex];

    if (lowBound >= highBound && currElement != numberToSearch) {
      return -1;
    }
    if (currElement > numberToSearch) {
      return searchRecursively(input, numberToSearch, lowBound, currentElementIndex - 1);
    } else if (currElement < numberToSearch) {
      return searchRecursively(input, numberToSearch, currentElementIndex + 1, highBound);
    } else {
      return currentElementIndex;
    }

  }

  public static int searchIteratively(int[] input, int numberToSearch) {

    for (int i = 0, j = input.length; i <= j; ) {

      var currentIndex = (j + i) / 2;

      var currElement = input[currentIndex];

      if (currElement == numberToSearch) {
        return currentIndex;
      } else if (currElement > numberToSearch) {
        j = currentIndex;
        j--;
      } else {
        i = currentIndex;
        i++;
      }
    }
    return -1;
  }

}
