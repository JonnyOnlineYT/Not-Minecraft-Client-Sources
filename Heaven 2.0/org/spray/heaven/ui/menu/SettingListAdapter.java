package org.spray.heaven.ui.menu;

import java.util.List;

import org.spray.heaven.features.module.Setting;
import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.avx.listeners.OnClickListener;
import org.spray.heaven.ui.avx.listeners.OnClickListenerM;
import org.spray.heaven.ui.avx.view.ViewAdapter;
import org.spray.heaven.ui.avx.view.ViewHolder;
import org.spray.heaven.ui.avx.view.views.ImageView;
import org.spray.heaven.ui.menu.views.MCheckBoxView;
import org.spray.heaven.ui.menu.views.MComboBoxView;
import org.spray.heaven.ui.menu.views.MSliderView;
import org.spray.heaven.util.MathUtil;

import net.minecraft.util.ResourceLocation;

public class SettingListAdapter extends ViewAdapter<Setting, SettingListAdapter.SettingViewHolder> {

	public SettingListAdapter(List<Setting> dataSet) {
		super(dataSet);
	}

	@Override
	public void renderVH(Setting data, SettingViewHolder holder, int mouseX, int mouseY, float delta) {
		holder.render(data, mouseX, mouseY, delta);
	}

	@Override
	public SettingViewHolder createVH(int pos) {
		Setting setting = getData().get(pos);
		switch (setting.getCategory()) {
		case BOOLEAN:
			return new CheckBoxHolder(setting, pos);
		case MODE:
			return new ComboBoxHolder(setting, pos);
		case NUMBER:
			return new SliderHolder(setting, pos);
		case COLOR:
			return new ColorSettingHolder(setting, pos);
		default:
			return new SettingViewHolder(setting, pos);
		}
	}

	public class SettingViewHolder extends ViewHolder<Setting> {

		protected Setting setting;

		public SettingViewHolder(Setting setting, int pos) {
			super(pos);
			this.setting = setting;
		}

		@Override
		protected void renderVH(Setting data, int mouseX, int mouseY, float delta) {
		}
	}

	public class CheckBoxHolder extends SettingViewHolder {

		private MCheckBoxView view;

		public CheckBoxHolder(Setting setting, int pos) {
			super(setting, pos);
			view = new MCheckBoxView(setting.getName());
			view.setState(setting.isToggle());

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onMouseClick(int button) {
					setting.setToggle(view.isState());
				}
			});
		}

		@Override
		protected void renderVH(Setting data, int mouseX, int mouseY, float delta) {
			view.setDimension(x, y, width, height);
			view.render(mouseX, mouseY, delta);
		}

		@Override
		protected boolean viewClicked(int mouseX, int mouseY, int button) {
			return view.mouseClicked(mouseX, mouseY, button);
		}
	}

	public class ComboBoxHolder extends SettingViewHolder {

		private MComboBoxView view;

		public ComboBoxHolder(Setting setting, int pos) {
			super(setting, pos);
			zIndex = 15;
			view = new MComboBoxView(setting.getModes());
			view.setCurrent(setting.getModes().indexOf(setting.getCurrentMode()));

			view.setOnSelectListener(new MComboBoxView.OnSelectListener() {
				@Override
				public void onSelect(int pos) {
					setting.setCurrentMode(setting.getModes().get(pos));
				}
			});
		}

		@Override
		protected void renderVH(Setting data, int mouseX, int mouseY, float delta) {
			IFont.WEB_SETTINGS.drawVCenteredString(data.getName(), x + 2, y, height,
					context.getTheme().getTextColor().getRGB());
			float cx = x + width / 2 + 2;
			view.setDimension(cx, y, width / 2 - 4, height);
			view.render(mouseX, mouseY, delta);
		}

		@Override
		public boolean mouseClicked(int mouseX, int mouseY, int button) {
			if (!hidden)
				return view.mouseClicked(mouseX, mouseY, button);
			return false;
		}
	}

	public class SliderHolder extends SettingViewHolder {

		private MSliderView view;

		public SliderHolder(Setting setting, int pos) {
			super(setting, pos);
			zIndex = 15;
			view = new MSliderView(setting.getValue(), setting.getMinValue(), setting.getMaxValue());

			view.setOnUpdateListener(new MSliderView.onUpdateListener() {
				@Override
				public void onUpdate(double value) {
					setting.setValue(value);
				}
			});
		}
		
		@Override
		public void tick() {
			view.tick();
		}

		@Override
		protected void renderVH(Setting data, int mouseX, int mouseY, float delta) {
			IFont.WEB_SETTINGS.drawVCenteredString(data.getName(), x + 2, y, height,
					context.getTheme().getTextColor().getRGB());
			float cx = x + width / 2 + 2;
			view.setDimension(cx, y, width / 2 - 4, height);
			
			String value = String.valueOf(MathUtil.round(data.getValue(), 2));
			IFont.WEB_SETTINGS.drawVCenteredString(value, (cx - 1) - IFont.WEB_SETTINGS.getStringWidth(value), y, height,
					context.getTheme().getTextColor().getRGB());
			
			view.render(mouseX, mouseY, delta);
		}

		@Override
		public boolean viewClicked(int mouseX, int mouseY, int button) {
			return view.mouseClicked(mouseX, mouseY, button);
		}
		
		@Override
		public void mouseReleased(int mouseX, int mouseY, int button) {
			view.mouseReleased(mouseX, mouseY, button);
		}
	}
	
	public class ColorSettingHolder extends SettingViewHolder {
		
		private final ImageView view = new ImageView(new ResourceLocation("heaven/textures/icons/windows/combat.png"));

		public ColorSettingHolder(Setting setting, int pos) {
			super(setting, pos);
			view.setOnClickListener(new OnClickListenerM() {
				@Override
				public void onMouseClick(int x, int y, int button) {
					MenuProvider.getInstance().showPicker(x, y, setting.getColor(), setting.getName());
				}
			});
		}

		@Override
		protected void renderVH(Setting data, int mouseX, int mouseY, float delta) {
			IFont.WEB_SETTINGS.drawVCenteredString(data.getName(), x + 2, y, height,
					context.getTheme().getTextColor().getRGB());
			
			float vw = height - 4;
			view.setDimension(x + width - vw - 4, y + 2, vw, vw);
			view.setColor(context.getTheme().getTextColor());
			view.setPadding(2);
			view.render(mouseX, mouseY, delta);
		}
		
		@Override
		protected boolean viewClicked(int mouseX, int mouseY, int button) {
			return true;
		}
	}
}