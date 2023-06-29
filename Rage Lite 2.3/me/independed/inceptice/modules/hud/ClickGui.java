//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.hud;

import java.io.IOException;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClickGui extends Module {
      public NumberSetting colorSet1;
      public NumberSetting colorSet3;
      me.independed.inceptice.clickguis.click.ClickGui clickGui;
      public ModeSetting rainbowSet;
      public NumberSetting colorSet2;

      public void onEnable() {
            Minecraft var10000 = mc;
            if (!"buy a domain and everything else you need at namecheap.com".equals("your mom your dad the one you never had")) {
                  ;
            }

            var10000.displayGuiScreen(this.clickGui);
            this.toggle();
      }

      public ClickGui() throws IOException {
            super("ClickGui", "gui menu", (12 & 4 | 0) & 2 ^ 73, Module.Category.HUD);
            if (!"nefariousMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            NumberSetting var10001 = new NumberSetting;
            if ((1042627388 >>> 4 & 27893036 ^ 27267360) == 0) {
                  ;
            }

            var10001.<init>("Red", this, 255.0D, 0.0D, 255.0D, 1.0D);
            this.colorSet1 = var10001;
            var10001 = new NumberSetting;
            if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                  ;
            }

            var10001.<init>("Green", this, 0.0D, 0.0D, 255.0D, 1.0D);
            if ((((1315881243 ^ 334123874) & 889672931) >> 1 >>> 1 ^ -762526691) != 0) {
                  ;
            }

            this.colorSet2 = var10001;
            var10001 = new NumberSetting;
            if ((914345424 >> 4 >> 4 >>> 4 ^ 133094 ^ 90138) == 0) {
                  ;
            }

            if (!"please dont crack my plugin".equals("i hope you catch fire ngl")) {
                  ;
            }

            var10001.<init>("Blue", this, 0.0D, 0.0D, 255.0D, 1.0D);
            if (!"minecraft".equals("shitted on you harder than archybot")) {
                  ;
            }

            this.colorSet3 = var10001;
            if ((1352652064 << 1 >> 4 ^ -99353948) == 0) {
                  ;
            }

            if (((164068 << 1 ^ '쬁') >>> 2 << 1 ^ -1969296927) != 0) {
                  ;
            }

            ModeSetting var1 = new ModeSetting;
            if (!"you're dogshit".equals("your mom your dad the one you never had")) {
                  ;
            }

            String[] var10006 = new String[(0 >> 4 ^ 141399634) >> 4 ^ 8837476];
            var10006[187533729 >>> 4 ^ 11720858] = "rainbow";
            var1.<init>("Theme", this, "default", var10006);
            this.rainbowSet = var1;
            me.independed.inceptice.clickguis.click.ClickGui var2 = new me.independed.inceptice.clickguis.click.ClickGui();
            if (((1466173672 ^ 174813127) << 1 ^ 2101852244 ^ 1806288307) != 0) {
                  ;
            }

            this.clickGui = var2;
            this.settings.add(this.colorSet1);
            this.settings.add(this.colorSet2);
            if (((16843068 << 2 & 48511942 | 171866) ^ -443113673) != 0) {
                  ;
            }

            if (((1123402756 | 471821194) << 2 >> 1 ^ 311936420 ^ -1955363659) != 0) {
                  ;
            }

            if ((1898200922 >>> 1 >>> 2 << 1 >>> 4 ^ 29659389) == 0) {
                  ;
            }

            this.settings.add(this.colorSet3);
            this.settings.add(this.rainbowSet);
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            if (((66266186 ^ 23383663 | 19942129) >> 2 ^ 2670326 ^ 146426074) != 0) {
                  ;
            }

      }
}
