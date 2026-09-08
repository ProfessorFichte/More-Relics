package com.more_relics.forge.client;

import more_relics.MoreRelicsClient;
import more_relics.client.render.GoldenPlayerRenderLayer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/// Only ever touched behind `FMLEnvironment.dist == Dist.CLIENT` (see `ForgeMod`), so no
/// `@EventBusSubscriber(value = Dist.CLIENT)` annotation is needed — that shape has a different
/// meaning on Forge 47 and would classload this on a dedicated server.
public class ForgeClientMod {
    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClientMod::onClientSetup);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.AddLayers.class, ForgeClientMod::onAddLayers);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        MoreRelicsClient.init();
    }

    private static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (var skin : event.getSkins()) {
            PlayerEntityRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addFeature(new GoldenPlayerRenderLayer(renderer));
            }
        }
    }
}
