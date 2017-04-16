package com.boyle.autocomplete;

import com.googlecode.concurrenttrees.common.KeyValuePair;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.node.NodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class AutocompleteProvider {

    private final NodeFactory nodeFactory = new DefaultCharArrayNodeFactory();
    private ConcurrentRadixTree<Integer> wordTree;

    public AutocompleteProvider()
    {
        this.wordTree = new ConcurrentRadixTree(nodeFactory);
    }

    /**
     * Returns list of candidates ordered from highest confidence to lowest confidence.
     * @param fragment An incomplete word to retrieve auto-complete candidates for.
     * @return List of candidates ordered by confidence.
     */
    public List<Candidate> getWords(String fragment)
    {
        Iterable<KeyValuePair<Integer>> keyValuePairs = wordTree.getKeyValuePairsForKeysStartingWith(fragment);
        List<Candidate> candidates = transformPairsToCandidates(keyValuePairs);
        Collections.sort(candidates, Collections.reverseOrder());
        return candidates;
    }

    private List<Candidate> transformPairsToCandidates(Iterable<KeyValuePair<Integer>> keyValuePairs)
    {
        List<Candidate> candidates = new ArrayList<Candidate>();

        for (KeyValuePair<Integer> keyValuePair : keyValuePairs)
        {
            String word = keyValuePair.getKey().toString();
            Integer confidence = keyValuePair.getValue();
            Candidate candidate = new Candidate(word, confidence);
            candidates.add(candidate);
        }

        return candidates;
    }

    /**
     * Use a provided passage to train auto-complete predictions.
     * @param passage A passage of words.
     */
    public void train(String passage)
    {
        Iterable<String> wordsInMessage = splitMessage(passage);
        indexWords(wordsInMessage);
    }

    private Iterable<String> splitMessage(String passage)
    {
        String delimiterRegex = "[^a-zA-Z_-]+";
        String[] words = passage.split(delimiterRegex);

        return Arrays.asList(words);
    }

    private void indexWords(Iterable<String> words)
    {
        for(String word : words)
        {
            indexLowercaseWord(word);
        }
    }

    private void indexLowercaseWord(String word)
    {
        String lowercaseWord = word.toLowerCase();
        Integer insertValue = determineUpdatedValue(lowercaseWord);
        wordTree.put(lowercaseWord, insertValue);
    }

    private Integer determineUpdatedValue(String word)
    {
        // Every time a word is indexed, its value increases by 1 to represent number of occurrences.
        Integer value = wordTree.getValueForExactKey(word);
        if (value == null)
        {
            return new Integer(1);
        }
        else
        {
            return value + 1;
        }
    }
}
