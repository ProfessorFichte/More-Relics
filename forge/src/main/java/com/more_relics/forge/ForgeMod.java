package com.more_relics.forge;

import com.more_relics.forge.client.ForgeClientMod;
import com.more_relics.forge.compat.CompatFeatures;
import more_relics.MoreRelics;
import more_relics.item.Group;
import more_relics.item.MoreRelicsItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

@Mod(MoreRelics.MOD_ID)
public final class ForgeMod {
    @SuppressWarnings("removal")
    public ForgeMod() {
        CompatFeatures.init();
        MoreRelics.init();
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientMod.register(modBus);
        }
    }

    /// One window per registry — Forge locks every other registry while a window is open.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> MoreRelics.registerSounds());
        event.register(RegistryKeys.ITEM_GROUP, reg -> {
            // Vanilla `ItemGroup.Builder` — the static `ItemGroup.builder()` is a Fabric API
            // interface-injected method and does not exist on Forge at runtime.
            Group.GROUP = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                    .icon(Group.ICON)
                    .displayName(Text.translatable(Group.translationKey))
                    .entries((ctx, entries) -> MoreRelicsItems.addToGroup(entries))
                    .build();
            Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);
        });
        event.register(RegistryKeys.ITEM, reg -> MoreRelics.registerItems());
        event.register(RegistryKeys.STATUS_EFFECT, reg -> MoreRelics.registerEffects());
    }
}
