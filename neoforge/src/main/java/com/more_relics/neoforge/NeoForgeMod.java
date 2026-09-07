package com.more_relics.neoforge;

import com.more_relics.neoforge.compat.CompatFeatures;
import more_relics.MoreRelics;
import more_relics.item.Group;
import more_relics.item.MoreRelicsItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(MoreRelics.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        CompatFeatures.init();
        MoreRelics.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
    }
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            MoreRelics.registerSounds();
        });
        event.register(RegistryKeys.ITEM_GROUP, reg -> {
            // Create and register item group (NeoForge-specific)
            Group.GROUP = ItemGroup.builder()
                    .icon(Group.ICON)
                    .displayName(Text.translatable(Group.translationKey))
                    .entries((ctx, entries) -> MoreRelicsItems.addToGroup(entries))
                    .build();
            Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);
        });
        event.register(RegistryKeys.ITEM, reg -> {
            MoreRelics.registerItems();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            MoreRelics.registerEffects();
        });
    }
}
