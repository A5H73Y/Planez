package io.github.a5h73y.planez.enums;

public enum Commands {
    SPAWN("Command.Spawn"),
    PURCHASE("Command.Purchase"),
    REFUEL("Command.Refuel"),
    UPGRADE("Command.Upgrade");

    final String configPath;

    Commands(String configPath) {
        this.configPath = configPath;
    }

    public String getConfigPath() {
        return configPath;
    }
}
