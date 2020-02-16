package com.wordup.utils;


import java.util.List;

public class Word {

    private PartOfSpeech pos;
    private List<Meaning> meanings;

    public PartOfSpeech getPos() {
        return pos;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }
}
