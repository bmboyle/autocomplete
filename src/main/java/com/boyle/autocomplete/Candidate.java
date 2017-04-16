package com.boyle.autocomplete;


public class Candidate implements Comparable<Candidate>{

    public String word;
    public Integer confidence;

    public Candidate(String word, Integer confidence)
    {
        this.word = word;
        this.confidence = confidence;
    }

    /**
     * Returns the autocomplete candidate word.
     * @return The autocomplete candidate word.
     */
    public String getWord()
    {
        return this.word;
    }

    /**
     * Returns the confidence of the candidate.
     * @return The confidence of the candidate. A higher Integer is a higher confidence.
     */
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

    @Override
    public int compareTo(Candidate candidate) {
        return this.confidence.compareTo(candidate.getConfidence());
    }
}
