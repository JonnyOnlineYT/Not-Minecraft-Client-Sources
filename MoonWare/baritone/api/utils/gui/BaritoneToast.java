/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.api.utils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.toasts.ToastHud;
import net.minecraft.client.gui.toasts.Toast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;
import org.moonware.client.utils.MWFont;

public class BaritoneToast implements Toast {
    private String title;
    private String subtitle;
    private long firstDrawTime;
    private boolean newDisplay;
    private long totalShowTime;

    public BaritoneToast(Component titleComponent, Component subtitleComponent, long totalShowTime) {
        title = titleComponent.asFormattedString();
        subtitle = subtitleComponent == null ? null : subtitleComponent.asFormattedString();
        this.totalShowTime = totalShowTime;
    }

    public void setDisplayedText(Component titleComponent, Component subtitleComponent) {
        title = titleComponent.asFormattedString();
        subtitle = subtitleComponent == null ? null : subtitleComponent.asFormattedString();
        newDisplay = true;
    }

    public static void addOrUpdate(ToastHud toast, Component title, Component subtitle, long totalShowTime) {
        BaritoneToast baritonetoast = toast.getToast(BaritoneToast.class, new Object());

        if (baritonetoast == null) {
            toast.queue(new BaritoneToast(title, subtitle, totalShowTime));
        } else {
            baritonetoast.setDisplayedText(title, subtitle);
        }
    }

    public static void addOrUpdate(Component title, Component subtitle) {
        addOrUpdate(Minecraft.getToastHud(), title, subtitle, baritone.api.BaritoneAPI.getSettings().toastTimer.value);
    }

    @Override
    public Visibility draw(long delta) {
        if (newDisplay) {
            firstDrawTime = delta;
            newDisplay = false;
        }

        Minecraft.getTextureManager().bindTexture(new Namespaced("textures/gui/toasts.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 255.0f);
        Gui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);

        if (subtitle == null) {
            MWFont.SF_UI_DISPLAY_REGULAR.get(18).draw(title, 18, 12, -11534256);
        } else {
            MWFont.SF_UI_DISPLAY_REGULAR.get(18).draw(title, 18, 7, -11534256);
            MWFont.SF_UI_DISPLAY_REGULAR.get(18).draw(subtitle, 18, 18, -16777216);
        }

        return delta - firstDrawTime < totalShowTime ? Visibility.SHOW : Visibility.HIDE;
    }
}
