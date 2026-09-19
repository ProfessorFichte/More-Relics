package com.more_relics.forge.compat.curios;

import net.spell_engine.Platform;

public class CuriosCompat {
    public static void init() {
        if (Platform.util().isModLoaded("curios")) {
            CuriosHelper.registerFactory();
        }
    }
}
