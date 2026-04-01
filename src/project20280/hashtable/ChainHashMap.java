package project20280.hashtable;

import project20280.interfaces.Entry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

/*
 * Map implementation using hash table with separate chaining.
 */

public class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
    // a fixed capacity array of UnsortedTableMap that serve as buckets
    private UnsortedTableMap<K, V>[] table; // initialized within createTable

    /**
     * Creates a hash table with capacity 11 and prime factor 109345121.
     */
    public ChainHashMap() {
        super();
        createTable();
    }

    /**
     * Creates a hash table with given capacity and prime factor 109345121.
     */
    public ChainHashMap(int cap) {
        super(cap);
        createTable();
    }

    /**
     * Creates a hash table with the given capacity and prime factor.
     */
    public ChainHashMap(int cap, int p) {
        super(cap, p);
        createTable();
    }

    /**
     * Creates an empty table having length equal to current capacity.
     */
    @Override
    @SuppressWarnings({"unchecked"})
    protected void createTable() {
        table = new UnsortedTableMap[capacity];
    }

    /**
     * Returns value associated with key k in bucket with hash value h. If no such
     * entry exists, returns null.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return associate value (or null, if no such entry)
     */
    @Override
    protected V bucketGet(int h, K k) {
        return table[h] == null ? null : table[h].get(k);
    }

    /**
     * Associates key k with value v in bucket with hash value h, returning the
     * previously associated value, if any.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @param v the value to be associated
     * @return previous value associated with k (or null, if no such entry)
     */
    @Override
    protected V bucketPut(int h, K k, V v) {
        if (table[h] == null) table[h] = new UnsortedTableMap<>();
        return table[h].put(k, v);
    }


    /**
     * Removes entry having key k from bucket with hash value h, returning the
     * previously associated value, if found.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return previous value associated with k (or null, if no such entry)
     */
    @Override
    protected V bucketRemove(int h, K k) {
        return table[h] == null ? null : table[h].remove(k);
    }

    /**
     * Returns an iterable collection of all key-value entries of the map.
     *
     * @return iterable collection of the map's entries
     */
    @Override
    public Iterable<Entry<K, V>> entrySet() {
        /*
        for each element in (UnsortedTableMap []) table
            for each element in bucket:
                print element
        */
        ArrayList<Entry<K, V>> entries = new ArrayList<>();
        for (UnsortedTableMap<K, V> tm : table) {
            if (tm != null) {
                for (Entry<K, V> e : tm.entrySet()) {
                    entries.add(e);
                }
            }
        }
        return entries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Entry<K, V> e : entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(e.getKey()).append("=").append(e.getValue());
            first = false;
        }

        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("/Users/davidneskrabal/Downloads/sample_text.txt"); // check the path to the file
        ChainHashMap<String, Integer> counter = new ChainHashMap<>();
        // use a Scanner to read words from the file
        Scanner scanner = new Scanner(f);
        while (scanner.hasNext()) { // read the file word at a time
            String word = scanner.next();
            // if word is not in the hashmap, add it with count=1
            // otherwise, find the entry for this word and increment
            Integer count = counter.get(word);
            counter.put(word, count==null? 1 : count+1);
        }

        Stream<Entry<String, Integer>> stream =
                ((Collection<Entry<String, Integer>>) counter.entrySet()).stream();


        List<String> mostFreq = stream
                .sorted(Comparator.comparing(Entry<String, Integer>::getValue).reversed())
                .map(Entry::getKey)
                .limit(10)
                .toList();

        System.out.println(mostFreq);
    }
}
