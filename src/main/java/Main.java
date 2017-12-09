import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Solver solver = new Solver();
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
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
        solver.solve(grammar, graph, out);
    }
}


