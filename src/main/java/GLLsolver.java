import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class GLLsolver {
    Map<String, Set<String>[][]> RFA;
    Set<String>[][] graph;
    Set<String> ans;
    Set<String> used;
    int k = 0;
    int solve(String fileNameGrammar, String fileNameGraph, String fileout) throws FileNotFoundException {
        RFA = parseGrammar(fileNameGrammar);
        graph = parseGraph(fileNameGraph);
        ans = new TreeSet<>();
        used = new TreeSet<>();
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

//        for (int i = 0; i < graph.length; i++) {
//            for (int j = 0; j < graph.length; j++) {
//                System.out.print(graph[i][j]);
//            }
//            System.out.println();
//        }
//        System.out.println(RFA.get("S").length);
//        System.out.println(graph.length);
//        for (int i = 0; i < graph.length; i++) {
//            rec(i, i, "S", 0);
//            used.clear();
//        }
        rec(0, 0, "S", 0);
//        System.out.println(ans.size());
        ArrayList<String> array = new ArrayList<>();
        for (String string : ans) {
//            System.out.println(string);
            array.add(string);
        }
        PrintWriter pw = null;
        if (fileout != null && !fileout.equals("")) {
            pw = new PrintWriter(new File(fileout));
        }
        Collections.sort(array);
//        System.out.println(array.size());
        int k = 0;
        for (String string : array) {
            k++;
            if (fileout != null && !fileout.equals("")) {
                pw.print(string + '\n');
            } else if (fileout.equals("")){
                System.out.println(string);
            }
        }
//        System.out.println(k);
//        System.out.println(ans.size());
        if (pw != null) {
            pw.close();
        }
        return ans.size();
    }

    ArrayList<Integer> rec(int start, int position, String nonterm, int modulePosition) {
//        System.out.println(start + " " + position + " " + nonterm + " " + modulePosition);
        ArrayList<CommonPositionCall> queue = new ArrayList<>();
        ArrayList<Integer> positions = new ArrayList<>();
        ArrayList<Integer> forReturnPositions = new ArrayList<>();
        find(position, nonterm, modulePosition, queue);
        for (CommonPositionCall call : queue) {
//            System.out.println(call.position + " " + call.module + " " + call.modulePosition);
            if (call.module == null) {
//                System.out.println("Answer1: " + start + " " + nonterm + " " + call.position);
                ans.add(start + "," + nonterm + "," + call.position);
                forReturnPositions.add(call.position);
            } else {
                String next = call.position + "#" + call.position + "#" + call.module;
                if (!used.contains(next)) {
                    used.add(next);
                    positions.addAll(rec(call.position, call.position, call.module, 0));
                }
                for (Integer i : positions) {
                    ArrayList<CommonPositionCall> q = new ArrayList<>();
                    find(i, nonterm, call.finishModulePosition, q);

                    for (CommonPositionCall positionCall : q) {
                        if (positionCall.module == null) {
                            ans.add(start + "," + nonterm + "," + positionCall.position);
                            forReturnPositions.add(positionCall.position);
                        } else {
                            String next2 = call.position + "#" + call.position + "#" + call.module;
                            if (!used.contains(next2)) {
                                used.add(next2);
                                positions.addAll(rec(i, i,  positionCall.module, positionCall.modulePosition));
                            }
                        }
                    }
                }

            }
        }
        return forReturnPositions;
    }

    void find(int position, String nonterm, int modulePosition, ArrayList<CommonPositionCall> queue) {
        Set<String>[][] module = RFA.get(nonterm);
        for (int i = 0; i < module.length; i++) {
            for (String edgeModule : module[modulePosition][i]) {
                if (isTerminal(edgeModule)) {
                    for (int j = 0; j < graph.length; j++) {
                        for (String edgeGraph : graph[position][j]) {
                            if (edgeGraph.equals(edgeModule)) {
                                if (module.length - 1 == i) {
                                    queue.add(new CommonPositionCall(j, null, i, -1));// -1 ?????
                                } else {
                                    find(j, nonterm, i, queue);
                                }
                            }
                        }
                    }
                } else if (!isTerminal(edgeModule)) {
                    queue.add(new CommonPositionCall(position, edgeModule, modulePosition, i));
                }
            }
        }
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

    boolean isTerminal(String string) {
        return !RFA.containsKey(string);
    }
}

class Triple {
    int a;
    int b;
    int c;

    public Triple(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}

class CommonPositionCall {
    int position;
    String module;
    int modulePosition;
    int finishModulePosition;

    public CommonPositionCall(int position, String module, int modulePosition, int finishModulePosition) {
        this.position = position;
        this.module = module;
        this.modulePosition = modulePosition;
        this.finishModulePosition = finishModulePosition;
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
