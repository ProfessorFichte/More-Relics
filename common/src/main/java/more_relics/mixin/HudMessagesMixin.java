package more_relics.mixin;
import more_relics.spell.MoreRelicEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.client.gui.HudMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HudMessages.class, remap = false)
public class HudMessagesMixin {
    @Inject(method = "actionImpaired", at = @At("HEAD"), cancellable = true)
    private void moreRelics$customActionImpairedMessages(EntityActionsAllowed.SemanticType reason, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            PlayerEntity player = client.player;

            if (player.hasStatusEffect(MoreRelicEffects.SUPERIOR_GUARDIAN_ANGEL.entry)) {
                ((HudMessages)(Object)this).error(Text.translatable("hud.more_relics.guardian_angel").formatted(Formatting.RED));
                ci.cancel();
                return;
            }
            if (player.hasStatusEffect(MoreRelicEffects.SUPERIOR_ZHONYAS_HOURGLASS.entry)) {
                ((HudMessages)(Object)this).error(Text.translatable("hud.more_relics.zhonyas").formatted(Formatting.RED));
                ci.cancel();
                return;
            }
        }
    }
}
