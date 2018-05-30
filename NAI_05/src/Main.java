import java.util.List;

public class Main {

    // TODO: add progress bar
    // TODO: multithreading

    public static Solution bruteforce(List<Item> items, double capacity) {
        int optimalVector = -1;
        double optimalVal = 0;
        double optimalWeight = 0;

        for (int i = 0; i < Math.pow(2, items.size()); i++) {
            double currVal = 0;
            double currWeight = 0;

            for (int j = 0; j < items.size(); j++) {
                if((1 << j & i) != 0) {
                    currVal += items.get(j).getValue();
                    currWeight += items.get(j).getWeight();
                }
            }
            if(currWeight <= capacity && currVal > optimalVal) {
                optimalVector = i;
                optimalVal = currVal;
                optimalWeight = currWeight;
            }
        }
        return new Solution(optimalVector, optimalVal, optimalWeight);
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("NO DATA FILE PATH SPECIFIED");
            System.exit(1);
        }
        String path = args[0];

        double capacity = FileParser.parseCapacity(path);
        List<Item> items = FileParser.parseItems(path);

        long timeBefore = System.currentTimeMillis();
        Solution optimalSolution = bruteforce(items, capacity);
        long timeAfter = System.currentTimeMillis();

        System.out.println(optimalSolution);
        System.out.println(toTimeString(timeBefore, timeAfter));
    }

    private static String toTimeString(long timeBefore, long timeAfter) {
        long timeDiff = timeAfter - timeBefore;

        long minutes = (timeDiff / (1000 * 60)) % 60;
        long seconds = (timeDiff / 1000) % 60;
        long milliseconds = timeDiff % 1000;

        return String.format("%02d:%02d:%d", minutes, seconds, milliseconds);
    }

}
