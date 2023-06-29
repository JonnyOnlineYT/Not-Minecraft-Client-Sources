package org.spongepowered.asm.lib.tree.analysis;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

class SmallSet extends AbstractSet implements Iterator {
   Object e1;
   Object e2;

   static final Set emptySet() {
      return new SmallSet((Object)null, (Object)null);
   }

   SmallSet(Object e1, Object e2) {
      this.e1 = e1;
      this.e2 = e2;
   }

   public Iterator iterator() {
      return new SmallSet(this.e1, this.e2);
   }

   public int size() {
      return this.e1 == null ? 0 : (this.e2 == null ? 1 : 2);
   }

   public boolean hasNext() {
      return this.e1 != null;
   }

   public Object next() {
      if (this.e1 == null) {
         throw new NoSuchElementException();
      } else {
         Object e = this.e1;
         this.e1 = this.e2;
         this.e2 = null;
         return e;
      }
   }

   public void remove() {
   }

   Set union(SmallSet s) {
      if ((s.e1 != this.e1 || s.e2 != this.e2) && (s.e1 != this.e2 || s.e2 != this.e1)) {
         if (s.e1 == null) {
            return this;
         } else if (this.e1 == null) {
            return s;
         } else {
            if (s.e2 == null) {
               if (this.e2 == null) {
                  return new SmallSet(this.e1, s.e1);
               }

               if (s.e1 == this.e1 || s.e1 == this.e2) {
                  return this;
               }
            }

            if (this.e2 != null || this.e1 != s.e1 && this.e1 != s.e2) {
               HashSet r = new HashSet(4);
               r.add(this.e1);
               if (this.e2 != null) {
                  r.add(this.e2);
               }

               r.add(s.e1);
               if (s.e2 != null) {
                  r.add(s.e2);
               }

               return r;
            } else {
               return s;
            }
         }
      } else {
         return this;
      }
   }
}
