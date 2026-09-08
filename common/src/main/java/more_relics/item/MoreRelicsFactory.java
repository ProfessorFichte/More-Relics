package more_relics.item;

import net.minecraft.item.Item;
import net.spell_engine.api.item.ItemAttributeModifiers;
import net.spell_engine.utils.AttributeModifierUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MoreRelicsFactory {
    /// `attributes` is SpellEngine's 1.20.1 stand-in for the 1.21 `AttributeModifiersComponent`
    /// (`net.spell_engine.api.item.ItemAttributeModifiers`).
    public record ItemArgs(Item.Settings settings, @Nullable ItemAttributeModifiers attributes) { }

    public static Function<ItemArgs, Item> factory = args -> {
        var item = new Item(args.settings());
        if (args.attributes() != null) {
            // 1.20.1 has no `Item.Settings#attributeModifiers`; SpellEngine keeps a per-item map that
            // `ItemStackAttributeModifiersMixin` serves from `ItemStack#getAttributeModifiers`.
            AttributeModifierUtil.setItemModifiers(item, args.attributes());
        }
        return item;
    };

    public static Function<ItemArgs, Item> getFactory() { return factory; }
}
