import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class RegexTest {
    @Test
    public void test() throws FileNotFoundException {
        GLLSolver gllSolver = new GLLSolver();
        GLRsolver glRsolver = new GLRsolver();
        String grammar = "data/grammars/regex";
        String graph = "data/graphs/regex";
        System.out.println("GLL");
        System.out.print("Grammar: " + grammar + " Graph: " + graph);
        int ans = gllSolver.solve(grammar, graph, null);
        assertEquals(1, ans);
        System.out.println(" Got: " + ans);
        System.out.println("GLR");
        System.out.print("Grammar: " + grammar + " Graph: " + graph);
        ans = glRsolver.solve(grammar, graph, null);
        System.out.println(" Got: " + ans);
        assertEquals(1, ans);
    }
}
