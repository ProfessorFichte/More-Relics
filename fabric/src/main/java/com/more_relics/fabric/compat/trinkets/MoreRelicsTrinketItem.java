package com.more_relics.fabric.compat.trinkets;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.relics_rpgs.compat.RelicModifierIds;
import net.spell_engine.api.item.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MoreRelicsTrinketItem extends TrinketItem {
    private ItemAttributeModifiers customAttributes = ItemAttributeModifiers.builder().build();

    public MoreRelicsTrinketItem(Settings settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    /// 1.20.1 Trinkets keys the modifier by a slot-unique `UUID` + display name, not by the 1.21
    /// `Identifier`. Fold the item id into that UUID (the same derivation Relics uses) so swapping a
    /// different relic into one slot cannot reuse a key and trip vanilla's
    /// "Modifier is already applied" guard.
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        var itemPath = Registries.ITEM.getId(stack.getItem()).getPath();
        var modifierUuid = RelicModifierIds.perSlotAndItem(uuid, itemPath);
        var modifierName = RelicModifierIds.name(itemPath);
        for (var entry : this.customAttributes.modifiers()) {
            // SpellEngine 1.10.5.004: entries carry an attribute *id*, so one whose attribute is not
            // registered on this runtime resolves to null and is skipped, exactly like
            // `ItemAttributeModifiers#forSlot`.
            var attribute = entry.attributeValue();
            if (attribute == null) {
                continue;
            }
            modifiers.put(attribute,
                    new EntityAttributeModifier(modifierUuid, modifierName,
                            entry.modifier().getValue(), entry.modifier().getOperation()));
        }
        return modifiers;
    }

    public void setConfigurableModifiers(ItemAttributeModifiers modifiers) {
        this.customAttributes = modifiers;
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var isOnCooldown = false;
        if (entity instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack.getItem());
        }
        return super.canUnequip(stack, slot, entity) && !isOnCooldown;
    }
}
