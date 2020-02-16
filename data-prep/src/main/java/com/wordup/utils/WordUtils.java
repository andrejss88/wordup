package com.wordup.utils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isUpperCase;
import static java.util.stream.Collectors.toList;

public class WordUtils {
    /**
     * Remove plural forms of the same noun(i.e. if there are "gift" and "gifts" - keep only "gift")
     * Remove declined versions of the same verb (i.e. if there are "use" , "uses" and "using" -  keep only "use")
     *
     * @param list to process and remove duplicate words from
     * @return list with filtered out words
     */
    public static List<String> doSimpleStem(List<String> list) {

        // verbs
        List<String> stemmed1 = removeWordWithEnding(list, "s");
        List<String> stemmed2 = removeWordWithEnding(stemmed1, "ing");
        List<String> stemmed3 = removeWordWithEnding(stemmed2, "d");
        List<String> stemmed4 = removeWordWithEnding(stemmed3, "ed");

        // comparatives
        List<String> stemmed5 = removeWordWithEnding(stemmed4, "er");
        List<String> stemmed6 = removeWordWithEnding(stemmed5, "est");

        // adver forms
        List<String> stemmed7 = removeWordWithEnding(stemmed6, "ly");

        return stemmed7;
    }

    private static List<String> removeWordWithEnding(List<String> inputList, String ending) {
        List<String> result = new ArrayList<>(inputList);

        inputList.stream()
                .filter(word -> !word.endsWith(ending))
                .map(word -> word + ending)
                .forEach(result::remove);

        return result;
    }

    /**
     * Sanitize the word list by removing words we don't want
     *
     * @param content
     * @return
     */
    public static List<String> sanitize(List<String> content) {
        return content.stream()
                // non-alphabeticals - could be a regex, but let's keep it simple(r)
                .filter(s -> !s.contains("'"))
                .filter(s -> !s.contains("."))
                .filter(s -> !s.contains("(") || !s.contains(")"))
                // weird
                .filter(s -> !s.contains(" or "))
                // probably proper nouns
                .filter(s -> !isUpperCase(s.charAt(0)))
                .map(String::strip)
                .collect(toList());
    }

    public static List<String> filter(List<String> wordList, List<String> wordToFilterOut) {
        return wordList
                .stream()
                // don't care about short words
                .filter(s -> s.length() > 3)
                .filter(s -> !wordToFilterOut.contains(s))
                .collect(toList());
    }
}
