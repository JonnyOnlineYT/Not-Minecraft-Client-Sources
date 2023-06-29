package ru.rockstar.api.changelogs;


import java.util.ArrayList;

import ru.rockstar.Main;

public class ChangeManager {

    public static ArrayList<ChangeLog> changeLogs = new ArrayList<>();

    public ArrayList<ChangeLog> getChangeLogs() {
        return changeLogs;
    }

    public ChangeManager() {
        changeLogs.add(new ChangeLog("Version " + Main.instance.version, ChangeType.NONE));
        changeLogs.add(new ChangeLog("TimerIndicator", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("DamageFlyIndicator", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("KeyBinds - shows a list of all plug-ins and enabled modules", ChangeType.ADD));
        changeLogs.add(new ChangeLog("Redesigned minecraft buttons", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("CustomModel \"Amogus\"", ChangeType.ADD));
        changeLogs.add(new ChangeLog("CustomModel \"Crab\"", ChangeType.ADD));
        changeLogs.add(new ChangeLog("CustomModel \"Demon\"", ChangeType.ADD));
        changeLogs.add(new ChangeLog("CustomModel \"Freddy Bear\"", ChangeType.ADD));
        changeLogs.add(new ChangeLog("CustomModel \"Crazy Rabbit\"", ChangeType.ADD));
        changeLogs.add(new ChangeLog("NoSlowDown \"AAC\" - now perfectly bypasses MagicGrief and so on", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("Flight \"Packet\"", ChangeType.ADD));
        changeLogs.add(new ChangeLog("NoClip \"Packet\"", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("NoClip \"ReallyWorld\"", ChangeType.ADD));
        changeLogs.add(new ChangeLog("StaffAlert", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("NoFall - improved, now after landing zero damage", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("ClickGUI Style \"Rockstar\" - cool clickgui style, now default", ChangeType.ADD));
        changeLogs.add(new ChangeLog("Keystrokes - shows the keys pressed", ChangeType.ADD));
        changeLogs.add(new ChangeLog("InventoryManager", ChangeType.ADD));
        changeLogs.add(new ChangeLog("KillAura \"DoubleTap\" - hits 2 times instead of one", ChangeType.ADD));
        changeLogs.add(new ChangeLog("AutoTotem \"Swap Back\" - automatically returns to the hand", ChangeType.ADD));
        changeLogs.add(new ChangeLog("  the item that was before taking the totem", ChangeType.NONE));
        
        changeLogs.add(new ChangeLog("AutoTotem - improved + notification added when totem is placed", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("HighJump - throws you high up, works perfectly on ReallyWorld/NexusGrief", ChangeType.ADD));
        changeLogs.add(new ChangeLog("AntiBot \"Matrix New\" - improved, now works flawlessly", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("ShieldDesync \"New\" - improved, now works topically", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog(" ShieldDesync \"Ground\"", ChangeType.DELETE));
        changeLogs.add(new ChangeLog("ShieldDesync \"Packet\"", ChangeType.ADD));
        changeLogs.add(new ChangeLog("KillAura -\" ShieldBreaker - improved", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("HudEditor - allows dragging visual modules", ChangeType.ADD));
        changeLogs.add(new ChangeLog("InventoryPreview - inventory preview, made in the style of keybinds and keystrokes", ChangeType.ADD));
        changeLogs.add(new ChangeLog("Speed \"Matrix New\" - now do not flag when jumping off blocks", ChangeType.ADD));
        changeLogs.add(new ChangeLog("AutoTotem \"Check Crystal\"", ChangeType.FIXED));
        
        changeLogs.add(new ChangeLog("hclip - improved, now you can easily clip through walls in 1 block (works on ReallyWorld)", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("Jesus \"NCP\"", ChangeType.FIXED));
        changeLogs.add(new ChangeLog("Jesus \"ReallyWorld\" - improved, now bounces when you go", ChangeType.IMPROVED));
        changeLogs.add(new ChangeLog("  on land to eliminate the chance of a kick", ChangeType.NONE));
    }
}
