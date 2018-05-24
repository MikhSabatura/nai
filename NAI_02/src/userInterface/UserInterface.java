package userInterface;

//todo 2: change all io.file to nio.path
//todo 3: rename methods

import analysers.LangAnalyser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInterface {

    private static final String FILE_MODE = "-f";
    private static final String STRING_MODE = "-s";

    private static final Pattern USER_INPUT_PATTERN = Pattern.compile("(" + FILE_MODE + "|" + STRING_MODE + ")\\s+((\\S+\\s*)+)");

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("ILLEGAL ARGUMENTS");
            System.exit(1);
        }
        String trainDirPath = args[0];
        double maxError = 0;
        try {
            maxError = Double.parseDouble(args[1]);
            if (maxError <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.err.println("ILLEGAL MAX ERROR VALUE, MUST BE A POSITIVE NUMBER");
        }
        double learnCoef = 0;
        try {
            learnCoef = Double.parseDouble(args[2]);
            if (learnCoef <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.err.println("ILLEGAL LEARNING COEFFICIENT VALUE, MUST BE A POSITIVE NUMBER");
            System.exit(1);
        }

        LangAnalyser analyser = new LangAnalyser(trainDirPath);
        System.out.println("Training network...");
        try {
            analyser.trainPerceptrons(maxError, learnCoef);
        } catch (IOException e) {
            System.err.println("ERROR WHEN TRAINING NETWORK");
            System.exit(1);
        }
        System.out.println("Finished training");
        processUserInput(analyser);
    }

    private static void processUserInput(LangAnalyser analyser) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.println("\nUsage: " + FILE_MODE + " [file name], " + STRING_MODE + " [input string], press 'Enter' to exit");
            input = scanner.nextLine();
            input = input.trim();
            if (input.isEmpty()) {
                break;
            }
            Matcher matcher = USER_INPUT_PATTERN.matcher(input);
            if (!matcher.matches()) {
                System.out.println("Illegal input\n");
                continue;
            }
            processInputResult(analyser, matcher.group(1), matcher.group(2));
        }
    }

    private static void processInputResult(LangAnalyser analyser, String mode, String arg) {
        if (mode.equals(FILE_MODE)) {
            File file = new File(arg);
            if (!file.exists()) {
                System.out.println("File not found: " + file.getAbsolutePath());
                return;
            }
            if (file.isDirectory()) {
                System.out.println(file.getAbsolutePath() + " is a directory");
                return;
            }
            System.out.println("Language: " + analyser.analyseLanguage(file).toUpperCase());
        } else if (mode.equals(STRING_MODE)) {
            System.out.println("Language: " + analyser.analyseLanguage(arg).toUpperCase());
        }
    }

}
