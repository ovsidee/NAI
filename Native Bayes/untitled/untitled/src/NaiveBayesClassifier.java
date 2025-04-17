import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class NaiveBayesClassifier {

    public Set<String> labels = new HashSet<>();

    //map of e.g. {E -> {0_x:10, 1_s:600}}
    public Map<String, Map<String, Integer>> featureCounts = new HashMap<>();

    //map of e.g. {E:100, P:200}
    public Map<String, Integer> labelCounts = new HashMap<>();

    public int featureCount = 0;

    // Train data
    public void train(List<Mushroom> data) {
        if (data.isEmpty()) return;

        featureCount = data.get(0).features.size();

        for (Mushroom m : data) {
            labels.add(m.label);
            labelCounts.put(m.label, labelCounts.getOrDefault(m.label, 0) + 1);
            featureCounts.putIfAbsent(m.label, new HashMap<>());

            Map<String, Integer> labelFeatureMap = featureCounts.get(m.label);

            for (int i = 0; i < m.features.size(); i++) {
                String value = m.features.get(i);
                String key = i + "_" + value;  // position + value
                labelFeatureMap.put(key, labelFeatureMap.getOrDefault(key, 0) + 1);
            }
        }
    }

    // Prediction
    public String predict(Mushroom mushroom) {
        double bestProb = Double.NEGATIVE_INFINITY;
        String bestLabel = null;

        // total number of samples (p + e)
        int totalLabels = labelCounts.values().stream().mapToInt(i -> i).sum();

        // label = E \or\ P
        for (String label : labels) {
            // probability of (E) \or\ (P)
            double logProb = Math.log((double) labelCounts.get(label) / totalLabels);
            // how much features label has (each feature) ->
            // e.g. {
            //  "0_x": 54,
            //  "1_s": 40,
            //  "3_n": 35,
            //  ...
            //}
            Map<String, Integer> labelFeatureCounts = featureCounts.getOrDefault(label, new HashMap<>());

            // through each feature
            for (int i = 0; i < mushroom.features.size(); i++) {
                String value = mushroom.features.get(i);
                String key = i + "_" + value;

                int countHowOftenFeatureAppeared  = labelFeatureCounts.getOrDefault(key, 0);

                //To avoid zero probability (smoothing)
                int total = labelFeatureCounts.values().stream().mapToInt(v -> v).sum();
                int uniqueValues = new HashSet<>(labelFeatureCounts.keySet()).size();
                logProb += Math.log((countHowOftenFeatureAppeared  + 1.0) / (total + uniqueValues));
            }

            if (logProb > bestProb) {
                bestProb = logProb;
                bestLabel = label;
            }
        }
        return bestLabel;
    }

    // Load data
    public static List<Mushroom> loadData(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .filter(line -> !line.trim().isEmpty())
                .map(line -> {
                    String[] parts = line.split(",");
                    String label = parts[0];
                    List<String> features = Arrays.asList(parts).subList(1, parts.length);
                    return new Mushroom(label, features);
                })
                .collect(Collectors.toList());
    }

    // Evaluate metrics
    public static void evaluate(List<String> predicted, List<String> actuals) {
        int tp = 0, tn = 0, fp = 0, fn = 0;

        for (int i = 0; i < actuals.size(); i++) {
            String actual = actuals.get(i);
            String predictedd = predicted.get(i);

            if (actual.equals("p") && predictedd.equals("p")) tp++;
            else if (actual.equals("e") && predictedd.equals("e")) tn++;
            else if (actual.equals("e") && predictedd.equals("p")) fp++;
            else if (actual.equals("p") && predictedd.equals("e")) fn++;
        }

        double accuracy = (tp + tn) / (double) actuals.size();
        double precision = (tp + fp) == 0 ? 0 : tp / (double)(tp + fp);
        double recall = (tp + fn) == 0 ? 0 : tp / (double)(tp + fn);
        double f1 = (precision + recall) == 0 ? 0 : 2 * precision * recall / (precision + recall);

        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F1 Score: " + f1);
    }
}
