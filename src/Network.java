import java.io.*;
import java.util.*;

/**
 * Created by Terence on 4/29/14.
 */
public class Network {

    static HashMap<String, ArrayList<String>> network = new HashMap<String, ArrayList<String>>();
    static HashMap<String, ArrayList<String>> randomNetwork = new HashMap<String, ArrayList<String>>();
    static ArrayList<Integer> degrees = new ArrayList<Integer>();

    public Network(String fileName) throws IOException {
        process(fileName);
    }

    public double sampleRealNetwork(int samples) {
        int total = 0;
        for(int i = 0; i < samples; i++) {
            String A = pickRandomPerson();
            String B = pickRandomPerson();
            int degrees = findDegreesOfSeparation(A, B, new ArrayList<String>());
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
            int degrees = findDegreesOfSeparationRandom(A, B, new ArrayList<String>());
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

    public int findDegreesOfSeparation(String A, String B, ArrayList<String> visited) {
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
                int separation = findDegreesOfSeparation(friend, B, visited);
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
        for (Map.Entry<String, ArrayList<String>> entry : network.entrySet())
        {
            int randomSize = 1 + (int)(Math.random() * ((100 - 1) + 1));
            HashSet<String> randomFriends = new HashSet<String>();
            for(int i = 0; i < randomSize; i++) {
                randomFriends.add(pickRandomPerson());
            }
            randomFriends.remove(entry.getKey());
            ArrayList<String> randomList = new ArrayList<String>(randomFriends);
            randomNetwork.put(entry.getKey(), randomList);
            for(String random : randomList) {
                if(!randomNetwork.containsKey(random)) {
                    randomNetwork.put(random, new ArrayList<String>());
                } else {
                    ArrayList<String> x = randomNetwork.get(random);
                    x.add(entry.getKey());
                    randomNetwork.put(random, x);
                }
            }
        }

        for (Map.Entry<String, ArrayList<String>> entry : randomNetwork.entrySet())
        {
            System.out.println(entry.getKey() + " : " + entry.getValue().toString());
        }
    }
}