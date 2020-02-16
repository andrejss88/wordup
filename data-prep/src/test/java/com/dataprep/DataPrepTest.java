package com.dataprep;

import org.testng.annotations.Test;

import java.util.List;

import static com.wordup.utils.WordUtils.doSimpleStem;
import static org.assertj.core.api.Assertions.assertThat;

public class DataPrepTest {


    @Test
    public void testRemoveNounPlurals() {
        List<String> listWithDups = List.of("friend", "friends", "dog", "dogs", "serious", "has");

        List<String> filteredList = doSimpleStem(listWithDups);

        assertThat(filteredList)
                .hasSize(4)
                .containsOnly("friend", "dog", "serious", "has");
    }

    @Test
    public void testRemoveConjugatedVerbs() {
        List<String> listWithDups = List.of("do", "doing", "walk", "walking", "walked", "walks", "run");

        List<String> filteredList = doSimpleStem(listWithDups);

        assertThat(filteredList)
                .hasSize(3)
                .containsOnly("do", "walk", "run");
    }

    @Test
    public void testRemoveComparatives() {
        List<String> listWithDups = List.of("strong", "stronger", "strongest", "better", "serious");

        List<String> filteredList = doSimpleStem(listWithDups);

        assertThat(filteredList)
                .hasSize(3)
                .containsOnly("strong", "better", "serious");
    }
}

