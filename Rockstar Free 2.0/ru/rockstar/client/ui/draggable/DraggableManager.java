package ru.rockstar.client.ui.draggable;


import java.util.ArrayList;

import ru.rockstar.client.ui.draggable.impl.*;

public class DraggableManager {

    public ArrayList<DraggableModule> mods = new ArrayList<>();

    public DraggableManager() {
    	mods.add(new ArrayComponent());
        mods.add(new KeystrokesComponent());
        mods.add(new InvPreviewComponent());
        mods.add(new TargetHudComponent());  
        mods.add(new StaffComponent());   
        mods.add(new PotionComponent());   
    }

    public ArrayList<DraggableModule> getMods() {
        return this.mods;
    }
    
    public void setMods(final ArrayList<DraggableModule> mods) {
        this.mods = mods;
    }
}