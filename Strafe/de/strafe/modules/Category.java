package de.strafe.modules;

public enum Category {

    COMBAT("Combat"), MOVEMENT("Movement"), PLAYER("Player"), MISC("Misc"), RENDER("Render"), GUI("Gui"), SCRIPTS("Scripts");

    private final String text;

    Category(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
