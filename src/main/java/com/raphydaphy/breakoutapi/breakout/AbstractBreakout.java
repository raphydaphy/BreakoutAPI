package com.raphydaphy.breakoutapi.breakout;

import com.mojang.blaze3d.systems.RenderSystem;
import com.raphydaphy.breakoutapi.breakout.window.BreakoutWindow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;

public abstract class AbstractBreakout {
  private Identifier identifier;
  protected BreakoutWindow window;
  protected SimpleFramebuffer framebuffer;
  protected MinecraftClient client;
  protected SavedGlState glState;

  public AbstractBreakout(Identifier identifier, BreakoutWindow window) {
    this.identifier = identifier;
    this.window = window;
    this.client = MinecraftClient.getInstance();
    this.glState = new SavedGlState();
    try (BreakoutWindow.ContextHolder ctx = window.switchToContext()) {
      this.glState.glRecord();

      this.framebuffer = new SimpleFramebuffer(this.window.getFramebufferWidth(), this.window.getFramebufferHeight(), true, MinecraftClient.IS_SYSTEM_MAC);
      this.framebuffer.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);

      RenderSystem.setupDefaultState(0, 0, this.window.getFramebufferWidth(), this.window.getFramebufferHeight());

      this.onResolutionChanged(this.window.getFramebufferWidth(), this.window.getFramebufferHeight());
      this.window.keeper.getChainResolutionChangedCallback().add(this::onResolutionChanged);
    }
  }

  public abstract void render(MatrixStack matrixStack);
  protected void postRender() {}

  public void setupRender() {
    if (this.window.shouldClose()) return;

    RenderContextTracker.pushContext(this);

    GLFW.glfwMakeContextCurrent(this.window.getHandle());
    glState.apply();

    MatrixStack matrixStack = RenderSystem.getModelViewStack();
    matrixStack.push();
    RenderSystem.applyModelViewMatrix();
    RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);

    this.framebuffer.beginWrite(true);

    RenderSystem.viewport(0, 0, this.window.getFramebufferWidth(), this.window.getFramebufferHeight());

    this.render(matrixStack);

    this.framebuffer.endWrite();
    matrixStack.pop();

    matrixStack.push();
    RenderSystem.applyModelViewMatrix();
    this.framebuffer.draw(this.window.getFramebufferWidth(), this.window.getFramebufferHeight());
    matrixStack.pop();

    this.window.swapBuffers();

    this.postRender();

    glState.record();
    RenderContextTracker.popContext();
  }


  public static void checkError(String stage) {
    int error = GL45.glGetError();
    if (error != GL30.GL_NO_ERROR) {
      System.out.println("GL Error at stage " + stage + ": " + error);
    }
  }

  public void destroy() {
    framebuffer.delete();
    GLFW.glfwDestroyWindow(this.window.getHandle());
  }

  public void onResolutionChanged(int width, int height) {
    long existingContext = GLFW.glfwGetCurrentContext();
    GLFW.glfwMakeContextCurrent(this.window.getHandle());
    this.framebuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
    GLFW.glfwMakeContextCurrent(existingContext);
  }

  public BreakoutWindow getWindow() {
     return this.window;
  }

  public Identifier getIdentifier() {
    return this.identifier;
  }
}
