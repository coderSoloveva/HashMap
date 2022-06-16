/**
 * Hash table based implementation of the MyMap interface.
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class MyHashMap<K,V> implements MyMap<K,V> {
    private final int DEFAULT_INITIAL_CAPACITY = 16;
    private final float DEFAULT_LOAD_FACTOR = 0.75f;
    Node[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int size = 0;
    /**
     * Returns the number of key-value mappings in this map.
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if this map contains no key-value mappings.
     * @return true if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is replaced.
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    @Override
    public V put(K key, V value) {
        int hashValue = hash(key);
        int i = (hashValue % table.length);
        for(Node node = table[i];node != null; node = node.next){
            K k;
            if(node.hash == hashValue && ((k = (K) node.key)==key||key.equals(k))){
                Object oldValue = node.value;
                node.value = value;
                return (V) oldValue;
            }
        }
        addEntry(key,value,hashValue,i);
        return null;
    }
    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    @Override
    public V get(K key) {
        int hashValue = hash(key);
        int i = (hashValue % table.length);
        for(Node node = table[i];node != null;node = node.next){
            if(node.key.equals(key) && hashValue == node.hash){
                return (V) node.value;
            }
        }
        return null;
    }
    /**
     * Removes the mapping for the specified key from this map if present.
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    @Override
    public V remove(K key) {
        int hashValue = hash(key);
        int i = (hashValue % table.length);
        for(Node node = table[i], prev = table[i]; node != null; node = node.next){
            if(hashValue == node.hash && node.key.equals(key)){
                if (prev.equals(node)) {
                    table[i] = null;
                } else {
                    prev.next = node.next;
                    return (V) node.value;
                }
            }
            prev = node;
        }
        return null;
    }
    /**
     * Returns true if this map contains a mapping for the specified key.
     * @param key The key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }
    public void addEntry(K key, V value, int hashValue, int i){
        if(++size >= table.length * DEFAULT_LOAD_FACTOR){
            Node[] newTable = new Node[table.length << 1];
            transfer(table, newTable);
            table = newTable;
        }
        Node eNode = table[i];
        table[i] = new Node(hashValue, key, value, eNode);
    }

    public void transfer (Node[] table, Node[] newTable) {
        int newLength = newTable.length;
        for (int j = 0; j < table.length; j++) {
            Node e = table[j];
            if (e != null) {
                do {
                    Node next = e.next;
                    int i = (e.hash % newLength);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    int hash(K key){
        return key.hashCode();
    }
    /**
     * Returns a string representation of this collection.
     * @return a string representation of this collection
     */
    @Override
    public String toString() {
        String rez = "{";
        boolean isFirst = true;
        for (int j = 0; j < table.length; j++) {
            Node e = table[j];
            if (e != null) {
                do {
                    if (!isFirst) {
                        rez += ", " + e.toString();
                    } else {
                        isFirst = false;
                        rez += e.toString();
                    }
                    e = e.next;
                } while (e != null);
            }
        }
        return rez+="}";
    }

    /**
     * A map entry
     */
    static class Node<K,V> implements MyMap.Entry<K,V>{
        int hash;
        K key;
        V value;
        Node next;
        Node(int hash, K key, V value, Node next){
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * Returns the key corresponding to this entry.
         * @return the key corresponding to this entry
         */
        @Override
        public K getKey() {
            return key;
        }
        /**
         * Returns the value corresponding to this entry.
         * @return the value corresponding to this entry
         */
        @Override
        public V getValue() {
            return value;
        }
        /**
         * Returns a string representation of this collection.
         * @return a string representation of this collection
         */
        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}