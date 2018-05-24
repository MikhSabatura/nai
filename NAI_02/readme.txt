
Training directory contains subdirectories named according to the language of files it contains
Each subdirectory in contains 11 files

// pseudocode:

// uses unipolar sigmoid function
Perceptron {

    double computeOutput() {
        //unipolar
        //f(x) = 1 / (1 + e^(-net))
    }

    void updateWeights() {
        for(int i = 0; i < w.length; i++) {
            w[i] = w[i] +η(d−y)y(1−y)x[i]
        }
        //w - weights
        //x - inputs
        //d - desired output
        //y - actual output
        //η - learning coefficient
    }

}

LangAnalyser {

    List<Perceptron> perceptrons;

    int ACTIVATED = 1;
    int NOT_ACTIVATED = 0;

    initializePerceptrons(File trainDir) {
        for(Dir subDir in trainDir) {
            Perceptron p = new Perceptron();
            p.type = subDir.name();
        }
    }

    trainPerceptrons(double maxError, double learnCoef) {
        while(true) {
            calculate output of each perceptron for each file
            if (perceptronOutput != desiredOutput) {
                calculate the error
            }
            accumulate the error
            if(error < maxError) {
                break;
            }
        }
    }

    analyzeLanguage(File f) {
        return getMostActivatedPerceptron(calculateLetterFrequency(f)).type;
    }

}

