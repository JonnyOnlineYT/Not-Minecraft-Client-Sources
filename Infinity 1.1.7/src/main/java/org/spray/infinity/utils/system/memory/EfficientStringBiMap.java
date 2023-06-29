package org.spray.infinity.utils.system.memory;

import java.util.Arrays;

/**
 * A memory efficient bimap of EfficientString to int that uses the int index of the EfficientStrings rather than object
 * references.
 */
class EfficientStringBiMap {
    final Bucket[] buckets;
    private final int capacity;

    public EfficientStringBiMap(int capacity) {
        this.capacity = capacity;
        buckets=new Bucket[capacity * 2];
    }

    public void put(EfficientString es) {
        Bucket upper = getOrCreateBucket(capacity + es.index() % capacity);
        Bucket lower = getOrCreateBucket(es.hashCode() % capacity);
        lower.append(es);
        upper.append(es);
    }

    Bucket getOrCreateBucket(int index) {
        if (buckets[index] == null) {
            if (buckets[index] == null) {
                buckets[index] = new Bucket();
            }
        }
        return buckets[index];
    }

    public int get(EfficientString key) {
        Bucket bucket = buckets[key.hashCode() % capacity];
        if (bucket == null) {
            return -1;
        } else {
            return bucket.get(key);
        }
    }

    public EfficientString get(int index) {
        Bucket bucket = buckets[capacity + index % capacity];
        if (bucket == null) {
            return null;
        } else {

            return bucket.get(index);
        }
    }

    class Bucket {
        volatile EfficientString[] array = new EfficientString[5];

        public void append(EfficientString es) {
            int freeSlot = findFreeOrExistingSlot(es);
            if (freeSlot >= array.length) {
                array = Arrays.copyOf(array, (int) Math.round(array.length * 1.3));
            }
            array[freeSlot] = es;
        }

        private int findFreeOrExistingSlot(EfficientString es) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
                EfficientString entry = array[i];
                if (entry.index() == es.index()) {
                    return i;
                }
            }
            return array.length;
        }

        public int get(EfficientString key) {
            for (EfficientString e : array) {
                if (e == null) {
                    break;
                }
                if (key.equals(e)) {
                    return e.index();
                }
            }
            return -1;
        }

        public EfficientString get(int value) {
            for (EfficientString e : array) {
                if (e == null) {
                    return null;
                }
                if (value == e.index()) {
                    return e;
                }
            }
            return null;
        }

        public boolean contains(EfficientString key) {
            return get(key) >= 0;
        }
    }
}
