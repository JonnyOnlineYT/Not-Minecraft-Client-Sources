package org.spongepowered.asm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.tree.LineNumberNode;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.analysis.Analyzer;
import org.spongepowered.asm.lib.tree.analysis.AnalyzerException;
import org.spongepowered.asm.lib.tree.analysis.BasicValue;
import org.spongepowered.asm.lib.tree.analysis.Frame;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.util.asm.MixinVerifier;
import org.spongepowered.asm.util.throwables.LVTGeneratorException;

public final class Locals {
   private static final Map calculatedLocalVariables = new HashMap();

   private Locals() {
   }

   public static void loadLocals(Type[] locals, InsnList insns, int pos, int limit) {
      for(; pos < locals.length && limit > 0; ++pos) {
         if (locals[pos] != null) {
            insns.add((AbstractInsnNode)(new VarInsnNode(locals[pos].getOpcode(21), pos)));
            --limit;
         }
      }

   }

   public static LocalVariableNode[] getLocalsAt(ClassNode classNode, MethodNode method, AbstractInsnNode node) {
      for(int i = 0; i < 3 && (node instanceof LabelNode || node instanceof LineNumberNode); ++i) {
         node = nextNode(method.instructions, node);
      }

      ClassInfo classInfo = ClassInfo.forName(classNode.name);
      if (classInfo == null) {
         throw new LVTGeneratorException("Could not load class metadata for " + classNode.name + " generating LVT for " + method.name);
      } else {
         ClassInfo.Method methodInfo = classInfo.findMethod(method);
         if (methodInfo == null) {
            throw new LVTGeneratorException("Could not locate method metadata for " + method.name + " generating LVT in " + classNode.name);
         } else {
            List frames = methodInfo.getFrames();
            LocalVariableNode[] frame = new LocalVariableNode[method.maxLocals];
            int local = 0;
            int index = 0;
            if ((method.access & 8) == 0) {
               frame[local++] = new LocalVariableNode("this", classNode.name, (String)null, (LabelNode)null, (LabelNode)null, 0);
            }

            Type[] var9 = Type.getArgumentTypes(method.desc);
            int frameIndex = var9.length;

            int locals;
            for(locals = 0; locals < frameIndex; ++locals) {
               Type argType = var9[locals];
               frame[local] = new LocalVariableNode("arg" + index++, argType.toString(), (String)null, (LabelNode)null, (LabelNode)null, local);
               local += argType.getSize();
            }

            int initialFrameSize = local;
            frameIndex = -1;
            locals = 0;
            Iterator iter = method.instructions.iterator();

            while(iter.hasNext()) {
               AbstractInsnNode insn = (AbstractInsnNode)iter.next();
               if (insn instanceof FrameNode) {
                  ++frameIndex;
                  FrameNode frameNode = (FrameNode)insn;
                  ClassInfo.FrameData frameData = frameIndex < frames.size() ? (ClassInfo.FrameData)frames.get(frameIndex) : null;
                  locals = frameData != null && frameData.type == 0 ? Math.min(locals, frameData.locals) : frameNode.local.size();
                  int localPos = 0;

                  for(int framePos = 0; framePos < frame.length; ++localPos) {
                     Object localType = localPos < frameNode.local.size() ? frameNode.local.get(localPos) : null;
                     if (localType instanceof String) {
                        frame[framePos] = getLocalVariableAt(classNode, method, node, framePos);
                     } else if (!(localType instanceof Integer)) {
                        if (localType != null) {
                           throw new LVTGeneratorException("Invalid value " + localType + " in locals array at position " + localPos + " in " + classNode.name + "." + method.name + method.desc);
                        }

                        if (framePos >= initialFrameSize && framePos >= locals && locals > 0) {
                           frame[framePos] = null;
                        }
                     } else {
                        boolean isMarkerType = localType == Opcodes.UNINITIALIZED_THIS || localType == Opcodes.NULL;
                        boolean is32bitValue = localType == Opcodes.INTEGER || localType == Opcodes.FLOAT;
                        boolean is64bitValue = localType == Opcodes.DOUBLE || localType == Opcodes.LONG;
                        if (localType != Opcodes.TOP) {
                           if (isMarkerType) {
                              frame[framePos] = null;
                           } else {
                              if (!is32bitValue && !is64bitValue) {
                                 throw new LVTGeneratorException("Unrecognised locals opcode " + localType + " in locals array at position " + localPos + " in " + classNode.name + "." + method.name + method.desc);
                              }

                              frame[framePos] = getLocalVariableAt(classNode, method, node, framePos);
                              if (is64bitValue) {
                                 ++framePos;
                                 frame[framePos] = null;
                              }
                           }
                        }
                     }

                     ++framePos;
                  }
               } else if (insn instanceof VarInsnNode) {
                  VarInsnNode varNode = (VarInsnNode)insn;
                  frame[varNode.var] = getLocalVariableAt(classNode, method, node, varNode.var);
               }

               if (insn == node) {
                  break;
               }
            }

            for(int l = 0; l < frame.length; ++l) {
               if (frame[l] != null && frame[l].desc == null) {
                  frame[l] = null;
               }
            }

            return frame;
         }
      }
   }

   public static LocalVariableNode getLocalVariableAt(ClassNode classNode, MethodNode method, AbstractInsnNode node, int var) {
      return getLocalVariableAt(classNode, method, method.instructions.indexOf(node), var);
   }

   private static LocalVariableNode getLocalVariableAt(ClassNode classNode, MethodNode method, int pos, int var) {
      LocalVariableNode localVariableNode = null;
      LocalVariableNode fallbackNode = null;
      Iterator var6 = getLocalVariableTable(classNode, method).iterator();

      LocalVariableNode local;
      while(var6.hasNext()) {
         local = (LocalVariableNode)var6.next();
         if (local.index == var) {
            if (isOpcodeInRange(method.instructions, local, pos)) {
               localVariableNode = local;
            } else if (localVariableNode == null) {
               fallbackNode = local;
            }
         }
      }

      if (localVariableNode == null && !method.localVariables.isEmpty()) {
         var6 = getGeneratedLocalVariableTable(classNode, method).iterator();

         while(var6.hasNext()) {
            local = (LocalVariableNode)var6.next();
            if (local.index == var && isOpcodeInRange(method.instructions, local, pos)) {
               localVariableNode = local;
            }
         }
      }

      return localVariableNode != null ? localVariableNode : fallbackNode;
   }

   private static boolean isOpcodeInRange(InsnList insns, LocalVariableNode local, int pos) {
      return insns.indexOf(local.start) < pos && insns.indexOf(local.end) > pos;
   }

   public static List getLocalVariableTable(ClassNode classNode, MethodNode method) {
      return method.localVariables.isEmpty() ? getGeneratedLocalVariableTable(classNode, method) : method.localVariables;
   }

   public static List getGeneratedLocalVariableTable(ClassNode classNode, MethodNode method) {
      String methodId = String.format("%s.%s%s", classNode.name, method.name, method.desc);
      List localVars = (List)calculatedLocalVariables.get(methodId);
      if (localVars != null) {
         return localVars;
      } else {
         localVars = generateLocalVariableTable(classNode, method);
         calculatedLocalVariables.put(methodId, localVars);
         return localVars;
      }
   }

   public static List generateLocalVariableTable(ClassNode classNode, MethodNode method) {
      List interfaces = null;
      if (classNode.interfaces != null) {
         interfaces = new ArrayList();
         Iterator var3 = classNode.interfaces.iterator();

         while(var3.hasNext()) {
            String iface = (String)var3.next();
            interfaces.add(Type.getObjectType(iface));
         }
      }

      Type objectType = null;
      if (classNode.superName != null) {
         objectType = Type.getObjectType(classNode.superName);
      }

      Analyzer analyzer = new Analyzer(new MixinVerifier(Type.getObjectType(classNode.name), objectType, interfaces, false));

      try {
         analyzer.analyze(classNode.name, method);
      } catch (AnalyzerException var18) {
         var18.printStackTrace();
      }

      Frame[] frames = analyzer.getFrames();
      int methodSize = method.instructions.size();
      List localVariables = new ArrayList();
      LocalVariableNode[] localNodes = new LocalVariableNode[method.maxLocals];
      BasicValue[] locals = new BasicValue[method.maxLocals];
      LabelNode[] labels = new LabelNode[methodSize];
      String[] lastKnownType = new String[method.maxLocals];

      for(int i = 0; i < methodSize; ++i) {
         Frame f = frames[i];
         if (f != null) {
            LabelNode label = null;

            for(int j = 0; j < f.getLocals(); ++j) {
               BasicValue local = (BasicValue)f.getLocal(j);
               if ((local != null || locals[j] != null) && (local == null || !local.equals(locals[j]))) {
                  if (label == null) {
                     AbstractInsnNode existingLabel = method.instructions.get(i);
                     if (existingLabel instanceof LabelNode) {
                        label = (LabelNode)existingLabel;
                     } else {
                        labels[i] = label = new LabelNode();
                     }
                  }

                  if (local == null && locals[j] != null) {
                     localVariables.add(localNodes[j]);
                     localNodes[j].end = label;
                     localNodes[j] = null;
                  } else if (local != null) {
                     if (locals[j] != null) {
                        localVariables.add(localNodes[j]);
                        localNodes[j].end = label;
                        localNodes[j] = null;
                     }

                     String desc = local.getType() != null ? local.getType().getDescriptor() : lastKnownType[j];
                     localNodes[j] = new LocalVariableNode("var" + j, desc, (String)null, label, (LabelNode)null, j);
                     if (desc != null) {
                        lastKnownType[j] = desc;
                     }
                  }

                  locals[j] = local;
               }
            }
         }
      }

      LabelNode label = null;

      int n;
      for(n = 0; n < localNodes.length; ++n) {
         if (localNodes[n] != null) {
            if (label == null) {
               label = new LabelNode();
               method.instructions.add((AbstractInsnNode)label);
            }

            localNodes[n].end = label;
            localVariables.add(localNodes[n]);
         }
      }

      for(n = methodSize - 1; n >= 0; --n) {
         if (labels[n] != null) {
            method.instructions.insert(method.instructions.get(n), (AbstractInsnNode)labels[n]);
         }
      }

      return localVariables;
   }

   private static AbstractInsnNode nextNode(InsnList insns, AbstractInsnNode insn) {
      int index = insns.indexOf(insn) + 1;
      return index > 0 && index < insns.size() ? insns.get(index) : insn;
   }
}
