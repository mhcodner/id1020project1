package se.kth.id1020.util;

public class Word implements Comparable<Word> {
    public final PartOfSpeech pos;
    public final String word;

    public Word(String word, String wordTag) {
        this.pos = PartOfSpeech.getType(wordTag);
        this.word = word;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof Word)) {
            return false;
        } else {
            Word word1 = (Word)o;
            return this.word.equals(word1.word) && this.pos == word1.pos;
        }
    }

    public int hashCode() {
        int result = this.pos.hashCode();
        result = 31 * result + this.word.hashCode();
        return result;
    }

    public int compareTo(Word key) {
        return this.word.compareTo(key.word);
    }

    public int compareTo(String key) {
        return this.word.compareTo(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Word{\"");
        sb.append(this.word);
        sb.append("\" // ");
        sb.append(this.pos.name());
        sb.append("}");
        return sb.toString();
    }
}

