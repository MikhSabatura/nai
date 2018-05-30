public abstract class X{

int bruteforce(X[] arr, double capacity) {
    double maxVal = 0;
    int maxValVector;

    for (int i = 0; i < Math.pow(2, arr.length); i++) {
        double currVal = 0;
        double currWeight = 0;
        for (int j = i, pow = arr.length - 1; pow >= 0; j /= 2, pow--) {
            if(j % 2 != 0) {
                currVal += value(arr[pow]);
                currWeight += weight(arr[pow]);
            }
        }
        if(currWeight <= capacity && currVal > maxVal) {
            maxVal = currVal;
            maxValVector = i;
        }
    }
    return maxValVector;
}

abstract double value(X x);
abstract double weight(X x);

}

