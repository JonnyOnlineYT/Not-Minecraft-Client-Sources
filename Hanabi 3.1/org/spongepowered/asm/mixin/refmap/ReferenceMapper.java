package org.spongepowered.asm.mixin.refmap;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;

public final class ReferenceMapper implements IReferenceMapper, Serializable {
   private static final long serialVersionUID = 2L;
   public static final String DEFAULT_RESOURCE = "mixin.refmap.json";
   public static final ReferenceMapper DEFAULT_MAPPER = new ReferenceMapper(true, "invalid");
   private final Map mappings;
   private final Map data;
   private final transient boolean readOnly;
   private transient String context;
   private transient String resource;

   public ReferenceMapper() {
      this(false, "mixin.refmap.json");
   }

   private ReferenceMapper(boolean readOnly, String resource) {
      this.mappings = Maps.newHashMap();
      this.data = Maps.newHashMap();
      this.context = null;
      this.readOnly = readOnly;
      this.resource = resource;
   }

   public boolean isDefault() {
      return this.readOnly;
   }

   private void setResourceName(String resource) {
      if (!this.readOnly) {
         this.resource = resource != null ? resource : "<unknown resource>";
      }

   }

   public String getResourceName() {
      return this.resource;
   }

   public String getStatus() {
      return this.isDefault() ? "No refMap loaded." : "Using refmap " + this.getResourceName();
   }

   public String getContext() {
      return this.context;
   }

   public void setContext(String context) {
      this.context = context;
   }

   public String remap(String className, String reference) {
      return this.remapWithContext(this.context, className, reference);
   }

   public String remapWithContext(String context, String className, String reference) {
      Map mappings = this.mappings;
      if (context != null) {
         mappings = (Map)this.data.get(context);
         if (mappings == null) {
            mappings = this.mappings;
         }
      }

      return this.remap(mappings, className, reference);
   }

   private String remap(Map mappings, String className, String reference) {
      if (className == null) {
         Iterator var4 = mappings.values().iterator();

         while(var4.hasNext()) {
            Map mapping = (Map)var4.next();
            if (mapping.containsKey(reference)) {
               return (String)mapping.get(reference);
            }
         }
      }

      Map classMappings = (Map)mappings.get(className);
      if (classMappings == null) {
         return reference;
      } else {
         String remappedReference = (String)classMappings.get(reference);
         return remappedReference != null ? remappedReference : reference;
      }
   }

   public String addMapping(String context, String className, String reference, String newReference) {
      if (!this.readOnly && reference != null && newReference != null && !reference.equals(newReference)) {
         Map mappings = this.mappings;
         if (context != null) {
            mappings = (Map)this.data.get(context);
            if (mappings == null) {
               mappings = Maps.newHashMap();
               this.data.put(context, mappings);
            }
         }

         Map classMappings = (Map)((Map)mappings).get(className);
         if (classMappings == null) {
            classMappings = new HashMap();
            ((Map)mappings).put(className, classMappings);
         }

         return (String)((Map)classMappings).put(reference, newReference);
      } else {
         return null;
      }
   }

   public void write(Appendable writer) {
      (new GsonBuilder()).setPrettyPrinting().create().toJson(this, writer);
   }

   public static ReferenceMapper read(String resourcePath) {
      Logger logger = LogManager.getLogger("mixin");
      Reader reader = null;

      try {
         IMixinService service = MixinService.getService();
         InputStream resource = service.getResourceAsStream(resourcePath);
         if (resource != null) {
            reader = new InputStreamReader(resource);
            ReferenceMapper mapper = readJson(reader);
            mapper.setResourceName(resourcePath);
            ReferenceMapper var6 = mapper;
            return var6;
         }
      } catch (JsonParseException var11) {
         logger.error("Invalid REFMAP JSON in " + resourcePath + ": " + var11.getClass().getName() + " " + var11.getMessage());
      } catch (Exception var12) {
         logger.error("Failed reading REFMAP JSON from " + resourcePath + ": " + var12.getClass().getName() + " " + var12.getMessage());
      } finally {
         IOUtils.closeQuietly(reader);
      }

      return DEFAULT_MAPPER;
   }

   public static ReferenceMapper read(Reader reader, String name) {
      try {
         ReferenceMapper mapper = readJson(reader);
         mapper.setResourceName(name);
         return mapper;
      } catch (Exception var3) {
         return DEFAULT_MAPPER;
      }
   }

   private static ReferenceMapper readJson(Reader reader) {
      return (ReferenceMapper)(new Gson()).fromJson(reader, ReferenceMapper.class);
   }
}
