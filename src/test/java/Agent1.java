import java.util.Random;
import java.util.Scanner;

public class Agent1 {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String color = in.next(); // your piece color ("r", "g" or "b")
        System.err.println(color);

        // game loop
        while (true) {
            for (int i = 0; i < 3; i++) {
                String player = in.next(); // player color
                int status = in.nextInt(); // 0: player has left the game, 1: player is alive
                String lastMove = in.next(); // last move played ("null" if no move has been played)
                System.err.printf("%s %d %s%n", player, status, lastMove);
            }
            for (int i = 0; i < 8; i++) {
                String line = in.next(); // horizontal row
                System.err.println(line);
            }
            int moveCount = in.nextInt(); // number of legal moves
            System.err.println(moveCount);
            for (int i = 0; i < moveCount; i++) {
                String move = in.next(); // a legal move
                System.err.println(move);
            }

            // Write an answer using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("random"); // e.g. e3 (move piece to e3) or random
        }
    }
}
