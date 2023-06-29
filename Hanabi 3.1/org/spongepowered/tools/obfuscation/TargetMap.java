package org.spongepowered.tools.obfuscation;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeReference;

public final class TargetMap extends HashMap {
   private static final long serialVersionUID = 1L;
   private final String sessionId;

   private TargetMap() {
      this(String.valueOf(System.currentTimeMillis()));
   }

   private TargetMap(String sessionId) {
      this.sessionId = sessionId;
   }

   public String getSessionId() {
      return this.sessionId;
   }

   public void registerTargets(AnnotatedMixin mixin) {
      this.registerTargets(mixin.getTargets(), mixin.getHandle());
   }

   public void registerTargets(List targets, TypeHandle mixin) {
      Iterator var3 = targets.iterator();

      while(var3.hasNext()) {
         TypeHandle target = (TypeHandle)var3.next();
         this.addMixin(target, mixin);
      }

   }

   public void addMixin(TypeHandle target, TypeHandle mixin) {
      this.addMixin(target.getReference(), mixin.getReference());
   }

   public void addMixin(String target, String mixin) {
      this.addMixin(new TypeReference(target), new TypeReference(mixin));
   }

   public void addMixin(TypeReference target, TypeReference mixin) {
      Set mixins = this.getMixinsFor(target);
      mixins.add(mixin);
   }

   public Collection getMixinsTargeting(TypeElement target) {
      return this.getMixinsTargeting(new TypeHandle(target));
   }

   public Collection getMixinsTargeting(TypeHandle target) {
      return this.getMixinsTargeting(target.getReference());
   }

   public Collection getMixinsTargeting(TypeReference target) {
      return Collections.unmodifiableCollection(this.getMixinsFor(target));
   }

   private Set getMixinsFor(TypeReference target) {
      Set mixins = (Set)this.get(target);
      if (mixins == null) {
         mixins = new HashSet();
         this.put(target, mixins);
      }

      return (Set)mixins;
   }

   public void readImports(File file) throws IOException {
      if (file.isFile()) {
         Iterator var2 = Files.readLines(file, Charset.defaultCharset()).iterator();

         while(var2.hasNext()) {
            String line = (String)var2.next();
            String[] parts = line.split("\t");
            if (parts.length == 2) {
               this.addMixin(parts[1], parts[0]);
            }
         }

      }
   }

   public void write(boolean temp) {
      ObjectOutputStream oos = null;
      FileOutputStream fout = null;

      try {
         File sessionFile = getSessionFile(this.sessionId);
         if (temp) {
            sessionFile.deleteOnExit();
         }

         fout = new FileOutputStream(sessionFile, true);
         oos = new ObjectOutputStream(fout);
         oos.writeObject(this);
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         if (oos != null) {
            try {
               oos.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }

      }

   }

   private static TargetMap read(File sessionFile) {
      ObjectInputStream objectinputstream = null;
      FileInputStream streamIn = null;

      try {
         streamIn = new FileInputStream(sessionFile);
         objectinputstream = new ObjectInputStream(streamIn);
         TargetMap var3 = (TargetMap)objectinputstream.readObject();
         return var3;
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         if (objectinputstream != null) {
            try {
               objectinputstream.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }

      }

      return null;
   }

   public static TargetMap create(String sessionId) {
      if (sessionId != null) {
         File sessionFile = getSessionFile(sessionId);
         if (sessionFile.exists()) {
            TargetMap map = read(sessionFile);
            if (map != null) {
               return map;
            }
         }
      }

      return new TargetMap();
   }

   private static File getSessionFile(String sessionId) {
      File tempDir = new File(System.getProperty("java.io.tmpdir"));
      return new File(tempDir, String.format("mixin-targetdb-%s.tmp", sessionId));
   }
}
