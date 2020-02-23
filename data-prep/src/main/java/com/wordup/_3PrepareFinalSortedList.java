package com.wordup;

import java.nio.file.Paths;
import java.util.List;

import static com.wordup.utils.Constants.OUTPUT_PATH;
import static com.wordup.utils.FileUtils.convertFileToStringList;
import static com.wordup.utils.FileUtils.write;
import static java.lang.String.join;

public class _3PrepareFinalSortedList {


    public static void main(String[] args) {

        List<String> sortedWords = convertFileToStringList(OUTPUT_PATH + "preparedWordList.txt");
        List<String> meanings = convertFileToStringList(OUTPUT_PATH + "preparedMeanings.txt");

        String delim = ";";
        String delimEndOfLine = delim + "\n";
        String listDelim = ",";

        StringBuilder sb = new StringBuilder();
        int frequencyPosition = 0;


        for (String word : sortedWords) {
            for (String meaning : meanings) {
                String wordInMeaning = meaning.split(delim)[0];

                if (word.equalsIgnoreCase(wordInMeaning)) {

                    String partOfSpeech = meaning.split(delim)[1];
                    String definition = meaning.split(delim)[2];
                    String synonyms = join(listDelim, meaning.split(delim)[3]);
                    String examples = join(listDelim, meaning.split(delim)[4]);

                    frequencyPosition++;
                    sb.append(frequencyPosition).append(delim)
                            .append(word).append(delim)
                            .append(partOfSpeech).append(delim)
                            .append(definition).append(delim)
                            .append(synonyms).append(delim)
                            .append(examples).append(delimEndOfLine);
                }
            }
            write(sb.toString(), Paths.get(OUTPUT_PATH + "finalSortedWordListWithMeanings.txt"));
        }
    }
}
