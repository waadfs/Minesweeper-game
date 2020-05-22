/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepergame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class MinesweeperServer {
    
        public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(8901);
        System.out.println("Minesweeper Server is Running");
        try {
            while (true) {
                Game game = new Game();
                 game.Players[0] = game.new Player(listener.accept(), "Player One");
                 game.Players[1] = game.new Player(listener.accept(), "Player Two");
                game.Players[0].start();
                game.Players[1].start();
            }
        } finally {
            listener.close();
        }
    }
}

class Game {
public Player[] Players = new Player[2];

    /**
     * Returns whether the current state of the board is such that one
     * of the players is a winner.
     */

    /**
     * The class for the helper threads in this multithreaded server
     * application.  A Player is identified by a character mark
     * which is either 'X' or 'O'.  For communication with the
     * client the player has a socket with its input and output
     * streams.  Since only text is being communicated we use a
     * reader and a writer.
     */
    class Player extends Thread {
        String playerName;
        Player opponent;
        Socket socket;
        BufferedReader input;
        public PrintWriter output;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, String playerName) {
            this.socket = socket;
            this.playerName = playerName;
            try {
                input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println(playerName);
                System.out.println("WELCOME " + playerName);
                output.println("Waiting for second player to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }


        /**
         * The run method of this thread.
         */
        @Override
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println("MESSAGE All players connected");

                // Tell the first player that it is her turn.
                if ("Player One".equals(playerName)) {
                    output.println("Waiting the other player to connect");
                }

                // Repeatedly get commands from the client and process them.
                while (true) {
                    String response = input.readLine();
                    System.out.println("Received Message from player: " + response);

                    if (response.contains("lost") || response.contains("won")) {
                            //Inform player two
                           Players[1].output.println(response);
                           //Inform player one
                           Players[0].output.println(response); 
                        
                    } else  {
                        output.println("You can play now");
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }
}
