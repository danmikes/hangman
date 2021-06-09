import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Play hangman
 *
 * @author Daniel Mikes (daniel.mikes@hva.nl)
 */
public class Hangman {

    // create Scanner
    public static Scanner input = new Scanner(System.in);
    // Declare and initialise constants
    public static final int ALPHABET = 26; // letters in alphabet
    public static final int DEAD = 10; // number of attempts allowed = number of steps to draw hangman
    // declare arrays
    public static char[] oldLetter;
    public static char[] starArray;
    public static char[] wordArray;
    // declare and initialise array
    public static String[] wordList = {
            "aardvark", "ant", "antelope", "badger", "bat", "bear", "beaver", "bee", "bird", "buffalo", "bumblebee",
            "cat", "chameleon", "cheetah", "chicken", "cow", "crocodile", "crow",
            "dinosaur", "dog", "dolphin", "dragon", "elephant", "fish", "fly", "fox", "frog",
            "giraffe", "gull", "gorilla", "goat", "hedgehog", "horse", "hyena", "hippopotamus",
            "kangaroo", "leopard", "lion", "mammoth", "moose", "monkey", "mosquito", "ostrich", "otter",
            "panther", "pigeon", "rhinoceros",
            "seagull", "seal", "shark", "sheep", "skunk", "snake", "spider", "squirrel", "tiger",
            "wasp", "whale", "wolf", "zebra"};

    /**
     * Main method
     * Ask user to play and respond accordingly
     */
    public static void main(String[] args) {
        // explain game
        System.out.println();
        System.out.println("Let's play hangman. I select an animal. You guess a letter.");
        System.out.println("Each time letter occurs in word, I show it's position");
        System.out.println("If you guess all letters within " + DEAD + " times, you win. If not, you lose.");
        // ask to play (first time)
        if (!askPlay(1)) { // Quit
        } else {
            do {
                // play game until user wants to quit
                pickWord();
                playGame(); // Play
            } while (askPlay(2)); // ask to play (again)
        }
    }

    /**
     * Ask to play
     * Print question to play (again)
     * Print farewell (thank you for playing)
     *
     * @return true if user wishes to continue
     */
    static boolean askPlay(int x) {
        char answer = ' ';
        // ask to play until answer valid
        while (answer != 'n' && answer != 'y') {// check if answer 'y' or 'n'
            System.out.println();
            switch (x) { // Check if first or second time
                case 1: // first time
                    System.out.print("Wish to play (y/n)? ");
                    answer = input.next().charAt(0);
                    break;
                case 2: // second time
                    System.out.print("Wish to play again (y/n)? ");
                    answer = input.next().charAt(0);
                    break;
            }
        }
        if (answer == 'n') { // Quit
            switch (x) { // Check if first or second time
                case 1:
                    System.out.println("Fare well");
                    break; // First time
                case 2:
                    System.out.println("Fare well, thank you for playing.");
                    break; // Second time
            }
            return false; // Quit
        }
        return true; // Continue
    }

    /**
     * Randomly select word from list
     */
    static void pickWord() {
        // randomly select word from list and convert to array
        int r = (int) (Math.random() * wordList.length);
        String wordString = wordList[r];
        // convert word string to word array
        wordArray = new char[wordString.length()];
        for (int i = 0; i < wordArray.length; i++) {
            wordArray[i] = wordString.charAt(i);
        }
        // create star array
        starArray = new char[wordArray.length];
        for (int i = 0; i < starArray.length; i++) {
            starArray[i] = '*';
        }
    }

    /**
     * Play game
     */
    static void playGame() {
        // print star array
        System.out.println();
        System.out.print("word :      ");
        printArray(starArray);
        System.out.println();
        // declare array
        oldLetter = new char[ALPHABET];
        // ask letters until win or lose
        int hitCount = 0; // initiate hit count
        int missCount = 0; // initiate miss count
        // play while alive
        while (hasStar() && hasLife(missCount)) { // stars left & life left
            boolean miss = false;
            char letter = askLetter();
            if (!hadLetter(letter)) { // old letter
                if (hasLetter(letter)) { // letter in word
                    hitCount++;
                    System.out.printf("Hit %-2d  ", hitCount);
                    for (int i = 0; i < wordArray.length; i++) {
                        if (wordArray[i] == letter) {
                            starArray[i] = letter;
                        }
                    }
                } else { // letter not in word
                    missCount++;
                    System.out.printf("Miss %-2d ", missCount);
                    miss = true;
                }
                oldLetter[letter - 'a'] = letter;
            } else { // letter not in word
                if (hasLetter(letter)) {
                    System.out.print("Old hit ");
                } else {
                    System.out.print("Old miss");
                }
            }
            System.out.printf("%4c", ' ');
            printArray(starArray); // print stars (and hit letters)
            if (miss) { // miss
                System.out.println();
                drawPicture(missCount); // draw hangman
            }
            System.out.println();
        }
        System.out.println();
        if (!hasStar() && hasLife(missCount)) { // no stars & life -> win
            System.out.println("Congratulations, you won.");
            System.out.print("Word :      ");
        } else if (hasStar() && !hasLife(missCount)) { // stars & no life -> lose
            System.out.println("Condolences, you lost.");
            System.out.print("Word :      ");
        }
        // print word in capitals
        printArray(wordArray);
        System.out.println();
    }

    /**
     * Ask letter
     * If input letter upper case, change to lower case
     *
     * @return letter
     */
    static char askLetter() {
        // ask user for letter
        System.out.println();
        char letter;
        String inputString;
        do {
            System.out.print("< letter > ");
            // initiate variables
            inputString = input.next();
            letter = Character.toLowerCase(inputString.charAt(0)); // first letter of input string in lower case
        } while (!Character.isLetter(letter) || inputString.length() != 1); // check if input is one letter
        // check if letter given before and if in word
        return letter;
    }

    /**
     * Check if letter in word
     *
     * @param letter input letter
     * @return true if letter present in array
     */
    static boolean hasLetter(char letter) {
        // check if letter occurs in word array
        for (int i = 0; i < wordArray.length; i++) {
            if (wordArray[i] == letter) {
                return true; // letter in array
            }
        }
        return false; // letter not in array
    }

    /**
     * If hit add letter to oldLetter array
     *
     * @return oldLetter
     */
    static boolean hadLetter(char letter) {
        if (oldLetter[letter - 'a'] == letter) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if stars left
     *
     * @return true if star array has star(s)
     */
    static boolean hasStar() {
        // check if star in star array
        for (int i = 0; i < starArray.length; i++) {
            if (starArray[i] == '*') {
                return true; // > 0 stars
            }
        }
        return false; // 0 stars
    }

    /**
     * Check if user alive
     *
     * @param miss number of misses
     * @return true if less guesses than allowed
     */
    static boolean hasLife(int miss) {
        if (miss < DEAD) { // DEAD = number of misses allowed
            return true; // is alive
        }
        return false; // is dead
    }

    /**
     * Print array
     */
    static void printArray(char[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(Character.toUpperCase(array[i]) + " ");
        }
    }

    /**
     * Draw picture in (DEAD == 10) steps
     *
     * @param missCount
     */
    static void drawPicture(int missCount) {
        switch (missCount) {
            case 1:
                System.out.print("\n________");
                break;
            case 2:
                System.out.print("\n |\n |\n |\n |\n_|______");
                break;
            case 3:
                System.out.print(" _____\n |\n |\n |\n |\n_|______");
                break;
            case 4:
                System.out.print(" _____\n |   |\n |\n |\n |\n_|______");
                break;
            case 5:
                System.out.print(" _____\n |   |\n |   o\n |\n |\n_|______");
                break;
            case 6:
                System.out.print(" _____\n |   |\n |   o\n |   |\n |\n_|______");
                break;
            case 7:
                System.out.print(" _____\n |   |\n |   o\n |  /|\n |\n_|______");
                break;
            case 8:
                System.out.print(" _____\n |   |\n |   o\n |  /|\\\n |\n_|______");
                break;
            case 9:
                System.out.print(" _____\n |   |\n |   o\n |  /|\\\n |  /\n_|______");
                break;
            case 10:
                System.out.print(" _____\n |   |\n |   o\n |  /|\\\n |  / \\\n_|______");
                break;
        }
    }

}
