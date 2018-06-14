import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

public class RNG implements Serializable {
    RNG() {
        pitybreak = 1;
        low = 0;
    }

    private int pitybreak;
    private int low;

    public int roll(int input) {
        Random test = new SecureRandom();
        int result;
        int limit = input;
        int i;
        if (low >= pitybreak) {
            result = (int) (test.nextDouble() * limit / 2 + limit / 2) + 1;
            pitybreak = test.nextInt(3) + 1;
        } else {
            result = test.nextInt(limit) + 1;
        }
        if (result < (input + 1) / 2) {
            low++;
        } else {
            low = 0;
            pitybreak = test.nextInt(3) + 1;
        }
        return result;
    }
}
