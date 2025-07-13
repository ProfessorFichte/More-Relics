package more_relics.item;

import net.fabricmc.loader.api.FabricLoader;

public class ItemCompat {
    public static void register() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            MoreRelicsItems.factory = args -> new MoreRelicsTrinketItem(args.settings(), args.attributes());
        }
    }
}
