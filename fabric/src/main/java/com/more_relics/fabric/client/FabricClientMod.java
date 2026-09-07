package com.more_relics.fabric.client;

import more_relics.MoreRelicsClient;
import more_relics.client.render.GoldenPlayerRenderLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.PlayerEntityRenderer;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MoreRelicsClient.init();

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerEntityRenderer playerRenderer) {
                registrationHelper.register(new GoldenPlayerRenderLayer(playerRenderer));
            }
        });
    }
}
