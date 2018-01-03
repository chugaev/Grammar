import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        GLLSolver gllSolver = new GLLSolver();
        TCSolver tcSolver = new TCSolver();
        GLRsolver glRsolver = new GLRsolver();
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int mode = sc.nextInt();
        System.out.println(s);
        String grammar = null;
        String graph = null;
        String out = "";
        if (s.split(" ").length == 3) {
            grammar = s.split(" ")[0].trim();
            graph = s.split(" ")[1].trim();
            out = s.split(" ")[2].trim();
        } else if (s.split(" ").length == 2) {
            grammar = s.split(" ")[0].trim();
            graph = s.split(" ")[1].trim();
        }
        int k = 0;
        if (mode == 1) {
            k = tcSolver.solve(grammar, graph, out);
        } else if (mode == 2) {
            k = gllSolver.solve(grammar, graph, out);
        } else if (mode == 3) {
            k = glRsolver.solve(grammar, graph, out);
        }
        System.out.println(k);
    }
}


