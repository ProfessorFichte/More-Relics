package com.more_relics.fabric.compat;

import com.more_relics.fabric.compat.trinkets.TrinketsCompat;
import more_relics.compat.AccessoriesCompat;
import net.spell_engine.fabric.compat.FabricCompatFeatures;

public class CompatFeatures {
    public static void init() {
        var id = FabricCompatFeatures.initSlotCompat();
        if ("trinkets".equals(id)) {
            TrinketsCompat.init();
        } else if ("accessories".equals(id)) {
            AccessoriesCompat.init();
        }
    }
}
