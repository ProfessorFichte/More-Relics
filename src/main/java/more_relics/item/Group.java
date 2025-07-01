package more_relics.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

import static more_relics.MoreRelics.MOD_ID;

public class Group {
    public static Identifier ID = Identifier.of(MOD_ID, "generic");
    public static String translationKey = "itemGroup." + ID.getNamespace() + "." + ID.getPath();
    public static RegistryKey<ItemGroup> KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), ID);
    public static ItemGroup GROUP;
    public static Supplier<ItemStack> ICON = () -> {
        return new ItemStack(MoreRelicsItems.MEDIUM_USE_WATER_POWER.item().get());
    };
}
