import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class Tests {

    @Test
    public void simpleTestDegree() throws IOException {
        String file = "C:\\Users\\Terence\\Desktop\\NetworkMotifs-2014-05-05\\NetworkMotifs\\src\\data\\test.txt";
        Network network = new Network(file);
        for(int i = 0; i <= 5; i++) {
            for(int j = 0; j <= 5; j++) {
                int degree = network.findDegreeOfSeparationBFS(String.valueOf(i), String.valueOf(j), Network.network);
                System.out.println(i + " : " + j + " = " + degree);
                Assert.assertEquals(Math.abs(i-j), degree);
            }
        }
    }

    @Test
    public void facebookSimulation() throws IOException {
        String file = "C:\\Users\\Terence\\Desktop\\NetworkMotifs-2014-05-05\\NetworkMotifs\\src\\data\\facebook.txt";
        Network network = new Network(file);
        double avgDeg = network.sampleRealNetwork(100);
        System.out.println(avgDeg);
    }

    @Test
    public void randomFacebookSimulation() throws IOException {
        String file = "C:\\Users\\Terence\\Desktop\\NetworkMotifs-2014-05-05\\NetworkMotifs\\src\\data\\facebook.txt";
        Network network = new Network(file);
        network.createRandomNetwork();
        double avgDeg = network.sampleRandomNetwork(100);
        System.out.println(avgDeg);
    }
}
