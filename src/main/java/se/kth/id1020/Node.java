package se.kth.id1020;

import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;

public class Node implements Comparable<Node> {
    public Word word;
    public Document document;
    public int occurrence;

    Node(Word word, Document document, int occurrence) {
        this.word = word;
        this.document = document;
        this.occurrence = occurrence;
    }

    public boolean equals (Object key) {
        if (key instanceof Node) {
            Node that = (Node)key;
            return this.word.equals(that.word) && this.document.equals(that.document);
        }
        return false;
    }

    public int compareTo(Node key) {
        int i = this.word.compareTo(key.word);
        if (i != 0) return i;

        i = this.document.compareTo(key.document);
        return i;
    }

    public int compareTo(String key) {
        return this.word.compareTo(key);
    }
}
