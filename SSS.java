import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SSS {
    BigInteger n; // Number of Shares
    BigInteger k; // Threshold Shares
    BigInteger prime; // Big Prime used for Modulus
    ArrayList<BigInteger> a; // Polynomial co-efficients
    final Integer bitLength = 512; // Default bit-length of big prime number

    /**
     * Construct class and generate a random big prime number.
     * 
     * @param n BigInteger - Number of Shares
     * @param k BigInteger - Threshold Number
     */
    public SSS(BigInteger n, BigInteger k) {
        this.n = n;
        this.k = k;
        this.prime = this.genPrime(this.bitLength);
        this.a = new ArrayList<>();
    }

    /**
     * Generate a big prime number
     * 
     * @param bitLength Integer - Bit length of generated prime number
     * @return BigInteger - Prime number.
     */
    public BigInteger genPrime(Integer bitLength) {
        SecureRandom s_rand = new SecureRandom();
        return BigInteger.probablePrime(bitLength, s_rand);
    }

    /**
     * Set Prime Number
     * 
     * @param prime BigInteger - Prime number
     */
    public void setPrime(BigInteger prime) {
        this.prime = prime;
    }

    /**
     * Ser Secret
     * 
     * @param secret BigInteger - Secret to be divided into different share
     */
    public void setSecret(BigInteger secret) {
        this.a.add(secret);
        SecureRandom s_rand = new SecureRandom();
        for (int i = 1; i < Integer.valueOf(k.toString()); i++) {
            this.a.add(new BigInteger(this.bitLength, s_rand).mod(this.prime));
        }
    }

    /**
     * Compute share based on the given values
     * 
     * @param x BigInteger - Proddice xth share
     * @return BigInteger - xth share
     * @throws Exception
     */
    public BigInteger computeShare(BigInteger x) throws Exception {
        BigInteger result = this.a.get(0);
        BigInteger temp = x;
        for (int i = 1; i < Integer.valueOf(k.toString()); i++) {
            result = result.add(this.a.get(i).multiply(temp));
            temp = temp.multiply(x);
        }
        return result.mod(this.prime);
    }

    /**
     * Compute and return all shares at onces.
     * 
     * @return HashMap - map of all shares (1 <= x <= k)
     * @throws Exception
     */
    public HashMap<Integer, BigInteger> computeAllShare() throws Exception {
        HashMap<Integer, BigInteger> result = new HashMap<>();
        for (int x = 1; x < Integer.valueOf(n.toString()); x++) {
            BigInteger holdVal = this.a.get(0);
            BigInteger temp = BigInteger.valueOf(x);
            for (int i = 1; i < Integer.valueOf(k.toString()); i++) {
                holdVal = holdVal.add(this.a.get(i).multiply(temp));
                temp = temp.multiply(BigInteger.valueOf(x));
            }
            result.put(x, holdVal.mod(this.prime));
        }
        return result;
    }

    /**
     * Computes the secret using the provided share if enough
     * 
     * @param shares Map of (shareId, distributedSecret)
     * @return BigInteger - Reconstructed secret
     * @throws Exception
     */
    public BigInteger reconstructSecret(HashMap<Integer, BigInteger> shares) throws Exception {
        if (shares.size() < Integer.valueOf(this.k.toString())) {
            throw new Exception("Number of shares must be equal or greater to threshold");
        }
        BigInteger result = BigInteger.ZERO;
        BigInteger xi, xj, yi;
        for (Map.Entry<Integer, BigInteger> e : shares.entrySet()) {
            xi = BigInteger.valueOf(e.getKey());
            yi = e.getValue();
            for (Map.Entry<Integer, BigInteger> e2 : shares.entrySet()) {
                xj = BigInteger.valueOf(e2.getKey());
                if (!xi.equals(xj)) {
                    yi = yi.multiply(xj).multiply(xj.subtract(xi).modInverse(prime)).mod(prime);
                }
            }
            result = result.add(yi).mod(prime);
        }
        return result;
    }
}