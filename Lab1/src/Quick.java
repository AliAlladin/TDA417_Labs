/**
 *  The {@code Quick} class provides static methods for sorting an
 *  array and selecting the ith smallest element in an array using quicksort.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/23quick">Section 2.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
import java.util.*;

public class Quick {
    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    public static void sort(int[] a) {

        //StdRandom.shuffle(a);
        // TODO: try randomising the array before sorting.


        sort(a, 0, a.length - 1);
        assert isSorted(a);
    }

    // quicksort the subarray from a[lo] to a[hi]
    public static void sort(int[] a, int lo, int hi) {
        // TODO: try switching to insertion sort if a[lo..hi] is small.
        if (hi - lo > 80){
            if (hi <= lo) return;

            int j = partition(a, lo, hi);
            sort(a, lo, j-1);
            sort(a, j+1, hi);
            assert isSorted(a, lo, hi);
        }
        else{
            Insertion.sort(a,lo,hi);
        }}

    // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
    // and return the index j.
    private static int partition(int[] a, int lo, int hi) {
        // TODO: find the median of the first, last and middle
        int median = median3(a,lo,(lo + hi)/2,hi);
        // elements of a[lo..hi], and swap that index with a[lo].
        exch(a,lo,median);

        int i = lo;
        int j = hi + 1;
        int v = a[lo];

        // a[lo] is unique largest element
        while (a[++i] < v) {
            if (i == hi) { exch(a, lo, hi); return hi; }
        }

        // a[lo] is unique smallest element
        while (v < a[--j]) {
            if (j == lo + 1) return lo;
        }

        // the main loop
        while (i < j) {
            exch(a, i, j);
            while (a[++i] < v) ;
            while (v < a[--j]) ;
        }

        // put partitioning item v at a[j]
        exch(a, lo, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }

    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // return the index of the median element among a[i], a[j], and a[k]
    private static final int median3(int[] a, int i, int j, int k) {
        if (a[i] <= a[j]) {
            if (a[j] <= a[k]) return j;      /* a[i] <= a[j] <= a[k] */
            else if (a[i] <= a[k]) return k; /* a[i] <= a[k] <  a[j] */
            else return i;                   /* a[k] <  a[i] <= a[j] */
        } else {
            if (a[k] <= a[j]) return j;      /* a[k] <= a[j] <  a[i] */
            else if (a[k] <= a[i]) return k; /* a[j] <  a[k] <= a[i] */
            else return i;                   /* a[j] <  a[i] <  a[k] */
        }
    }

    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(int[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (a[i] < a[i-1]) return false;
        return true;
    }

    // This class should not be instantiated.
    private Quick() { }
}

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/