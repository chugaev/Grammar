import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
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
        if (mode == 1) {
            System.out.println(tcSolver.solve(grammar, graph, out));
        } else if (mode == 2) {
            System.out.println(glRsolver.solve(grammar, graph, out));
        } else if (mode == 3) {

        }
    }
}


