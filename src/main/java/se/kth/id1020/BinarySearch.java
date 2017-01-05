package se.kth.id1020;

import java.util.List;

public class BinarySearch {
    // return the index of the key in the sorted array a[]; -1 if not found
    public static int search(String key, List<Node> a) {
        return search(key, a, 0, a.size());
    }

    public static int search(String key, List<Node> a, int lo, int hi) {
        // possible key indices in [lo, hi)
        if (hi <= lo) return -1;
        int mid = lo + (hi - lo) / 2;
        int cmp = a.get(mid).compareTo(key);
        if      (cmp > 0) return search(key, a, lo, mid);
        else if (cmp < 0) return search(key, a, mid+1, hi);
        else              return mid;
    }
}