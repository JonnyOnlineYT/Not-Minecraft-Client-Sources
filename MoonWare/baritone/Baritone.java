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

package baritone;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.event.listener.IEventBus;
import baritone.api.utils.Helper;
import baritone.api.utils.IPlayerContext;
import baritone.behavior.*;
import baritone.cache.WorldProvider;
import baritone.command.manager.CommandManager;
import baritone.event.GameEventHandler;
import baritone.process.*;
import baritone.selection.SelectionManager;
import baritone.utils.BlockStateInterface;
import baritone.utils.GuiClick;
import baritone.utils.InputOverrideHandler;
import baritone.utils.PathingControlManager;
import baritone.utils.player.PrimaryPlayerContext;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Brady
 * @since 7/31/2018
 */
public class Baritone implements IBaritone {

    private static ThreadPoolExecutor threadPool;
    private static File dir;

    static {
        threadPool = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

        dir = new File(Minecraft.gameDir, "baritone");
        if (!Files.exists(dir.toPath())) {
            try {
                Files.createDirectories(dir.toPath());
            } catch (IOException ignored) {}
        }
    }

    private GameEventHandler gameEventHandler;

    private PathingBehavior pathingBehavior;
    private LookBehavior lookBehavior;
    private MemoryBehavior memoryBehavior;
    private InventoryBehavior inventoryBehavior;
    private InputOverrideHandler inputOverrideHandler;

    private FollowProcess followProcess;
    private MineProcess mineProcess;
    private GetToBlockProcess getToBlockProcess;
    private CustomGoalProcess customGoalProcess;
    private BuilderProcess builderProcess;
    private ExploreProcess exploreProcess;
    private BackfillProcess backfillProcess;
    private FarmProcess farmProcess;

    private PathingControlManager pathingControlManager;
    private SelectionManager selectionManager;
    private CommandManager commandManager;

    private IPlayerContext playerContext;
    private WorldProvider worldProvider;

    public BlockStateInterface bsi;

    Baritone() {
        gameEventHandler = new GameEventHandler(this);

        // Define this before behaviors try and get it, or else it will be null and the builds will fail!
        playerContext = PrimaryPlayerContext.INSTANCE;

        {
            pathingBehavior = new PathingBehavior(this);
            lookBehavior = new LookBehavior(this);
            memoryBehavior = new MemoryBehavior(this);
            inventoryBehavior = new InventoryBehavior(this);
            inputOverrideHandler = new InputOverrideHandler(this);
        }

        pathingControlManager = new PathingControlManager(this);
        {
            pathingControlManager.registerProcess(followProcess = new FollowProcess(this));
            pathingControlManager.registerProcess(mineProcess = new MineProcess(this));
            pathingControlManager.registerProcess(customGoalProcess = new CustomGoalProcess(this));
            pathingControlManager.registerProcess(getToBlockProcess = new GetToBlockProcess(this));
            pathingControlManager.registerProcess(builderProcess = new BuilderProcess(this));
            pathingControlManager.registerProcess(exploreProcess = new ExploreProcess(this));
            pathingControlManager.registerProcess(backfillProcess = new BackfillProcess(this));
            pathingControlManager.registerProcess(farmProcess = new FarmProcess(this));
        }

        worldProvider = new WorldProvider();
        selectionManager = new SelectionManager(this);
        commandManager = new CommandManager(this);
    }

    @Override
    public PathingControlManager getPathingControlManager() {
        return pathingControlManager;
    }

    public void registerBehavior(Behavior behavior) {
        gameEventHandler.registerEventListener(behavior);
    }

    @Override
    public InputOverrideHandler getInputOverrideHandler() {
        return inputOverrideHandler;
    }

    @Override
    public CustomGoalProcess getCustomGoalProcess() {
        return customGoalProcess;
    }

    @Override
    public GetToBlockProcess getGetToBlockProcess() {
        return getToBlockProcess;
    }

    @Override
    public IPlayerContext getPlayerContext() {
        return playerContext;
    }

    public MemoryBehavior getMemoryBehavior() {
        return memoryBehavior;
    }

    @Override
    public FollowProcess getFollowProcess() {
        return followProcess;
    }

    @Override
    public BuilderProcess getBuilderProcess() {
        return builderProcess;
    }

    public InventoryBehavior getInventoryBehavior() {
        return inventoryBehavior;
    }

    @Override
    public LookBehavior getLookBehavior() {
        return lookBehavior;
    }

    public ExploreProcess getExploreProcess() {
        return exploreProcess;
    }

    @Override
    public MineProcess getMineProcess() {
        return mineProcess;
    }

    public FarmProcess getFarmProcess() {
        return farmProcess;
    }

    @Override
    public PathingBehavior getPathingBehavior() {
        return pathingBehavior;
    }

    @Override
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    @Override
    public WorldProvider getWorldProvider() {
        return worldProvider;
    }

    @Override
    public IEventBus getGameEventHandler() {
        return gameEventHandler;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void openClick() {
        new Thread(() -> {
            try {
                Thread.sleep(100);
                Helper.mc.addScheduledTask(() -> Minecraft.openScreen(new GuiClick()));
            } catch (Exception ignored) {}
        }).start();
    }

    public static Settings settings() {
        return BaritoneAPI.getSettings();
    }

    public static File getDir() {
        return dir;
    }

    public static Executor getExecutor() {
        return threadPool;
    }
}
