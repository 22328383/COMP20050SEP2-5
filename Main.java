import java.util.Scanner;
import java.util.Random;

public class Main {

    public static void mainMenu() {
        System.out.println("Welcome To Black Box!\nPlease type START to begin or QUIT to end the program.");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toUpperCase();


        switch (input) {
            case "START":
                Board userBoard = Board.newBoard();
                generateAtoms(userBoard);
                GraphicBoard gb= new GraphicBoard(userBoard);

                rayControl(userBoard, gb);
                break;
            case "QUIT":
                System.out.println("Ending Program...");
                System.exit(0);
                break;
            default:
                System.out.println("\nInvalid input detected.\n");
                mainMenu();
                break;
        }
        scanner.close();
    }

    public static void generateAtoms(Board userBoard) {
        Random random = new Random();
        for (int i=0;i<3;i++) {
            int number = random.nextInt(60) + 1;
            userBoard.setAtom(number);
        }
    }

    public static void rayControl(Board userBoard, GraphicBoard gb) {
        System.out.println("\nWould you like to ADD a ray or STOP the program?");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toUpperCase();

        switch (input) {
            case "ADD":
                int cellChoice = cellValidityCheck(scanner);
                int sideChoice = sideValidityCheck(scanner, cellChoice);
                userBoard.addRay(cellChoice,sideChoice);
                System.out.println("\nHere is your updated board with the ray you entered...");
                gb.setIsFirstTime();
                gb.repaint();
                rayControl(userBoard, gb);
                break;
            case "STOP":
                System.out.println("\nEnding program...");
                System.exit(0);
                break;
            default:
                System.out.println("\nInvalid input detected.\n");
                rayControl(userBoard, gb);
                break;
        }
        scanner.close();
    }

    public static int cellValidityCheck(Scanner scanner) {
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

    public static int sideValidityCheck(Scanner scanner, int cellChoice) {
        int currentArray[] = arrayPicker(cellChoice);
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

    public static int[] arrayPicker (int cellChoice) {
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

    public static void main(String[] args) {
        mainMenu();
    }
}