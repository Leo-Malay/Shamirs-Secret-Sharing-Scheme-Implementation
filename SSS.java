import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class SSS {
    BigInteger n;
    BigInteger k;
    BigInteger prime;
    ArrayList<BigInteger> a;
    final Integer bitLength = 512;

    public SSS(BigInteger n, BigInteger k) {
        this.n = n;
        this.k = k;
        this.prime = this.genPrime(this.bitLength);
        this.a = new ArrayList<>();
    }

    public BigInteger genPrime(Integer bitLength) {
        SecureRandom s_rand = new SecureRandom();
        return BigInteger.probablePrime(bitLength, s_rand);
    }

    public void setPrime(BigInteger prime) {
        this.prime = prime;
    }

    public void setSecret(BigInteger secret) {
        this.a.add(secret);
        SecureRandom s_rand = new SecureRandom();
        for (int i = 1; i < Integer.valueOf(k.toString()); i++) {
            this.a.add(new BigInteger(this.bitLength, s_rand).mod(this.prime));
        }
    }

    public BigInteger computeShare(BigInteger x) {
        BigInteger result = this.a.get(0);
        BigInteger temp = x;
        for (int i = 1; i < Integer.valueOf(k.toString()); i++) {
            result = result.add(this.a.get(i).multiply(temp));
            temp = temp.multiply(x);
        }
        return result.mod(this.prime);
    }

    public static void main(String[] args) {
        BigInteger n = new BigInteger("3");
        BigInteger k = new BigInteger("2");
        BigInteger prime = new BigInteger("11");
        BigInteger secret = new BigInteger("10");

        SSS s = new SSS(n, k);
        s.setPrime(prime);
        s.setSecret(secret);

        System.out.println("N = " + s.n);
        System.out.println("K = " + s.k);
        System.out.println("Prime = " + s.prime);
        System.out.println("Secret = " + s.a.get(0));
        for (int i = 0; i < Integer.valueOf(k.toString()); i++) {
            System.out.println("Random-" + (i + 1) + ": " + s.a.get(i));
        }

        for (int i = 1; i <= Integer.valueOf(n.toString()); i++) {
            System.out.println("Share-" + i + ": " + s.computeShare(new BigInteger(Integer.toString(i))));
        }
    }
}