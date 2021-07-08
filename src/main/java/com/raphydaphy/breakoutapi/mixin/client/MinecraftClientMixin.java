package com.raphydaphy.breakoutapi.mixin.client;

import com.raphydaphy.breakoutapi.BreakoutAPI;
import com.raphydaphy.breakoutapi.BreakoutAPIClient;
import com.raphydaphy.breakoutapi.breakout.AbstractBreakout;
import com.raphydaphy.breakoutapi.breakout.Breakout;
import com.raphydaphy.breakoutapi.breakout.SavedGlState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow @Final
	private Window window;

	@Unique
	private final SavedGlState mainContextState = new SavedGlState();

	@Inject(method = "onResolutionChanged", at = @At("HEAD"))
	public void onResolutionChanged(CallbackInfo info) {
		GLFW.glfwMakeContextCurrent(this.window.getHandle());
	}

	@Inject(method = "render", at = @At(value = "INVOKE_STRING", args = "ldc=yield", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
	private void afterRender(CallbackInfo info) {
		MinecraftClient.getInstance().getProfiler().swap("render breakouts");

		Breakout.checkError("before rendering breakouts");

		mainContextState.record();

		Iterator<Map.Entry<Identifier, AbstractBreakout>> iter = BreakoutAPIClient.getBreakouts().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Identifier, AbstractBreakout> entry = iter.next();
			Identifier identifier = entry.getKey();
			AbstractBreakout breakout = entry.getValue();

			if (breakout.getWindow().shouldClose()) {
				BreakoutAPI.LOGGER.info("Closing breakout '" + identifier + "'");

				breakout.destroy();
				iter.remove();
			} else {
				breakout.setupRender();
			}
		}

		Breakout.checkError("after rendering breakouts");
		GLFW.glfwMakeContextCurrent(this.window.getHandle());
		mainContextState.apply();
	}
}
