package com.boyle.autocomplete;


public class Candidate {

    public String word;
    public Integer confidence;

    public Candidate(String word, Integer confidence)
    {
        this.word = word;
        this.confidence = confidence;
    }

    public String getWord()
    {
        return this.word;
    }

    public Integer getConfidence()
    {
        return this.confidence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Candidate candidate = (Candidate) o;

        if (word != null ? !word.equals(candidate.word) : candidate.word != null) return false;
        return confidence != null ? confidence.equals(candidate.confidence) : candidate.confidence == null;
    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + (confidence != null ? confidence.hashCode() : 0);
        return result;
    }
}
