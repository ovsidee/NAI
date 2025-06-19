import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Provide k-number: ");
        int k = scanner.nextInt();

        System.out.println("Provide training data: (filename)");
        String trainFile = scanner.next();
//        String trainFilePath = "k-NN/dataFiles/" + trainFile;
        String trainFilePath = "C:\\Users\\Vital\\Desktop\\NAI\\k-NN project\\k-NN\\dataFiles\\iris.data";


        System.out.println("Provide test set data: (filename)");
        String testFile = scanner.next();
//        String testFilePath = "k-NN/dataFiles/" + testFile;
        String testFilePath = "C:\\Users\\Vital\\Desktop\\NAI\\k-NN project\\k-NN\\dataFiles\\iris.test.data";

        KNNClassifier knn = new KNNClassifier(k);
        knn.loadTrainingData(trainFilePath);

        double accuracy = knn.evaluate(testFilePath);
        System.out.println("Accuracy: " + accuracy * 100 + "%");
        System.out.println("Enter features (comma-separated) or 'exit' to quit:");
        while (true) {
            String input = scanner.next();

            if (input.equalsIgnoreCase("exit")) break;
            if (input.isEmpty()) {
                System.out.println("Error: Empty input. Try again.");
                continue;
            }

            String[] values = input.split(",");
            double[] features = new double[values.length];
            try {
                for (int i = 0; i < features.length; i++) {
                    features[i] = Double.parseDouble(values[i].trim());
                }

                DataPoint userPoint = new DataPoint(features, "");
                System.out.println("Predicted class: " + knn.classify(userPoint));
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Make sure to enter valid numeric values.");
            }
        }

        scanner.close();
    }
}