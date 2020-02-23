package com.wordup;

import com.wordup.utils.FileUtils;

import java.nio.file.Paths;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.wordup.utils.FileUtils.convertFileToStringList;
import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
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

            // only keep the words with all data present
            if (!definition.isEmpty() && !synonyms.isEmpty() && !examples.isEmpty()) {
                sb.append(word).append(delim)
                        .append(definition).append(delim)
                        .append(join(listDelim, synonyms)).append(delim)
                        .append(join(listDelim, examples)).append(delimEndOfLine);
            }

        }

        FileUtils.write(sb.toString(), Paths.get(OUTPUT_PATH + "preparedDict.txt"));
    }

    /**
     * Given the below input example:
     *
     * @param line: 02738211 43 v 02 shine 0 beam 3 | emit light; be bright, as of the sun or a light; "The sun shone bright that day";
     * @return substring the 5th space-divided element
     */
    private static String extractWord(String line) {
        return line.split("\\s")[4];
    }

    /**
     * Given the below input example:
     * 02738211 43 v 02 shine 0 beam 3 | emit light; be bright, as of the sun or a light; "The sun shone bright that day";
     * <p>
     * 1) Get the 4th space-divided element N (02) - this is how many total synonyms we can expect in this line
     * 2) If it's N < 2 - this means no synonyms, so we skip
     * 3) Otherwise get N-1 elements starting from the first synonym (5th element) that we use as the base word
     */
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
        List<String> syns = asList(copyOfRange(splitLine, firstPossibleSynonymIndex, lastPossibleSynonymIndex));

        return syns.stream() // e.g. 0 word1 0 word2 0 word3
                .filter(s -> s.length() > 1) // hackily remove digits
                .map(s -> s.replaceAll("_", " ")) // some words are like_that
                .collect(toList());
    }

    /**
     * Given the below input example:
     *
     * @param line: 02738211 43 v 02 shine 0 beam 3 | emit light; be bright, as of the sun or a light; "The sun shone bright that day";
     * @return substring between '|' and the first occurrence of ';'
     */
    private static String extractDefinition(String line) {
        String half = line.split("\\|")[1].trim();
        return half.substring(0, half.indexOf(';'));
    }

    /**
     * Given the below input example:
     *
     * @param line: 02738211 43 v 02 shine 0 beam 3 | emit light; be bright, as of the sun or a light; "The sun shone bright that day";
     * @return substring all text between quotes, e.g. "example 1" and "example 2"
     */
    private static List<String> extractExamples(String line) {
        return Pattern.compile(TEXT_IN_QUOTES_REGEX)
                .matcher(line)
                .results()
                .map(MatchResult::group)
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
}
