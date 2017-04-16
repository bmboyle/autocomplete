[![Build Status](https://travis-ci.org/bmboyle/autocomplete.svg?branch=master)](https://travis-ci.org/bmboyle/autocomplete)

# autocomplete

## Summary

The autocomplete library provides memory efficient, thread safe, and high performance auto-complete capabilities. Using the following API, users can continually provide training data and retrieve auto-complete predictions.

```java
public class AutocompleteProvider {
  // Returns an ordered list of recommendations from highest confidence to lowest confidence.
  public List<Candidate> getWords(String fragment);
  
  // Uses a message of one or more words to train the auto-complete capability.
  public void train(String passage);
}

public class Candidate {
  public String getWord();
  public Integer getConfidence();
}
```

## Design

The core data structure used to organize training data is a Radix Tree. A Radix Tree, also known as a compact prefix tree, is optimized based on common prefixes and provides both efficient use of memory and efficient auto-complete predictions.

![Radix Tree](https://upload.wikimedia.org/wikipedia/commons/a/ae/Patricia_trie.svg)

For memory usage, Radix Trees provide an enhancement over a traditional trie structure in that nodes and edges can represent groupings of characters instead of just a single character. The advantage of this is that it reduces the amount of nodes and edges and subsequently lowers the amount of object references that need to be stored in memory. 

For prediction performance, as well as tree insertions and deletions, an O(k) runtime exists where 'k' is the word length. This high performance exists because each character in a word simply has to be compared one by one until the appropriate branch or leaf is found. 

## Implementation

Due to the many details that go along with implementing a Radix Tree structure, an open source library was used that provides a concurrent Radix Tree structure. The repository can be found here:

https://github.com/npgall/concurrent-trees

Various test cases can be found in this autocomplete project demonstrating the use of the API and verifying the concurrency features. 

## Building

To build the autocomplete library and run the test cases, a gradle build script is provided. Install gradle on your machine, navigate to the autocomplete directory, and run `gradle check`. This command will build the project and run test cases. Upon completion, a `build` directory will be generated with a resulting JAR file and test results. 
