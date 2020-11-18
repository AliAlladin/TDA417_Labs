
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class RangeBinarySearch {

    // Returns the index of the *first* element in terms[] that equals the search key,
    // according to the given comparator, or -1 if there are no matching elements.
    // Complexity: O(log N), where N is the length of the array
    public static int firstIndexOf(Term[] terms, Term key, Comparator<Term> comparator) {
        /* TODO */
        int lo = 0;
        int hi = terms.length - 1;
        while (lo <= hi) {

            int mid = (lo + hi) / 2;
            int compared = comparator.compare(key, terms[mid]);

            if (compared < 0) {
                hi = mid - 1;
            } else if (compared > 0) {
                lo = mid + 1;
            } else {
                if (mid == lo){
                    return mid;
                }
                hi = mid;
            }
        }

        return -1;
    }

    // Returns the index of the *last* element in terms[] that equals the search key,
    // according to the given comparator, or -1 if there are no matching elements.
    // Complexity: O(log N)
    public static int lastIndexOf(Term[] terms, Term key, Comparator<Term> comparator) {
        /* TODO */

        int lo = 0;
        int hi = terms.length - 1;
        while (lo <= hi) {

            int mid = (int) Math.round(((double) hi+lo)/2);
            int compared = comparator.compare(key, terms[mid]);

            if (compared < 0) {
                hi = mid - 1;
            } else if (compared > 0) {
                lo = mid + 1;
            } else {
                if (mid == hi){
                    return mid;
                }
                lo = mid;
            }
        }

        return -1;
    }

}
