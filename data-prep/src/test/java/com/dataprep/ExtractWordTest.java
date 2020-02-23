package com.dataprep;

import org.testng.annotations.Test;

import static com.wordup.ExtractWordMeanings.extractSynonyms;
import static com.wordup.ExtractWordMeanings.extractWord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;

public class ExtractWordTest {

    @Test(description = "Extracting a definition comprised of a single word should work")
    public void extractWordWorks() {
        String lineWithNormalWord = "00020097 29 v 01 wake 0 003 + 05608221 n 0101 ! 00014762 v 0101 ~ 00020223 v 0000 01 + 02 00 | be awake, be alert, be there;";
        assertEquals(extractWord(lineWithNormalWord), "wake");
    }

    @Test(description = "Extracting a definition comprised of a several words concatenated_like_this should work")
    public void extractWordWithSpecialCharsWorks() {
        String lineWithOneUnderscore = "00004359 03 n 02 living_thing 0 animate_thing 0 007 @ 00003122 n 0000 -c 01706638 a 0000 ~ 00004576 n 0000 ~ 00006085 n 0000 ~ 00006216 n 0000 ~ 00006300 n 0000 -c 04997290 n 0000 | a living (or once living) entity;";
        String lineWithTwoUnderscores = "00017885 29 v 0a go_to_bed 0 turn_in 0 bed 0 crawl_in 0 kip_down 0 hit_the_hay 0 hit_the_sack 0 sack_out 0 go_to_sleep 1 retire 0 003 ! 00018178 v 0202 ! 00018178 v 0101 ~ 00017551 v 0000 01 + 02 00 | prepare for sleep; \"I usually turn in at midnight\"; \"He goes to bed at the crack of dawn\";";
        String lineWithDash = "00023177 29 v 02 de-energize 0 de-energise 0 003 @ 00221826 v 0000 ! 00022619 v 0205 ! 00022619 v 0104 01 + 10 00 | deprive of energy;";

        assertEquals(extractWord(lineWithOneUnderscore), "living thing");
        assertEquals(extractWord(lineWithTwoUnderscores), "go to bed");
        assertEquals(extractWord(lineWithDash), "de-energize"); // should stay the same
    }


    @Test
    public void extractSynonymsShouldWork() {
        String lineWithOneSynonym = "00020223 29 v 02 stay_up 0 sit_up 0 001 @ 00020097 v 0000 01 + 02 00 | not go to bed; \"Don't stay up so late--you have to go to work tomorrow\"; \"We sat up all night to watch the election\";";
        String lineWithThreeSynonyms = "00024582 29 v 03 refresh 0 freshen 0 refreshen 1 004 @ 00123976 v 0000 + 03359504 n 0201 ^ 00024747 v 0204 ! 00074290 v 0101 02 + 09 00 + 10 00 | make fresh again;";

        assertThat(extractSynonyms(lineWithOneSynonym))
                .hasSize(1)
                .containsOnly("sit up");

        assertThat(extractSynonyms(lineWithThreeSynonyms))
                .hasSize(2)
                .containsOnly("freshen", "refreshen");
    }
}
