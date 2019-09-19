package edu.sdsu.cs.datastructures;

import java.util.*;

/**
 * Natalia Alvarez
 */

public class BalancedMap <K extends Comparable<K>, V> implements IMap<K,V> {
    private TreeMap<K,V> redBlackMap;

    public BalancedMap(){
        redBlackMap = new TreeMap<K,V>();
    }

    public BalancedMap(IMap<K,V> source){
        redBlackMap = new TreeMap<K,V>();
        for (K key : source.keyset()){
            redBlackMap.put(key, source.getValue(key));
        }
    }

    @Override
    public boolean contains(K key) {
        return redBlackMap.containsKey(key);
    }

    @Override
    public boolean add(K key, V value) {
      redBlackMap.put(key,value);
      return true;
    }

    @Override
    public V delete(K key) {
        return redBlackMap.remove(key);
    }

    @Override
    public V getValue(K key) {
        return redBlackMap.get(key);
    }

    @Override
    public K getKey(V value) {
        for( K key : redBlackMap.keySet()){
            if(value.equals(redBlackMap.get(key))){
                return key;
            }
        }
        return null;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        List<K> toReturn= new LinkedList<>();
        for( K key : redBlackMap.keySet()){
            if (value.equals(redBlackMap.get(key))) {
                toReturn.add((key));
            }
        }

        return toReturn;
    }

    @Override
    public int size() {
        return redBlackMap.size();
    }

    @Override
    public boolean isEmpty() {
        return redBlackMap.isEmpty();
    }

    @Override
    public void clear() {
        redBlackMap.clear();
    }

    @Override
    public Iterable<K> keyset() {
        return redBlackMap.keySet();
    }

    @Override
    public Iterable<V> values() {
        return redBlackMap.values();
    }
}
