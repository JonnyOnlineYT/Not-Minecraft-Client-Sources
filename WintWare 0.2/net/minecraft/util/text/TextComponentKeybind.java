package net.minecraft.util.text;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public class TextComponentKeybind extends TextComponentBase {
   public static Function<String, Supplier<String>> field_193637_b = (p_193635_0_) -> {
      return () -> {
         return p_193635_0_;
      };
   };
   private final String field_193638_c;
   private Supplier<String> field_193639_d;

   public TextComponentKeybind(String p_i47521_1_) {
      this.field_193638_c = p_i47521_1_;
   }

   public String getUnformattedComponentText() {
      if (this.field_193639_d == null) {
         this.field_193639_d = (Supplier)field_193637_b.apply(this.field_193638_c);
      }

      return (String)this.field_193639_d.get();
   }

   public TextComponentKeybind createCopy() {
      TextComponentKeybind textcomponentkeybind = new TextComponentKeybind(this.field_193638_c);
      textcomponentkeybind.setStyle(this.getStyle().createShallowCopy());
      Iterator var2 = this.getSiblings().iterator();

      while(var2.hasNext()) {
         ITextComponent itextcomponent = (ITextComponent)var2.next();
         textcomponentkeybind.appendSibling(itextcomponent.createCopy());
      }

      return textcomponentkeybind;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof TextComponentKeybind)) {
         return false;
      } else {
         TextComponentKeybind textcomponentkeybind = (TextComponentKeybind)p_equals_1_;
         return this.field_193638_c.equals(textcomponentkeybind.field_193638_c) && super.equals(p_equals_1_);
      }
   }

   public String toString() {
      return "KeybindComponent{keybind='" + this.field_193638_c + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
   }

   public String func_193633_h() {
      return this.field_193638_c;
   }
}
