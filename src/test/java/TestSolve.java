import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class TestSolve {
    @Test
    public void solveTest() throws FileNotFoundException {
        Solver solver = new Solver();
        Scanner sc = new Scanner(new File("data" + File.separator +"input_for_test"));
        String[] fileNames = new String[11];
        String[] fileNameGrammars = new String[2];
        int[] q1 = new int[11];
        int[] q2 = new int[11];
        sc.next();
        fileNameGrammars[0] = "data" + File.separator + "grammars" + File.separator + sc.next();
        fileNameGrammars[1] = "data" + File.separator + "grammars" + File.separator + sc.next();
        for (int i = 0; i < 11; i++) {
            fileNames[i] = Paths.get("data" + File.separator + "graphs" + File.separator + sc.next()).toString();
            q1[i] = sc.nextInt();
            q2[i] = sc.nextInt();
        }
        for (String grammarName : fileNameGrammars) {
            System.out.println(grammarName);
            for (int i = 0; i < fileNames.length; i++) {
                int num = solver.solve(grammarName, fileNames[i], null);
                System.out.println();
                int answer = 0;
                if (grammarName.contains("grammar1")) {
                    answer = q1[i];
                } else if (grammarName.contains("grammar2")) {
                    answer = q2[i];
                }
                assertEquals(num, answer);
            }
        }
    }
}
