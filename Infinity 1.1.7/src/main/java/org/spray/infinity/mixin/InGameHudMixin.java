package org.spray.infinity.mixin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.HudRenderEvent;
import org.spray.infinity.features.module.misc.NameProtect;
import org.spray.infinity.features.module.visual.ScoreboardMod;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventManager;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

	@Shadow
	private int scaledWidth;

	@Shadow
	private int scaledHeight;

	@Shadow
	public abstract TextRenderer getTextRenderer();

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(at = @At(value = "RETURN"), method = "render")
	private void onRender(MatrixStack matrices, float tickDelta, CallbackInfo info) {
		if (Helper.MC.options.debugEnabled)
			return;

		Infinity.getModuleManager().onRender(matrices, tickDelta, scaledWidth, scaledHeight);
		HudRenderEvent hudRenderEvent = new HudRenderEvent(MinecraftClient.getInstance().getWindow(), scaledWidth,
				scaledHeight, matrices);
		EventManager.call(hudRenderEvent);
	}

	@Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
	private void onRenderScoreboard(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo info) {
		ScoreboardMod mod = ((ScoreboardMod) Infinity.getModuleManager().get(ScoreboardMod.class));
		if (!mod.isEnabled())
			return;

		if (mod.remove.isToggle()) {
			info.cancel();
		} else {

			Scoreboard scoreboard = objective.getScoreboard();
			Collection<ScoreboardPlayerScore> collection = scoreboard.getAllPlayerScores(objective);
			List<ScoreboardPlayerScore> list = (List) collection.stream().filter((score) -> {
				return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
			}).collect(Collectors.toList());
			Object collection1;
			if (list.size() > 15) {
				collection1 = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
			} else {
				collection1 = list;
			}

			NameProtect nameProtect = ((NameProtect) Infinity.getModuleManager().get(NameProtect.class));

			List<Pair<ScoreboardPlayerScore, Text>> list2 = Lists
					.newArrayListWithCapacity(((Collection) collection1).size());
			String playerName = objective.getDisplayName().getString();

			if (nameProtect.isEnabled() && Helper.MC.getSession().getUsername().contains(playerName))
				playerName = playerName.replace(playerName, nameProtect.name.getText());

			Text text = new LiteralText(playerName);
			int i = this.getTextRenderer().getWidth((StringVisitable) text);
			int j = i;
			int k = this.getTextRenderer().getWidth(": ");

			ScoreboardPlayerScore scoreboardPlayerScore;
			MutableText text2;
			for (Iterator var11 = ((Collection) collection1).iterator(); var11
					.hasNext(); j = Math.max(j, this.getTextRenderer().getWidth((StringVisitable) text2) + k
							+ this.getTextRenderer().getWidth(Integer.toString(scoreboardPlayerScore.getScore())))) {
				scoreboardPlayerScore = (ScoreboardPlayerScore) var11.next();

				Team team = scoreboard.getPlayerTeam(scoreboardPlayerScore.getPlayerName());
				text2 = Team.decorateName(team, new LiteralText(scoreboardPlayerScore.getPlayerName()));
				list2.add(Pair.of(scoreboardPlayerScore, text2));
			}

			int var10000 = ((Collection) collection1).size();
			Objects.requireNonNull(this.getTextRenderer());
			int l = var10000 * 9;
			int m = this.scaledHeight / 2 + l / 3;
			int o = this.scaledWidth - j - 3;
			int p = 0;
			int q = this.client.options.getTextBackgroundColor(0.3F);
			int r = this.client.options.getTextBackgroundColor(0.4F);
			Iterator var18 = list2.iterator();

			while (var18.hasNext()) {
				Pair<ScoreboardPlayerScore, Text> pair = (Pair) var18.next();
				++p;
				ScoreboardPlayerScore scoreboardPlayerScore2 = (ScoreboardPlayerScore) pair.getFirst();
				Text text3 = (Text) pair.getSecond();
				Formatting var31 = Formatting.RED;
				String string = var31 + String.valueOf(scoreboardPlayerScore2.getScore());
				Objects.requireNonNull(this.getTextRenderer());
				int t = (int) (m - p * 9 + mod.height.getCurrentValueDouble());
				int u = this.scaledWidth - 3 + 2;
				int var10001 = o - 2;
				Objects.requireNonNull(this.getTextRenderer());
				fill(matrices, var10001, t, u, t + 9, q);
				this.getTextRenderer().draw(matrices, (Text) text3, (float) o, (float) t, -1);
				this.getTextRenderer().draw(matrices, (String) string,
						(float) (u - this.getTextRenderer().getWidth(string)), (float) t, -1);
				if (p == ((Collection) collection1).size()) {
					var10001 = o - 2;
					Objects.requireNonNull(this.getTextRenderer());
					fill(matrices, var10001, t - 9 - 1, u, t - 1, r);
					fill(matrices, o - 2, t - 1, u, t, q);
					TextRenderer var32 = this.getTextRenderer();
					float var10003 = (float) (o + j / 2 - i / 2);
					Objects.requireNonNull(this.getTextRenderer());
					var32.draw(matrices, (Text) text, var10003, (float) (t - 9), -1);
				}
			}
			info.cancel();
		}
	}

}
