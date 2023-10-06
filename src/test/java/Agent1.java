import java.util.Random;
import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String color = in.next();
        System.err.println(color);
        while (true) {

            for (int y = 0; y < 8; ++y) {
                String line = in.next();
                System.err.println(line);
            }

            String last_action = in.next();
            System.err.println(last_action);

            int actions = in.nextInt();
            System.err.println(actions);
            for (int y = 0; y < actions; ++y) {
                String line = in.next();
                System.err.println(line);
            }
            System.out.println("random");
        }
    }
}
