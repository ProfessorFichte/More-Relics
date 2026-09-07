package com.more_relics.fabric;

import com.more_relics.fabric.compat.CompatFeatures;
import more_relics.MoreRelics;
import more_relics.item.Group;
import more_relics.item.MoreRelicsItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatFeatures.init();
        MoreRelics.init();
        MoreRelics.registerSounds();

        Group.GROUP = FabricItemGroup.builder()
                .icon(Group.ICON)
                .displayName(Text.translatable(Group.translationKey))
                .entries((ctx, entries) -> MoreRelicsItems.addToGroup(entries))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);

        MoreRelics.registerItems();
        MoreRelics.registerEffects();
    }
}
