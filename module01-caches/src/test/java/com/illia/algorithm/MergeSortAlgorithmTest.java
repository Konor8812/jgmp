package com.illia.algorithm;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class MergeSortAlgorithmTest {

  @Test
  public void mergeSortAlgorithmTestShouldSortInputArray() {
    var input = new int[]{54, 11, 16, 4, 44, 59, 12, 34};
    var sorted = new int[]{4, 11, 12, 16, 34, 44, 54, 59};

    MergeSortAlgorithm.mergeSort(input);
    assertArrayEquals(input, sorted);
  }
}
