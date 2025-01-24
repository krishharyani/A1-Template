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
        System.out.println("** Starting Maze Runner");
        Options options = new Options();
        options.addOption("i", true, "Path to file containing maze");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                String inputfile = cmd.getOptionValue("i");
                logger.info("**** Reading the maze from file ", inputfile);
    
                BufferedReader reader = new BufferedReader(new FileReader(inputfile));
                String line;
                while ((line = reader.readLine()) != null) {
                    for (int idx = 0; idx < line.length(); idx++) {
                        if (line.charAt(idx) == '#') {
                            logger.info("WALL ");
                        } else if (line.charAt(idx) == ' ') {
                            logger.info("PASS ");
                        }
                    }
                    logger.info(System.lineSeparator());
                }
            
            }
            else {
                logger.error("Wrong format, please use '-i'");
            }
        } catch(Exception e) {
            logger.error("An error has occured");
        }
        logger.info("**** Computing path");
        logger.info("PATH NOT COMPUTED");
        logger.info("** End of MazeRunner");
    }
}


class Maze {
    private char[][] mazegrid;
    private int rows = 0;
    private int columns = 0;

    public Maze(String inputfile) throws IOException {
        initializeMaze(inputfile);
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

        FileReader filereader2 = new FileReader(inputfile);
        BufferedReader bufferedreader2 = new BufferedReader(filereader2);
        int x = 0;
        while ((currentLine = bufferedreader2.readLine()) != null) {
            for (int y = 0; y < currentLine.length(); y++) {
                mazegrid[x][y] = currentLine.charAt(y);
            }
            x++;
        }
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

