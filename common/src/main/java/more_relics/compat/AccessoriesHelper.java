package more_relics.compat;

import io.wispforest.accessories.api.components.AccessoriesDataComponents;
import io.wispforest.accessories.api.components.AccessoryItemAttributeModifiers;
import more_relics.item.MoreRelicsFactory;

public class AccessoriesHelper {
    public static void registerFactory() {
        MoreRelicsFactory.factory = args -> {
            var settings = args.settings();
            if (args.attributes() != null) {
                var builder = AccessoryItemAttributeModifiers.builder();
                for (var entry : args.attributes().modifiers()) {
                    builder = builder.addForSlot(entry.attribute(), entry.modifier(), "spell_trinket", true);
                }
                settings = settings.component(AccessoriesDataComponents.ATTRIBUTES, builder.build());
            }
            return new MoreRelicsAccessoriesItem(settings);
        };
    }
}
