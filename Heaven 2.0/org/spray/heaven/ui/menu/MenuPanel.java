package org.spray.heaven.ui.menu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.ui.avx.view.views.ListView;
import org.spray.heaven.ui.avx.view.views.PanelView;
import org.spray.heaven.ui.avx.view.views.ListView.ListMode;
import org.spray.heaven.ui.avx.view.views.ScrollableListView;
import org.spray.heaven.ui.avx.view.views.SelectableListView;
import org.spray.heaven.ui.avx.view.views.SelectableView;
import org.spray.heaven.ui.menu.views.MColorPicker;
import org.spray.heaven.ui.menu.views.MComboBoxView;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

public class MenuPanel extends PanelView {

	private final HashMap<Integer, ScrollableListView> moduleViews = new HashMap<>();
	private final HashMap<Integer, List<ScrollableListView>> settingViews = new HashMap<>();

	private final MComboBoxView themeView = new MComboBoxView(new ArrayList<>(context.getThemes().keySet()));
	
	private MColorPicker picker = new MColorPicker(Color.BLUE, "Target Color");

	private int openedIndex = -1;

	public MenuPanel() {
		MenuProvider.init();
		
		MenuProvider.getInstance().setColorPickerListener(new MenuProvider.ColorPickerListener() {
			
			@Override
			public MColorPicker showPicker(int x, int y, Color color, String title) {
				picker.setX(x);
				picker.setY(y);
				picker.show();
				return picker;
			}
		});
		
		picker.hide();
		picker.setDimension(9, 9, 130, 60);
		
		SelectableListView categoryList = new SelectableListView();
		categoryList.setPoints(2, 40, 90, 180);
		int cindex = 1;
		for (Category c : Category.values()) {
			categoryList.add(new SelectableView(c.getName()));

			ModuleListAdapter adapter = new ModuleListAdapter(Wrapper.getModule().getByCategory(c));
			ScrollableListView moduleView = new ScrollableListView(adapter, ListMode.LIST);
			moduleView.setPoints(categoryList.getPointX() + categoryList.getWidth() + 6, 26, 90, height);

			List<ScrollableListView> settingList = new ArrayList<>();
			for (Module module : adapter.getData()) {
				SettingListAdapter settingsAdapter = new SettingListAdapter(module.getSettings());
				ScrollableListView settingView = new ScrollableListView(settingsAdapter, ListMode.GRID);

				settingView.setPoints(moduleView.getPointX() + moduleView.getWidth() + 6, 26,
						width - settingView.getPointX() - 6, height);

				settingView.setOffset(6);
				settingView.hide();
				settingList.add(settingView);
			}

			moduleView.setOnClickListener(new ListView.OnClickListener<Module, ModuleViewHolder>() {
				@Override
				public void onMouseClick(Module data, ModuleViewHolder viewHolder, int button) {
					if (button == 0)
						data.updateState();
					else if (button == 1) {
						viewHolder.updateOpened();

						settingViews.values().forEach(viewList -> viewList.forEach(view -> view.hide()));
						if (viewHolder.opened && openedIndex != -1) {
							settingViews.get(openedIndex).get(viewHolder.getPos()).show();
						}
					}
				}
			});
			moduleView.hide();

			settingViews.put(cindex, settingList);
			moduleViews.put(cindex, moduleView);

			cindex++;
		}

		categoryList.setOnSelectListener(new SelectableListView.OnSelectListener() {
			@Override
			public void onSelect(SelectableView view, int index, boolean state) {
				moduleViews.values().forEach(View::hide);
				settingViews.values().forEach(viewList -> viewList.forEach(View::hide));
				if (state) {
					moduleViews.get(index + 1).show();
					openedIndex = index + 1;
				} else
					openedIndex = -1;
			}
		});

		add(categoryList);

		themeView.setPoints(categoryList.getPointX() + categoryList.getWidth() + 6, 3, 56, 15);
		themeView.setOnSelectListener(new MComboBoxView.OnSelectListener() {
			@Override
			public void onSelect(int pos) {
				context.setTheme(context.getThemes().get(context.getThemes().keySet().toArray()[pos]));
			}
		});
		add(themeView);
	}

	@Override
	public void tick() {
		super.tick();
		moduleViews.values().forEach(ScrollableListView::tick);
		settingViews.values().forEach(list -> list.forEach(ScrollableListView::tick));
	}

	@Override
	protected void renderContent(int mouseX, int mouseY, float delta) {
			Drawable.drawBlurredShadow(x, y, width, height, 14,
					ColorUtil.applyOpacity(context.getTheme().getShadowColor(), 0.5f));

		RoundedShader.drawRound(x, y, width, height, 8, context.getTheme().getBackground());

		themeView.render(mouseX, mouseY, delta);
		Drawable.startScissor(x + padding, y + padding + 25, width - (padding * 2), height - (padding * 2) - 25);
		moduleViews.values().forEach(view -> {
			view.setHeight(height - view.getPointY() - padding * 2);
			view.setX(getVX(view));
			view.setY(getVY(view));
			view.render(mouseX, mouseY, delta);
		});

		int columns = 2;
		settingViews.values().forEach(viewList -> viewList.forEach(view -> {
			view.setWidth(width - view.getPointX() - (padding * 2) - 2);
			view.setHeight(height - view.getPointY() - padding * 2);
			view.setX(getVX(view));
			view.setY(getVY(view));

			view.setColumns(columns);
			view.render(mouseX, mouseY, delta);
		}));

		Drawable.stopScissor();
	
		picker.render(mouseX, mouseY, delta);
	}

	@Override
	protected boolean viewClicked(int mouseX, int mouseY, int button) {
		if (themeView.mouseClicked(mouseX, mouseY, button))
			return true;
		
		if (picker.mouseClicked(mouseX, mouseY, button))
			return true;
		else 
			picker.hide();

		for (View view : moduleViews.values()) {
			if (view.mouseClicked(mouseX, mouseY, button))
				return true;
		}

		for (List<ScrollableListView> viewList : settingViews.values()) {
			for (View view : viewList) {
				if (view.mouseClicked(mouseX, mouseY, button))
					return true;
			}
		}

		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);
		picker.mouseReleased(mouseX, mouseY, button);
		moduleViews.values().forEach(mv -> mv.mouseReleased(mouseX, mouseY, button));
		settingViews.values().forEach(list -> list.forEach(sv -> sv.mouseReleased(mouseX, mouseY, button)));
	}

}
