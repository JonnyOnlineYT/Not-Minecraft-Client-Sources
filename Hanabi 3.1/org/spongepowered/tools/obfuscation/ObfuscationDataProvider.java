package org.spongepowered.tools.obfuscation;

import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;

public class ObfuscationDataProvider implements IObfuscationDataProvider {
   private final IMixinAnnotationProcessor ap;
   private final List environments;

   public ObfuscationDataProvider(IMixinAnnotationProcessor ap, List environments) {
      this.ap = ap;
      this.environments = environments;
   }

   public ObfuscationData getObfEntryRecursive(MemberInfo targetMember) {
      MemberInfo currentTarget = targetMember;
      ObfuscationData obfTargetNames = this.getObfClass(targetMember.owner);
      ObfuscationData obfData = this.getObfEntry(targetMember);

      try {
         while(obfData.isEmpty()) {
            TypeHandle targetType = this.ap.getTypeProvider().getTypeHandle(currentTarget.owner);
            if (targetType == null) {
               return obfData;
            }

            TypeHandle superClass = targetType.getSuperclass();
            obfData = this.getObfEntryUsing(currentTarget, superClass);
            if (!obfData.isEmpty()) {
               return applyParents(obfTargetNames, obfData);
            }

            Iterator var7 = targetType.getInterfaces().iterator();

            while(var7.hasNext()) {
               TypeHandle iface = (TypeHandle)var7.next();
               obfData = this.getObfEntryUsing(currentTarget, iface);
               if (!obfData.isEmpty()) {
                  return applyParents(obfTargetNames, obfData);
               }
            }

            if (superClass == null) {
               break;
            }

            currentTarget = currentTarget.move(superClass.getName());
         }

         return obfData;
      } catch (Exception var9) {
         var9.printStackTrace();
         return this.getObfEntry(targetMember);
      }
   }

   private ObfuscationData getObfEntryUsing(MemberInfo targetMember, TypeHandle targetClass) {
      return targetClass == null ? new ObfuscationData() : this.getObfEntry(targetMember.move(targetClass.getName()));
   }

   public ObfuscationData getObfEntry(MemberInfo targetMember) {
      return targetMember.isField() ? this.getObfField(targetMember) : this.getObfMethod(targetMember.asMethodMapping());
   }

   public ObfuscationData getObfEntry(IMapping mapping) {
      if (mapping != null) {
         if (mapping.getType() == IMapping.Type.FIELD) {
            return this.getObfField((MappingField)mapping);
         }

         if (mapping.getType() == IMapping.Type.METHOD) {
            return this.getObfMethod((MappingMethod)mapping);
         }
      }

      return new ObfuscationData();
   }

   public ObfuscationData getObfMethodRecursive(MemberInfo targetMember) {
      return this.getObfEntryRecursive(targetMember);
   }

   public ObfuscationData getObfMethod(MemberInfo method) {
      return this.getRemappedMethod(method, method.isConstructor());
   }

   public ObfuscationData getRemappedMethod(MemberInfo method) {
      return this.getRemappedMethod(method, true);
   }

   private ObfuscationData getRemappedMethod(MemberInfo method, boolean remapDescriptor) {
      ObfuscationData data = new ObfuscationData();
      Iterator var4 = this.environments.iterator();

      while(var4.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var4.next();
         MappingMethod obfMethod = env.getObfMethod(method);
         if (obfMethod != null) {
            data.put(env.getType(), obfMethod);
         }
      }

      if (data.isEmpty() && remapDescriptor) {
         return this.remapDescriptor(data, method);
      } else {
         return data;
      }
   }

   public ObfuscationData getObfMethod(MappingMethod method) {
      return this.getRemappedMethod(method, method.isConstructor());
   }

   public ObfuscationData getRemappedMethod(MappingMethod method) {
      return this.getRemappedMethod(method, true);
   }

   private ObfuscationData getRemappedMethod(MappingMethod method, boolean remapDescriptor) {
      ObfuscationData data = new ObfuscationData();
      Iterator var4 = this.environments.iterator();

      while(var4.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var4.next();
         MappingMethod obfMethod = env.getObfMethod(method);
         if (obfMethod != null) {
            data.put(env.getType(), obfMethod);
         }
      }

      if (data.isEmpty() && remapDescriptor) {
         return this.remapDescriptor(data, new MemberInfo(method));
      } else {
         return data;
      }
   }

   public ObfuscationData remapDescriptor(ObfuscationData data, MemberInfo method) {
      Iterator var3 = this.environments.iterator();

      while(var3.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var3.next();
         MemberInfo obfMethod = env.remapDescriptor(method);
         if (obfMethod != null) {
            data.put(env.getType(), obfMethod.asMethodMapping());
         }
      }

      return data;
   }

   public ObfuscationData getObfFieldRecursive(MemberInfo targetMember) {
      return this.getObfEntryRecursive(targetMember);
   }

   public ObfuscationData getObfField(MemberInfo field) {
      return this.getObfField(field.asFieldMapping());
   }

   public ObfuscationData getObfField(MappingField field) {
      ObfuscationData data = new ObfuscationData();
      Iterator var3 = this.environments.iterator();

      while(var3.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var3.next();
         MappingField obfField = env.getObfField(field);
         if (obfField != null) {
            if (obfField.getDesc() == null && field.getDesc() != null) {
               obfField = obfField.transform(env.remapDescriptor(field.getDesc()));
            }

            data.put(env.getType(), obfField);
         }
      }

      return data;
   }

   public ObfuscationData getObfClass(TypeHandle type) {
      return this.getObfClass(type.getName());
   }

   public ObfuscationData getObfClass(String className) {
      ObfuscationData data = new ObfuscationData(className);
      Iterator var3 = this.environments.iterator();

      while(var3.hasNext()) {
         ObfuscationEnvironment env = (ObfuscationEnvironment)var3.next();
         String obfClass = env.getObfClass(className);
         if (obfClass != null) {
            data.put(env.getType(), obfClass);
         }
      }

      return data;
   }

   private static ObfuscationData applyParents(ObfuscationData parents, ObfuscationData members) {
      Iterator var2 = members.iterator();

      while(var2.hasNext()) {
         ObfuscationType type = (ObfuscationType)var2.next();
         String obfClass = (String)parents.get(type);
         Object obfMember = members.get(type);
         members.put(type, MemberInfo.fromMapping((IMapping)obfMember).move(obfClass).asMapping());
      }

      return members;
   }
}
