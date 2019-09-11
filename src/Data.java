import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Terence on 4/29/14.
 */
public class Data {

    HashMap<String, ArrayList<String>> network = new HashMap<String, ArrayList<String>>();
    ArrayList<String> edges = new ArrayList<String>();

    public Data(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            if(!network.containsKey(line.split("\t")[0])) {
                ArrayList<String> friends = new ArrayList<String>();
                friends.add(line.split("\t")[1]);
                network.put(line.split("\t")[0], friends);
            } else {
                network.get(line.split("\t")[0]).add(line.split("\t")[1]);
            }
        };
        br.close();
    }

    public int findDegreeOfSeparation(String A, String B) {
        int degree = 0;
        ArrayList<String> visited = new ArrayList<String>();
        ArrayList<String> friends = network.get(A);
        if(friends == null) {
            return degree;
        }
        if(friends.contains(B)) {
           degree++;
           return degree;
        }
        degree++;
        visited.add(A);
        for(String friend : friends) {
            if(!visited.contains(friend)) {
                if(network.get(friend).contains(B)) {
                    degree++;
                    return degree;
                } else {
                    degree += findDegreeOfSeparation(friend, B);
                }
            }
        }
        return degree;
    }






    public ArrayList<String> findSubgraphFromRandomRoot(int size) {
        ArrayList<String> subGraph = new ArrayList<String>();
        if(size == 0) {
            return subGraph;
        }
        Random generator = new Random();
        Object[] keys = network.keySet().toArray();
        String root = (String) keys[generator.nextInt(keys.length)];
        subGraph.add(root);
        if(size == 1) {
            return subGraph;
        }
        subGraph.addAll(findSubgraph(root, size-1));
        return subGraph;
    }

    public ArrayList<String> findSubgraph(String root, int size) {

        ArrayList<String> subGraph = new ArrayList<String>();
        if(size == 0) {
            return subGraph;
        }
        ArrayList<String> friends = network.get(root);
        if(friends != null) {
            Random generator = new Random();
            String connection = friends.get(generator.nextInt(friends.size()));
            subGraph.add(connection);
            subGraph.addAll(findSubgraph(connection, size-1));
        }
        return subGraph;
    }
}
