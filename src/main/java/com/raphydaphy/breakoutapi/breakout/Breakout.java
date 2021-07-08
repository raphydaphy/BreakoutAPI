package com.raphydaphy.breakoutapi.breakout;

import com.mojang.blaze3d.systems.RenderSystem;
import com.raphydaphy.breakoutapi.breakout.window.BreakoutWindow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

public abstract class Breakout extends AbstractBreakout {

  public Breakout(Identifier identifier, BreakoutWindow window) {
    super(identifier, window);
  }

  @Override
  public void render(MatrixStack matrixStack) {
    RenderSystem.enableTexture();
    RenderSystem.enableCull();

    RenderSystem.clearColor(1, 1, 1, 1);
    RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);

    Matrix4f matrix4f = Matrix4f.projectionMatrix(0.0F, (float)((double)window.getFramebufferWidth()), 0.0F, (float)((double)window.getFramebufferHeight()), 1000.0F, 3000.0F);
    RenderSystem.setProjectionMatrix(matrix4f);

    matrixStack.loadIdentity();
    matrixStack.translate(0.0D, 0.0D, -2000.0D);
    RenderSystem.applyModelViewMatrix();
    DiffuseLighting.enableGuiDepthLighting();
  }
}
