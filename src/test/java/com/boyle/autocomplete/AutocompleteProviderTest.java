package com.boyle.autocomplete;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AutocompleteProviderTest {

    @Test
    public void testTrain_SingleMessage()
    {
        String message = "The third thing that I need to tell you is that this thing does not think thoroughly.";
        AutocompleteProvider provider = new AutocompleteProvider();
        provider.train(message);

        List<Candidate> candidates = provider.getWords("thi");
        assertTrue(candidates.contains(new Candidate("thing", 2)));
        assertTrue(candidates.contains(new Candidate("think", 1)));
        assertTrue(candidates.contains(new Candidate("third", 1)));
        assertTrue(candidates.contains(new Candidate("this", 1)));
        assertEquals(4, candidates.size());

        candidates = provider.getWords("nee");
        assertTrue(candidates.contains(new Candidate("need", 1)));
        assertEquals(1, candidates.size());

        candidates = provider.getWords("th");
        assertTrue(candidates.contains(new Candidate("that", 2)));
        assertTrue(candidates.contains(new Candidate("thing", 2)));
        assertTrue(candidates.contains(new Candidate("think", 1)));
        assertTrue(candidates.contains(new Candidate("this", 1)));
        assertTrue(candidates.contains(new Candidate("third", 1)));
        assertTrue(candidates.contains(new Candidate("the", 1)));
        assertTrue(candidates.contains(new Candidate("thoroughly", 1)));
        assertEquals(7, candidates.size());
    }
}