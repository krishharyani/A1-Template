package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class MazeTest {
    
    @Test
    void testGetRows () throws IOException {
        Maze.reset();
        Maze maze = Maze.getInstance("examples/rectangle.maz.txt");
        assertEquals(21, maze.getRows());
    }

    @Test
    void testGetColumns () throws IOException {
        Maze.reset();
        Maze maze = Maze.getInstance("examples/rectangle.maz.txt");
        assertEquals(51, maze.getColumns());
    }

}
