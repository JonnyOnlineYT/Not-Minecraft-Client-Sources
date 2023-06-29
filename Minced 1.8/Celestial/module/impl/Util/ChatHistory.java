package Celestial.module.impl.Util;

import Celestial.module.Module;
import Celestial.module.ModuleCategory;

public class ChatHistory
        extends Module {
    public ChatHistory() {
        super("ChatHistory", "Показывает историю чата, даже если вы перезашли на сервер", ModuleCategory.Util);
    }
}
