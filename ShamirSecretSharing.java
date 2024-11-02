import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShamirSecretSharing {
    
    public static void main(String[] args) {
        // Example JSON input
        Map<String, Map<String, String>> input = Map.of(
            "keys", Map.of("n", "4", "k", "3"),
            "1", Map.of("base", "10", "value", "4"),
            "2", Map.of("base", "2", "value", "111"),
            "3", Map.of("base", "10", "value", "12"),
            "6", Map.of("base", "4", "value", "213")
        );

        BigInteger secret = findConstantTerm(input);
        System.out.println("The secret constant term is: " + secret);
    }

    public static BigInteger findConstantTerm(Map<String, Map<String, String>> input) {
        int n = Integer.parseInt(input.get("keys").get("n"));
        int k = Integer.parseInt(input.get("keys").get("k"));

        List<Point> points = new ArrayList<>();

        // Parse and decode points
        for (String key : input.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key);
                int base = Integer.parseInt(input.get(key).get("base"));
                String value = input.get(key).get("value");
                
                BigInteger y = new BigInteger(value, base);  // Decode y value from given base
                points.add(new Point(x, y));
            }
        }

        // Calculate the constant term (c) using Lagrange interpolation
        BigInteger constantTerm = BigInteger.ZERO;
        
        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(points.get(i).x);
            BigInteger yi = points.get(i).y;
            BigInteger term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(points.get(j).x);
                    term = term.multiply(xj.negate()).divide(xi.subtract(xj));
                }
            }
            
            constantTerm = constantTerm.add(term);
        }
        
        return constantTerm;
    }

    // Helper class to store (x, y) points
    static class Point {
        int x;
        BigInteger y;

        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
}
