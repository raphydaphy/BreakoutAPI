package com.raphydaphy.breakoutapi.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlStateManager.CapabilityTracker.class)
public interface CapabilityTrackerAccessor {
  @Accessor
  boolean getState();

  @Accessor("state")
  void setStateInternal(boolean state);
}
