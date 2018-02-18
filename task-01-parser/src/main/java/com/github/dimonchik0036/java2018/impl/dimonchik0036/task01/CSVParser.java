/*
 * Task 01 - Parser
 * NSU Nsk Java 2018
 * Created by dimonchik0036 on 2018/02/08
 */

package main.java.com.github.dimonchik0036.java2018.impl.dimonchik0036.task01;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;


public class  CSVParser {
    private final Scanner scanner;

    public CSVParser(final Readable input) {
        Pattern pattern = Pattern.compile("[^\\p{L}\\d]+");
        scanner = new Scanner(input);
        scanner.useDelimiter(pattern);
    }

    public Map<String, Integer> calculateTheFrequency() {
        Map<String, Integer> map = new HashMap<>();
        scanner.forEachRemaining(string -> map.merge(string, 1, (a, b) -> a + b));
        return map;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.print("Usage: <filename>");
            System.exit(1);
        }

        CSVParser parser = null;
        try {
            parser = new CSVParser(new BufferedReader(new FileReader(args[0])));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Map<String, Integer> frequency = parser.calculateTheFrequency();
        frequency.forEach((k, v) -> System.out.println(k + " -> " + v + " " + (double) v / frequency.size() * 100 + "%"));
    }
}
