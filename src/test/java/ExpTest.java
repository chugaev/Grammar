import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class ExpTest {
    @Test
    public void test() throws FileNotFoundException {
        GLLSolver gllSolver = new GLLSolver();
        GLRsolver glRsolver = new GLRsolver();
        String grammar = "data/grammars/grammarE";
        String graph = "data/graphs/graphE";
        System.out.println("GLL");
        System.out.print("Grammar: " + grammar + " Graph: " + graph);
        int ans = gllSolver.solve(grammar, graph, null);
        assertEquals(6, ans);
        System.out.println(" Got: " + ans);
        System.out.println("GLR");
        System.out.print("Grammar: " + grammar + " Graph: " + graph);
        ans = glRsolver.solve(grammar, graph, null);
        System.out.println(" Got: " + ans);
        assertEquals(6, ans);
    }
}
