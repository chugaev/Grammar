import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class TestTC {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    @Test
    public void solveTest() throws FileNotFoundException {
        TCSolver solver = new TCSolver();
        Scanner sc = new Scanner(new File("data" + File.separator +"input_for_test"));
        String[] fileNames = new String[11];
        String[] fileNameGrammars = new String[2];
        int[] q1 = new int[11];
        int[] q2 = new int[11];
        sc.next();
        fileNameGrammars[0] = "data" + File.separator + "grammars" + File.separator + sc.next() + "CNF";
        fileNameGrammars[1] = "data" + File.separator + "grammars" + File.separator + sc.next() + "CNF";
        for (int i = 0; i < 11; i++) {
            fileNames[i] = Paths.get("data" + File.separator + "graphs" + File.separator + sc.next()).toString();
            q1[i] = sc.nextInt();
            q2[i] = sc.nextInt();
        }
        System.out.println("TC testing...");
        for (String grammarName : fileNameGrammars) {
            System.out.println(grammarName);
            for (int i = 0; i < fileNames.length; i++) {
                int num = solver.solve(grammarName, fileNames[i], null);
                int answer = 0;
                if (grammarName.contains("grammar1")) {
                    answer = q1[i];
                } else if (grammarName.contains("grammar2")) {
                    answer = q2[i];
                }
                System.out.print(fileNames[i] + " answer: " + answer + " got: " + num);
                if (num == answer) {
                    System.out.println(ANSI_GREEN + " [OK]" + ANSI_RESET);
                } else {
                    System.out.println(ANSI_RED + " [FAIL]" + ANSI_RESET);
                }
                assertEquals(num, answer);
            }
        }
    }
}
