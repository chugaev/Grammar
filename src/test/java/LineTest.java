import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class LineTest {
    @Test
    public void test() throws FileNotFoundException {
        GLLSolver gllSolver = new GLLSolver();
        GLRsolver glRsolver = new GLRsolver();
        String grammar = "data/grammars/ambiguous";
        String graph = "data/graphs/line";
        System.out.println("GLL");
        System.out.print("Grammar: " + grammar + " Graph: " + graph);
        int ans = gllSolver.solve(grammar, graph, null);
        assertEquals(15, ans);
        System.out.println(" Got: " + ans);
        System.out.println("GLR");
        System.out.print("Grammar: " + grammar + " Graph: " + graph);
        ans = glRsolver.solve(grammar, graph, null);
        System.out.println(" Got: " + ans);
        assertEquals(15, ans);
    }
}

