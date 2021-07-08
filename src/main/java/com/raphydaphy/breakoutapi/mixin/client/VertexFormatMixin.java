package com.raphydaphy.breakoutapi.mixin.client;

import com.raphydaphy.breakoutapi.breakout.RenderContextTracker;
import net.minecraft.client.render.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VertexFormat.class)
public class VertexFormatMixin {
  @Inject(method = "method_34446", at = @At("HEAD"), cancellable = true)
  public void getVertexArray(CallbackInfoReturnable<Integer> cir) {
    if (RenderContextTracker.getCurrentContext() != null)
      cir.setReturnValue(RenderContextTracker.getCurrentContext().getWindow().getVAOforVertexFormat((VertexFormat)(Object) this));
  }
}
