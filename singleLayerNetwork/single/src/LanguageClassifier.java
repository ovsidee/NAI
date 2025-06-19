import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LanguageClassifier {

    public static final int INPUT_SIZE = 26;
    public static final String[] LANGUAGES = {"English", "German", "Polish", "Spanish"};

    public static List<DataSample> loadData(String filepath) throws IOException {
        List<DataSample> data = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filepath));

        for (String line : lines) {
            String[] parts = parseCSVLine(line);
            if (parts.length != 2) continue;
            data.add(new DataSample(parts[0], parts[1]));
        }
        return data;
    }

    public static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }

    public static double[] preprocess(String text) {
        double[] vector = new double[INPUT_SIZE];
        text = text.toLowerCase();

        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                vector[c - 'a'] += 1.0;
            }
        }

        double norm = 0.0;
        for (double v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);

        if (norm > 0) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= norm;
            }
        }

        return vector;
    }
}
