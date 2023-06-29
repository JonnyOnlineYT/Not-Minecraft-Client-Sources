package net.minecraft.util;

public interface Progress {
    void setTitle(String title);
    void setText(String text);
    void setProgress(int progress);
    void done();
}
