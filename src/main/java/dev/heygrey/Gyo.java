package dev.heygrey;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class Gyo implements ClientModInitializer {
  boolean resizeActivated = false;

  @Override
  public void onInitializeClient() {
    ClientTickEvents.END_CLIENT_TICK.register(
        client -> {
          if (!resizeActivated && client.currentScreen instanceof TitleScreen) {
            resizeWindow();
            resizeActivated = !resizeActivated;
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
