package me.independed.inceptice.modules.hud;

import me.independed.inceptice.modules.Module;

public class KeyStrokes extends Module {
      public KeyStrokes() {
            super("KeyStrokes", "keys that you are pressing", (654382784 | 506156582) << 2 >> 1 ^ -27873844, Module.Category.HUD);
            this.toggle();
      }
}
