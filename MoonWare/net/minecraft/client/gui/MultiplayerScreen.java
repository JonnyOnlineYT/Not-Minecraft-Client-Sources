package net.minecraft.client.gui;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClientButtonWidget;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Formatting;
import org.lwjgl.input.Keyboard;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.qol.QOLConfig;
import org.moonware.client.qol.ui.DropDownMenu;
import org.moonware.client.ui.titlescreen.TitleLikeScreen;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiplayerScreen extends TitleLikeScreen {
    public static final List<ProtocolVersion> PROTOCOLS;
    public static final Function<ProtocolVersion, String> PROTOCOL_TO_STRING = pv -> {
        String s = pv == QOLConfig.via ? Formatting.UNDERLINE.toString() : "";
        if (pv == null) return Formatting.RED + s + "Off";
        if (!pv.isKnown()) return Formatting.GOLD + s + "Auto";
        return s + pv.getName();
    };
    static {
        List<ProtocolVersion> protocols = new ArrayList<>();
        protocols.add(null);
        protocols.add(ProtocolVersion.unknown);
        protocols.addAll(ProtocolVersion.getProtocols().stream().filter(p -> p.getVersion() >= 340).collect(Collectors.toList()));
        PROTOCOLS = protocols;
    }

    @Getter private final ServerPinger pinger = new ServerPinger();
    private ServerSelectionList serverListSelector;
    private ServerList savedServerList;
    private ButtonWidget btnEditServer;
    private ButtonWidget btnSelectServer;
    private ButtonWidget btnDeleteServer;
    private boolean deletingServer;
    private boolean addingServer;
    private boolean editingServer;
    private boolean directConnect;
    private ServerData selectedServer;
    private boolean initialized;
    private DropDownMenu<ProtocolVersion> menu;

    @Override
    public void init() {
        super.init();
        menu = null;
        Keyboard.enableRepeatEvents(true);
        if (initialized) {
            serverListSelector.setDimensions(width, height, 32, height - 64);
        } else {
            initialized = true;
            savedServerList = new ServerList(minecraft);
            savedServerList.loadServerList();
            serverListSelector = new ServerSelectionList();
            serverListSelector.updateOnlineServers(savedServerList);
        }
        widgets.add(btnSelectServer = new ClientButtonWidget(Minecraft.getScaledRoundedWidth()-142 + 7,
                75, 128, 20, I18n.format("selectServer.select"), btn -> {
            join();
        }));
        widgets.add(btnEditServer = new ClientButtonWidget(7, Minecraft.getScaledRoundedWidth()-142 + 7, 75 + 27+27+27, 128, 20, I18n.format("selectServer.edit")));
        widgets.add(btnDeleteServer = new ClientButtonWidget(2, Minecraft.getScaledRoundedWidth()-142 + 7, 75 + 27+27+27+27, 128, 20, I18n.format("selectServer.delete")));
        widgets.add(new ClientButtonWidget(4, Minecraft.getScaledRoundedWidth()-142 + 7, 75 + 27, 128, 20, I18n.format("selectServer.direct")));
        widgets.add(new ClientButtonWidget(3, Minecraft.getScaledRoundedWidth()-142 + 7, 75 + 27+27, 128, 20, I18n.format("selectServer.add")));
        widgets.add(new ClientButtonWidget(8, Minecraft.getScaledRoundedWidth()-142 + 7, 75 + 27+27+27+27+27, 128, 20, I18n.format("selectServer.refresh")));
        selectServer(serverListSelector.getSelected());
        widgets.add(new ClientButtonWidget(Minecraft.getScaledRoundedWidth()-142 + 7,
                75 - 27, 128, 20, "Via: " + (QOLConfig.via == null ? "Off" : QOLConfig.via.isKnown() ? QOLConfig.via.getName() : "Auto"),
                btn -> menu = new DropDownMenu<>(btn.x, btn.y + btn.height, PROTOCOLS,
                        PROTOCOL_TO_STRING, v -> {
                    QOLConfig.via = v;
                    btn.text = "Via: " + (v == null ? "Off" : v.isKnown() ? v.getName() : "Auto");
                })
        ));
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawTitle();
        serverListSelector.drawMultiScreen(mouseX, mouseY, partialTick);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.pushMatrix();
        RenderUtils2.drawBlur(15,() -> RoundedUtil.drawRound(sr.getScaledWidth() - 9-142  + 9,  24, 162, Minecraft.getScaledRoundedHeight(), 4, true, new Color(0, 0, 0, 90)));
        RoundedUtil.drawRound(sr.getScaledWidth() - 9-142 + 9,  24, 162, Minecraft.getScaledRoundedHeight(), 4, true, new Color(0, 0, 0, 50));
        MWFont.MONTSERRAT_BOLD.get(30).drawCenterShadow(I18n.format("multiplayer.title"), sr.getScaledWidth() - 69, 28f, -1);
        GlStateManager.popMatrix();
        super.draw(mouseX, mouseY, partialTick);
        if (menu != null) menu.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public void update() {
        super.update();
        pinger.pingPendingNetworks();
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {
        if (menu != null) {
            if (menu.mousePressed(mouseX, mouseY, button)) return;
            menu = null;
            return;
        }
        super.mousePressed(mouseX, mouseY, button);
        serverListSelector.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        serverListSelector.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseScrolled(int mouseX, int mouseY, int scroll) {
        if (menu != null) menu.mouseScrolled(mouseX, mouseY, scroll);
        super.mouseScrolled(mouseX, mouseY, scroll);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        serverListSelector.handleMouseInput();
    }

    @Override
    public void onClosed() {
        super.onClosed();
        Keyboard.enableRepeatEvents(false);
        pinger.clearPendingNetworks();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    public void actionPerformed(ButtonWidget button) {
        if (button.enabled) {
            GuiListExtended.IGuiListEntry guilistextended$iguilistentry = serverListSelector.getSelected() < 0 ? null : serverListSelector.getListEntry(serverListSelector.getSelected());

            if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                String s4 = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData().name;

                if (s4 != null) {
                    deletingServer = true;
                    String s = I18n.format("selectServer.deleteQuestion");
                    String s1 = "'" + s4 + "' " + I18n.format("selectServer.deleteWarning");
                    String s2 = I18n.format("selectServer.deleteButton");
                    String s3 = I18n.format("gui.cancel");
                    ConfirmScreen guiyesno = new ConfirmScreen(b -> {
                        if (b) {
                            savedServerList.removeServerData(serverListSelector.getSelected());
                            savedServerList.saveServerList();
                            serverListSelector.select(-1);
                            serverListSelector.updateOnlineServers(savedServerList);
                        }
                        Minecraft.openScreen(this);
                    }, s, s1, s2, s3);
                    Minecraft.openScreen(guiyesno);
                }
            } else if (button.id == 1) {

            } else if (button.id == 4) {
                directConnect = true;
                selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "");
                Minecraft.openScreen(new GuiScreenServerList(this, selectedServer));
            } else if (button.id == 3) {
                addingServer = true;
                selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "");
                Minecraft.openScreen(new GuiScreenAddServer(this, selectedServer));
            } else if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                editingServer = true;
                ServerData serverdata = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData();
                selectedServer = new ServerData(serverdata.name, serverdata.ip);
                selectedServer.copyFrom(serverdata);
                Minecraft.openScreen(new GuiScreenAddServer(this, selectedServer));
            } else if (button.id == 8) {
                refreshServerList();
            }
        }
    }

    private void refreshServerList() {
        Minecraft.openScreen(new MultiplayerScreen());
    }

    public void confirmClicked(boolean result, int id) {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = serverListSelector.getSelected() < 0 ? null : serverListSelector.getListEntry(serverListSelector.getSelected());

        if (deletingServer) {
            deletingServer = false;
        } else if (directConnect) {
            directConnect = false;

            if (result) {
                connectToServer(selectedServer);
            } else {
                Minecraft.openScreen(this);
            }
        } else if (addingServer) {
            addingServer = false;

            if (result) {
                savedServerList.addServerData(selectedServer);
                savedServerList.saveServerList();
                serverListSelector.select(-1);
                serverListSelector.updateOnlineServers(savedServerList);
            }

            Minecraft.openScreen(this);
        } else if (editingServer) {
            editingServer = false;

            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                ServerData serverdata = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData();
                serverdata.name = selectedServer.name;
                serverdata.ip = selectedServer.ip;
                serverdata.copyFrom(selectedServer);
                savedServerList.saveServerList();
                serverListSelector.updateOnlineServers(savedServerList);
            }

            Minecraft.openScreen(this);
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c) {
        int i = serverListSelector.getSelected();
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null : serverListSelector.getListEntry(i);

        if (key == 63) {
            refreshServerList();
        } else {
            if (i >= 0) {
                if (key == 200) {
                    if (Screen.hasShiftDown()) {
                        if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                            savedServerList.swapServers(i, i - 1);
                            selectServer(serverListSelector.getSelected() - 1);
                            serverListSelector.scrollBy(-serverListSelector.getEntryHeight());
                            serverListSelector.updateOnlineServers(savedServerList);
                        }
                    } else if (i > 0) {
                        selectServer(serverListSelector.getSelected() - 1);
                        serverListSelector.scrollBy(-serverListSelector.getEntryHeight());
                    } else {
                        selectServer(-1);
                    }
                } else if (key == 208) {
                    if (Screen.hasShiftDown()) {
                        if (i < savedServerList.getSize() - 1) {
                            savedServerList.swapServers(i, i + 1);
                            selectServer(i + 1);
                            serverListSelector.scrollBy(serverListSelector.getEntryHeight());
                            serverListSelector.updateOnlineServers(savedServerList);
                        }
                    } else if (i < serverListSelector.getSize()) {
                        selectServer(serverListSelector.getSelected() + 1);
                        serverListSelector.scrollBy(serverListSelector.getEntryHeight());
                    } else {
                        selectServer(-1);
                    }
                } else if (key != 28 && key != 156) {
                    super.keyPressed(key, c);
                } else {
                    actionPerformed(widgets.get(2));
                }
            } else {
                super.keyPressed(key, c);
            }
        }
    }

    public void join() {
        ServerListEntryNormal entry = serverListSelector.getSelected() < 0 ? null : serverListSelector.getListEntry(serverListSelector.getSelected());
        if (entry != null) {
            connectToServer(entry.getServerData());
        }
    }

    private void connectToServer(ServerData server) {
        Minecraft.openScreen(new ConnectingScreen(this, minecraft, server));
    }

    public void selectServer(int index) {
        serverListSelector.select(index);
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null : serverListSelector.getListEntry(index);
        btnSelectServer.enabled = false;
        btnEditServer.enabled = false;
        btnDeleteServer.enabled = false;

        if (guilistextended$iguilistentry != null) {
            btnSelectServer.enabled = true;
            btnEditServer.enabled = true;
            btnDeleteServer.enabled = true;
        }
    }

    public ServerList getServerList() {
        return savedServerList;
    }

    public boolean canMoveUp(ServerListEntryNormal p_175392_1_, int p_175392_2_) {
        return p_175392_2_ > 0;
    }

    public boolean canMoveDown(ServerListEntryNormal p_175394_1_, int p_175394_2_) {
        return p_175394_2_ < savedServerList.getSize() - 1;
    }

    public void moveServerUp(ServerListEntryNormal p_175391_1_, int p_175391_2_, boolean p_175391_3_) {
        int i = p_175391_3_ ? 0 : p_175391_2_ - 1;
        savedServerList.swapServers(p_175391_2_, i);

        if (serverListSelector.getSelected() == p_175391_2_) {
            selectServer(i);
        }

        serverListSelector.updateOnlineServers(savedServerList);
    }

    public void moveServerDown(ServerListEntryNormal p_175393_1_, int p_175393_2_, boolean p_175393_3_) {
        int i = p_175393_3_ ? savedServerList.getSize() - 1 : p_175393_2_ + 1;
        savedServerList.swapServers(p_175393_2_, i);

        if (serverListSelector.getSelected() == p_175393_2_) {
            selectServer(i);
        }

        serverListSelector.updateOnlineServers(savedServerList);
    }

    public class ServerSelectionList extends GuiListExtended {
        private final List<ServerListEntryNormal> entries = new ArrayList<>();
        public ServerSelectionList() {
            super(minecraft, MultiplayerScreen.this.width, MultiplayerScreen.this.height, 32, MultiplayerScreen.this.height - 64, 36);
        }

        @Override
        public ServerListEntryNormal getListEntry(int index) {
            return entries.get(index);
        }

        @Override
        public int getSize() {
            return entries.size();
        }

        @Override
        public boolean isSelected(int slotIndex) {
            return slotIndex == selectedElement;
        }

        public void select(int i) {
            selectedElement = i;
        }

        public int getSelected() {
            return selectedElement;
        }

        public void updateOnlineServers(ServerList list) {
            entries.clear();
            for (int i = 0; i < list.getSize(); ++i) {
                entries.add(new ServerListEntryNormal(list.getServerData(i)));
            }
        }

        protected int getScrollBarX() {
            return super.getScrollBarX() + 30;
        }

        public int getListWidth() {
            return super.getListWidth() + 85;
        }
    }

    public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry {

        private final Namespaced UNKNOWN_SERVER = new Namespaced("textures/misc/unknown_server.png");
        private final Namespaced SERVER_SELECTION_BUTTONS = new Namespaced("textures/gui/server_selection.png");
        private final ServerData server;
        private final Namespaced serverIcon;
        private String lastIconB64;
        private DynamicTexture icon;
        private long lastClickTime;

        protected ServerListEntryNormal(ServerData server) {
            this.server = server;
            serverIcon = new Namespaced("servers/" + server.ip + "/icon");
            icon = (DynamicTexture) Minecraft.getTextureManager().getTexture(serverIcon);
        }

        public void draw(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_) {
            if (!server.pinged) {
                server.pinged = true;
                server.pingToServer = -2L;
                server.motd = "";
                server.onlineMax = "";
                ServerPinger.EXECUTOR.execute(() -> {
                    try {
                        getPinger().ping(server);
                    } catch (UnknownHostException var2) {
                        server.pingToServer = -1L;
                        server.motd = Formatting.DARK_RED + I18n.format("multiplayer.status.cannot_resolve");
                    } catch (Exception var3) {
                        server.pingToServer = -1L;
                        server.motd = Formatting.DARK_RED + I18n.format("multiplayer.status.cannot_connect");
                    }
                });
            }

            boolean flag = server.protocol > 340;
            boolean flag1 = server.protocol < 340;
            boolean flag2 = flag || flag1;
            Minecraft.font.drawString(server.name, p_192634_2_ + 32 + 3, p_192634_3_ + 1, 16777215);
            List<String> list = Minecraft.font.split(server.motd, p_192634_4_ - 32 - 2);

            for (int i = 0; i < Math.min(list.size(), 2); ++i) {
                Minecraft.font.drawString(list.get(i), p_192634_2_ + 32 + 3, p_192634_3_ + 12 + Minecraft.font.height * i, 8421504);
            }

            String s2 = flag2 ? Formatting.DARK_RED + server.version : server.onlineMax;
            int j = Minecraft.font.getStringWidth(s2);
            Minecraft.font.drawString(s2, p_192634_2_ + p_192634_4_ - j - 15 - 2, p_192634_3_ + 1, 8421504);
            int k = 0;
            String s = null;
            int l;
            String s1;

            if (flag2) {
                l = 5;
                s1 = I18n.format(flag ? "multiplayer.status.client_out_of_date" : "multiplayer.status.server_out_of_date");
                s = server.players;
            } else if (server.pinged && server.pingToServer != -2L) {
                if (server.pingToServer < 0L) {
                    l = 5;
                } else if (server.pingToServer < 150L) {
                    l = 0;
                } else if (server.pingToServer < 300L) {
                    l = 1;
                } else if (server.pingToServer < 600L) {
                    l = 2;
                } else if (server.pingToServer < 1000L) {
                    l = 3;
                } else {
                    l = 4;
                }

                if (server.pingToServer < 0L) {
                    s1 = I18n.format("multiplayer.status.no_connection");
                } else {
                    s1 = server.pingToServer + "ms";
                    s = server.players;
                }
            } else {
                k = 1;
                l = (int) (Minecraft.getSystemTime() / 100L + (long) (p_192634_1_ * 2) & 7L);

                if (l > 4) {
                    l = 8 - l;
                }

                s1 = I18n.format("multiplayer.status.pinging");
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getTextureManager().bindTexture(Gui.ICONS);
            Gui.drawModalRectWithCustomSizedTexture(p_192634_2_ + p_192634_4_ - 15, p_192634_3_, (float) (k * 10), (float) (176 + l * 8), 10, 8, 256.0F, 256.0F);

            if (server.getFavicon() != null && !server.getFavicon().equals(lastIconB64)) {
                lastIconB64 = server.getFavicon();
                prepareServerIcon();
                getServerList().saveServerList();
            }

            if (icon != null) {
                drawTextureAt(p_192634_2_, p_192634_3_, serverIcon);
            } else {
                drawTextureAt(p_192634_2_, p_192634_3_, UNKNOWN_SERVER);
            }

            int i1 = p_192634_6_ - p_192634_2_;
            int j1 = p_192634_7_ - p_192634_3_;

            if (i1 >= p_192634_4_ - 15 && i1 <= p_192634_4_ - 5 && j1 >= 0 && j1 <= 8) {
                //FIXME setHoveringText(s1);
            } else if (i1 >= p_192634_4_ - j - 15 - 2 && i1 <= p_192634_4_ - 15 - 2 && j1 >= 0 && j1 <= 8) {
                //FIXME setHoveringText(s);
            }

            if (p_192634_8_) {
                Minecraft.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
                Gui.drawRect(p_192634_2_, p_192634_3_, p_192634_2_ + 32, p_192634_3_ + 32, -1601138544);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                int k1 = p_192634_6_ - p_192634_2_;
                int l1 = p_192634_7_ - p_192634_3_;

                if (canJoin()) {
                    if (k1 < 32 && k1 > 16) {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (canMoveUp(this, p_192634_1_)) {
                    if (k1 < 16 && l1 < 16) {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (canMoveDown(this, p_192634_1_)) {
                    if (k1 < 16 && l1 > 16) {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }
            }
        }

        protected void drawTextureAt(int p_178012_1_, int p_178012_2_, Namespaced p_178012_3_) {
            Minecraft.getTextureManager().bindTexture(p_178012_3_);
            GlStateManager.enableBlend();
            Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
            GlStateManager.disableBlend();
        }

        private boolean canJoin() {
            return true;
        }

        private void prepareServerIcon() {
            if (server.getFavicon() == null) {
                Minecraft.getTextureManager().deleteTexture(serverIcon);
                icon = null;
            } else {
                ByteBuf bytebuf = Unpooled.copiedBuffer(server.getFavicon(), StandardCharsets.UTF_8);
                ByteBuf bytebuf1 = null;
                BufferedImage bufferedimage;
                label99:
                {
                    try {
                        bytebuf1 = Base64.decode(bytebuf);
                        bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(bytebuf1));
                        Preconditions.checkState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                        Preconditions.checkState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
                        break label99;
                    } catch (Throwable throwable) {
                        System.err.println(String.format("Invalid icon for server %s (%s)", server.name, server.ip));
                        throwable.printStackTrace();
                        server.setFavicon(null);
                    } finally {
                        bytebuf.release();

                        if (bytebuf1 != null) {
                            bytebuf1.release();
                        }
                    }

                    return;
                }

                if (icon == null) {
                    icon = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                    Minecraft.getTextureManager().loadTexture(serverIcon, icon);
                }

                bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), icon.getTextureData(), 0, bufferedimage.getWidth());
                icon.updateDynamicTexture();
            }
        }

        /**
         * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
         * clicked and the list should not be dragged.
         */
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if (relativeX <= 32) {
                if (relativeX < 32 && relativeX > 16 && canJoin()) {
                    selectServer(slotIndex);
                    join();
                    return true;
                }

                if (relativeX < 16 && relativeY < 16 && canMoveUp(this, slotIndex)) {
                    moveServerUp(this, slotIndex, Screen.hasShiftDown());
                    return true;
                }

                if (relativeX < 16 && relativeY > 16 && canMoveDown(this, slotIndex)) {
                    moveServerDown(this, slotIndex, Screen.hasShiftDown());
                    return true;
                }
            }

            selectServer(slotIndex);

            if (Minecraft.getSystemTime() - lastClickTime < 250L) {
                join();
            }

            lastClickTime = Minecraft.getSystemTime();
            return false;
        }

        public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
        }

        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        }

        public ServerData getServerData() {
            return server;
        }
    }

}
