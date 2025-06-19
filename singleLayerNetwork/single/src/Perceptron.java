import java.util.Random;

public class Perceptron {
    double[][] weights;
    double lr;

    public Perceptron(int inputSize, int outputSize) {
        weights = new double[outputSize][inputSize];
        Random rand = new Random();
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = rand.nextDouble();
            }
        }
        lr = 1.0;
    }

    public int predict(double[] x) {
        double[] activations = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            activations[i] = dotProduct(weights[i], x);
        }

        int maxIndex = 0;
        for (int i = 1; i < activations.length; i++) {
            if (activations[i] > activations[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public void train(double[][] X, int[] y, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < X.length; i++) {
                int pred = predict(X[i]);
                if (pred != y[i]) {
                    // Update weights
                    for (int j = 0; j < weights[0].length; j++) {
                        weights[y[i]][j] += lr * X[i][j];
                        weights[pred][j] -= lr * X[i][j];
                    }
                }
            }
        }
    }

    public double dotProduct(double[] a, double[] b) {
        double result = 0.0;
        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }
}
