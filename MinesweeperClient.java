/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepergame;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Java Minesweeper Game
 *
 * Author: Jan Bodnar Website: http://zetcode.com
 */
public class MinesweeperClient extends JFrame {
  private JFrame frame = new JFrame("Minesweeper");
    private JLabel statusbar;
    private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Board gameBoard;

    public MinesweeperClient(String serverAddress) throws IOException {
        // Setup networking
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        initUI();

    }
  private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(frame,
            "Want to play again?",
            "Minesweeper is Fun Fun Fun",
            JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }
    private void initUI() {

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);
        gameBoard = new Board(out, statusbar);
        add(gameBoard);
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void play() throws Exception {
        String response;
        try {
            response = in.readLine();
            System.out.println("player received message :" + response);
            statusbar.setText(response);
            if (response.startsWith("P")) {

                gameBoard.PlayerName = response;
            }
            boolean x  = true;
            while (x) {
                response = in.readLine();
                statusbar.setText(response);
                if (response.contains("won") || response.contains("lost")) {
                    JOptionPane.showMessageDialog(this, response);  
                    x  = false;
                }
            }
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) {
       
 
        //EventQueue.invokeLater(() -> {
        try {
            while (true) {
            String serverAddress = (args.length == 0) ? "localhost" : args[1];
            MinesweeperClient client = new MinesweeperClient(serverAddress);
            
            client.setVisible(true);
            client.play();
            if (!client.wantsToPlayAgain()) {
                break;
            }
            }}
         catch (IOException ex1) {
            Logger.getLogger(MinesweeperClient.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (Exception ex) {
            Logger.getLogger(MinesweeperClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //});
            
    }
    }

