package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i", true, "Path to file containing maze");
        options.addOption("p", true, "Input path to check for legitimacy");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                String inputfile = cmd.getOptionValue("i");
                if(cmd.hasOption("p")) {
                    String path = cmd.getOptionValue("p");
                    NavigateMaze navigate = new NavigateMaze(inputfile, path);
                    boolean isValid = navigate.PathValidate(path);
                    if (isValid) {
                        logger.info("Correct Path");
                    }
                    else {
                        logger.info("Incorrect Path");
                    }
                }
                else {
                    NavigateMaze navigate = new NavigateMaze(inputfile);
                    String path1 = navigate.PathCompute();
                    logger.info(path1);
                }
            }
            else {
                logger.error("Wrong format, please use '-i'");
            }
        } catch(Exception e) {
            logger.error("An error has occured");
        }
    }
}


class Maze {
    private static Maze instance;
    private char[][] mazegrid;
    private int rows = 0;
    private int columns = 0;

    private Maze(String inputfile) throws IOException {
        initializeMaze(inputfile);
    }

    public static Maze getInstance(String inputfile) throws IOException {
        if (instance == null) {
            instance = new Maze(inputfile);
        }
        return instance;
    }

    private void initializeMaze(String inputfile) throws IOException {
        FileReader filereader = new FileReader(inputfile);
        BufferedReader bufferedreader = new BufferedReader(filereader);
        String currentLine;
        while ((currentLine = bufferedreader.readLine()) != null) {
            if (rows == 0) {
                columns = currentLine.length();
            }
            rows++;
        }
        mazegrid = new char[rows][columns];
        bufferedreader.close();

        FileReader filereader2 = new FileReader(inputfile);
        BufferedReader bufferedreader2 = new BufferedReader(filereader2);
        int x = 0;
        while ((currentLine = bufferedreader2.readLine()) != null) {
            for (int y = 0; y < currentLine.length(); y++) {
                mazegrid[x][y] = currentLine.charAt(y);
            }
            x++;
        }
        bufferedreader2.close();
    }

    public static void reset() {
        instance = null;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public char[][] getMazegrid() {
        return mazegrid;
    }

    public void displayMazegrid() {
        for (int x = 0; x < getRows(); x++) {
            for (int y = 0; y < getColumns(); y++) {
                System.out.print(mazegrid[x][y]);
            }
            System.out.println();
        }
    }
}

interface PathFindingStrategy {
    String PathCompute(Maze maze);
}
class MazeEntryExit {
    public static int findEntry(char[][] mazegrid) {
        for (int x = 0; x < mazegrid.length; x++) {
            if (mazegrid[x][0] != '#') return x;
        }
        return -1;
    }

    public static int findExit(char[][] mazegrid) {
        for (int x = 0; x < mazegrid.length; x++) {
            if (mazegrid[x][mazegrid[0].length - 1] != '#') return x;
        }
        return -1;
    }
}

class RightHandStrategy implements PathFindingStrategy {
    public String PathCompute(Maze maze) {
        char [][] tempMazegrid = maze.getMazegrid();
        int row = MazeEntryExit.findEntry(maze.getMazegrid());
        int col = 0;
        int finalRow = MazeEntryExit.findExit(maze.getMazegrid());
        int finalCol = maze.getColumns() - 1;
        int direction = 1; // 0 = North | 1 = East | 2 = South | 3 = West
        StringBuilder path = new StringBuilder();
        int [][] moveset = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // moves forward based on orientation North, East, South, West respectively
        while ((col != finalCol)) {
            int rightSide = (direction + 1) % 4;
            int rightSideRow = row + moveset[rightSide][0];
            int rightSideCol = col + moveset[rightSide][1];
            if (tempMazegrid[rightSideRow][rightSideCol] != '#') {
                direction = rightSide;
                path.append("R");
                row = rightSideRow;
                col = rightSideCol;
                path.append("F");
            }
            else {
                int adjacentRow = row + moveset[direction][0];
                int adjacentCol = col + moveset[direction][1];

                if (tempMazegrid[adjacentRow][adjacentCol] != '#') {
                    row = adjacentRow;
                    col = adjacentCol;
                    path.append("F");
                }
                else {
                    direction = (direction + 3) % 4;
                    path.append("L");
                }
            }
        }
        formChanger changeForm = new formChanger();
        String factoredFrom = changeForm.canonicalToFactored(path.toString());
        return factoredFrom;
    }
}
class NavigateMaze {
    private Maze maze;
    String path;
    private PathFindingStrategy strategy;

    public NavigateMaze(String inputfile) throws IOException {
        this.maze = Maze.getInstance(inputfile);
        this.strategy = new RightHandStrategy();
    }

    public NavigateMaze(String inputfile, String path) throws IOException {
        this.maze = Maze.getInstance(inputfile);
        this.path = path;
    }

    public void setStrategy(PathFindingStrategy strategy) {
        this.strategy = strategy;
    }

    public int findEntry(char[][] mazegrid) {
        for(int x = 0; x < maze.getRows(); x++) {
            if(mazegrid[x][0] != '#') {
                return x;
            }
        }
        return -1;
    }

    public int findExit(char[][] mazegrid) {
        for(int x = 0; x < maze.getRows(); x++) {
            if(mazegrid[x][maze.getColumns() - 1] != '#') {
                return x;
            }
        }
        return -1;
    }


    public boolean PathValidate(String path) {
        formChanger changeForm =  new formChanger();
        String canonicalPath = changeForm.factoredToCanonical(path);
        int row = findEntry(maze.getMazegrid());
        int col = 0;
        int finalRow = findExit(maze.getMazegrid());
        int finalCol = maze.getColumns() - 1;
        int direction = 1; // 0 = North | 1 = East | 2 = South | 3 = West
        char [][] tempMazegrid = maze.getMazegrid();
        for (char step : canonicalPath.toCharArray()) {
            if (step == 'L') {
                direction = (direction + 3) % 4;
            }
            else if (step == 'R') {
                direction = (direction + 1) % 4;
            }
            else if (step == 'F') {
                if (direction == 0) {
                    row--;
                }
                else if (direction == 1) {
                    col++;
                }
                else if (direction == 2) {
                    row++;
                }
                else if (direction == 3) {
                    col--;
                }

                if (row > finalRow || col > finalCol || row < 0 || col < 0 || tempMazegrid[row][col] == '#') {
                    return false;
                }
            }
            else {
                return false;
            }

        }
        return col == finalCol;
    }

    public String PathCompute() {
        if (strategy == null) {
            strategy = new RightHandStrategy();
        }
        return strategy.PathCompute(maze);
    }

}

class formChanger {
    private static final Logger logger = LogManager.getLogger();
    public String canonicalToFactored(String canonicalPath) {
        StringBuilder factoredPath = new StringBuilder();
        String canonical = canonicalPath;
        canonical = canonical + " ";
        int count = 1;
        for(int i = 1; i < canonicalPath.length() + 1; i++) {
            if (canonical.charAt(i) == canonical.charAt(i-1)) {
                count++;
            }
            else {
                if (count == 1) {
                    factoredPath.append(canonical.charAt(i-1)).append(" ");
                }
                else {
                    factoredPath.append(count).append(canonical.charAt(i-1)).append(" ");
                    count = 1;
                }
            }
        }
        return factoredPath.toString();
    }
    public String factoredToCanonical(String factoredPath) {
        StringBuilder canonicalPath = new StringBuilder();
        int count = 1;
        for (int i = 0; i < factoredPath.length(); i++) {
            if (factoredPath.charAt(i) == ' ') {
                continue;
            }
            if (Character.isDigit(factoredPath.charAt(i))) {
                count = Character.getNumericValue(factoredPath.charAt(i));
            }
            else {
                for (int j = 0; j < count; j++) {
                    canonicalPath.append(factoredPath.charAt(i));
                }
                count = 1;
            }
        }
        return canonicalPath.toString();
    }
}
