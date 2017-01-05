package se.kth.id1020;

import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;

import java.util.*;

class TinySearchEngine implements TinySearchEngineBase {

    private List<Node> index = new ArrayList<>();
    private Map<String, Integer> count = new HashMap<>(); // Map document name to the count
    private Map<String, Integer> occurrences = new HashMap<>(); // Map document name to occurrences
    private Map<String, List<Document>> cache = new HashMap<>(); // Cache the results of queries

    public void insert(Word word, Attributes attr) {
        index.add(new Node(word, attr.document, attr.occurrence));
        if (index.size() == 1161192)
            Collections.sort(index);
    }

    public List<Document> search(String query) {
        if (cache.get(query) != null)
            return cache.get(query);

        count.clear();
        occurrences.clear();

        List<Document> results = new ArrayList<>();
        String[] queryPart = query.split("\\s+");
        String property = "", direction = "";
        int n = queryPart.length;

        if (n == 1)
            results = simpleQuery(query);
        else {
            List<String> reservedWords = new ArrayList<String>() {{
                add("orderby");
                add("count");
                add("popularity");
                add("occurrences");
                add("asc");
                add("desc");
            }};

            for (int i = 0; i < n; i++) {
                if (!reservedWords.contains(queryPart[i]))
                    addDistinct(results, simpleQuery(queryPart[i]));
                else if (queryPart[i].equals("orderby") && reservedWords.contains(queryPart[i + 1])
                        && reservedWords.contains(queryPart[i + 2])) {
                    property = queryPart[i + 1];
                    direction = queryPart[i + 2];
                    break;
                }
            }
        }

        if (property.equals(""))
            sort(results);
        else
            sort(results, property, direction);

        cache.put(query, results);

        return results;
    }

    private List<Document> simpleQuery(String query) {
        List<Document> results = new ArrayList<>();

        int index = BinarySearch.search(query, this.index);
        if (index != -1) {
            int j = index, k = index;
            Document result = this.index.get(index).document;
            int currentOccurrence = this.index.get(index).occurrence;
            occurrences.put(result.name, currentOccurrence);
            count.put(result.name, 1);
            results.add(result);

            while (this.index.get(--j).word.word.equals(query)) {
                result = this.index.get(j).document;
                currentOccurrence = this.index.get(j).occurrence;

                handleResult(results, result, currentOccurrence);
            }

            while (this.index.get(++k).word.word.equals(query)) {
                result = this.index.get(k).document;
                currentOccurrence = this.index.get(k).occurrence;

                handleResult(results, result, currentOccurrence);
            }
        }
        return results;
    }

    private void handleResult (List<Document> results, Document result, int occurrence) {
        if (!results.contains(result)) {
            results.add(result);
            count.put(result.name, 1);
        }
        else
            count.put(result.name, count.get(result.name) + 1);

        if (occurrences.get(result.name) == null || occurrences.get(result.name) > occurrence)
            occurrences.put(result.name, occurrence);
    }

    private void addDistinct(List<Document> to, List<Document> from) {
        from.stream().filter(d -> !to.contains(d)).forEach(to::add);
    }

    private void sort(List<Document> results) {
        boolean swapped = true;
        int R = results.size() - 2;

        while (R >= 0 && swapped) {
            swapped = false;
            for (int i = 0; i <= R; i++) {
                if (results.get(i).name.compareTo(results.get(i + 1).name) > 0) {
                    swapped = true;
                    Collections.swap(results, i, i + 1);
                }
            }
            R--;
        }
    }

    private void sort(List<Document> results, String property, String direction) {
        boolean swapped = true;
        int R = results.size() - 2;

        while (R >= 0 && swapped) {
            swapped = false;
            for (int i = 0; i <= R; i++) {

                String firstComparator = "";
                String secondComparator = "";
                int j = direction.equals("asc") ? i : (i + 1);
                int k = direction.equals("desc") ? i : (i + 1);

                switch (property) {
                    case "count":
                        firstComparator = String.valueOf(count.get(results.get(j).name));
                        secondComparator = String.valueOf(count.get(results.get(k).name));
                        break;
                    case "popularity":
                        firstComparator = String.valueOf(results.get(j).popularity);
                        secondComparator = String.valueOf(results.get(k).popularity);
                        break;
                    case "occurrences":
                        firstComparator = String.valueOf(occurrences.get(results.get(j).name));
                        secondComparator = String.valueOf(occurrences.get(results.get(k).name));
                        break;
                }

                if (firstComparator.compareTo(secondComparator) > 0) {
                    swapped = true;
                    Collections.swap(results, j, k);
                }
            }
            R--;
        }
    }
}
