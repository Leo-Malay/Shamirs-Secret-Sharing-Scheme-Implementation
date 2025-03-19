import java.math.BigInteger;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        try {
            // Defining test values
            BigInteger n = new BigInteger("3");
            BigInteger k = new BigInteger("2");
            BigInteger prime = new BigInteger("11");
            BigInteger secret = new BigInteger("10");

            // Generating instance
            SSS s = new SSS(n, k);
            s.setPrime(prime);
            s.setSecret(secret);

            // Printing important variables
            System.out.println("N = " + s.n);
            System.out.println("K = " + s.k);
            System.out.println("Prime = " + s.prime);
            System.out.println("Secret = " + s.a.get(0));
            for (int i = 0; i < Integer.valueOf(k.toString()); i++) {
                System.out.println("Random-" + (i + 1) + ": " + s.a.get(i));
            }

            // Generating all the shares
            System.out.println("Shares:");
            for (int i = 1; i <= Integer.valueOf(n.toString()); i++) {
                System.out.print("(" + i + ", " + s.computeShare(new BigInteger(Integer.toString(i))) + ") ");
            }

            // Matching all possible pairs of shares.
            HashMap<Integer, BigInteger> s1 = new HashMap<>();
            s1.put(1, BigInteger.valueOf(4));
            s1.put(2, BigInteger.valueOf(9));
            HashMap<Integer, BigInteger> s2 = new HashMap<>();
            s2.put(2, BigInteger.valueOf(9));
            s2.put(3, BigInteger.valueOf(3));
            HashMap<Integer, BigInteger> s3 = new HashMap<>();
            s3.put(1, BigInteger.valueOf(4));
            s3.put(3, BigInteger.valueOf(3));

            // Reconstructed share using uniques pairs
            System.out.println("Reconstructed Shares using different pairs:");
            System.out.print("(" + s.reconstructSecret(s1) + ", ");
            System.out.print(s.reconstructSecret(s2) + ", ");
            System.out.print(s.reconstructSecret(s3) + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
