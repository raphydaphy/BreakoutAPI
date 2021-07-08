package com.raphydaphy.breakoutapi.breakout;

import java.util.ArrayDeque;
import java.util.Deque;

public class RenderContextTracker {
  private static final ThreadLocal<Deque<AbstractBreakout>> CONTEXT = ThreadLocal.withInitial(ArrayDeque::new);

  public static AbstractBreakout getCurrentContext() {
    return CONTEXT.get().peek();
  }

  public static void popContext() {
    CONTEXT.get().pop();
  }

  public static void pushContext(AbstractBreakout breakout) {
    CONTEXT.get().push(breakout);
  }
}
