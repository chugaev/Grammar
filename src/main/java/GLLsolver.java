import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class GLLsolver {
    Map<String, Set<String>[][]> RFA;
    Set<String>[][] graph;
    boolean[] startAns;
    Map<String, Boolean> nontermAns;
    boolean[] finalAns;
    int k = 0;
    int solve(String fileNameGrammar, String fileNameGraph, String fileout) throws FileNotFoundException {
        RFA = parseGrammar(fileNameGrammar);
        graph = parseGraph(fileNameGraph);
        startAns = new boolean[graph.length];
        finalAns = new boolean[graph.length];
        nontermAns = new HashMap<>();
//        System.out.println();
//        for (String key : RFA.keySet()) {
//            Set<String>[][] module = RFA.get(key);
//            for (int i = 0; i < module.length; i++) {
//                for (int j = 0; j < module.length; j++) {
//                    System.out.print(module[i][j]);
//                }
//                System.out.println();
//            }
//        }
//
//        for (int i = 0; i < graph.length; i++) {
//            for (int j = 0; j < graph.length; j++) {
//                System.out.print(graph[i][j]);
//            }
//            System.out.println();
//        }
//        System.out.println(RFA.get("S").length);
//        System.out.println(graph.length);
        rec(0, 0, "S", 0);
//        for (int i = 0; i < graph.length; i++) {
//            rec(i, i, "S", 0);
//        }
        for (int i = 0; i < startAns.length; i++) {
            for (String string : nontermAns.keySet()) {
                for (int j = 0; j < finalAns.length; j++) {
                    if (startAns[i] && nontermAns.get(string) && finalAns[j]) {
                        k++;
                        System.out.println(i + " " + string + " " + j);
                    }
                }
            }
        }
        System.out.println(k);

//        Queue<Configuration> configurations = new PriorityQueue<>();
//        Set<Configuration> popped = new TreeSet<>();
//
//        configurations.add(new Configuration(0, new RFAPosition("S", 0), new GSSNode(0, "S")));
//label1: while (!configurations.isEmpty()) {
//            Configuration conf = configurations.poll();
//            if (popped.contains(conf)) {
//                continue;
//            }
//            popped.add(conf);
//            int graphPosition = conf.position;
//            RFAPosition rfaPosition = conf.rfaPosition;
//            GSSNode gssNode = conf.node;
//            Set<String>[][] module = RFA.get(rfaPosition.nonterm);
//            for (int j = 0; j < module.length; j++) {
//                Set<String> edges = module[rfaPosition.position][j];
//                for (String edge : edges) {
//                    if (j == module.length - 1) { // final state in module
//                        break label1;
//                    } else if (isTerminal(RFA, edge)) { // terminal
//
//                    } else if (!isTerminal(RFA, edge)) { // nonterminal
//
//                    }
//                }
//            }
//        }
        return 0;
    }

    int rec(int start, int position, String nonterm, int modulePosition) {
        int oldPosition = position;
        Set<String>[][] module = RFA.get(nonterm);
        if (modulePosition == module.length - 1) {
//            System.out.println("answer: " + start + " " + nonterm + " " + position);
            startAns[start] = true;
            finalAns[position] = true;
            if (!nontermAns.containsKey(nonterm)) {
                nontermAns.put(nonterm, true);
            }
            return position;
        }
//        System.out.println(start + " " + position + " " + nonterm + " " + modulePosition);
        for (int i = 0; i < module.length; i++) {
            for (String edgeModule : module[modulePosition][i]) {
                if (isTerminal(edgeModule)) {
                    for (int j = 0; j < graph.length; j++) {
                        for (String edgeGraph : graph[oldPosition][j]) {
                            if (edgeGraph.equals(edgeModule)) {
                                int t = rec(start, j, nonterm, i);
                                if (t != -1) {
                                    position = t;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < module.length; i++) {
            for (String edgeModule : module[modulePosition][i]) {
                if (!isTerminal(edgeModule)) {
                    rec(position, oldPosition, edgeModule, 0);
//                    if (pos != -1) {
//                        System.out.println("asdasdad");
//                        rec(oldPosition, pos, edgeModule, i);
//                    }
                }
            }
        }
        return -1;
    }

    Set<String>[][] parseGraph(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        sc.nextLine();
        sc.nextLine();
        int size = Collections.max(Arrays.stream(sc.nextLine().split("; ")).map(str -> Integer.parseInt(str)).collect(Collectors.toList())) + 1;
        Set<String>[][] matrix = new TreeSet[size][size];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = new TreeSet<>();
            }
        }
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

    Map<String, Set<String>[][]> parseGrammar(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        Map<String, ArrayList<String>> map = new HashMap();
        while (sc.hasNext()) {
            String string = sc.nextLine();
            String left = string.split("->")[0].replace(" ", "");
            String right = string.split("->")[1].replace(" ", "");
            if (map.containsKey(left)) {
                map.get(left).add(right);
            } else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(right);
                map.put(left, arrayList);
            }
        }
        Map<String, Set<String>[][]> RFA = new HashMap();
        for (String key : map.keySet()) {
            ArrayList<String> arrayList = map.get(key);
            for (String s : arrayList) {
//                System.out.print(s + " ");
            }
            int size = arrayList.stream().mapToInt(w -> w.length()).sum() - arrayList.size() + 2;
            Set<String>[][] module = new TreeSet[size][size];
            for (int i = 0; i < module.length; i++) {
                for (int j = 0; j < module.length; j++) {
                    module[i][j] = new TreeSet<>();
                }
            }
            int i = 0;
            for (String right : arrayList) {
                for (int j = 0; j < right.length(); j++) {
                    if (j == right.length() - 1) {
                        module[i][size - 1].add(String.valueOf(right.charAt(j)));
                    } else {

                        if (j == 0) {
                            module[0][i + 1].add(String.valueOf(right.charAt(j)));
                        } else {
                            module[i][i + 1].add(String.valueOf(right.charAt(j)));
                        }
                        i++;
                    }
                }
            }
            RFA.put(key, module);
        }
        return RFA;
    }

    boolean isTerminal(String string) {
        return !RFA.containsKey(string);
    }
}

class Configuration {
    int position;
    RFAPosition rfaPosition;
    GSSNode node;

    public Configuration(int position, RFAPosition rfaPosition, GSSNode node) {
        this.position = position;
        this.rfaPosition = rfaPosition;
        this.node = node;
    }
}

class GSS {
    Map<Integer, GSSNode> nodes;
    Set<RFAPosition>[][] matrix;

}

class GSSNode implements Comparator<GSSNode> {
    int position;
    String nonterm;

    public GSSNode() {

    }

    public GSSNode(int position, String nonterm) {
        this.position = position;
        this.nonterm = nonterm;
    }

    @Override
    public int compare(GSSNode node, GSSNode t1) {
        if (node.nonterm.equals(t1.nonterm) && node.position == t1.position) {
            return 0;
        } else {
            return 1;
        }
    }
}

class RFAPosition {
    String nonterm;
    int position;

    public RFAPosition(String nonterm, int position) {
        this.nonterm = nonterm;
        this.position = position;
    }
}
