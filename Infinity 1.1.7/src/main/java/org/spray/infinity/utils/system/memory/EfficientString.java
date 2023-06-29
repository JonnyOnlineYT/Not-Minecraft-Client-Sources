package org.spray.infinity.utils.system.memory;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * Efficient String enables you to keep tens of millions of strings in memory and manipulate the resulting data set with
 * e.g. hash maps in a memory efficient manner.
 *
 * The problem with Strings in Java is that Java stores them as UTF-16 internally, which means that for many scripts you
 * are wasting memory. Additionally the hashCode function is not suitable for extremely large hashmaps and results in
 * too few buckets, which means your insert speed becomes progressively slower.
 *
 * This class solves both problems and in addition adds the ability to use ints as identifiers for your strings. This
 * limits the number of strings to MAXINT (2.147.483.647), about two billion. Having an int as an identifier means you
 * can store string references in int arrays, which is a lot more compact than having arrays of object references, which
 * are 64 bit in Java. To avoid wasting memory, identical strings have the same integer identifier (i.e. no duplicate
 * strings are created).
 *
 * The hashcode function is based on a CRC hash of the string with a modulo of 50000. This gives you a reasonable
 * distribution of strings over a hashtable without introducing a lot of memory overhead for creating additional
 * buckets. You may want to tweak this function for your use case.
 *
 * This class is thread safe and locks at the bucket level.
 */
public class EfficientString {
    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_32();
    static final int HASH_MODULO = 50000;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final byte[] bytes;

    private final int hashCode;

    private static EfficientStringBiMap allStrings = new EfficientStringBiMap(HASH_MODULO);
    // static int index = 0;
    private static AtomicInteger indexCounter = new AtomicInteger(0);
    private int index = -1;
    private static ReentrantReadWriteLock lock=new ReentrantReadWriteLock();;

    private EfficientString(String s) {
        bytes = s.getBytes(UTF8);
        // EfficientString is immutable so we can pre-calculate the hashCode and reuse it.
        hashCode = calculateHashCode();
    }

    /**
     * @param s a string
     * @return efficient string object for the given string
     */
    public static EfficientString fromString(String s) {
        EfficientString efficientString = new EfficientString(s);
        int existingIndex;
        lock.readLock().lock();
        try {
            existingIndex = allStrings.get(efficientString);
            if (existingIndex >= 0) {
                return allStrings.get(existingIndex);
            }
        } finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        try {
            existingIndex = allStrings.get(efficientString);
            if (existingIndex >= 0) {
                // conflicting write
                return allStrings.get(existingIndex);
            }
            efficientString.index = indexCounter.getAndIncrement();
            allStrings.put(efficientString);
        } finally {
            lock.writeLock().unlock();
        }
        return efficientString;
    }

    /**
     * @param index index of an efficient string
     * @return the efficient string or null if it doesn't exist.
     */
    public static EfficientString get(int index) {
        lock.readLock().lock();
        try {
            return allStrings.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * @return index of this efficient string.
     */
    public int index() {
        return index;
    }

    /**
     * @return the index that will be used for the next string that is created with fromString.
     */
    public static int nextIndex() {
        return indexCounter.get();
    }

    private int calculateHashCode() {
        int intHash = HASH_FUNCTION.hashBytes(bytes).asInt();
        if(intHash ==  Integer.MIN_VALUE) {
            // Math.abs does not handle MIN_VALUE very nicely. So, pick something else.
            intHash=0;
        }
        return Math.abs(intHash);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EfficientString)) {
            return false;
        }
        EfficientString es = (EfficientString) obj;
        // before they are added to the bimap, efficient strings have an index of -1
        if (es.index != -1 && index != -1) {
            return es.index == index;
        }
        // edge case that only occurs for strings that are being created
        return Arrays.equals(bytes, es.bytes);
    }

    @Override
    public String toString() {
        return new String(bytes, UTF8);
    }

    /**
     * @return the UTF-8 bytes for this efficient string
     */
    public byte[] bytes() {
        return bytes;
    }

    /**
     * Clears the registry of existing Strings and resets the index to 0. Use this to free up memory.
     */
    public static void clear() {
        lock.writeLock().lock();
        try {
            allStrings = new EfficientStringBiMap(HASH_MODULO);
            indexCounter.set(0);
        } finally {
            lock.writeLock().unlock();
        }
    }
}