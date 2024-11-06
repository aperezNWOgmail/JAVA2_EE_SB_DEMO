package com.example.demo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class AlgorithmManager {

    public static String generateRandomPoints(int vertexSize, int sampleSize, int sourcePoint) {
        int[][] graph = new int[vertexSize][vertexSize];
        String statusMessage = "";
        long currentTimeMillis = System.currentTimeMillis();
        Random randX = new Random(currentTimeMillis / 2);
        Random randY = new Random(currentTimeMillis * 2);

        int[] vertexX = fisherYates(sampleSize, randX);
        int[] vertexY = fisherYates(sampleSize, randY);
        List<String> vertexArray = new ArrayList<>();

        for (int index = 0; index < vertexSize; index++) {
            String separator1 = (index < vertexSize - 1) ? "|" : "";
            vertexArray.add(String.format("[%d,%d]%s", vertexX[index], vertexY[index], separator1));
        }

        StringJoiner vertexArrayString = new StringJoiner("");
        for (String vertex : vertexArray) {
            vertexArrayString.add(vertex);
        }

        String separator2 = "■";
        String vertexMatrix = generateRandomMatrix(vertexArray, graph, vertexSize);
        String vertexList = dijkstra(vertexArray, graph, vertexSize, sampleSize, sourcePoint);

        String sortedListEncoded = vertexList.replace(",", "<br/>").replace("\t", "&nbsp;");
        statusMessage = String.format("%s%s%s%s%s", vertexArrayString.toString(), separator2, vertexMatrix, sortedListEncoded);

        // LogModel.log(String.format("DIJSTRA_DEMO. GENERATE_RANDOM_VERTEX : %s", statusMessage));

        return statusMessage;
    }

    public static String generateRandomMatrix(List<String> vertexString, int[][] graph, int vertexSize) {
        StringBuilder statusMessage = new StringBuilder();
        
        for (int index = 0; index < vertexSize; index++) {
            graph[index][index] = 0;
        }
        
        long currentTimeMillis = System.currentTimeMillis();
        Random rnd = new Random(currentTimeMillis % 1000);
        
        for (int index_x = 0; index_x < vertexSize; index_x++) {
            for (int index_y = (index_x + 1); index_y < vertexSize; index_y++) {
                double randomValue = rnd.nextInt(2);

                if (randomValue == 1) {
                    randomValue = getHipotemuza(vertexString, index_x, index_y);
                }

                graph[index_x][index_y] = (int) randomValue;
                graph[index_y][index_x] = (int) randomValue;
            }
        }

        for (int index_x = 0; index_x < vertexSize; index_x++) {
            int zeroCount = 0;

            for (int index_y = 0; index_y < vertexSize; index_y++) {
                if ((index_x != index_y) && (graph[index_x][index_y] == 0)) {
                    zeroCount++;

                    if (zeroCount == (vertexSize - 1)) {
                        int hipotemuza = (int) getHipotemuza(vertexString, index_x, index_y);
                        graph[index_x][index_y] = hipotemuza;
                        graph[index_y][index_x] = hipotemuza;
                    }
                }
            }
        }

        for (int index_x = 0; index_x < vertexSize; index_x++) {
            String separator_1 = (index_x < vertexSize - 1) ? "|" : "";
            StringBuilder stringArray = new StringBuilder();
            String stringArrayValues = "";

            for (int index_y = 0; index_y < vertexSize; index_y++) {
                String separator_2 = (index_y < vertexSize - 1) ? "," : "";
                stringArray.append(String.format("%d%s", graph[index_x][index_y], separator_2));
            }
            stringArrayValues = String.format("{%s}", stringArray.toString());
            statusMessage.append(String.format("%s%s", stringArrayValues, separator_1));
        }
        return statusMessage.toString();
    }

    private static double getHipotemuza(List<String> vertexString, int index_x, int index_y) {
        // Implement the logic for GetHipotemuza here
        return 0; // Placeholder return value
    }

    // Placeholder for the FisherYates method
    private static int[] fisherYates(int sampleSize, Random rand) {
        // Implementation needed
        return new int[sampleSize];
    }

    // Placeholder for the dijkstra method
    private static String dijkstra(List<String> vertexArray, int[][] graph, int vertexSize, int sampleSize, int sourcePoint) {
        // Implementation needed
        return "";
    }
}
