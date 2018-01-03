import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class GLLSolver {

    int solve(String fileNameGrammar, String fileNameGraph, String fileout) throws FileNotFoundException {
        Map<String, Set<String>[][]> RFA = parseGrammar(fileNameGrammar);
        Set<String>[][] graph = parseGraph(fileNameGraph);
        Set<String> ans = new TreeSet<>();
        for (int z = 0; z < graph.length; z++) {
            Queue<Configuration> queue = new LinkedList<>();
            ArrayList<Configuration> poped = new ArrayList<>();
            Pair start = new Pair("S", z);
            GSS gss = new GSS(start);
            queue.add(new Configuration(z, new Pair("S", 0), start));
            while (!queue.isEmpty()) {
                Configuration cur_conf = queue.poll();
                int f = 0;
                for (Configuration configuration : poped) {
                    if (cur_conf.eq(configuration)) {
                        f = 1;
                        break;
                    }
                }
                if (f == 1) {
                    continue;
                }
                poped.add(cur_conf);
                int graphPosition = cur_conf.position;
                Set<String>[][] module = RFA.get(cur_conf.RFAPosition.a);
                int modulePosition = cur_conf.RFAPosition.b;
                if (modulePosition == module.length - 1) {
                    Pair node = cur_conf.node;
                    ans.add(node.b + "," + node.a + "," + graphPosition);
                    ArrayList<PairOfPair> pairOfPairs = gss.getNodes(node);
                    for (PairOfPair pairOfPair : pairOfPairs) {
                        Configuration conf = new Configuration(
                                graphPosition,
                                pairOfPair.a,
                                pairOfPair.b);
                        queue.add(conf);
                    }
                }
                for (int i = 0; i < module.length; i++) {
                    for (String edgeRFA : module[modulePosition][i]) {
                        if (isTerminal(RFA, edgeRFA)) {
                            for (int j = 0; j < graph.length; j++) {
                                for (String edgeGraph : graph[graphPosition][j]) {
                                    if (edgeRFA.equals(edgeGraph)) {
                                        Configuration conf = new Configuration(
                                                j,
                                                new Pair(cur_conf.RFAPosition.a, i),
                                                cur_conf.node);
                                        queue.add(conf);
                                    }
                                }
                            }
                        } else if (!isTerminal(RFA, edgeRFA)) {
                            Configuration conf = new Configuration(
                                    graphPosition,
                                    new Pair(edgeRFA, 0),
                                    new Pair(edgeRFA, graphPosition));
                            queue.add(conf);
                            gss.add(new Pair(cur_conf.RFAPosition.a, i),
                                    new Pair(edgeRFA, graphPosition),
                                    cur_conf.node);
                        }
                    }
                }
            }
        }
        PrintWriter pw = null;
        if (fileout != null && !fileout.equals("")) {
            pw = new PrintWriter(new File(fileout));
        }
        int k = 0;
        for (String s : ans) {
            if (fileout != null) {
                if (fileout.equals("")) {
                    System.out.println(s);
                } else {
                    pw.print(s + '\n');
                }
            }
            if (s.contains("S")) {
                k++;
            }
        }
        if (pw != null) {
            pw.close();
        }
        return k;
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
//            System.out.println(size);
            Set<String>[][] module = new TreeSet[size][size];
            for (int i = 0; i < module.length; i++) {
                for (int j = 0; j < module.length; j++) {
                    module[i][j] = new TreeSet<>();
                }
            }
            int i = 0;
            for (String right : arrayList) {
                if (right.length() == 1) {
                    module[0][size - 1].add(right);
                    continue;
                }
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

    boolean isTerminal(Map<String, Set<String>[][]> RFA, String string) {
        return !RFA.containsKey(string);
    }
}

class PairOfPair {
    Pair a;
    Pair b;

    public PairOfPair(Pair a, Pair b) {
        this.a = a;
        this.b = b;
    }
}


class Pair {
    String a;
    int b;

    public Pair(String a, int b) {
        this.a = a;
        this.b = b;
    }
}

class Configuration {
    int position;
    Pair RFAPosition;
    Pair node;

    public Configuration(int pos, Pair R, Pair n) {
        this.position = pos;
        this.RFAPosition = R;
        this.node = n;
    }

    void print() {
        System.out.println(position + " " + RFAPosition.a + " " + RFAPosition.b + " " + node.a + " " + node.b);
    }

    boolean eq(Configuration c) {
        return position == c.position
                && RFAPosition.a.equals(c.RFAPosition.a)
                && RFAPosition.b == c.RFAPosition.b
                && node.a.equals(c.node.a)
                && node.b == c.node.b;
    }

}

class GSS {
    ArrayList<Integer>[][] gss;
    Map<Integer, Pair> gssVertexes;
    Map<Integer, Pair> gssEdges;
    int countEdge;
    int countVertex;

    public GSS(Pair pair) {
        gss = new ArrayList[1111][1111];

        gssVertexes = new HashMap<>();
        gssEdges = new HashMap<>();
        gssEdges.put(0, new Pair(pair.a, pair.b));
        countEdge = 1;
        countVertex = 0;
    }

    void add(Pair vertex, Pair fromEdge, Pair toEdge) {
        ArrayList<PairOfPair> nodes = getNodes(fromEdge);
        if (nodes == null) {
            gssEdges.put(countEdge, fromEdge);
            int from = countEdge;
            countEdge++;
            int to = getIndexEdge(toEdge);
            gssVertexes.put(countVertex, vertex);
            if (gss[from][to] == null) {
                gss[from][to] = new ArrayList<>();
            }
            gss[from][to].add(countVertex);
            countVertex++;
            return;
        }
        for (PairOfPair pop : nodes) {
            Pair v = pop.a;
            Pair e = pop.b;
            if (v.a == vertex.a && v.b == vertex.b && toEdge.a == e.a && toEdge.b == e.b) {
                return;
            }
        }
        int indexEdgeFrom = getIndexEdge(fromEdge);
        int indexEdgeTo = getIndexEdge(toEdge);
        gssVertexes.put(countVertex, vertex);
        try {
            if (gss[indexEdgeFrom][indexEdgeTo] == null) {
                gss[indexEdgeFrom][indexEdgeTo] = new ArrayList<>();
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            print();
        }
        gss[indexEdgeFrom][indexEdgeTo].add(countVertex);
        countVertex++;
    }

    int getIndexEdge(Pair edge) {
        for (Integer i : gssEdges.keySet()) {
            Pair pair = gssEdges.get(i);
            if (pair.a.equals(edge.a) && pair.b == edge.b) {
                return i;
            }
        }
        return -1;
    }

    ArrayList<PairOfPair> getNodes(Pair edge) {
        int indexEdge = getIndexEdge(edge);

        if (indexEdge == -1) {
            return null;
        }
        ArrayList<PairOfPair> ans = new ArrayList<>();
        for (int i = 0; i < gss.length; i++) {
            if (gss[indexEdge][i] != null) {
                for (Integer j : gss[indexEdge][i]) {
                    Pair a = gssVertexes.get(j);
                    Pair b = gssEdges.get(i);
                    PairOfPair pop = new PairOfPair(a, b);
                    ans.add(pop);
                }
            }
        }
        return ans;
    }

    private void printVertex() {
        System.out.println("Vertex: ");
        for (Integer i : gssVertexes.keySet()) {
            System.out.println(i + " : " + gssVertexes.get(i).a + " " + gssVertexes.get(i).b);
        }
    }

    private void printEdge() {
        System.out.println("Edge: ");
        for (Integer i : gssEdges.keySet()) {
            System.out.println(i + " : " + gssEdges.get(i).a + " " + gssEdges.get(i).b);
        }
    }

    void print() {
        printEdge();
        printVertex();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (gss[i][j] == null) {
                    System.out.print("()");
                } else {
                    System.out.print(gss[i][j]);
                }
            }
            System.out.println();
        }
    }
}