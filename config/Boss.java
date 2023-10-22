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
        String color = in.next(); // your piece color ("r", "g" or "b")

        // game loop
        while (true) {
            for (int i = 0; i < 3; i++) {
                String player = in.next(); // player color
                int status = in.nextInt(); // 0: player has left the game, 1: player is alive
                String lastMove = in.next(); // last move played ("null" if no move has been played)
            }
            for (int i = 0; i < 8; i++) {
                String line = in.next(); // horizontal row
            }
            int moveCount = in.nextInt(); // number of legal moves
            for (int i = 0; i < moveCount; i++) {
                String move = in.next(); // a legal move
            }

            // Write an answer using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("random"); // e.g. e3 (move piece to e3) or random
        }
    }
}