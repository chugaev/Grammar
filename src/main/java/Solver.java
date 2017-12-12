import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Solver {
    int solve(String fileNameGrammar, String fileNameGraph, String fileout) throws FileNotFoundException {
        Grammar grammar = parseGrammar(fileNameGrammar);
        Set<String>[][] graph = parseGraph(fileNameGraph);
        int size = graph.length;
        Set<String>[][] matrix = new TreeSet[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = new TreeSet<>();
                if (graph[i][j] != null) {
                    for (String e: graph[i][j]) {
                        matrix[i][j].addAll(grammar.getNetFromTer(e));
                    }
                }
            }
        }
        int count = 0, last_count = 0;
        while (true) {
            for (int k = 0; k < size; k++) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        Set<String> start = matrix[i][k];
                        Set<String> finish = matrix[k][j];
                        Set<String> v = new TreeSet<>();
                        for (String s : start) {
                            for (String f : finish) {
                                v.addAll(grammar.getNetFromTwoNet(s, f));
                            }
                        }
                        matrix[i][j].addAll(v);
                    }
                }
            }
            count = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (matrix[i][j].contains("S")) {
                        count++;
                    }
                }
            }
            if (last_count == count) {
                break;
            }
            last_count = count;
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
                    for (String str : matrix[i][j]) {
                        pw.print(i + "," + str + "," + j + "\n");
                    }
                }
            }
        }
        if (pw != null) {
            pw.close();
        }
        return count;
    }

    Grammar parseGrammar(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        Grammar grammar = new Grammar();
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            String left = s.split("->")[0].trim();
            String right = s.split("->")[1].trim();
            grammar.addProduct(left, right);
        }
        sc.close();
        return grammar;
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
}

class Grammar {
    ArrayList<ProductTypeOne> productTypeOnes;
    ArrayList<ProductTypeTwo> productTypeTwos;

    public Grammar() {
        productTypeOnes = new ArrayList<>();
        productTypeTwos = new ArrayList<>();
    }

    void addProduct(String left, String right) {
        if (right.length() == 1) {
            ProductTypeOne p = new ProductTypeOne(left, right);
            productTypeOnes.add(p);
        } else if (right.length() == 2) {
            ProductTypeTwo p = new ProductTypeTwo(left, right.substring(0, 1), right.substring(1, 2));
            productTypeTwos.add(p);
        } else {
            System.out.println("wtf");
        }
    }

    Set<String> getNetFromTer(String ter) {
        Set<String> set = new TreeSet<>();
        for (ProductTypeOne p : productTypeOnes) {
            if (p.t.equals(ter)) {
                set.add(p.n);
            }
        }
        return set;
    }

    Set<String> getNetFromTwoNet(String n1, String n2) {
        Set<String> set = new TreeSet<>();
        for (ProductTypeTwo p : productTypeTwos) {
            if (p.n1.equals(n1) && p.n2.equals(n2)) {
                set.add(p.n);
            }
        }
        return set;
    }

    void print() {
        for (ProductTypeTwo p : productTypeTwos) {
            System.out.println(p.n + " -> " + p.n1 + p.n2);
        }
        for (ProductTypeOne p : productTypeOnes) {
            System.out.println(p.n + " -> " + p.t);
        }
    }
}

class ProductTypeOne {
    String n;
    String t;

    public ProductTypeOne(String n, String t) {
        this.n = n;
        this.t = t;
    }
}

class ProductTypeTwo {
    String n;
    String n1;
    String n2;

    public ProductTypeTwo(String n, String n1, String n2) {
        this.n = n;
        this.n1 = n1;
        this.n2 = n2;
    }
}
