package com.illia.algorithm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BinarySearchAlgorithmTest {

  static int[] input = new int[]{5, 11, 16, 94, 144, 253, 255, 349};

  @Test
  public void testRecursiveBinarySearchImplementationShouldReturnElementsIndex() {
    for (int i = 0; i < input.length; i++) {
      var recursiveResult = BinarySearchAlgorithm.searchRecursively(input, input[i], 0,
          input.length - 1);
      assertEquals(i, recursiveResult);
    }

    assertEquals(-1,
        BinarySearchAlgorithm.searchRecursively(input,
            1,
            0,
            input.length - 1));


  }

  @Test
  public void testIterativeBinarySearchImplementationShouldReturnElementsIndex() {
    for (int i = 0; i < input.length; i++) {
      var iterativeResult = BinarySearchAlgorithm.searchIteratively(input, input[i]);
      assertEquals(i, iterativeResult);
    }
    assertEquals(-1,
        BinarySearchAlgorithm.searchIteratively(input, 1));

  }
}
