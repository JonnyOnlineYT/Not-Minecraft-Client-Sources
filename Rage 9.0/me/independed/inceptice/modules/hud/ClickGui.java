//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.hud;

import java.io.IOException;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClickGui extends Module {
      public ModeSetting rainbowSet;
      public NumberSetting colorSet3;
      me.independed.inceptice.clickguis.click.ClickGui clickGui;
      public NumberSetting colorSet2;
      public NumberSetting colorSet1 = new NumberSetting("Red", this, 255.0D, 0.0D, 255.0D, 1.0D);

      public void onEnable() {
            mc.displayGuiScreen(this.clickGui);
            this.toggle();
      }

      public ClickGui() throws IOException {
            super("ClickGui", "gui menu", (36 ^ 18) << 1 ^ 37, Module.Category.HUD);
            NumberSetting var10001 = new NumberSetting;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("yo mama name maurice")) {
                  ;
            }

            var10001.<init>("Green", this, 0.0D, 0.0D, 255.0D, 1.0D);
            this.colorSet2 = var10001;
            var10001 = new NumberSetting;
            if ((((4525088 ^ 2890099) & 1155066 ^ 69049) >> 2 ^ 1594) == 0) {
                  ;
            }

            var10001.<init>("Blue", this, 0.0D, 0.0D, 255.0D, 1.0D);
            if (((312993251 << 2 ^ 1079656138) >>> 1 ^ 1320613890) != 0) {
                  ;
            }

            this.colorSet3 = var10001;
            String[] var10006 = new String[0 << 2 << 4 ^ 1];
            var10006[284576488 >> 1 >>> 3 >> 3 << 3 ^ 17786024] = "rainbow";
            this.rainbowSet = new ModeSetting("Theme", this, "default", var10006);
            this.clickGui = new me.independed.inceptice.clickguis.click.ClickGui();
            this.settings.add(this.colorSet1);
            this.settings.add(this.colorSet2);
            if ((1853516663 >>> 3 >> 2 ^ 57922395) == 0) {
                  ;
            }

            this.settings.add(this.colorSet3);
            this.settings.add(this.rainbowSet);
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
      }
}
