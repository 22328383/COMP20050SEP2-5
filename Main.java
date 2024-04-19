import java.util.Scanner;
import java.util.Random;

public class Main {
    static int[] atomNums = new int[3]; // Keeps track of where atoms are.
    static int rayCount; // Keeps track of the number of rays a user has added.



    public static void mainMenu() { // Allows the user to start the game or to exit the program.
        System.out.println("Welcome To Black Box!\nPlease type START to begin or QUIT to end the program.");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toUpperCase();


        switch (input) {
            case "START": // Goes to generateAtom method, starting the game.
                Board userBoard = Board.newBoard();
                generateAtoms(userBoard);
                GraphicBoard gb= new GraphicBoard(userBoard);
                rayCount = 0;

                rayControl(userBoard, gb);
                break;
            case "QUIT": // Exits program.
                System.out.println("Ending Program...");
                System.exit(0);
                break;
            default: // Error checking.
                System.out.println("\nInvalid input detected.\n");
                mainMenu();
                break;
        }
        scanner.close();
    }

    public static void generateAtoms(Board userBoard) { // Generates three random numbers between 0-60 and adds atoms there. Doesn't allow duplicates.
        int[] currentAtoms = {-1,-1,-1};
        boolean flag = false; // Flag set to true when a duplicate is detected.
        Random random = new Random();
        for (int i=0;i<3;i++) {
            int number = random.nextInt(60);
            for (int j=0;j<3;j++) {
                if (number == currentAtoms[j]) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                userBoard.setAtom(number);
                atomNums[i] = number;
                currentAtoms[i] = number;
            }
            else {
                i--;
                flag = false;
            }
        }
    }

    public static void rayControl(Board userBoard, GraphicBoard gb) { // User can add rays, guess atom positions, or stop the program.
        System.out.println("\nWould you like to ADD a ray, GUESS atom positions (will end game) or STOP the program?");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toUpperCase();

        switch (input) {
            case "ADD": // First runs user input through validity checks, then adds the ray to the board.
                int cellChoice = cellValidityCheck(scanner);
                int sideChoice = sideValidityCheck(scanner, cellChoice);
                userBoard.addRay(cellChoice,sideChoice);
                System.out.println("\nHere is your updated board with the ray you entered...");
                gb.setIsFirstTime();
                gb.setRayCellSide(sideChoice);
                gb.repaint();
                rayCount++;
                rayControl(userBoard, gb);
                break;
            case "STOP": // Exits program.
                System.out.println("\nEnding program...");
                System.exit(0);
                break;
            case "GUESS": // Takes user to the guessAtoms method.
                guessAtoms(scanner, gb);
                break;
            default:
                System.out.println("\nInvalid input detected.\n");
                rayControl(userBoard, gb);
                break;
        }
        scanner.close();
    }

    public static int cellValidityCheck(Scanner scanner) { // Checks user input against the array to see if the cell is valid (an edge cell).
        int[] validCells = {0,1,2,3,4,5,10,11,17,18,25,26,34,35,42,43,49,50,55,56,57,58,59,60};
        while (1==1) {
            System.out.println("\nWhich cell would you like to add a ray from?");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                boolean valid = false;

                for (int i=0;i<validCells.length;i++) {
                    if (input == validCells[i]) {
                        valid = true;
                        break;
                    }
                }
                if (valid) {
                    return input;
                }
                else {
                    System.out.println("\nInvalid cell choice. Please input a cell that is on the edge.");
                }

            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input detected. Please enter an integer.");
            }
        }
    }

    public static int sideValidityCheck(Scanner scanner, int cellChoice) { // Does the same as cellValidityCheck but for the proper side.
        int currentArray[] = arrayPicker(cellChoice); // Based on the cell the user picks, picks the array to compare user's side input to.
        while (1==1) {
            System.out.println("\nWhich side of the cell would you like the ray to come from?");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                boolean valid = false;

                for(int i=0;i<currentArray.length;i++) {
                    if (input == currentArray[i]) {
                        valid = true;
                        break;
                    }
                }
                if (valid) {
                    return input;
                }
                else {
                    System.out.println("\nInvalid side choice. Please input a valid side for your selected cell.");
                }

            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input detected. Please enter an integer.");
            }
        }
    }

    public static int[] arrayPicker (int cellChoice) { // Picks an array to compare side validity against based on the user's cell choice.
        int[] arrayA = {0,4,5};
        int[] arrayB = {0,5};
        int[] arrayC = {0,1,5};
        int[] arrayD = {4,5};
        int[] arrayE = {0,1};
        int[] arrayF = {3,4,5};
        int[] arrayG = {0,1,2};
        int[] arrayH = {3,4};
        int[] arrayI = {1,2};
        int[] arrayJ = {2,3,4};
        int[] arrayK = {2,3};
        int[] arrayL = {1,2,3};

        if (cellChoice == 0) {
            return arrayA;
        }
        if (cellChoice == 1 || cellChoice == 2 || cellChoice == 3) {
            return arrayB;
        }
        if (cellChoice == 4) {
            return arrayC;
        }
        if (cellChoice == 5 || cellChoice == 11 || cellChoice == 18) {
            return arrayD;
        }
        if (cellChoice == 10 || cellChoice == 17 || cellChoice == 25) {
            return arrayE;
        }
        if (cellChoice == 26) {
            return arrayF;
        }
        if (cellChoice == 34) {
            return arrayG;
        }
        if (cellChoice == 35 || cellChoice == 43 || cellChoice == 50) {
            return arrayH;
        }
        if (cellChoice == 42 || cellChoice == 49 || cellChoice == 55) {
            return arrayI;
        }
        if (cellChoice == 56) {
            return arrayJ;
        }
        if (cellChoice == 57 || cellChoice == 58 || cellChoice == 59) {
            return arrayK;
        }
        if (cellChoice == 60) {
            return arrayL;
        }
        return null;
    }

    public static void guessAtoms(Scanner scanner, GraphicBoard gb) { // User guesses three cells where they think the atom is. Doesn't allow duplicate guesses, or invalid ones.
        int i = 0;
        int guessInput;
        int wrongGuesses = 0;
        int[] guesses = {-1,-1,-1};
        boolean flag = false;
        boolean flag2 = false;
        while (i<3) {
            System.out.println("\nEnter the cell number where you think an atom is located:");
            try {
                while (1==1) {
                    guessInput = Integer.parseInt(scanner.nextLine());
                    for (int j=0;j<guesses.length;j++) {
                        if (guessInput == guesses[j]) {
                            flag2 = true;
                            break;
                        }
                    }
                    if (flag2) {
                        System.out.println("\nEnter a cell number you haven't entered before.");
                        flag2 = false;
                    }
                    else if (guessInput < 0 || guessInput > 60) {
                        System.out.println("\nPlease input a valid cell number.");
                    }
                    else {
                        guesses[i] = guessInput;
                        break;
                    }
                }

                for (int j=0;j<atomNums.length;j++) {
                    if (guessInput == atomNums[j]) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    wrongGuesses++;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid cell choice. Please input an integer.");
                i--;
            }
            i++;
            flag = false;
        }
        displayScore(wrongGuesses, gb);
    }

    public static void displayScore (int wrongGuesses, GraphicBoard gb) { // Shows the score for the board.
        int score = wrongGuesses * 5; // A wrong guess is worth 5 points.
        score += rayCount; // Each ray is worth one.
        System.out.println("\nYour final score for this board is " + score);
        gb.afterGamePaintAtom();
        mainMenu(); // Goes back to main menu should the user wish to play again.
    }

    public static void main(String[] args) {
        mainMenu();
    }
}