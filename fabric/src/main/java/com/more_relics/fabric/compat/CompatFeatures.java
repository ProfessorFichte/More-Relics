package com.more_relics.fabric.compat;

import com.more_relics.fabric.compat.trinkets.TrinketsCompat;
import net.spell_engine.fabric.compat.FabricCompatFeatures;

public class CompatFeatures {
    public static void init() {
        if ("trinkets".equals(FabricCompatFeatures.initSlotCompat())) {
            TrinketsCompat.init();
        }
    }
}
