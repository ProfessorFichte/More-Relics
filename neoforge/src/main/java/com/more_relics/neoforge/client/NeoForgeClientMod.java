package com.more_relics.neoforge.client;

import more_relics.MoreRelics;
import more_relics.MoreRelicsClient;
import more_relics.client.render.GoldenPlayerRenderLayer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = MoreRelics.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClientMod {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MoreRelicsClient.init();
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (var skin : event.getSkins()) {
            PlayerEntityRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addFeature(new GoldenPlayerRenderLayer(renderer));
            }
        }
    }
}
