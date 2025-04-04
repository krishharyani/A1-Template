package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class navigateMazeTest {

    @Test
    void testEntryAndExit () throws IOException {
        NavigateMaze navigate = new NavigateMaze("examples/rectangle.maz.txt");
        Maze maze = new Maze("examples/rectangle.maz.txt");
        assertEquals(12, navigate.findEntry(maze.getMazegrid()));
        assertEquals(10, navigate.findExit(maze.getMazegrid()));
    }

    @Test
    void testPathValidate () throws IOException {
        NavigateMaze navigate = new NavigateMaze("examples/straight.maz.txt", "4F");
        assertTrue(navigate.PathValidate("4F"));
    }

    @Test
    void testPathValidateFalse () throws IOException {
        NavigateMaze navigate = new NavigateMaze("examples/straight.maz.txt", "5F");
        assertFalse(navigate.PathValidate("5F"));
    }

    @Test
    void testPathValidateInvalidChar () throws IOException {
        NavigateMaze navigate = new NavigateMaze("examples/straight.maz.txt", "4K");
        assertFalse(navigate.PathValidate("4K"));
    }

    @Test
    void testPathCompute () throws IOException {
        NavigateMaze navigate = new NavigateMaze("examples/direct.maz.txt");
        assertEquals("F R 2F L 3F R F L F R F L 2F ", navigate.PathCompute());
    }
    @Test
    void testPathComputeValidateAllignment () throws IOException {
        NavigateMaze navigate = new NavigateMaze("examples/direct.maz.txt");
        String path = navigate.PathCompute();
        assertTrue(navigate.PathValidate(path));
}

}
