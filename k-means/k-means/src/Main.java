import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String path ="F:\\githubAllRepositories\\NAI\\k-means\\k-means\\src\\";
        Scanner sc = new Scanner(System.in);

        System.out.println("Input the test set name:");
        String fileName = sc.nextLine();

        System.out.println("Enter the k value:");
        int k = sc.nextInt();

        path+=fileName;

        List<Point> points = DataLoader.load(path);


        initializeKMeans(points, k, 100);
    }

    //Setting centroids randomly
    public static List<double[]> initializeCentroids(int k, List<Point> points) {
        List<double[]> centroids = new ArrayList<>();

        // Create list of indices and shuffle it
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        // Select first k shuffled indices as initial centroids
        for (int i = 0; i < k; i++) {
            centroids.add(Arrays.copyOf(
                    points.get(indices.get(i)).getVector(),
                    points.get(0).getVector().length)
            );
        }

        return centroids;
    }

    public static double[] calculateCentroid(List<Point> cluster){
        int dimension = cluster.get(0).getVector().length;
        double[] centroid = new double[dimension];


        for(Point point : cluster){
            double vector[] = point.getVector();

            for(int i = 0; i < dimension; i++){
                centroid[i] += vector[i];
            }
        }

        for(int i = 0; i < dimension; i++){
            centroid[i] /= cluster.size(); // calculates average of each centroid
        }

        return centroid;
    }

    public static double calculateEucildianDistance(double[] a, double[] b){
        double sum = 0.0;
        for(int i = 0; i < a.length; i++){
            sum += Math.pow(a[i] - b[i], 2);
        }

        return Math.sqrt(sum);
    }

    public static void initializeKMeans(List<Point> points, int k, int iterations){
        //initializing random centroids
        List<double[]> centroids = initializeCentroids(k, points);

        Map<Integer, List<Point>> clusters = new HashMap<>();
        double prevDistanceSum = Double.MAX_VALUE;

        for(int iteration = 1; iteration <= iterations; iteration++){

            clusters.clear(); // clearing old cluster assignments
            for (int i = 0; i < k; i++) clusters.put(i, new ArrayList<>()); // k empty clusters


            //Assignment step. Assigning each point to nearest centroid by calculating destances
            for(Point point : points){

                int closedCentroids = 0;

                double midDistance = calculateEucildianDistance(point.getVector(), centroids.get(0));

                for(int i = 0; i < k; i++){
                    double dist = calculateEucildianDistance(point.getVector(), centroids.get(i));

                    if(dist < midDistance){
                        midDistance = dist;
                        closedCentroids = i;
                    }
                }

                clusters.get(closedCentroids).add(point);
            }

            //Update centroids after assignment
            for(int i = 0; i < k; i++){

                if(!clusters.get(i).isEmpty()){
                    centroids.set(i, calculateCentroid(clusters.get(i))); // updating a centroid
                }
            }

            //Compute total distance
            double distanceSum = 0.0;
            for(int i = 0; i < k; i++){
                for(Point point : clusters.get(i)){
                    distanceSum += calculateEucildianDistance(point.getVector(), centroids.get(i)); // gets tighter with each iteration
                }
            }

            System.out.println("Iteration " + iteration + ", distanceSum: " + distanceSum);

            //convergence check
            if(Math.abs(prevDistanceSum - distanceSum) < 1e-2) {
                System.out.println("Converged at iteration: " + iteration);
                break;
            }
            prevDistanceSum = distanceSum;
        }

        // Final cluster output:
        for (int i = 0; i < k; i++) {
            System.out.println("\nCluster " + i + ":");
            for (Point p : clusters.get(i)) {
                System.out.println(p);
            }
        }

        // Homogenity calculation
        System.out.println("\nCluster homogeneity (label counts):");
        for (int i = 0; i < k; i++) {
            Map<String, Integer> labelCounts = new HashMap<>();
            for (Point p : clusters.get(i)) {
                labelCounts.put(p.getName(), labelCounts.getOrDefault(p.getName(), 0) + 1);
            }
            System.out.println("Cluster " + i + ": " + labelCounts);
        }
    }
}