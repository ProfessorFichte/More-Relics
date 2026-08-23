package more_relics.mixin;

import more_relics.spell.MoreRelicEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    private boolean moreRelics$isStasis(LivingEntity entity) {
        return entity.hasStatusEffect(MoreRelicEffects.SUPERIOR_ZHONYAS_HOURGLASS.entry) || entity.hasStatusEffect(MoreRelicEffects.SUPERIOR_GUARDIAN_ANGEL.entry);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)

    private void zhonyas_damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Entity attacker = source.getAttacker();
        if (source.isIn(DamageTypeTags.BYPASSES_RESISTANCE) || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || attacker == null || amount <= 0
                || entity.getWorld().isClient()) {
            return;
        }
        if (moreRelics$isStasis(entity)) {
            cir.cancel();
        }
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void moreRelics$blockMobTargeting(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (moreRelics$isStasis(target)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void moreRelics$blockNegativeEffects(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (effect.getEffectType().value().getCategory() == StatusEffectCategory.HARMFUL && moreRelics$isStasis(entity)) {
            cir.setReturnValue(false);
        }
    }
}
