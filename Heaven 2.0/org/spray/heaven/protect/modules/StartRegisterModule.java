package org.spray.heaven.protect.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.Provider;
import org.spray.heaven.protect.events.ClientInitializeEvent;
import org.spray.heaven.protect.events.StartRegisterEvent;
import org.spray.heaven.protect.ui.AuthUI;

public class StartRegisterModule extends ProtectModule {

    @EventTarget(4)
    public void onInit(StartRegisterEvent event) {
        switch (event.getType()) {
            case PRE:
                Provider.getKeyAuth().preInit();
                break;
            case POST:
                EventManager.call(new ClientInitializeEvent(EventType.PRE));
                Provider.getKeyAuth().posInit();
                Wrapper.MC.displayGuiScreen(new AuthUI());
                break;
            default:
                break;
        }
    }

}
