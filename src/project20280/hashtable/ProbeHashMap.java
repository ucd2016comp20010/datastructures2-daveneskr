package project20280.hashtable;

import project20280.interfaces.Entry;

import java.util.ArrayList;

public class ProbeHashMap<K, V> extends AbstractHashMap<K, V> {
    private MapEntry<K, V>[] table;
    private final MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null);

    public ProbeHashMap() {
        super();
    }

    /**
     * Creates a hash table with given capacity and prime factor 109345121.
     */
    public ProbeHashMap(int cap) {
        super(cap);
    }

    /**
     * Creates a hash table with the given capacity and prime factor.
     */
    public ProbeHashMap(int cap, int p) {
        super(cap, p);
    }

    @Override
    protected void createTable() {
        table = new MapEntry[capacity];
    }

    int findSlot(int h, K k) {
        int i = h, defunctSlot = -1;
        while(table[i] != null) {
            if (table[i].getKey().equals(k)) { /* key found */
                return i;
            }
            if (defunctSlot == -1 && table[i] == DEFUNCT) { /* remember defunct position */
                defunctSlot = i;
            }
            i = (i+1) % capacity;
        }
        if (defunctSlot != -1) {
            return defunctSlot;
        }
        return i;
    }

    @Override
    protected V bucketGet(int h, K k) {
        int i = findSlot(h, k);
        if (table[i] != null) return table[i].getValue();
        return null;
    }

    @Override
    protected V bucketPut(int h, K k, V v) {
        int i = findSlot(h, k);
        V prev = table[i]!=null? table[i].getValue() : null;
        table[i] = new MapEntry<>(k, v);
        return prev;
    }

    @Override
    protected V bucketRemove(int h, K k) {
        int i = findSlot(h, k);
        V prev = null;
        if (table[i] != null) {
            prev = table[i].getValue();
            table[i] = DEFUNCT;
        }
        return prev;
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> iteratable = new ArrayList<>(size());
        for (MapEntry entry : table) {
            if (entry != null && entry != DEFUNCT) iteratable.add(entry);
        }
        return iteratable;
    }
}
