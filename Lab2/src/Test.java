public class Test {
    public static void main(String[] args) {
        Term[] arr = new Term[16];
         arr[0] = new Term("a", 0);
         arr[1] = new Term("b", 0);
         arr[2] = new Term("c", 0);
         arr[3] = new Term("d", 0);
         arr[4] = new Term("d", 0);
         arr[5] = new Term("d", 0);
         arr[6] = new Term("d", 0);
         arr[7] = new Term("d", 0);
         arr[8] = new Term("d", 0);
         arr[9] = new Term("d", 0);
        arr[10] = new Term("e", 0);
        arr[11] = new Term("e", 0);
        arr[12] = new Term("e", 0);
        arr[13] = new Term("e", 0);
        arr[14] = new Term("e", 0);
        arr[15] = new Term("f", 0);

        Term[] arr2 = new Term[256];
        for (int i = 0; i < 256; i++) {
            if (i < 10){
                arr2[i] = new Term("A", 0);
            }
            else {
                arr2[i] = new Term("B", 0);
            }
        }
        System.out.println("Index: " + RangeBinarySearch.lastIndexOf(arr2, new Term("b", 0), Term.byLexicographicOrder()));
    }
}
