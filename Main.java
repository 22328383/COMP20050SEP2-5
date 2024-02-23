import java.util.Scanner;

public class Main {

    public static void mainMenu() {
        System.out.println("Welcome To Black Box!\nPlease type START to begin or QUIT to end the program.");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        switch (input) {
            case "START":
                new GraphicBoard(new Board());
                break;
            case "QUIT":
                System.out.println("Ending Program...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input detected.");
                mainMenu();
                break;
        }
        scanner.close();
    }

    public static void main(String[] args) {
        mainMenu();
    }
}