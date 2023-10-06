import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String color = in.next(); // current color of your pieces ("w" or "b")

        // game loop
        while (true) {
            for (int i = 0; i < 8; i++) {
                String line = in.next(); // horizontal row
            }
            String lastMove = in.next(); // last move made by the opponent ("null" if it's the first turn)
            int moveCount = in.nextInt(); // number of legal moves
            for (int i = 0; i < moveCount; i++) {
                String move = in.next(); // a legal move
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("random"); // e.g. e2e3 (move piece at e2 to e3) or random
        }
    }
}