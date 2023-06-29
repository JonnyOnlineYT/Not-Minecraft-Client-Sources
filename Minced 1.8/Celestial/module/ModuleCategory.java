package Celestial.module;

public enum ModuleCategory {
    Combat("Combat"),
    Movement("Movement"),
    Render("Render"),
    Player("Player"),
    Util("Util");
    private final String displayName;

    ModuleCategory(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
