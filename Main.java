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
                new GraphicBoard(userBoard);
                generateAtoms(userBoard);
                rayControl(userBoard);
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

    public static void rayControl(Board userBoard) {
        System.out.println("\nWould you like to ADD a ray or STOP the program?");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toUpperCase();

        switch (input) {
            case "ADD":
                System.out.println("\nWhich cell would you like to add a ray from?");
                int cellChoice = Integer.parseInt(scanner.nextLine());
                System.out.println("\nWhich side of the cell would you like the ray to come from?");
                int sideChoice = Integer.parseInt(scanner.nextLine());
                userBoard.addRay(cellChoice,sideChoice);
                System.out.println("\nHere is your updated board with the ray you entered...");
                new GraphicBoard(userBoard);
                rayControl(userBoard);
                break;
            case "STOP":
                System.out.println("\nEnding program...");
                System.exit(0);
                break;
            default:
                System.out.println("\nInvalid input detected.\n");
                rayControl(userBoard);
                break;
        }
        scanner.close();
    }

    public static void main(String[] args) {
        mainMenu();
    }
}