import java.io.*;
import java.util.*;

public class Network {

    static HashMap<String, ArrayList<String>> network = new HashMap<String, ArrayList<String>>();
    static HashMap<String, ArrayList<String>> randomNetwork = new HashMap<String, ArrayList<String>>();

    public Network(String fileName) throws IOException {
        process(fileName);
    }

    public double sampleRealNetwork(int samples) {
        int total = 0;
        for(int i = 0; i < samples; i++) {
            String A = pickRandomPerson();
            String B = pickRandomPerson();
            int degrees = findDegreeOfSeparationBFS(A, B, network);
            System.out.println(A + ":" + B + "=" + String.valueOf(degrees));
            total += degrees;

        }
        double average = (double)total/(double)samples;
        return average;
    }

    public double sampleRandomNetwork(int samples) {
        int total = 0;
        for(int i = 0; i < samples; i++) {
            String A = pickRandomPerson();
            String B = pickRandomPerson();
            int degrees = findDegreeOfSeparationBFS(A, B, randomNetwork);
            System.out.println(A + ":" + B + "=" + String.valueOf(degrees));
            total += degrees;
        }
        double average = (double)total/(double)samples;
        return average;
    }

    public String pickRandomPerson() {
        Random generator = new Random();
        Object[] keys = network.keySet().toArray();
        String random = (String) keys[generator.nextInt(keys.length)];
        return random;
    }

    public int findDegreeOfSeparationBFS(String A, String B, HashMap<String, ArrayList<String>> networkMap) {
        int degree = 0;
        // If A and B are the same person, there is no separation.
        if(A.equals(B)) {
            return degree;
        }
        // The superNetwork will contain A's complete network including friends of friends of etc...
        HashSet<String> superNetwork = new HashSet<String>();
        // A queue is used to store the next immediate set of friends
        LinkedList<ArrayList<String>> queue = new LinkedList<ArrayList<String>>();
        queue.push(networkMap.get(A));
        superNetwork.add(A);
        superNetwork.addAll(networkMap.get(A));
        degree+=1;
        while(!superNetwork.contains(B)) {
            ArrayList<String> circle = new ArrayList<String>();
            // A circle consists of the next immediate set of friends
            for(String friend : queue.pop()) {
                circle.addAll(networkMap.get(friend));
                circle.removeAll(superNetwork);
            }
            queue.add(circle);
            superNetwork.addAll(circle);
            // For each layer of the network, we increment the degree by 1
            degree+=1;
        }
        return degree;
    }

    public int findDegreesOfSeparationDFS(String A, String B, ArrayList<String> visited) {
        int degree = 0;
        if(A.equals(B)) {
            return 0;
        }
        ArrayList<String> friends = network.get(A);
        if(friends == null) {
            return 0;
        }
        visited.add(A);
        if(friends.contains(B)) {
            return 1;
        }
        for(String friend : friends) {
            if(network.get(friend).contains(B)) {
                return 2;
            }
        }
        ArrayList<Integer> d = new ArrayList<Integer>();
        for(String friend : friends) {
            if(!visited.contains(friend)) {
                int separation = findDegreesOfSeparationDFS(friend, B, visited);
                if(separation > 0) {
                    d.add(1+separation);
                }
            }
        }
        if(d.size() > 0) {
            degree += Collections.min(d);
        }

        return degree;
    }

    public int findDegreesOfSeparationRandom(String A, String B, ArrayList<String> visited) {
        int degree = 0;
        if(A.equals(B)) {
            return 0;
        }
        ArrayList<String> friends = randomNetwork.get(A);
        if(friends == null) {
            return 0;
        }
        visited.add(A);
        if(friends.contains(B)) {
            return 1;
        }
        for(String friend : friends) {
            if(randomNetwork.get(friend).contains(B)) {
                return 2;
            }
        }
        ArrayList<Integer> d = new ArrayList<Integer>();
        for(String friend : friends) {
            if(!visited.contains(friend)) {
                int separation = findDegreesOfSeparationRandom(friend, B, visited);
                if(separation > 0) {
                    d.add(1+separation);
                }
            }
        }
        if(d.size() > 0) {
            degree += Collections.min(d);
        }

        return degree;
    }


    public static void process(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] edge = line.split(" ");
            if (!network.containsKey(edge[0])) {
                ArrayList<String> friends = new ArrayList<String>();
                friends.add(edge[1]);
                network.put(edge[0], friends);
            } else {
                network.get(edge[0]).add(edge[1]);
            }
            if(!network.containsKey(edge[1])) {
                ArrayList<String> friends = new ArrayList<String>();
                friends.add(edge[0]);
                network.put(edge[1], friends);
            } else {
                network.get(edge[1]).add(edge[0]);
            }
        }
        br.close();

        for (Map.Entry<String, ArrayList<String>> entry : network.entrySet())
        {
            System.out.println(entry.getKey() + " : " + entry.getValue().toString());
        }
    }

    public void createRandomNetwork() {
        randomNetwork = new HashMap<String, ArrayList<String>>();
        for (Map.Entry<String, ArrayList<String>> entry : network.entrySet()) {
            int randomSize = 1 + (int) (Math.random() * ((maximumNumberOfFriends() - 1) + 1));
            HashSet<String> randomFriends = new HashSet<String>();
            for (int i = 0; i < randomSize; i++) {
                randomFriends.add(pickRandomPerson());
            }
            randomFriends.remove(entry.getKey());
            ArrayList<String> randomList = new ArrayList<String>(randomFriends);
            if (randomNetwork.containsKey(entry.getKey())) {
                randomNetwork.get(entry.getKey()).addAll(randomList);
            } else {
                randomNetwork.put(entry.getKey(), randomList);
            }
        }
        for(Map.Entry<String, ArrayList<String>> entry : randomNetwork.entrySet()) {
            for(String friend : entry.getValue()) {
                if(!randomNetwork.get(friend).contains(entry.getKey())) {
                    randomNetwork.get(friend).add(entry.getKey());
                }
            }
        }
    }

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public int averageNumberOfFriends() {
        int numFriends = 0;
        int numNodes = network.size();
        for (Map.Entry<String, ArrayList<String>> entry : network.entrySet())
        {
            numFriends += entry.getValue().size();
        }
        return numFriends/numNodes;
    }

    public int maximumNumberOfFriends() {
        int max = 0;
        for (Map.Entry<String, ArrayList<String>> entry : network.entrySet())
        {
            if(entry.getValue().size() > max) {
                max = entry.getValue().size();
            }
        }
        return max;
    }
}