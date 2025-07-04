package more_relics.spell;

import more_relics.spell.effect.LiandrysTornmentStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.relics_rpgs.util.SpellSchoolUtil;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.Effects;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

import static more_relics.MoreRelics.MOD_ID;

public class MoreRelicEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static final float T1_BUFF_MULTIPLIER = 0.1F;
    private static final float T2_BUFF_MULTIPLIER = 0.2F;
    private static final float T3_BUFF_MULTIPLIER = 0.3F;
    private static final float T4_BUFF_MULTIPLIER = 0.4F;

    public static Effects.Entry LESSER_RAGE_POWER = add(new Effects.Entry(Identifier.of(MOD_ID,"lesser_rage_power"),
            "Rage Power",
            "Increases Rage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    MRPGCEntityAttributes.RAGE_MODIFIER.getIdAsString(),
                                    T1_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry LESSER_POWER_AIR_LIGHTNING = add(new Effects.Entry(Identifier.of(MOD_ID,"lesser_air_lightning"),
            "Air and Lightning Power",
            "Increases Air and Lightning spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(MoreSpellSchools.AIR, SpellSchools.LIGHTNING).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            0.15F,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry LESSER_POWER_EARTH_FIRE = add(new Effects.Entry(Identifier.of(MOD_ID,"lesser_earth_fire"),
            "Earth and Fire Power",
            "Increases Earth and Fire spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(MoreSpellSchools.EARTH, SpellSchools.FIRE).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            0.15F,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry LESSER_POWER_WATER_FROST = add(new Effects.Entry(Identifier.of(MOD_ID,"lesser_water_frost"),
            "Water and Frost Power",
            "Increases Water and Frost spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(MoreSpellSchools.WATER, SpellSchools.FROST).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            0.15F,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));

    public static Effects.Entry MEDIUM_AIR_POWER = add(new Effects.Entry(Identifier.of(MOD_ID,"medium_air_power"),
            "Air Power",
            "Increases Air spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(MoreSpellSchools.AIR).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T2_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry MEDIUM_EARTH_POWER = add(new Effects.Entry(Identifier.of(MOD_ID,"medium_earth_power"),
            "Earth Power",
            "Increases Earth spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(MoreSpellSchools.EARTH).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T2_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry MEDIUM_WATER_POWER = add(new Effects.Entry(Identifier.of(MOD_ID,"medium_water_power"),
            "Water Power",
            "Increases Water spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(MoreSpellSchools.WATER).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T2_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry MEDIUM_RAGE_POWER = add(new Effects.Entry(Identifier.of(MOD_ID,"medium_rage_power"),
            "Mardroeme",
            "Increases Rage and Attack Speed, but silences the caster.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    MRPGCEntityAttributes.RAGE_MODIFIER.getIdAsString(),
                                    T2_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry MEDIUM_LIFESTEAL_POWER = add(new Effects.Entry(Identifier.of(MOD_ID,"medium_lifesteal_power"),
            "Vampiric Scepter",
            "Increases Lifesteal and Spell Vampire.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    MRPGCEntityAttributes.LIFESTEAL_MODIFIER.getIdAsString(),
                                    T2_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    MRPGCEntityAttributes.SPELL_VAMPIRE.getIdAsString(),
                                    T2_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry GREATER_RAGE_POWER = add(new Effects.Entry(Identifier.of(MOD_ID,"greater_rage_power"),
            "Svablods Ritual",
            "Increases Rage and attack speed, also reduces incoming Damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    MRPGCEntityAttributes.RAGE_MODIFIER.getIdAsString(),
                                    T3_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.65F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                            ,
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(),
                                    0.15F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry GREATER_FROZEN_HEART = add(new Effects.Entry(Identifier.of(MOD_ID,"greater_frozen_heart"),
            "Frozen Heart",
            "Reduces the attack speed drastically.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(),
                                    -0.3F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -0.3F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry GREATER_LIANDRYS_TORNMENT = add(new Effects.Entry(Identifier.of(MOD_ID,"greater_liandrys_tornment"),
            "Liandry's Tornment",
            "Increases incoming Damage and dealing damage according to the maximum health of the target per second.",
            new LiandrysTornmentStatusEffect(StatusEffectCategory.HARMFUL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry SUPERIOR_ZHONYAS_HOURGLASS = add(new Effects.Entry(Identifier.of(MOD_ID,"superior_zhonyas_hourglass"),
            "Zhonyas Hourglass",
            "Cant move, jump or cast spells but you're invulnerable.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE.getIdAsString(),
                                    10.0F,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry SUPERIOR_MEJAIS_SOULSTEALER = add(new Effects.Entry(Identifier.of(MOD_ID,"superior_mejais_soulstealer"),
            "Mejais Soulstealer",
            "Increases Spell Power on kills.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    SpellSchoolUtil.allOffensiveMagicSchools().stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            0.1F,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry SUPERIOR_SHURELYAS_BATTLESONG = add(new Effects.Entry(Identifier.of(MOD_ID,"superior_shurelyas_battlesong"),
            "Shurelya's Battlesong",
            "Increases Movement Speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.75F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));


    public static void register(ConfigFile.Effects config) {
        ActionImpairing.configure(GREATER_RAGE_POWER.effect, EntityActionsAllowed.SILENCE);
        ActionImpairing.configure(SUPERIOR_ZHONYAS_HOURGLASS.effect, EntityActionsAllowed.STUN);
        for (var entry: entries) {
            Synchronized.configure(entry.effect, true);
        }

        Effects.register(entries, config.effects);
    }
}
