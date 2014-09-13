// Michael A. Valadez
// A01 Hangman "Flayed Man"
// CS 2450
// V.1.0

package flayman;

import java.util.Scanner;
import java.util.Random;
import java.io.*;

public class Flayman {

    private Scanner in = new Scanner(System.in);
    private boardPiece[] board = {new boardPiece(0), new boardPiece(0), new boardPiece(3),
        new boardPiece(1), new boardPiece(2), new boardPiece(0)};
    private String puzzle;
    private String puzzleWord;
    private int puzzleIndex;
    private Random generator = new Random(System.currentTimeMillis());
    private boolean win;
    private boolean over;
    private String correct = "";
    private char guesses[] = new char[26];
    private int guessed;
    private int misses;
    private int lives;
    private String puzzles[] = {"stark", "lannister", "greyjoy", "tyrell", "martell", "targaryen"};

    public static void main(String[] args) {

        String letter;
        String again = "y";

        Flayman game = new Flayman();

        try {
            BufferedReader in
                    = new BufferedReader(new FileReader("C:\\Users\\michael\\My Documents\\puzzles.txt"));

            int count = 0;
            while (in.ready()) { //while there is another line in the input file
                game.puzzles[count] = in.readLine(); //get line of data
                count++; //Increment CWID counter
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Error during reading/writing");
        }

        System.out.println("Welcome to Flayed Man!");
        System.out.println("From Which House Does Our Prisoner Come M'Lord?");

        while (again.charAt(0) == 'y') {
            game.init();

            while (!game.over) {
                game.printBoard();
                game.printGuesses();
                System.out.println("Puzzle: " + game.puzzle);
                System.out.println("Guess a letter: ");
                letter = game.in.next();

                game.guesses[game.guessed] = letter.charAt(0);
                game.guessed++;
                game.sort();

                if (game.puzzleWord.indexOf(letter.charAt(0)) != -1) {
                    game.correct = game.correct + letter.charAt(0);
                    game.puzzle = game.puzzleWord.replaceAll("[^" + game.correct + " ]", "-");
                    if (game.puzzleWord.replaceAll("[" + game.correct + " ]", "").length() == 0) {
                        game.win = true;
                        game.over = true;
                    }
                } else {
                    game.miss();
                }
            }
            game.printBoard();
            System.out.println("The Flayed Man Is From House: " + game.puzzleWord);
            if (game.win) {
                System.out.println("Cheers! For House Bolton!");
            } else {
                System.out.println("A Naked Man Holds Few Secrets, But A Flayed Man Holds None!");
            }

            System.out.println();
            System.out.println("Would you like to play again? (y/n)");
            again = game.in.next();
        }
        System.out.println("Goodbye!");
    }

    void init() {

        win = false;
        over = false;

        board[0].piece = "   _____  ";
        board[1].piece = "  |     | ";
        board[2].piece = "  |     | ";
        board[3].piece = "  |     | ";
        board[4].piece = "  |     | ";
        board[5].piece = "  |_____| ";

        puzzleIndex = generator.nextInt(puzzles.length);
        puzzleWord = puzzles[puzzleIndex];
        puzzle = puzzleWord.replaceAll("[A-Za-z]", "-");
        correct = "";
        for (int x = 0; x < 26; x++) {
            guesses[x] = '~';
        }
        guessed = 0;
        misses = 0;
        lives = 6;

    }

    void printBoard() {
        for (int x = 0; x < 6; x++) {
            System.out.println(board[x].piece);
        }
    }

    void miss() {
        misses++;
        lives--;
        if (misses == 1) {
            board[2].piece = "  |  0  | ";
            System.out.println("Lives remaining: " + lives);
        } else if (misses == 2) {
            board[2].piece = "  | \\0  | ";
            System.out.println("Lives remaining: " + lives);
        } else if (misses == 3) {
            board[2].piece = "  | \\0/ | ";
            System.out.println("Lives remaining: " + lives);
        } else if (misses == 4) {
            board[3].piece = "  |  |  | ";
            System.out.println("Lives remaining: " + lives);
        } else if (misses == 5) {
            board[4].piece = "  | /   | ";
            System.out.println("Lives remaining: " + lives);
        } else if (misses == 6) {
            board[4].piece = "  | / \\ | ";
            System.out.println("Lives remaining: " + lives);
            over = true;
        }
    }

    void printGuesses() {

        System.out.print("Your Guesses: ");
        for (int x = 0; x < 26; x++) {
            if (guesses[x] != '~') {
                System.out.print(guesses[x] + " ");
            }
        }
        System.out.println();
    }

    void sort() {
        boolean doMore = true;
        while (doMore) {
            doMore = false;  // assume this is last pass over array
            for (int i = 0; i < guesses.length - 1; i++) {
                if (guesses[i] > guesses[i + 1]) {
                    char temp = guesses[i];
                    guesses[i] = guesses[i + 1];
                    guesses[i + 1] = temp;
                    doMore = true;  // after an exchange, must look again 
                }
            }
        }
    }

    class boardPiece {

        public String piece;
        public int total;
        public int used;

        boardPiece(int x) {
            used = 0;
            total = x;
        }
    }
}
