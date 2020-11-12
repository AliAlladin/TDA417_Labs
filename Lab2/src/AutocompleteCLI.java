/******************************************************************************
 *  Compilation:  javac AutocompleteCLI.java
 *  Execution:    java  AutocompleteCLI dictionary.txt max-matches
 *  Dependencies: Autocomplete.java Term.java
 *    
 *  @author Peter Ljunglöf
 *    
 *  Interactive command-line tool used to demonstrate the Autocomplete data type.
 *
 *     * Reads a list of terms and weights from a file, specified as a
 *       command-line argument.
 *
 *     * When the user enters a string, it displays the top max-matches 
 *       terms that start with the text that the user typed.
 *
 ******************************************************************************/

import java.util.Scanner;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;


class AutocompleteCLI {
    public static void main(String[] args) {
        try {
            // Read the command-line arguments
            String dictfile = args[0];
            int max_matches = Integer.parseInt(args[1]);

            // Read the dictionary file using Java 8 streams, see e.g.:
            // https://www.mkyong.com/java8/java-8-how-to-convert-a-stream-to-array/
            Term[] dictionary = Files.lines(Paths.get(dictfile))
                .map(line -> line.trim().split("\t"))
                .map(pair -> new Term(pair[1], Long.valueOf(pair[0])))
                .toArray(size -> new Term[size]);

            // Print some help
            System.out.println("Dictionary size: " + dictionary.length);
            System.out.println("Write a prefix and press enter, quit by just pressing enter");
            System.out.println();

            // The main REPL (read-eval-print loop)
            Autocomplete autocomplete = new Autocomplete(dictionary);
            Scanner input = new Scanner(System.in);
            while (true) {
                // Print prompt and read input, exit if there is no input
                System.out.print("? ");
                if (!input.hasNextLine()) break;
                String prefix = input.nextLine();
                if (prefix.isEmpty()) break;

                // Find all results, print the number of matches and the topmost ones
                int nrMatches = autocomplete.numberOfMatches(prefix);
                System.out.println("Number of matches: " + nrMatches);
                Term[] results = autocomplete.allMatches(prefix);
                for (int i = 0; i < Math.min(max_matches, results.length); i++) {
                    System.out.println(results[i]);
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("You have to provide two arguments:");
            System.err.println("  (1) the path to a dictionary file");
            System.err.println("  (2) the maximum number of matches that will be displayed");
            System.exit(1);
        }
    }
}
