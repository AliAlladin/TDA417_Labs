import java.util.stream.*;

// A class representing an n-gram.
// You can complete the lab without looking at the code.
class Ngram implements Comparable<Ngram> {
    // An object of class Ngram represents the subsequence of the 'tokens'
    // array starting at index 'offset' and having length 'length':
    //
    //   tokens[offset], tokens[offset+1], ..., tokens[offset+1-length].
    // 
    // This design allows us to reduce memory use by sharing the tokens
    // array between all n-grams coming from the same input string.
    private String[] tokens;
    private int offset, length;
    
    public Ngram(String[] tokens, int offset, int length) {
        this.tokens = tokens;
        this.offset = offset;
        this.length = length;
    }

    // Return all n-grams of a given input string.
    public static Ngram[] ngrams(String input, int n) {
        String[] tokens =
            Stream.of(input.split("\\W")).
            filter(str -> !str.equals("")).
            map(str -> str.toLowerCase()).
            toArray(String[]::new);

        int count = tokens.length-n+1;
        if (count < 0) count = 0;
        Ngram[] ngrams = new Ngram[count];
        for (int i = 0; i < count; i++) {
            ngrams[i] = new Ngram(tokens, i, n);
        }
        return ngrams;
    }
        
    // Accessing the individual tokens of the n-gram.
    public int length() { return length; }
    public String at(int i) { return tokens[i+offset]; }
        
    public int compareTo(Ngram other) {
        if (this.length != other.length) return this.length - other.length;

        for (int i = 0; i < this.length; i++) {
            int wordDiff = this.at(i).compareTo(other.at(i));
            if (wordDiff != 0) return wordDiff;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        return compareTo((Ngram)obj) == 0;
    }

    public int hashCode() {
        int hash = 1;
        for (int i = 0; i < length(); i++)
            hash = hash*37 + at(i).hashCode();
        return hash;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        if (length() != 0) {
            result.append(at(0));
            for (int i = 1; i < length; i++) {
                result.append('/');
                result.append(at(i));
            }
        }
        return result.toString();
    }
}
