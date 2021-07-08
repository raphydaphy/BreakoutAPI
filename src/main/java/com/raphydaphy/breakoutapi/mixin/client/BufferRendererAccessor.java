package com.raphydaphy.breakoutapi.mixin.client;

import net.minecraft.client.render.BufferRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BufferRenderer.class)
public interface BufferRendererAccessor {
  @Accessor("currentElementBuffer")
  static int getCurrentElementBuffer() {
    throw new IllegalStateException("Mixin not applied!");
  }

  @Accessor("currentElementBuffer")
  static void setCurrentElementBuffer(int ebo) {
    throw new IllegalStateException("Mixin not applied!");
  }

  @Accessor("currentVertexArray")
  static int getCurrentVertexArray() {
    throw new IllegalStateException("Mixin not applied!");
  }

  @Accessor("currentVertexArray")
  static void setCurrentVertexArray(int vao) {
    throw new IllegalStateException("Mixin not applied!");
  }

  @Accessor("currentVertexBuffer")
  static int getCurrentVertexBuffer() {
    throw new IllegalStateException("Mixin not applied!");
  }

  @Accessor("currentVertexBuffer")
  static void setCurrentVertexBuffer(int vbo) {
    throw new IllegalStateException("Mixin not applied!");
  }
}
