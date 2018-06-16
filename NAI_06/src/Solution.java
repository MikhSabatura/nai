import java.util.*;

public class Solution {

    private List<Integer> routeList;

    private Solution(List<Integer> routeList) {
        this.routeList = routeList;
    }

    public static Solution getInstance(List<List<Integer>> distances) {
        List<Integer> solutionList = new ArrayList<>();
        for (int i = 0; i < distances.size(); i++) {
            solutionList.add(i);
        }
        return new Solution(solutionList);
    }

    public static Solution getInstance(List<List<Integer>> distances, boolean quatilyCode) {
        List<Integer> temp = new ArrayList<>(distances.size());
        for (int i = 0; i < distances.size(); i++) {
            temp.add(i);
        }
        List<Integer> solutionList = new ArrayList<>(distances.size());
        Random random = new Random();
        while (!temp.isEmpty()) {
            int indx = random.nextInt(temp.size());
            solutionList.add(temp.remove(indx));
        }
        return new Solution(solutionList);
    }

    public List<Solution> generateNeighbours() {
        List<Solution> result = new ArrayList<>();

        for (int i = 0; i < routeList.size(); i++) {
            Solution neighbour = new Solution(new ArrayList<>(routeList));
            if (i == routeList.size() - 1) {
                neighbour.routeList.set(0, routeList.get(i));
                neighbour.routeList.set(i, routeList.get(0));
            } else {
                neighbour.routeList.set(i + 1, routeList.get(i));
                neighbour.routeList.set(i, routeList.get(i + 1));
            }
            result.add(neighbour);
        }
        return result;
    }

    public List<Integer> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Integer> routeList) {
        this.routeList = routeList;
    }

    @Override
    public String toString() {
        return routeList.toString();
    }
}
