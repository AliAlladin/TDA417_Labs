
import java.util.Comparator;

public class Term {
    private String word;
    private long weight;

    // Initializes a term with a given word and weight.
    public Term(String word, long weight) {
        this.word = word;
        this.weight = weight;
    }

    // Gets the word.
    public String getWord() {
        return word;
    }

    // Gets the weight.
    public long getWeight() {
        return weight;
    }

    // Extracts a prefix from the word.
    public String getPrefix(int len) {
        if (len > word.length()) {
            return  "";
        }
        return String.copyValueOf(word.toCharArray(), 0, len);

    }

    // Compares the two terms in case-insensitive lexicographic order.
    public static Comparator<Term> byLexicographicOrder() {
        /* TODO */
        return new Comparator<Term>() {
            @Override
            public int compare(Term term1, Term term2) {
                return term1.word.compareToIgnoreCase(term2.word);
            }
        };
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        /* TODO */
        return new Comparator<Term>() {
            @Override
            public int compare(Term term1, Term term2) {
                return Long.compare(term2.weight, term1.weight);
            }
        };
    }

    // Compares the two terms in case-insensitive lexicographic order,
    // but using only the first k characters of each word.
    public static Comparator<Term> byPrefixOrder(int k) {
        /* TODO */
        return new Comparator<Term>() {
            @Override
            public int compare(Term term1, Term term2) {
                return term1.getPrefix(k).compareToIgnoreCase(term2.getPrefix(k));
            }
        };
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by whitespace, followed by the word.
    public String toString() {
        return String.format("%12d    %s", this.getWeight(), this.getWord());
    }

}
