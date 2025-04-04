package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class formChangerTest {
    
    @Test
    void testCanonicaltoFactored() {
        formChanger change = new formChanger();
        assertEquals("4F 3L 3R ", change.canonicalToFactored("FFFFLLLRRR"));
    }

    @Test
    void testFactoredtoCanonical() {
        formChanger change = new formChanger();
        assertEquals("FFFFLLLRRR", change.factoredToCanonical("4F 3L 3R"));
    }
}
