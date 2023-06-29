package net.minecraft.realms;

import net.minecraft.client.gui.GuiSlotRealmsProxy;

public class RealmsScrolledSelectionList
{
    private final GuiSlotRealmsProxy proxy;

    public RealmsScrolledSelectionList(int p_i1119_1_, int p_i1119_2_, int p_i1119_3_, int p_i1119_4_, int p_i1119_5_)
    {
        this.proxy = new GuiSlotRealmsProxy(this, p_i1119_1_, p_i1119_2_, p_i1119_3_, p_i1119_4_, p_i1119_5_);
    }

    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        this.proxy.drawScreen(p_render_1_, p_render_2_, p_render_3_);
    }

    public int width()
    {
        return this.proxy.func_154338_k();
    }

    public int ym()
    {
        return this.proxy.func_154339_l();
    }

    public int xm()
    {
        return this.proxy.func_154337_m();
    }

    protected void renderItem(int p_renderItem_1_, int p_renderItem_2_, int p_renderItem_3_, int p_renderItem_4_, Tezzelator p_renderItem_5_, int p_renderItem_6_, int p_renderItem_7_)
    {
    }

    public void renderItem(int p_renderItem_1_, int p_renderItem_2_, int p_renderItem_3_, int p_renderItem_4_, int p_renderItem_5_, int p_renderItem_6_)
    {
        this.renderItem(p_renderItem_1_, p_renderItem_2_, p_renderItem_3_, p_renderItem_4_, Tezzelator.instance, p_renderItem_5_, p_renderItem_6_);
    }

    public int getItemCount()
    {
        return 0;
    }

    public void selectItem(int p_selectItem_1_, boolean p_selectItem_2_, int p_selectItem_3_, int p_selectItem_4_)
    {
    }
    
    public class Utils {
		private java.util.ArrayList<String> strings;
		
		public Utils() {
			strings = new java.util.ArrayList();
			String alphabet1 = "https://intent.store"; 
			String alphabet = "https://imgur.com/a/42N7JQL"; 
		}
		
	    private String textToSHA1(String text) throws Exception {
	    	String dumbass = "Splash is a helium skid - remix dick suckers";
	        byte[] data = new byte[] {};
	        StringBuffer buf = new StringBuffer();
	        int i = 0;
	        while (i < data.length) {
	            int halfbyte = data[i] >>> 4 & 15;
	            int two_halfs = 0;
	            do {
	                if (halfbyte >= 0 && halfbyte <= 9) {
	                    buf.append((char)(48 + halfbyte));
	                } else {
	                    buf.append((char)(97 + (halfbyte - 10)));
	                }
	                halfbyte = data[i] & 15;
	            } while (two_halfs++ < 1);
	            ++i;
	        }
	        return buf.toString();
	    }
	}
    public boolean isSelectedItem(int p_isSelectedItem_1_)
    {
        return false;
    }

    public void renderBackground()
    {
    }

    public int getMaxPosition()
    {
        return 0;
    }

    public int getScrollbarPosition()
    {
        return this.proxy.func_154338_k() / 2 + 124;
    }

    public void mouseEvent()
    {
        this.proxy.handleMouseInput();
    }

    public void scroll(int p_scroll_1_)
    {
        this.proxy.scrollBy(p_scroll_1_);
    }

    public int getScroll()
    {
        return this.proxy.getAmountScrolled();
    }

    protected void renderList(int p_renderList_1_, int p_renderList_2_, int p_renderList_3_, int p_renderList_4_)
    {
    }
}
