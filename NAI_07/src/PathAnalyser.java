import java.io.IOException;
import java.util.List;

public class PathAnalyser {

    private List<List<Integer>> distances;

    public PathAnalyser(List<List<Integer>> distances) {
        this.distances = distances;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("NO DATA FILE SPECIFIED");
            System.exit(1);
        }
        PathAnalyser pathAnalyser = null;
        try {
            pathAnalyser = new PathAnalyser(TSP_Parser.parseDistances(args[0]));
        } catch (IOException e) {
            System.err.println("PROBLEM WITH READING DATA FROM THE FILE");
            System.exit(1);
        }
        Solution optimalPath = pathAnalyser.findOptimalPath();
        System.out.println("\noptimal path = " + optimalPath);
        System.out.println("optimal length = " + pathAnalyser.calculatePathLength(optimalPath));
    }

    private Solution findOptimalPath() {
//        Solution globalOptimal = Solution.getInstance(distances);
        Solution globalOptimal = Solution.getInstance(distances, false);
        Solution localOptimal = globalOptimal;
        int globalOptimalLength = calculatePathLength(globalOptimal);
        int localOptimalLength = globalOptimalLength;

        System.out.println("initial path = " + globalOptimal);
        System.out.println("initial length = " + globalOptimalLength + "\n");

        double temp = 100000;
        while (temp > 0) {
            List<Solution> neighbours = localOptimal.generateNeighbours();

            for (Solution neighbour : neighbours) {
                int currLength = calculatePathLength(neighbour);
                if (currLength < localOptimalLength) {
                    localOptimal = neighbour;
                    localOptimalLength = currLength;
                    System.out.println("upd local length = " + localOptimalLength);
                    System.out.println("upd local path   = " + localOptimal);
                } else if (worthGoingLower(localOptimal, neighbour, temp)) {
                    localOptimal = neighbour;
                    localOptimalLength = currLength;
                    System.out.println("upd local length = " + localOptimalLength);
                    System.out.println("upd local path   = " + localOptimal);
                }
                if (localOptimalLength < globalOptimalLength) {
                    globalOptimal = localOptimal;
                    globalOptimalLength = localOptimalLength;
                    System.out.println("upd global length = " + globalOptimalLength);
                    System.out.println("upd global path   = " + globalOptimal);
                }
            }
            temp -= 0.5;
        }
        return globalOptimal;
    }

    private boolean worthGoingLower(Solution curr, Solution neighbour, double temp) {
        double probability = Math.pow(Math.E, -((calculatePathLength(curr) - calculatePathLength(neighbour)) / temp));
//        System.out.println("probability = " + probability);
//        System.out.println((calculatePathLength(curr) - calculatePathLength(neighbour)) + " " + temp);
        return probability > 2;
    }

    private int calculatePathLength(Solution solution) {
        int result = 0;
        for (int i = 0; i < solution.getRouteList().size(); i++) {
            if (i == solution.getRouteList().size() - 1) {
                result += distances.get(solution.getRouteList().get(i)).get(0);
            } else {
                result += distances.get(solution.getRouteList().get(i)).get(solution.getRouteList().get(i + 1));
            }
        }
        return result;
    }

}
