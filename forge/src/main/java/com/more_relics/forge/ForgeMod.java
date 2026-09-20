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

    // Goes through the helper on purpose, on Forge 47.0-47.3 a plain Registry.register throws "Can not register to a locked registry".
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, helper ->
                MoreRelicSounds.soundsToRegister().forEach(helper::register));

        event.register(RegistryKeys.ITEM, helper -> {
            MoreRelicsItems.itemsToRegister(MoreRelics.itemConfig.value.entries).forEach(helper::register);
            MoreRelics.itemConfig.save();
        });

        event.register(RegistryKeys.STATUS_EFFECT, helper -> {
            MoreRelicEffects.effectsToRegister(MoreRelics.effectConfig.value).forEach(helper::register);
            MoreRelicEffects.linkEntries();
            MoreRelics.effectConfig.save();
        });

        event.register(RegistryKeys.ITEM_GROUP, helper -> {
            Group.GROUP = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                    .icon(Group.ICON)
                    .displayName(Text.translatable(Group.translationKey))
                    .entries((ctx, entries) -> MoreRelicsItems.addToGroup(entries))
                    .build();
            helper.register(Group.KEY, Group.GROUP);
        });
    }
}
