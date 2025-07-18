/*
package dev.heygrey.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderer.class)
public abstract class BlockEntityRendererMixin<T extends BlockEntity> {
  @Inject(method = "render", at = @At("HEAD"))
  private void beforeRender(
      BlockEntity entity,
      float tickProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      int overlay,
      Vec3d cameraPos,
      CallbackInfo ci) {}
}
*/
