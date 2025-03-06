import java.util.Random;
import java.util.Scanner;

/**
  The BattleShip class manages the gameplay of the Battleship game between two players.
  It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
     * The main method that runs the game loop.
     * It initializes the grids for both players, places ships randomly, and manages turns.
     * The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    /**
     * Initializes the grid by filling it with water ('~').
     *
     * @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                grid[row][column] = '~'; // Empty water
            }
        }
    }

    /**
     * Places ships randomly on the given grid.
     * This method is called for both players to place their ships on their respective grids.
     *
     * @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        Random randomPlacement = new Random();
        int[] shipSize = {5, 4, 3, 2};
        for (int size : shipSize) {
            boolean isPlaced = false;
            while (!isPlaced) {
                int row = randomPlacement.nextInt(GRID_SIZE);
                int col = randomPlacement.nextInt(GRID_SIZE);
                boolean horizontal = randomPlacement.nextBoolean();

                if (canPlaceShip(grid, row, col, size, horizontal)) {

                    if (horizontal) {
                        for (int i = 0; i < size; i++) {
                            grid[row][col + i] = 'S';
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            grid[row + i][col] = 'S';
                        }
                    }

                    isPlaced = true;

                }

            }
        }
    }
    /**
     * Checks if a ship can be placed at the specified location on the grid.
     * This includes checking the size of the ship, its direction (horizontal or vertical),
     * and if there's enough space to place it.
     *
     * @param grid       The grid where the ship is to be placed.
     * @param row        The starting row for the ship.
     * @param col        The starting column for the ship.
     * @param size       The size of the ship.
     * @param horizontal The direction of the ship (horizontal or vertical).
     * @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) {
                return false;
            }
        } else {
            if (row + size > GRID_SIZE) {
                return false;
            }
        }

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            if (grid[r][c] != '~') {
                return false;
            }
        }

        return true;
    }


    /**
     * Manages a player's turn, allowing them to attack the opponent's grid
     * and updates their tracking grid with hits or misses.
     *
     * @param opponentGrid The opponent's grid to attack.
     * @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        String input;
        int row, col;
        boolean validInput = false;


        while (!validInput) {
            System.out.print("Enter target (for example A5): ");
            input = scanner.nextLine();
            if (isValidInput(input)) {
                col = input.charAt(0) - 'A';
                row = Integer.parseInt(input.substring(1)) - 1;

                if (trackingGrid[row][col] == 'X' || trackingGrid[row][col] == '0') {
                    System.out.println("You already shot here,try again.");
                } else {
                    validInput = true;

                    if (opponentGrid[row][col] == 'S') {
                        System.out.println("HIT!");
                        opponentGrid[row][col] = 'X';
                        trackingGrid[row][col] = 'X';
                    } else {
                        System.out.println("MISS!");
                        trackingGrid[row][col] = '0';
                    }

                }
            } else {
                System.out.println("Invalid input,try again.");
            }
        }
    }



    /**
     * Checks if the game is over by verifying if all ships are sunk.
     *
     * @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        return allShipsSunk(player1Grid) || allShipsSunk(player2Grid);
    }


    /**
     * Checks if all ships have been destroyed on a given grid.
     *
     * @param grid The grid to check for destroyed ships.
     * @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        for (int row= 0; row< GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == 'S') {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Validates if the user input is in the correct format (e.g., A5).
     *
     * @param input The input string to validate.
     * @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        if (input.length() < 2 || input.length() > 3)
            return false;

        int col = input.charAt(0);
        if (col < 65 || col > 74)
            return false;

        String rowDigit = input.substring(1);

        for (int i = 0; i < rowDigit.length(); i++) {
            if (!Character.isDigit(rowDigit.charAt(i))) {
                return false;
            }
        }
        int row = Integer.parseInt(rowDigit);
        return row >= 1 && row <= 10;
    }



    /**
     * Prints the current state of the player's tracking grid.
     * This method displays the grid, showing hits, misses, and untried locations.
     *
     * @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        System.out.print("    ");
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.printf("%2d  ", i + 1);
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}


