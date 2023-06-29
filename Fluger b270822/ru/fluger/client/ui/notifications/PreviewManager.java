/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.notifications;

import java.util.ArrayList;
import java.util.UUID;
import ru.fluger.client.ui.notifications.Preview;
import ru.fluger.client.ui.notifications.PreviewInfo;
import ru.fluger.client.ui.notifications.PreviewType;

public class PreviewManager {
    private ArrayList<Preview> list = new ArrayList();

    public Preview get(UUID id) {
        for (Preview p : this.list) {
            if (p.getUuid() != id) continue;
            return p;
        }
        return null;
    }

    public void remove(UUID id) {
        if (this.has(id)) {
            this.list.remove(this.get(id));
        }
    }

    public void remove(Preview preview) {
        if (this.has(preview)) {
            this.list.remove(preview);
        }
    }

    public boolean has(UUID id) {
        for (Preview p : this.list) {
            if (p.getUuid() != id) continue;
            return true;
        }
        return false;
    }

    public boolean has(Preview preview) {
        for (Preview p : this.list) {
            if (p.getUuid() != preview.getUuid()) continue;
            return true;
        }
        return false;
    }

    public void add(Preview preview) {
        if (!this.has(preview)) {
            this.list.add(preview);
        }
    }

    public void add(final PreviewType type, final String text, final int second, boolean rendericon) {
        Preview preview = new Preview(new PreviewInfo(){

            @Override
            public PreviewType type() {
                return type;
            }

            @Override
            public int seconds() {
                return second;
            }

            @Override
            public String text() {
                return text;
            }
        });
        this.add(preview);
    }

    public ArrayList<Preview> getList() {
        return this.list;
    }
}

