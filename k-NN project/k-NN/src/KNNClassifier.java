import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KNNClassifier {
    private int k;
    private List<DataPoint> trainData;

    public KNNClassifier(int k) {
        this.k = k;
        this.trainData = new ArrayList<>();
    }

    public void loadTrainingData(String filename) throws IOException {
        trainData = loadCSV(filename);
    }

    public List<DataPoint> loadCSV(String filename) throws IOException {
        List<DataPoint> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            double[] features = new double[values.length - 1];
            for (int i = 0; i < features.length; i++) {
                features[i] = Double.parseDouble(values[i]);
            }
            String label = values[values.length - 1];
            data.add(new DataPoint(features, label));
        }
        br.close();
        return data;
    }

    public String classify(DataPoint point) {
        //  calculate distances to all training points
        List<Neighbor> neighbors = new ArrayList<>();
        for (DataPoint trainPoint : trainData) {
            double distance = euclideanDistance(point.features, trainPoint.features);
            neighbors.add(new Neighbor(trainPoint.label, distance));
        }

        // sort by distance
        neighbors.sort(Comparator.comparingDouble(n -> n.distance));

        // count accurances of each type of flower
        Map<String, Integer> labelCounts = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Neighbor neighbor = neighbors.get(i);
            labelCounts.put(
                    neighbor.label,
                    labelCounts.getOrDefault(neighbor.label, 0) + 1
            );
        }

        // get the name of the flower with the highest count
        return labelCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    public double evaluate(String testFilename) throws IOException {
        List<DataPoint> testData = loadCSV(testFilename);
        int correct = 0;
        for (DataPoint point : testData) {
            String predicted = classify(point);
            if (predicted.equals(point.label)) {
                correct++;
            }
        }
        return (double) correct / testData.size();
    }

    private double euclideanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }
}
