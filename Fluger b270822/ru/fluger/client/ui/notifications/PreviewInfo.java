/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.notifications;

import ru.fluger.client.ui.notifications.PreviewType;

public interface PreviewInfo {
    public PreviewType type();

    public String text();

    public int seconds();
}

