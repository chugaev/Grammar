import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class GLRsolver {
    boolean added;
    int solve(String fileNameGrammar, String fileNameGraph, String fileout) throws FileNotFoundException {
        Grammar grammar = parseGrammar(fileNameGrammar);
        Set<String>[][] matrix = parseGraph(fileNameGraph);
        int maxLengthRule = grammar.getMaxLenghtRule();
        int size = matrix.length;
        while (true) {
            added = false;
            for (int i = 0; i < size; i++) {
                rec(matrix, grammar, i, "", maxLengthRule, i);
            }
            if (!added) {
                break;
            }
        }
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != null && matrix[i][j].contains("S")) {
                    count++;
                }
            }
        }
        PrintWriter pw = null;
        if (fileout!= null && fileout.equals("")) {
            pw = new PrintWriter(System.out, true);
        } else if (fileout != null) {
            pw = new PrintWriter(new File(fileout));
        }
        if (fileout != null) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (matrix[i][j] != null) {
                        for (String str : matrix[i][j]) {
                            if (grammar.isNonterm(str)) {
                                pw.print(i + "," + str + "," + j + "\n");
                            }
                        }
                    }
                }
            }
        }
        if (pw != null) {
            pw.close();
        }
        return count;
    }

    void rec(Set<String>[][] matrix, Grammar grammar, int from, String currPath, int len, int start) {
        if (grammar.hasRule(currPath)) {
            Set<String> nonterms = grammar.getNonterm(currPath);
            for (String nonterm : nonterms) {
                if (matrix[start][from] == null) {
                    matrix[start][from] = new TreeSet<>();
                }
                if (!matrix[start][from].contains(nonterm)) {
                    matrix[start][from].add(nonterm);
                    added = true;
                }
            }
        }
        if (len == 0) {
            return;
        }
        boolean flag = false;
        for (int to = 0; to < matrix.length; to++) {
            if (matrix[from][to] != null) {
                Set<String> set = new TreeSet<>();
                set.addAll(matrix[from][to]);
                for (String string : set) {
                    String newPath = currPath + string;
                    flag = true;
                    rec(matrix, grammar, to, newPath, len - 1, start);
                }
            }
        }
        if (!flag) {
            return;
        }
        return;
    }

    Set<String>[][] parseGraph(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        sc.nextLine();
        sc.nextLine();
        int size = Collections.max(Arrays.stream(sc.nextLine().split("; ")).map(str -> Integer.parseInt(str)).collect(Collectors.toList())) + 1;
        Set<String>[][] matrix = new TreeSet[size][size];
        while (true) {
            String s = sc.nextLine();
            if (s.contains("}")) {
                break;
            }
            String left = s.split("->")[0].trim();
            String right = s.split("->")[1].trim();
            right = right.substring(0, right.indexOf('['));
            String label = s.substring(s.indexOf("\"") + 1);
            label = label.substring(0, label.indexOf("\""));
            if (matrix[Integer.parseInt(left)][Integer.parseInt(right)] == null) {
                matrix[Integer.parseInt(left)][Integer.parseInt(right)] = new TreeSet<>();
            }
            matrix[Integer.parseInt(left)][Integer.parseInt(right)].add(label);
        }
        sc.close();
        return matrix;
    }

    Grammar parseGrammar(String filename) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        Scanner sc = new Scanner(new File(filename));
        while (sc.hasNext()) {
            String string = sc.nextLine();
            String left = string.split("->")[0].trim().replace(" ", "");
            String right = string.split("->")[1].trim().replace(" ", "");
            Rule rule = new Rule(left, right);
            grammar.addRule(rule);
        }
        return grammar;
    }
}

class Grammar {
    ArrayList<Rule> rules;

    public Grammar() {
        rules = new ArrayList<>();
    }

    public Grammar(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    void addRule(Rule rule) {
        rules.add(rule);
    }

    int getMaxLenghtRule() {
        int max = 0;
        for (Rule rule : rules) {
            int m = rule.right.length();
            if (max < m) {
                max = m;
            }
        }
        return max;
    }

    boolean hasRule(String string) {
        for (Rule rule : rules) {
            if (rule.right.equals(string)) {
                return true;
            }
        }
        return false;
    }

    Set<String> getNonterm(String string) {
        Set<String> s = new TreeSet<>();
        for (Rule rule : rules) {
            if (rule.right.equals(string)) {
                s.add(rule.left);
            }
        }
        return s;
    }

    boolean isNonterm(String string) {
        for (Rule rule : rules) {
            if (rule.left.equals(string)) {
                return true;
            }
        }
        return false;
    }

    void printGrammar() {
        for (Rule rule : rules) {
            rule.printRule();
        }
    }
}

class Rule {

    String left;
    String right;

    public Rule(String left, String right) {
        this.left = left;
        this.right = right;
    }

    void printRule() {
        System.out.println(left + " " + right);
    }
}

