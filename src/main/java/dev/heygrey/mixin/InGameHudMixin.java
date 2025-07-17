package dev.heygrey.mixin;

import dev.heygrey.config.GyoConfiguration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
  @Shadow
  protected abstract void renderAutosaveIndicator(
      DrawContext context, RenderTickCounter tickCounter);

  @Shadow
  protected abstract void renderChat(DrawContext context, RenderTickCounter tickCounter);

  @Shadow
  protected abstract void renderMiscOverlays(DrawContext context, RenderTickCounter tickCounter);

  // @Shadow
  // protected abstract void renderNauseaOverlay(DrawContext context, float nauseaStrength);

  @Shadow
  protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

  @Shadow
  protected abstract void renderPlayerList(DrawContext context, RenderTickCounter tickCounter);

  @Shadow
  protected abstract void renderPortalOverlay(DrawContext context, float nauseaStrength);

  @Shadow
  protected abstract void renderScoreboardSidebar(
      DrawContext context, RenderTickCounter tickCounter);

  @Shadow
  protected abstract void renderSleepOverlay(DrawContext context, RenderTickCounter tickCounter);

  @Shadow
  protected abstract void renderSpyglassOverlay(DrawContext context, float scale);

  @Shadow
  protected abstract void renderVignetteOverlay(DrawContext context, Entity entity);

  @Inject(method = "render", at = @At("TAIL"))
  private void afterRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
    if (!GyoConfiguration.getInstance().hudActiveOnStart
        && MinecraftClient.getInstance().options.hudHidden == true) {
      renderAutosaveIndicator(context, tickCounter);
      renderChat(context, tickCounter);
      renderMiscOverlays(context, tickCounter);
      // renderNauseaOverlay(context, nauseaStrength);
      // renderOverlay(context, texture, opacity);
      renderPlayerList(context, tickCounter);
      // renderPortalOverlay(context, nauseaStrength);
      // renderScoreboardSidebar(context, tickCounter);
      renderSleepOverlay(context, tickCounter);
      // renderSpyglassOverlay(context, scale);
      // renderVignetteOverlay(context, entity);
    }
  }
}
