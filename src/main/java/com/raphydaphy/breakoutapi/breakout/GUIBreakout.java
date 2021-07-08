package com.raphydaphy.breakoutapi.breakout;

import com.raphydaphy.breakoutapi.breakout.window.BreakoutWindow;
import com.raphydaphy.breakoutapi.mixin.client.BufferRendererAccessor;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.liquidengine.legui.animation.AnimatorProvider;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.handler.processor.SystemEventProcessor;
import org.liquidengine.legui.system.handler.processor.SystemEventProcessorImpl;
import org.liquidengine.legui.system.layout.LayoutManager;
import org.liquidengine.legui.system.renderer.nvg.NvgRenderer;

public abstract class GUIBreakout extends AbstractBreakout {
  protected Frame frame;
  protected Context context;

  protected SystemEventProcessor systemEventProcessor;
  private NvgRenderer renderer;

  public GUIBreakout(Identifier identifier, BreakoutWindow window) {
    super(identifier, window);

    try (BreakoutWindow.ContextHolder ctx = window.switchToContext()) {
      this.frame = new Frame(this.window.getWidth(), this.window.getHeight());

      this.context = new Context(this.getWindow().getHandle());

      this.systemEventProcessor = new SystemEventProcessorImpl();
      SystemEventProcessor.addDefaultCallbacks(this.window.keeper, this.systemEventProcessor);

      this.renderer = new NvgRenderer();
      renderer.initialize();

      this.window.keeper.getChainWindowSizeCallback().add(this::onWindowSizeChanged);
      this.createGuiElements(this.window.getWidth(), this.window.getHeight());
    }
  }

  protected abstract void createGuiElements(int width, int height);

  public void processSystemEvents() {
    this.systemEventProcessor.processEvents(this.frame, this.context);
  }

  public Frame getFrame() {
    return this.frame;
  }

  public Context getContext() {
    return this.context;
  }

  public NvgRenderer getRenderer() {
    return this.renderer;
  }

  @Override
  public void render(MatrixStack matrixStack) {
    this.context.updateGlfwWindow();
    LayoutManager.getInstance().layout(this.frame);
    this.renderer.render(this.getFrame(), this.getContext());

    BufferRendererAccessor.setCurrentElementBuffer(-1);
    BufferRendererAccessor.setCurrentVertexArray(-1);
    BufferRendererAccessor.setCurrentVertexBuffer(-1);
  }

  @Override
  public void postRender() {
    AnimatorProvider.getAnimator().runAnimations();

    this.processSystemEvents();
    EventProcessorProvider.getInstance().processEvents();
    this.frame.getContainer().updateRecursively(this.context, this.frame);
  }

  private void onWindowSizeChanged(long window, int width, int height) {
    if (window == this.getWindow().getHandle()) {
      this.frame.setSize(width, height);
    }
  }

  public void destroy() {
    this.renderer.destroy();
    super.destroy();
  }
}
