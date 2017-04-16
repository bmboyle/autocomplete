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

    private class TrainingThread implements Runnable {
        AutocompleteProvider provider;
        String message;

        public TrainingThread(AutocompleteProvider provider, String message)
        {
            this.provider = provider;
            this.message = message;
        }
        public void run()
        {
            provider.train(message);
        }
    }

    @Test
    public void testTrain_MultiThreaded()
    {
        AutocompleteProvider provider = new AutocompleteProvider();

        // third: 1, thing: 2, think: 2, thinking: 1, this: 2
        String message1 = "A third Thing is";
        String message2 = "Thinking of this is thoughtful";
        String message3 = "Just another test sentence.";
        String message4 = "Think of another one";
        String message5 = "I need to tell you that this thing does not think thoroughly.";

        Thread thread1 = new Thread(new TrainingThread(provider, message1));
        Thread thread2 = new Thread(new TrainingThread(provider, message2));
        Thread thread3 = new Thread(new TrainingThread(provider, message3));
        Thread thread4 = new Thread(new TrainingThread(provider, message4));
        Thread thread5 = new Thread(new TrainingThread(provider, message5));
        Thread[] threads = {thread1, thread2, thread3, thread4, thread5};

        for (Thread thread : threads)
        {
            thread.start();
        }
        for (Thread thread : threads)
        {
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail("Thread exited with InterruptedException.");
            }
        }

        List<Candidate> candidates = provider.getWords("thi");
        assertTrue(candidates.contains(new Candidate("thing", 2)));
        assertTrue(candidates.contains(new Candidate("think", 2)));
        assertTrue(candidates.contains(new Candidate("thinking", 1)));
        assertTrue(candidates.contains(new Candidate("third", 1)));
        assertTrue(candidates.contains(new Candidate("this", 2)));
        assertEquals(5, candidates.size());
    }

    @Test
    public void testTrain_DelimiterVariety()
    {
        String message = "The     third. thing that I need to tell?    you. is that this thing does not think thoroughly.";
        AutocompleteProvider provider = new AutocompleteProvider();
        provider.train(message);

        List<Candidate> candidates = provider.getWords("th");
        assertTrue(candidates.contains(new Candidate("that", 2)));
        assertTrue(candidates.contains(new Candidate("thing", 2)));
        assertTrue(candidates.contains(new Candidate("think", 1)));
        assertTrue(candidates.contains(new Candidate("this", 1)));
        assertTrue(candidates.contains(new Candidate("third", 1)));
        assertTrue(candidates.contains(new Candidate("the", 1)));
        assertTrue(candidates.contains(new Candidate("thoroughly", 1)));
        assertEquals(7, candidates.size());
    }

    @Test
    public void testGet_Ordering()
    {
        String message1 = "though thought thoughtful";
        String message2 = "thorough though thought";
        String message3 = "thought thought though";
        AutocompleteProvider provider = new AutocompleteProvider();
        provider.train(message1);
        provider.train(message2);
        provider.train(message3);

        List<Candidate> candidates = provider.getWords("thou");
        assertEquals(new Candidate("thought", 4), candidates.get(0));
        assertEquals(new Candidate("though", 3), candidates.get(1));
        assertEquals(new Candidate("thoughtful", 1), candidates.get(2));
        assertEquals(3, candidates.size());
    }
}