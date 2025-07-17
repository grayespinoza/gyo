package dev.heygrey;

import dev.heygrey.config.GyoConfiguration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class Gyo implements ClientModInitializer {
  private static KeyBinding toggleWindowBorderKey;
  private static KeyBinding refreshWindowResolutionKey;
  private int activations = 0;

  @Override
  public void onInitializeClient() {
    GyoConfiguration.init();
    toggleWindowBorderKey =
        KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.gyo.toggle_window_border_enabled",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.gyo"));
    refreshWindowResolutionKey =
        KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.gyo.refresh_window_resolution",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.gyo"));
    ClientTickEvents.END_CLIENT_TICK.register(
        client -> {
          if (client.currentScreen instanceof TitleScreen && activations < 59) {
            activations += 1;
            long window = client.getInstance().getWindow().getHandle();
            boolean isWindowMaximized =
                GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE;
            if (isWindowMaximized) {
              GLFW.glfwRestoreWindow(window);
            }
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
            GLFW.glfwPollEvents();
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            if (vidMode != null) {
              GLFW.glfwSetWindowSize(window, resolutionWidth(vidMode.height()), vidMode.height());
              GLFW.glfwSetWindowPos(
                  window, (vidMode.width() - resolutionWidth(vidMode.height())) / 2, 0);
            }

            if (!GyoConfiguration.getInstance().hudActiveOnStart) {
              client.options.hudHidden = true;
            }
          }
        });
  }

  public int resolutionWidth(int height) {
    return Math.round((height * 4f) / 3f);
  }
}
