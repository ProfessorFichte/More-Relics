package com.more_relics.neoforge.compat.curios;

import more_relics.item.MoreRelicsFactory;
import net.relics_rpgs.neoforge.compat.curios.RelicCurioItem;

public class CuriosHelper {
    public static void registerFactory() {
        MoreRelicsFactory.factory = args -> new RelicCurioItem(args.settings(), args.attributes());
    }
}
