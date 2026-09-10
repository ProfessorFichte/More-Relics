package com.more_relics.forge;

import com.more_relics.forge.client.ForgeClientMod;
import com.more_relics.forge.compat.CompatFeatures;
import more_relics.MoreRelics;
import more_relics.item.Group;
import more_relics.item.MoreRelicsItems;
import more_relics.spell.MoreRelicEffects;
import more_relics.spell.MoreRelicSounds;
import net.minecraft.item.ItemGroup;
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

    /// Forge only clears the *vanilla* registry's own lock from 47.4.0 onwards, so on Forge 47.0-47.3
    /// (and NeoForge 1.20.1) a plain `Registry.register` throws "Can not register to a locked registry"
    /// even inside the correct `RegisterEvent` window. Everything below therefore goes through the
    /// `RegisterHelper` the event hands out, iterating the same content `common` exposes.
    ///
    /// The loops are deliberate duplicates of what `common` runs on Fabric - no seam, no shared
    /// abstraction. `fabric/` is untouched.
    ///
    /// One `event.register` block per registry, declared unconditionally: `event.register` is a no-op
    /// unless its key matches the event's registry, and Forge posts one event per registry.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, helper ->
                MoreRelicSounds.soundsToRegister().forEach(helper::register));

        event.register(RegistryKeys.ITEM, helper -> {
            // `MoreRelics.registerItems()` saves the config after registering - part of the contract.
            MoreRelicsItems.itemsToRegister(MoreRelics.itemConfig.value.entries).forEach(helper::register);
            MoreRelics.itemConfig.save();
        });

        event.register(RegistryKeys.STATUS_EFFECT, helper -> {
            // `MoreRelics.registerEffects()` saves the config after registering - part of the contract.
            MoreRelicEffects.effectsToRegister(MoreRelics.effectConfig.value).forEach(helper::register);
            MoreRelicEffects.linkEntries();
            MoreRelics.effectConfig.save();
        });

        // The item group gets its OWN block: `creative_mode_tab` is event 65 while `item` is event 7,
        // so a group registered from the ITEM pass writes into a registry whose event has not fired
        // and vanishes with no error.
        event.register(RegistryKeys.ITEM_GROUP, helper -> {
            // Vanilla `ItemGroup.Builder` - the static `ItemGroup.builder()` is a Fabric API
            // interface-injected method and does not exist on Forge at runtime.
            Group.GROUP = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                    .icon(Group.ICON)
                    .displayName(Text.translatable(Group.translationKey))
                    .entries((ctx, entries) -> MoreRelicsItems.addToGroup(entries))
                    .build();
            helper.register(Group.KEY, Group.GROUP);
        });
    }
}
