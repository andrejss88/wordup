package com.wordup;

import com.wordup.utils.FileUtils;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.wordup.utils.FileUtils.convertFileToStringList;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

public class ExtractWordMeanings {

    private static final String PATH = "data-prep/src/main/resources/";
    private static final String OUTPUT_PATH = PATH + "output/";

    private static final String TEXT_IN_QUOTES_REGEX = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1";
    private static final List<String> EMPTY_LIST = List.of();

    public static void main(String[] args) {

        List<String> rawContent = convertFileToStringList(PATH + "input/input_definitions_verbs.txt");
        StringBuilder sb = new StringBuilder();
        String delim = ";";
        String delimEndOfLine = delim + "\n";
        String listDelim = ",";

        for (String line : rawContent) {
            String word = extractWord(line);
            String definition = extractDefinition(line);
            List<String> synonyms = extractSynonyms(line);
            List<String> examples = extractExamples(line);

            // we only want words with all data present
            if (!definition.isEmpty() && !synonyms.isEmpty() && !examples.isEmpty()) {
                sb.append(word).append(delim)
                        .append(definition).append(delim)
                        .append(join(listDelim, synonyms)).append(delim)
                        .append(join(listDelim, examples)).append(delimEndOfLine);
            }

        }

        FileUtils.write(sb.toString(), Paths.get(OUTPUT_PATH + "preparedDict.txt"));
    }

    private static List<String> extractSynonyms(String line) {
        String[] splitLine = line.split("\\s");
        String synonymNum = splitLine[3];
        int synonyms;

        if (!isNumeric(synonymNum)) {
            return EMPTY_LIST;
        }

        synonyms = Integer.parseInt(synonymNum);

        if (synonyms < 2) {
            return EMPTY_LIST;
        }


        int firstPossibleSynonymIndex = 6;
        int numOfSynonyms = synonyms - 1;
        int lastPossibleSynonymIndex = firstPossibleSynonymIndex + numOfSynonyms * 2 - 1;
        List<String> syns = Arrays.asList(Arrays.copyOfRange(splitLine, firstPossibleSynonymIndex, lastPossibleSynonymIndex));

        return syns.stream()
                .filter(s -> s.length() > 1) // hackily remove digits
                .map(s -> s.replaceAll("_", " "))
                .collect(toList());
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String extractDefinition(String line) {
        String half = line.split("\\|")[1].trim();
        return half.substring(0, half.indexOf(';'));
    }

    private static List<String> extractExamples(String line) {
        return extractSubstring(line, TEXT_IN_QUOTES_REGEX);
    }

    private static List<String> extractSubstring(String line, String regex) {
        return Pattern.compile(regex)
                .matcher(line)
                .results()
                .map(MatchResult::group)
                .collect(toList());
    }

    private static String extractWord(String line) {
        return line.split("\\s")[4];
    }
}
