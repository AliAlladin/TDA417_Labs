// Tests a sorting algorithm.
// By default, tests Quick.java.
// If you want to test a different algorithm, see the comment inside
// the 'main' method below.
import java.util.*;
import java.util.function.*;

public class Test {
    public static void main(String[] args) {
        // If you want to test a different algorithm,
        // change this to Merge::sort or Insertion::sort.
        testAlgorithm(Quick::sort);
    }

    private static void testAlgorithm(Consumer<int[]> algorithm) {
        for (int size = 0; size <= 1000; size++) {
            System.out.printf("Testing on arrays of size %d...\n", size);
            int[] sortedSample = Bench.generateSample(size, 0);
            int[] partiallySortedSample = Bench.generateSample(size, 5);
            int[] randomSample = Bench.generateSample(size, 100);

            if (!check(sortedSample, algorithm)) return;
            if (!check(partiallySortedSample, algorithm)) return;
            if (!check(randomSample, algorithm)) return;
        }
        System.out.println("All tests passed!");
    }

    private static boolean check(int[] array, Consumer<int[]> algorithm) {
        int[] reference = Arrays.copyOf(array, array.length);
        int[] result;
        Arrays.sort(reference);

        try {
            result = Arrays.copyOf(array, array.length);
            algorithm.accept(result);
        } catch (Exception|StackOverflowError e) {
            failed(array, reference);
            System.out.println("Threw exception:");
            e.printStackTrace(System.out);
            return false;
        }

        if (!Arrays.equals(result, reference)) {
            failed(array, reference);
            System.out.println("Actual answer: " + show(result));
            return false;
        }

        return true;
    }

    private static void failed(int[] array, int[] reference) {
        System.out.println("Failed!");
        System.out.println("Input array: " + show(array));
        System.out.println("Expected answer: " + show(reference));
    }

    private static String show(int[] array) {
        StringBuilder result = new StringBuilder();
        result.append("{");
        if (array.length > 0) result.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            result.append(", ");
            result.append(array[i]);
        }
        result.append("}");

        return result.toString();
    }

}
