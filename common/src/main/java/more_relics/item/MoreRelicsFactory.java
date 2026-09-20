package more_relics.item;

import net.minecraft.item.Item;
import net.spell_engine.api.item.ItemAttributeModifiers;
import net.spell_engine.utils.AttributeModifierUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MoreRelicsFactory {
    public record ItemArgs(Item.Settings settings, @Nullable ItemAttributeModifiers attributes) { }

    public static Function<ItemArgs, Item> factory = args -> {
        var item = new Item(args.settings());
        if (args.attributes() != null) {
            AttributeModifierUtil.setItemModifiers(item, args.attributes());
        }
        return item;
    };

    public static Function<ItemArgs, Item> getFactory() { return factory; }
}
