import java.io.IOException;
import java.util.List;
import java.util.Random;

public class PathAnalyser {

    private List<List<Integer>> distances;

    private static final double INITIAL_TEMPERATURE = 10000;
    private static final double COOLING_RATE = 0.0001;
    private static final double STOP_CONDITION = 100;


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

        double temp = INITIAL_TEMPERATURE;
        Random random = new Random();
        int notMovingCount = 0;
        while (notMovingCount < STOP_CONDITION) {
            List<Solution> neighbours = localOptimal.generateNeighbours();
            Solution current = neighbours.get(random.nextInt(neighbours.size()));
            int currLength = calculatePathLength(current);
            if (currLength < localOptimalLength) {
                notMovingCount = 0;
                localOptimal = current;
                localOptimalLength = currLength;
                System.out.println("-------- going lower --------");
                System.out.println("upd local length = " + localOptimalLength);
                System.out.println("upd local path   = " + localOptimal);
            } else if (currLength > localOptimalLength && worthGoingLower(localOptimal, current, temp)) {
                notMovingCount = 0;
                localOptimal = current;
                localOptimalLength = currLength;
                System.out.println("-------- going higher ---------");
                System.out.println("upd local length = " + localOptimalLength);
                System.out.println("upd local path   = " + localOptimal);
            } else {
                notMovingCount++;
            }
            if (localOptimalLength < globalOptimalLength) {
                globalOptimal = localOptimal;
                globalOptimalLength = localOptimalLength;
                System.out.println("upd global length = " + globalOptimalLength);
                System.out.println("upd global path   = " + globalOptimal);
            }
            temp *= (1-COOLING_RATE);
        }
        return globalOptimal;
    }

    private boolean worthGoingLower(Solution curr, Solution neighbour, double temp) {
        double power = -(Math.abs(calculatePathLength(curr) - calculatePathLength(neighbour)) / temp);
        double probability = Math.exp(power);
        return probability >= Math.random();
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
