import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Mushroom> train = NaiveBayesClassifier.loadData("data/agaricus-lepiota.data");
        List<Mushroom> test = NaiveBayesClassifier.loadData("data/agaricus-lepiota.test.data");

        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.train(train);

        List<String> predictions = new ArrayList<>();
        List<String> actuals = new ArrayList<>();

        for (Mushroom m : test) {
            predictions.add(classifier.predict(m));
            actuals.add(m.label);
        }

        NaiveBayesClassifier.evaluate(predictions, actuals);
    }
}
