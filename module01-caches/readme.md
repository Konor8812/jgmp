## Test Results
Test case test.java.com.illia.algorithm.BinarySearchAlgorithmTest#recursiveAndIterativeBinarySearchImplementationsPerformanceTest
Binary search implementations comparison conclusions:

- for considerably small input size (<100_000) there is no significant performance difference
- for larger input (100_000_000) elements iterative implementation is ~20% more efficient in terms
  of time usage
- as recursive implementation implies more stack usage, while iterative avoids it and proves to be
  more efficient in terms of memory usage   

In general, iterative implementation is expected to be faster