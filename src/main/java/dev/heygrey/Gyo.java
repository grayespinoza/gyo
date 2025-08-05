package dev.heygrey;

import dev.heygrey.config.GyoConfiguration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class Gyo implements ClientModInitializer {
  boolean resizeActivated = false;
  private static KeyBinding toggleHighContrastKey;
  private static KeyBinding toggleHighContrastBlockEntitiesKey;
  private static KeyBinding toggleHighContrastEntitiesKey;

  @Override
  public void onInitializeClient() {
    GyoConfiguration.init();
    GyoConfiguration gyoConfiguration = GyoConfiguration.getInstance();

    toggleHighContrastKey =
        KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.gyo.toggle_high_contrast",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.gyo"));
    toggleHighContrastBlockEntitiesKey =
        KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.gyo.toggle_high_contrast_block_entities",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.gyo"));
    toggleHighContrastEntitiesKey =
        KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.gyo.toggle_high_contrast_entities",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.gyo"));

    ClientTickEvents.END_CLIENT_TICK.register(
        client -> {
          if (!resizeActivated
              && client.currentScreen instanceof TitleScreen
              && gyoConfiguration.resizeOnStartup) {
            resizeWindow();
            resizeActivated = !resizeActivated;
          }

          while (toggleHighContrastKey.wasPressed()) {
            gyoConfiguration.highContrast = !gyoConfiguration.highContrast;
          }

          while (toggleHighContrastBlockEntitiesKey.wasPressed()) {
            gyoConfiguration.highContrastBlockEntities =
                !gyoConfiguration.highContrastBlockEntities;
          }

          while (toggleHighContrastEntitiesKey.wasPressed()) {
            gyoConfiguration.highContrastEntities = !gyoConfiguration.highContrastEntities;
          }
        });
  }

  public int resolutionWidth(int height) {
    return Math.round((height * 4f) / 3f);
  }

  public void resizeWindow() {
    long window = MinecraftClient.getInstance().getWindow().getHandle();

    boolean isWindowMaximized =
        GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE;
    if (isWindowMaximized) {
      GLFW.glfwRestoreWindow(window);
    }

    GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
    GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);

    GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
    if (vidMode != null) {
      GLFW.glfwSetWindowSize(window, resolutionWidth(vidMode.height()), vidMode.height());
      GLFW.glfwSetWindowPos(window, (vidMode.width() - resolutionWidth(vidMode.height())) / 2, 0);
    }

    GLFW.glfwPollEvents();
  }
}
