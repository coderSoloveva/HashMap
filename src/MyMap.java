
public interface MyMap<K,V> {
    int size();
    boolean isEmpty();

    V get(K key);

    V remove(K key);

    V put(K key, V value);

    boolean containsKey(K key);

    interface Entry<K, V> {
        K getKey();
        V getValue();
    }
}