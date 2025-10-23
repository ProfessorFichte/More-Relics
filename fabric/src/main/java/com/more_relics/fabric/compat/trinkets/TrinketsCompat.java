package com.more_relics.fabric.compat.trinkets;

import net.fabricmc.loader.api.FabricLoader;

public class TrinketsCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            TrinketsHelper.registerFactory();
        }
    }
}
