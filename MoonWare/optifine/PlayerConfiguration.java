package optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

public class PlayerConfiguration
{
    private PlayerItemModel[] playerItemModels = new PlayerItemModel[0];
    private boolean initialized;

    public void renderPlayerItems(ModelBiped p_renderPlayerItems_1_, AbstractClientPlayer p_renderPlayerItems_2_, float p_renderPlayerItems_3_, float p_renderPlayerItems_4_)
    {
        if (initialized)
        {
            for (int i = 0; i < playerItemModels.length; ++i)
            {
                PlayerItemModel playeritemmodel = playerItemModels[i];
                playeritemmodel.render(p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_, p_renderPlayerItems_4_);
            }
        }
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void setInitialized(boolean p_setInitialized_1_)
    {
        initialized = p_setInitialized_1_;
    }

    public PlayerItemModel[] getPlayerItemModels()
    {
        return playerItemModels;
    }

    public void addPlayerItemModel(PlayerItemModel p_addPlayerItemModel_1_)
    {
        playerItemModels = (PlayerItemModel[])Config.addObjectToArray(playerItemModels, p_addPlayerItemModel_1_);
    }
}
