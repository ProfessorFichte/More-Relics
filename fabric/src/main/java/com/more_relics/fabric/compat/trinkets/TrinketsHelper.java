package com.more_relics.fabric.compat.trinkets;

import more_relics.item.MoreRelicsFactory;

public class TrinketsHelper {
    public static void registerFactory() {
        MoreRelicsFactory.factory = args -> new MoreRelicsTrinketItem(args.settings(), args.attributes());
    }
}