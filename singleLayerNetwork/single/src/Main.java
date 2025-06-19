import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        List<DataSample> trainData = LanguageClassifier.loadData("data/lang.train.csv");
        List<DataSample> testData = LanguageClassifier.loadData("data/lang.test.csv");

        double[][] X_train = new double[trainData.size()][LanguageClassifier.INPUT_SIZE];
        int[] y_train = new int[trainData.size()];
        for (int i = 0; i < trainData.size(); i++) {
            X_train[i] = LanguageClassifier.preprocess(trainData.get(i).text);
            y_train[i] = Arrays.asList(LanguageClassifier.LANGUAGES).indexOf(trainData.get(i).lang);
        }

        double[][] X_test = new double[testData.size()][LanguageClassifier.INPUT_SIZE];
        int[] y_test = new int[testData.size()];
        for (int i = 0; i < testData.size(); i++) {
            X_test[i] = LanguageClassifier.preprocess(testData.get(i).text);
            y_test[i] = Arrays.asList(LanguageClassifier.LANGUAGES).indexOf(testData.get(i).lang);
        }

        Perceptron perceptron = new Perceptron(LanguageClassifier.INPUT_SIZE, LanguageClassifier.LANGUAGES.length);
        perceptron.train(X_train, y_train, 200);

        int correct = 0;
        for (int i = 0; i < X_test.length; i++) {
            int pred = perceptron.predict(X_test[i]);
            if (pred == y_test[i]) correct++;
        }

        System.out.printf("Test accuracy: %.2f%%%n", (correct * 100.0) / y_test.length);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter text to classify (or 'exit'): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;
            double[] vector = LanguageClassifier.preprocess(input);
            int langPred = perceptron.predict(vector);
            System.out.println("Predicted language: " + LanguageClassifier.LANGUAGES[langPred]);
        }
    }
}
