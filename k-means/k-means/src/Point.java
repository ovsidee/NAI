public class Point {
    private double[] vector;
    private String name;

    public Point(double[] vector, String name) {
        this.vector = vector;
        this.name = name;
    }


    public double[] getVector(){
        return vector;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        String res = "[";
        for(int i = 0; i < vector.length; i++){
            res += vector[i];
            res = i < vector.length-1 ? res+= ",": res+"]";
        }
        res += " " + name;
        return res;
    }
}
