package project20280.exercises;

import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Stream;

public class HashFunctions {
    public static int hash_poly(String s,  int a) {
        int h = 0;
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char s_i = (char) s.charAt(i);
            int v = s_i * ((int) Math.pow(a,n-i-1));
            h += v;
        }
        return h;
    }

    public static int hash_cyclic(String s,  int shift) {
        int h = 0;
        for (int i = 0; i < s.length(); ++i) {
            h = (h << shift) | (h >> (32-shift));
            h += (int) s.charAt(i);
        }
        return h;
    }

    public static int oldHashCode(String s) {
        int hash = 0;
        int skip = Math.max(1, s.length() / 8);
        for (int i = 0; i < s.length(); i += skip)
            hash = (hash * 37) + s.charAt(i);
        return hash;
    }

    public static void bestShift() throws IOException {
        File f = new File("/Users/davidneskrabal/Downloads/words.txt");
        Scanner scanner = new Scanner(f);

        int[] collisions = new int[32];

        @SuppressWarnings("unchecked")
        ChainHashMap<Integer, Integer>[] maps = new ChainHashMap[32];

        for (int i = 0; i < 32; i++) {
            maps[i] = new ChainHashMap<>();
        }

        while (scanner.hasNext()) {
            String word = scanner.next();

            for (int shift = 0; shift < 32; shift++) {
                int hash = hash_cyclic(word, shift);

                Integer count = maps[shift].get(hash);

                if (count == null) {
                    maps[shift].put(hash, 1);
                } else {
                    maps[shift].put(hash, count + 1);
                    collisions[shift]++;   // this word caused a collision for this shift
                }
            }
        }

        scanner.close();

        int bestShift = 0;
        for (int i = 1; i < 32; i++) {
            if (collisions[i] < collisions[bestShift]) {
                bestShift = i;
            }
        }

        for (int i = 0; i < 32; i++) {
            System.out.println("Shift " + i + ": " + collisions[i]);
        }

        System.out.println("Best shift: " + bestShift);
        System.out.println("Collisions: " + collisions[bestShift]);
    }

    public static void hashFunctionComparison() throws IOException{
        File f = new File("/Users/davidneskrabal/Downloads/words.txt"); // check the path to the file
        ChainHashMap<Integer, Integer> poly41 = new ChainHashMap<>();
        ChainHashMap<Integer, Integer> poly17 = new ChainHashMap<>();
        ChainHashMap<Integer, Integer> cyclic7 = new ChainHashMap<>();
        ChainHashMap<Integer, Integer> javaHashCode = new ChainHashMap<>();
        // use a Scanner to read words from the file
        Scanner scanner = new Scanner(f);
        while (scanner.hasNext()) { // read the file word at a time
            String word = scanner.next();
            // if word is not in the hashmap, add it with count=1
            // otherwise, find the entry for this word and increment
            Integer h = hash_poly(word, 41);
            Integer count = poly41.get(h);
            poly41.put(h, count==null? 0 : count+1);

            h = hash_poly(word, 17);
            count = poly17.get(h);
            poly17.put(h, count==null? 0 : count+1);

            h = hash_cyclic(word, 7);
            count = cyclic7.get(h);
            cyclic7.put(h, count==null? 0 : count+1);

            h = oldHashCode(word);
            count = javaHashCode.get(h);
            javaHashCode.put(h, count==null? 0 : count+1);
        }
        Stream<Entry<Integer, Integer>> streamPoly41 =
                ((Collection<Entry<Integer, Integer>>) poly41.entrySet()).stream();
        Stream<Entry<Integer, Integer>> streamPoly17 =
                ((Collection<Entry<Integer, Integer>>) poly17.entrySet()).stream();
        Stream<Entry<Integer, Integer>> streamCyclic7 =
                ((Collection<Entry<Integer, Integer>>) cyclic7.entrySet()).stream();
        Stream<Entry<Integer, Integer>> streamJavaHashCode =
                ((Collection<Entry<Integer, Integer>>) javaHashCode.entrySet()).stream();

        int collisionsPoly41 = streamPoly41
                .mapToInt(Entry::getValue)
                .sum();

        int collisionsPoly17 = streamPoly17
                .mapToInt(Entry::getValue)
                .sum();

        int collisionsCyclic7 = streamCyclic7
                .mapToInt(Entry::getValue)
                .sum();

        int collisionsJavaHashCode = streamJavaHashCode
                .mapToInt(Entry::getValue)
                .sum();

        System.out.println("Hash Function Method: Collisions");
        System.out.println("Polynomial accumulation (a=41): " + collisionsPoly41);
        System.out.println("Polynomial accumulation (a=17): " + collisionsPoly17);
        System.out.println("Cyclic shift (shift=7): " + collisionsCyclic7);
        System.out.println("Old Java Hash Code Function: " + collisionsJavaHashCode);
    }

    public static void main(String[] args) throws IOException {
        hashFunctionComparison();
    }
}
