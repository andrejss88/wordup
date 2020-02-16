package com.wordup;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.wordup.utils.FileUtils.convertFileToStringList;
import static com.wordup.utils.FileUtils.write;
import static com.wordup.utils.WordUtils.*;
import static java.util.stream.Collectors.toList;

public class PrepareWordList {

    private static final String PATH = "data-prep/src/main/resources/";
    private static final String META_PATH = PATH + "meta/";
    private static final String OUTPUT_PATH = PATH + "output/";

    public static void main(String[] args) {

        List<String> rawContent = convertFileToStringList(PATH + "input/input_word_list.txt");
        List<String> sanitized = sanitize(rawContent);

        List<String> conjunctions = convertFileToStringList(META_PATH + "conjunctions.txt");
        List<String> numbers = convertFileToStringList(META_PATH + "numbers.txt");
        List<String> pronouns = convertFileToStringList(META_PATH + "pronouns.txt");
        List<String> rudeWords = convertFileToStringList(META_PATH + "rudewords.txt");
        List<String> excludeWords = mergeLists(conjunctions, numbers, pronouns, rudeWords);

        List<String> filtered = filter(sanitized, excludeWords);
        List<String> finalList = doSimpleStem(filtered);

        write(String.join("\n", finalList), Paths.get(OUTPUT_PATH + "preparedWordList.txt"));
    }

    private static List<String> mergeLists(List<String>... collections) {
        return Stream.of(collections)
                .flatMap(Collection::stream)
                .collect(toList());
    }
}