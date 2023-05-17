package com.illia.algorithm;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class InsertionSortAlgorithmTest {

  @Test
  public void insertionSortShouldReturnSortedArray() {
    var input = new int[]{54, 11, 16, 4, 44, 59, 12, 34};
    var sorted = new int[]{4, 11, 12, 16, 34, 44, 54, 59};

    InsertionSortAlgorithm.insertionSort(input);
    assertArrayEquals(sorted, input);
  }
}
