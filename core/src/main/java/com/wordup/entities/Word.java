package com.wordup.entities;


import java.util.List;

public class Word {

    private String word;
    private PartOfSpeech pos;
    private List<Meaning> meanings;

    public String getWord() {
        return word;
    }

    public PartOfSpeech getPos() {
        return pos;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }
}
