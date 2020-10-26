import java.io.*;
import java.util.*;


public class Main {
    static public class Pair {
        public int first, second;

        public Pair(int a, int b){
            first = a;
            second = b;
        }
    }

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    public static void main(String[] args) {
        startProgram();
    }

    static void startProgram(){
        boolean mazeLoaded = false;
        String[] str = {"=== Menu ===", "1. Generate a new maze", "2. Load a maze", "0. Exit"};
        List<String> strings = Arrays.asList(str);
        File file;
        int[][] arr = new int[0][0];
        boolean exit = false;
        while(!exit) {
            for (var s : strings) {
                System.out.println(s);
            }
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    arr = mazeGenerator();
                    printMaze(arr);
                    mazeLoaded = true;
                    break;
                case 2:
                    System.out.println("Name path of the file: ");
                    String filePath = scanner.next();
                    file = new File(filePath);
                    if (!file.exists()) {
                        System.out.printf("The file %s does not exist", filePath);
                        break;
                    }
                    if (!file.canRead()) {
                        System.out.println("Cannot load the maze. It has an invalid format");
                        break;
                    }
                    try {
                        Scanner fileScanner = new Scanner(file);
                        Vector<String> vec = new Vector<>();
                        while (fileScanner.hasNextLine()) {
                            vec.add(fileScanner.nextLine());
                        }
                        arr = new int[vec.size()][vec.size()];
                        for (int i = 0; i < vec.size(); i++) {
                            String[] strArr = vec.elementAt(i).split(" ");
                            for (int j = 0; j < vec.size(); j++) {
                                arr[i][j] = Integer.parseInt(strArr[j]);
                            }
                        }

                    } catch (FileNotFoundException e) {
                        System.out.printf("The file %s does not exist", filePath);
                        break;
                    }

                    mazeLoaded = true;
                    break;
                case 3:
                    if (mazeLoaded) {
                        System.out.println("Name destination file: ");
                        scanner = new Scanner(System.in);
                        String fileName = scanner.next();
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter(fileName);
                            PrintWriter printWriter = new PrintWriter(fileWriter);
                            for (int[] ints : arr) {
                                for (int j = 0; j < arr[0].length; j++) {
                                    printWriter.print(ints[j] + " ");
                                }
                                printWriter.println();
                            }
                            printWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                } else {
                        System.out.println("Incorrect option. Please try again");
                        break;
                    }
                    break;
                case 4:
                    if (mazeLoaded){
                        printMaze(arr);
                } else {
                        System.out.println("Incorrect option. Please try again");
                    }
                    break;
                case 5:
                    int[][] wasHere = new int[arr.length][arr[0].length];
                    int[][] correctPath = new int[arr.length][arr[0].length]; // The solution to the maze
                    int startX , startY;
                    int endX, endY;
                    Vector<Pair> vec = new Vector<>();
                    int entrance = 0;
                    int finalCell = 0;
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i][0] == 0) {
                            entrance = i;
                        }
                        if (arr[i][arr[0].length - 1] == 0) {
                            finalCell = i;
                        }
                        if (entrance != 0 && finalCell != 0) {
                            break;
                        }
                    }
                    startX = entrance;
                    endX = finalCell;
                    startY= 0;
                    endY = arr[0].length - 1;
                    solveMaze(arr, wasHere, correctPath, startX, startY, endX, endY);
                    correctPath[finalCell][correctPath.length - 1] = 1;

                    for (int i = 0 ; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            if (arr[i][j] == 0 && correctPath[i][j] == 1 ) {
                                System.out.print("//");
                            } else if (arr[i][j] == 1) {
                                System.out.print("\u2588\u2588");
                            } else {
                                System.out.print("  ");
                            }
                        }
                        System.out.println();
                    }
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Incorrect option. Please try again");
                    break;

            }
            if (mazeLoaded){
                String[] tmp = {"=== Menu ===", "1. Generate a new maze", "2. Load a maze", "3. Save the maze", "4. Display the maze", "5. Find the escape", "0. Exit"};
                strings = Arrays.asList(tmp);
            }

        }

    }

    static int[][] mazeGenerator() {
        System.out.println("Enter the size of a new maze");
        int m = scanner.nextInt();
        int n = m;
        int[][] arr = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++){
                arr[i][j] = 1;
            }
        }
        Vector<Pair> walls = new Vector<>();
        int entrance = random.nextInt(m - 2) + 1;
        arr[entrance][0] = 0;
        addWalls(walls, new Pair(entrance, 0), arr);
        while (walls.size() > 0) {
            int rand = random.nextInt(walls.size());
            Pair p = walls.get(rand);
            if (p.first - 1 < 0 || p.first + 1 > arr.length - 1 || p.second <= 0 || p.second >= arr[0].length - 1) {

            } else if (arr[p.first + 1][p.second] == 0 && arr[p.first - 1][p.second] == 1) {
                arr[p.first][p.second] = 0;
                addWalls(walls, new Pair(p.first - 1, p.second), arr);
            } else if (arr[p.first + 1][p.second] == 1 && arr[p.first - 1][p.second] == 0) {
                arr[p.first][p.second] = 0;
                addWalls(walls, new Pair(p.first + 1, p.second), arr);
            }
            if (p.second - 1 < 0 || p.second + 1 > arr[0].length - 1 || p.first <= 0 || p.first >= arr.length - 1) {

            } else if (arr[p.first][p.second + 1] == 0 && arr[p.first][p.second - 1] == 1) {
                arr[p.first][p.second] = 0;
                addWalls(walls, new Pair(p.first, p.second - 1), arr);
            } else if (arr[p.first][p.second + 1] == 1 && arr[p.first][p.second - 1] == 0) {
                arr[p.first][p.second] = 0;
                addWalls(walls, new Pair(p.first, p.second + 1), arr);
            }
            walls.remove(rand);
        }
        while (true) {
            int x = random.nextInt(m - 2) + 1;
            if (arr[x][n - 2] == 0 && x != entrance ) {
                arr[x][n - 1] = 0;
                break;
            }
        }
        return arr;
    }

    static void printMaze(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++){
                if (arr[i][j] == 1){
                    System.out.print("\u2588\u2588");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    static void addWalls(Vector v, Pair p, int[][] tab) {
        for (int i = -1; i < 2; i += 2) {
                if (p.first + i <= 0 || p.first + i >= tab.length -1 || p.second <= 0 ||  p.second >= tab[0].length - 1){

                } else if (tab[p.first + i][p.second ] == 1) {
                    v.add(new Pair(p.first + i, p.second ));
                }
                if ( p.second + i <= 0 ||  p.second + i >= tab[0].length - 1 ){

                }
                else if (tab[p.first ][p.second + i] == 1 ){
                v.add(new Pair(p.first, p.second + i));
                }

        }
        if (p.first > 0 && p.first < tab.length - 1 && p.second > 0 && p.second < tab[0].length -1 )
        tab[p.first][p.second] = 0;
    }

    static void solveMaze(int[][] maze, int[][] wasHere, int[][] correctPath, int startX, int startY, int endX, int endY) {
        for (int row = 0; row < maze.length; row++)
            // Sets boolean Arrays to default values
            for (int col = 0; col < maze[row].length; col++){
                wasHere[row][col] = 0;
                correctPath[row][col] = 0;
            }
        boolean b = recursiveSolve(startX, startY, endX, endY, wasHere, correctPath, maze);
        // Will leave you with a boolean array (correctPath)
        // with the path indicated by true values.
        // If b is false, there is no solution to the maze
    }
    static boolean recursiveSolve(int x, int y, int endX, int endY, int[][] wasHere, int[][] correctPath, int[][] maze) {
        if (x == endX && y == endY) return true; // If you reached the end
        if (maze[x][y] ==  1|| wasHere[x][y] == 1) return false;
        // If you are on a wall or already were here
        wasHere[x][y] = 1;
        if (x != 0) // Checks if not on left edge
            if (recursiveSolve(x-1, y, endX, endY, wasHere, correctPath, maze)) { // Recalls method one to the left
                correctPath[x][y] = 1; // Sets that path value to true;
                return true;
            }
        if (x != maze.length - 1) // Checks if not on right edge
            if (recursiveSolve(x+1, y, endX, endY, wasHere, correctPath, maze)) { // Recalls method one to the right
                correctPath[x][y] = 1;
                return true;
            }
        if (y != 0)  // Checks if not on top edge
            if (recursiveSolve(x, y-1, endX, endY, wasHere, correctPath, maze)) { // Recalls method one up
                correctPath[x][y] = 1;
                return true;
            }
        if (y != maze.length - 1) // Checks if not on bottom edge
            if (recursiveSolve(x, y+1, endX, endY, wasHere, correctPath, maze)) { // Recalls method one down
                correctPath[x][y] = 1;
                return true;
            }
        return false;
    }
}
