package com.calliecirillo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        Scanner inputScanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter words bank file path ex: src/com/calliecirillo/fiveLetterWords.txt");
        LinkedHashMap<String, Double> fiveLetterWords = buildWordsMap(inputScanner.nextLine());

        // If letter is uppercase, that means it is a used letter in the correct position
        // If letter is lowercase, that means it is a used letter in the incorrect position
        // If the letter is ., that means incorrect letter in incorrect position
        // ex guess .R..o

        boolean solved = false;
        ArrayList<String> availableAlphabet = buildAlphabet();

        System.out.println("Ready for next Wordle round. I would suggest guessing " +
                fiveLetterWords.entrySet().iterator().next());

        while(!solved) {
            System.out.println("What word did you guess?");
            String guess = inputScanner.nextLine();
            System.out.println("What was the result of your guess?\n"+
                    "For the guess donut, Please use example format .o..T\n" +
                    "- uppercase letter indicates a correct letter in the correct position,\n"+
                    "- lowercase letter indicates a correct letter in the incorrect position,\n "+
                    "- period indicates an incorrect letter guessed.");
            String guessResult = inputScanner.nextLine();
            boolean guessed = true;
            for (int i = 0; i<guessResult.length(); i++) {
                char charResult = guessResult.charAt(i);
                if(Character.isLowerCase(charResult) || charResult == '.') {
                    guessed = false;
                }
            }
            solved = guessed;
            if(!solved && guess.length() == 5 && guessResult.length() == 5) {
                for(int i = 0; i<guessResult.length(); i++) {
                    int finalI = i;
                    char charResult = guessResult.charAt(i);
                    String guessedLetter = String.valueOf(guess.charAt(i));
                    ArrayList<String> wordsForRemoval = new ArrayList<>();
                    if(charResult == '.') {
                        fiveLetterWords.forEach((word, value) -> {
                            if(word.contains(guessedLetter)) {
                                wordsForRemoval.add(word);
                            }
                        });
                    }
                    if(Character.isUpperCase(charResult)) {
                        fiveLetterWords.forEach((word, value) -> {
                            if(!word.contains(guessedLetter) || word.indexOf(guessedLetter) != finalI) {
                                wordsForRemoval.add(word);
                            }
                        });
                    }
                    if(Character.isLowerCase(charResult)) {
                        fiveLetterWords.forEach((word, value) -> {
                            if(!word.contains(guessedLetter) || word.indexOf(guessedLetter) == finalI) {
                                wordsForRemoval.add(word);
                            }
                        });
                    }
                    for(String word : wordsForRemoval ) {
                        fiveLetterWords.remove(word);
                    }
                    availableAlphabet.remove(guessedLetter);
                }

                if(!fiveLetterWords.isEmpty()) {
                    System.out.println("OK. My next suggestion is: "+ fiveLetterWords.entrySet().iterator().next());
                } else {
                    System.out.println("Sorry, ran out of words");
                    System.exit(0);
                }
            } else {
                System.out.println("Yay! We won!");
            }
        }

    }

    private static LinkedHashMap<String, Double> buildWordsMap(String filePath) {
        LinkedHashMap<String, Double> fiveLetterWordsWithScore = new LinkedHashMap<String, Double>();
        File file = new File(filePath);
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            return fiveLetterWordsWithScore;
        }
        while(fileScanner.hasNextLine()) {
            String nextWord = fileScanner.nextLine();
            fiveLetterWordsWithScore.put(nextWord, getLetterFrequencyScore(nextWord));
        }
        return fiveLetterWordsWithScore.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, LinkedHashMap::new));


    }

    private static Double getLetterFrequencyScore(String word) {
        // letter frequency values source:
        // http://pi.math.cornell.edu/~mec/2003-2004/cryptography/subs/frequencies.html
        Double wordScore = 0.0;
        String lowercaseWord = word.toLowerCase(Locale.US);
        String stringToScore = removeDuplicateLettersFromWord(lowercaseWord);

        for(int i = 0; i < stringToScore.length(); i++) {
            char currentChar = stringToScore.charAt(i);
            switch(currentChar) {
                case 'e':
                    wordScore += 12.02;
                    break;
                case 't':
                    wordScore += 9.10;
                    break;
                case 'a':
                    wordScore += 8.12;
                    break;
                case 'o':
                    wordScore += 7.68;
                    break;
                case 'i':
                    wordScore += 7.31;
                    break;
                case 'n':
                    wordScore += 6.95;
                    break;
                case 's':
                    wordScore += 6.28;
                    break;
                case 'r':
                    wordScore += 6.02;
                    break;
                case 'h':
                    wordScore += 5.92;
                    break;
                case 'd':
                    wordScore += 4.32;
                    break;
                case 'l':
                    wordScore += 3.98;
                    break;
                case 'u':
                    wordScore += 2.88;
                    break;
                case 'c':
                    wordScore += 2.71;
                    break;
                case 'm':
                    wordScore += 2.61;
                    break;
                case 'f':
                    wordScore += 2.30;
                    break;
                case 'y':
                    wordScore += 2.11;
                    break;
                case 'w':
                    wordScore += 2.09;
                    break;
                case 'g':
                    wordScore += 2.03;
                    break;
                case 'p':
                    wordScore += 1.82;
                    break;
                case 'b':
                    wordScore += 1.49;
                    break;
                case 'v':
                    wordScore += 1.11;
                    break;
                case 'k':
                    wordScore += 0.69;
                    break;
                case 'x':
                    wordScore += 0.17;
                    break;
                case 'q':
                    wordScore += 0.11;
                    break;
                case 'j':
                    wordScore += 0.10;
                    break;
                case 'z':
                    wordScore += 0.07;
                    break;
                default:
                    throw new RuntimeException("found illegal char " + word.charAt(i));

            }
        }
        return wordScore;
    }

    private static String removeDuplicateLettersFromWord(String word) {
        StringBuilder wordNoDuplicates = new StringBuilder();
        for(int i = 0; i<word.length(); i++) {
            String testLetter = String.valueOf(word.charAt(i));
            if(!wordNoDuplicates.toString().contains(testLetter)) {
                wordNoDuplicates.append(testLetter);
            }
        }
        return wordNoDuplicates.toString();
    }

    private static ArrayList<String> buildAlphabet() {
        ArrayList<String> alphabetArrayList = new ArrayList<>();

        for(char alphabet = 'a'; alphabet <='z'; alphabet++ ) {
            alphabetArrayList.add(Character.toString(alphabet));
        }

        return alphabetArrayList;

    }
}
