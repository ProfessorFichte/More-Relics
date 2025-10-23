package com.more_relics.fabric.client;

import more_relics.MoreRelicsClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MoreRelicsClient.init();
    }
}
