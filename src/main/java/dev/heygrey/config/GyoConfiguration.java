package dev.heygrey.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = "gyo")
public class GyoConfiguration implements ConfigData {
  @ConfigEntry.Category("Client")
  @ConfigEntry.Gui.Tooltip
  public boolean resizeOnStartup = true;

  @ConfigEntry.Category("Client")
  @ConfigEntry.Gui.Tooltip
  public boolean highContrast = true;

  @ConfigEntry.Category("Client")
  @ConfigEntry.Gui.Tooltip
  public boolean highContrastBlockEntities = true;

  @ConfigEntry.Category("Client")
  @ConfigEntry.Gui.Tooltip
  public boolean highContrastEntities = true;

  public static void init() {
    AutoConfig.register(GyoConfiguration.class, Toml4jConfigSerializer::new);
  }

  public static GyoConfiguration getInstance() {
    return AutoConfig.getConfigHolder(GyoConfiguration.class).getConfig();
  }
}
