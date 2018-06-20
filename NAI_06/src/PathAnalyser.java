import java.io.IOException;
import java.util.List;

public class PathAnalyser  {


    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("NO DATA FILE SPECIFIED");
            System.exit(1);
        }
        List<List<Integer>> distances = null;
        try {
            distances = TSP_Parser.parseDistances(args[0]);
        } catch (IOException e) {
            System.err.println("PROBLEM WITH READING DATA FROM THE FILE");
            System.exit(1);
        }
        Solution optimalPath = findOptimalPath(distances);
        System.out.println("\noptimal path = " + optimalPath);
        System.out.println("optimal length = " + calculatePathLength(distances, optimalPath));
    }

    private static Solution findOptimalPath(List<List<Integer>> distances) {
//        Solution optimal = Solution.getInstance(distances);
        Solution optimal = Solution.getRandomInstance(distances);
        int optimalLength = calculatePathLength(distances, optimal);

        System.out.println("initial path = " + optimal);
        System.out.println("initial length = " + optimalLength + "\n");

        while(true) {
            List<Solution> neighbours = optimal.generateNeighbours();
            Solution newOptimal = optimal;
            int newOptimalLength = optimalLength;

            for (Solution neighbour : neighbours) {
                int currLength = calculatePathLength(distances, neighbour);
                if(currLength < optimalLength) {
                    newOptimal = neighbour;
                    newOptimalLength = currLength;
                }
            }
            if(optimalLength == newOptimalLength) {
                break;
            } else {
                optimal = newOptimal;
                optimalLength = newOptimalLength;
                System.out.println("upd length = " + optimalLength);
                System.out.println("upd path   = " + optimal);
            }
        }
        return optimal;
    }

    private static int calculatePathLength(List<List<Integer>> distances, Solution solution) {
        int result = 0;
        for (int i = 0; i < solution.getRouteList().size(); i++) {
            if(i == solution.getRouteList().size() - 1) {
                result += distances.get(solution.getRouteList().get(i)).get(0);
            } else {
                result += distances.get(solution.getRouteList().get(i)).get(solution.getRouteList().get(i + 1));
            }
        }
        return result;
    }

}
