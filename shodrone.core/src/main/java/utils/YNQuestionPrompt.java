package utils;

import eapli.framework.io.util.Console;

public class YNQuestionPrompt {

    public static boolean continueQuestion(String question) {
        String awsner;
        while (true) {
            awsner = Console.readNonEmptyLine(question,
                    ":(Y/N)");

            if (awsner.equalsIgnoreCase("N")) {
                return false;
            } else if (awsner.equalsIgnoreCase("Y")) {
                return true;
            }else{
                System.out.println("Invalid input. Please enter Y or N.");
            }
        }
    }

}
