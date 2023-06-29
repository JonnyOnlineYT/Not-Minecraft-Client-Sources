package org.apache.commons.codec;

import java.io.InputStream;

public class Resources {
   public static InputStream getInputStream(String name) {
      InputStream inputStream = Resources.class.getClassLoader().getResourceAsStream(name);
      if (inputStream == null) {
         throw new IllegalArgumentException("Unable to resolve required resource: " + name);
      } else {
         return inputStream;
      }
   }
}
