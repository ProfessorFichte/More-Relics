package more_relics.item;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Item;

import java.util.function.Function;

public class MoreRelicsFactory {
    public record ItemArgs(Item.Settings settings, @Nullable AttributeModifiersComponent attributes) { }
    public static Function<ItemArgs, Item> factory = args -> {
        var settings = args.settings;
        if (args.attributes != null) {
            settings.attributeModifiers(args.attributes);
        }
        return new Item(settings);
    };
    public static Function<ItemArgs, Item> getFactory() { return factory; }
}
