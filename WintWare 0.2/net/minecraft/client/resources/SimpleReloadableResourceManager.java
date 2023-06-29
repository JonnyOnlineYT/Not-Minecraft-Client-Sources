package net.minecraft.client.resources;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager implements IReloadableResourceManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Joiner JOINER_RESOURCE_PACKS = Joiner.on(", ");
   private final Map<String, FallbackResourceManager> domainResourceManagers = Maps.newHashMap();
   private final List<IResourceManagerReloadListener> reloadListeners = Lists.newArrayList();
   private final Set<String> setResourceDomains = Sets.newLinkedHashSet();
   private final MetadataSerializer rmMetadataSerializer;

   public SimpleReloadableResourceManager(MetadataSerializer rmMetadataSerializerIn) {
      this.rmMetadataSerializer = rmMetadataSerializerIn;
   }

   public void reloadResourcePack(IResourcePack resourcePack) {
      FallbackResourceManager fallbackresourcemanager;
      for(Iterator var2 = resourcePack.getResourceDomains().iterator(); var2.hasNext(); fallbackresourcemanager.addResourcePack(resourcePack)) {
         String s = (String)var2.next();
         this.setResourceDomains.add(s);
         fallbackresourcemanager = (FallbackResourceManager)this.domainResourceManagers.get(s);
         if (fallbackresourcemanager == null) {
            fallbackresourcemanager = new FallbackResourceManager(this.rmMetadataSerializer);
            this.domainResourceManagers.put(s, fallbackresourcemanager);
         }
      }

   }

   public Set<String> getResourceDomains() {
      return this.setResourceDomains;
   }

   public IResource getResource(ResourceLocation location) throws IOException {
      IResourceManager iresourcemanager = (IResourceManager)this.domainResourceManagers.get(location.getResourceDomain());
      if (iresourcemanager != null) {
         return iresourcemanager.getResource(location);
      } else {
         throw new FileNotFoundException(location.toString());
      }
   }

   public List<IResource> getAllResources(ResourceLocation location) throws IOException {
      IResourceManager iresourcemanager = (IResourceManager)this.domainResourceManagers.get(location.getResourceDomain());
      if (iresourcemanager != null) {
         return iresourcemanager.getAllResources(location);
      } else {
         throw new FileNotFoundException(location.toString());
      }
   }

   private void clearResources() {
      this.domainResourceManagers.clear();
      this.setResourceDomains.clear();
   }

   public void reloadResources(List<IResourcePack> resourcesPacksList) {
      this.clearResources();
      LOGGER.info("Reloading ResourceManager: {}", JOINER_RESOURCE_PACKS.join(Iterables.transform(resourcesPacksList, new Function<IResourcePack, String>() {
         public String apply(@Nullable IResourcePack p_apply_1_) {
            return p_apply_1_ == null ? "<NULL>" : p_apply_1_.getPackName();
         }
      })));
      Iterator var2 = resourcesPacksList.iterator();

      while(var2.hasNext()) {
         IResourcePack iresourcepack = (IResourcePack)var2.next();
         this.reloadResourcePack(iresourcepack);
      }

      this.notifyReloadListeners();
   }

   public void registerReloadListener(IResourceManagerReloadListener reloadListener) {
      this.reloadListeners.add(reloadListener);
      reloadListener.onResourceManagerReload(this);
   }

   private void notifyReloadListeners() {
      Iterator var1 = this.reloadListeners.iterator();

      while(var1.hasNext()) {
         IResourceManagerReloadListener iresourcemanagerreloadlistener = (IResourceManagerReloadListener)var1.next();
         iresourcemanagerreloadlistener.onResourceManagerReload(this);
      }

   }
}
