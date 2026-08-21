package more_relics.spell;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.client.particle.MoreParticles;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.more_rpg_classes.sounds.MRPGLibSounds;
import net.relics_rpgs.spell.RelicSounds;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static more_relics.MoreRelics.MOD_ID;

public class MoreRelicSpells {
    public record Entry(Identifier id, Spell spell, String title, String description) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }
    private static final float T1_USE_EFFECT_DURATION = 10;
    private static final float T1_PROC_EFFECT_DURATION = 6;
    private static final float T1_USE_EFFECT_COOLDOWN = 60;
    private static final float T1_PROC_EFFECT_COOLDOWN = 45;
    private static final float T1_PROC_CHANCE = 0.05F;

    private static final float T2_USE_EFFECT_DURATION = 10;
    private static final float T2_PROC_EFFECT_DURATION = 8;
    private static final float T2_USE_EFFECT_COOLDOWN = 60;
    private static final float T2_PROC_EFFECT_COOLDOWN = 45;
    private static final float T2_PROC_CHANCE = 0.06F;

    private static final float T3_TRANCE_CHANCE = 0.1F;
    private static final float T3_TRANCE_DURATION = 10F;
    private static final float T3_TRANCE_COOLDOWN = 45F;
    private static final float T3_PERK_CC_DURATION = 2;
    private static final float T3_PERK_CC_COOLDOWN = 20;
    private static final float T3_PROC_CHANCE = 0.15F;
    private static final float T3_PROC_EFFECT_COOLDOWN = 30;
    private static final float T3_PROC_EFFECT_DURATION = 10;
    private static final float T3_ZONE_RANGE = 3;
    private static final float T3_ZONE_DURATION = 10;

    private static final float T4_USE_EFFECT_DURATION = 15;
    private static final float T4_USE_EFFECT_COOLDOWN = 90;
    private static final float T4_PROC_EFFECT_COOLDOWN = 60;
    private static final float T4_PROC_EFFECT_DURATION= 15;
    private static final float T4_PROC_CHANCE = 0.3F;
    private static final float T4_AREA_RANGE = 15;
    private static final float T4_ZONE_RANGE = 3;

    private static Spell activeSpellBase() {
        var spell = new Spell();
        spell.range = 0;
        spell.tier = 0;

        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        return spell;
    }

    private static Spell passiveSpellBase() {
        var spell = new Spell();
        spell.range = 0;
        spell.tier = 7;

        spell.type = Spell.Type.PASSIVE;
        spell.passive = new Spell.Passive();

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        return spell;
    }

    /// The registry id of an attribute, for naming a specific modifier inside an `{effect|...}`
    /// tooltip token. Needed because an effect's modifier map is unordered, so a multi-modifier
    /// effect can only be read unambiguously by attribute, never by list position.
    private static Identifier attributeId(RegistryEntry<EntityAttribute> attribute) {
        return Identifier.of(attribute.getIdAsString());
    }

    private static Spell.Impact createEffectImpact(String effectIdString, float duration) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effectIdString;
        buff.action.status_effect.duration = duration;
        return buff;
    }

    private static void configureCooldown(Spell spell, float duration) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        if (spell.cost.cooldown == null) {
            spell.cost.cooldown = new Spell.Cost.Cooldown();
        }
        spell.cost.cooldown.duration = duration;
    }

    /// V1: `new ParticleBatch(id, Shape.SPHERE, Origin.CENTER, count, 0.14F, 0.15F)`.
    private static Consumer<ParticleGroup.Batch> lesserActivateBatch(int count) {
        return b -> b.shape(ParticleGroup.Shape.SPHERE)
                .count(count).speed(0.14F, 0.15F);
    }

    private static @NotNull ParticleGroup lesserActivateParticles(Color color, int count) {
        return sparkDecelerate().color(color)
                .batch(lesserActivateBatch(count));
    }

    private static @NotNull ParticleGroup lesserActivateParticles(SpellEngineParticles.Entry entry, int count) {
        return ParticleGroupBuilder.of(entry)
                .batch(lesserActivateBatch(count));
    }

    /// V1 `spell_engine:magic_spark_decelerate` — the id both the `SPARK_DECELERATE` and the
    /// (identical) `HEALING_PARTICLES` constant resolved to before the 32 magic variants
    /// collapsed to 8 entries plus a motion payload.
    private static ParticleGroupBuilder sparkDecelerate() {
        return ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE);
    }

    private static Spell.TargetCondition deadCondition() {
        var deadCondition = new Spell.TargetCondition();
        deadCondition.health_percent_below = 0F;
        deadCondition.health_percent_above = 0F;
        return deadCondition;
    }

    private static Spell.Trigger killedBySpellTrigger() {
        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        var deadCondition = deadCondition();
        trigger.target_conditions = List.of(deadCondition);
        return trigger;
    }

    private static Spell.Trigger deathPlayer() {
        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        var deadCondition = deadCondition();
        trigger.target_conditions = List.of(deadCondition);
        return trigger;
    }

    private static Spell.Impact createHeal(float coefficient) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.HEAL;
        buff.action.heal = new Spell.Impact.Action.Heal();
        buff.action.heal.spell_power_coefficient = coefficient;
        return buff;
    }

    public static Entry lesser_proc_air_water = add(lesser_proc_air_water());
    private static Entry lesser_proc_air_water() {
        var id = Identifier.of(MOD_ID, "lesser_proc_air_water");
        var effect = MoreRelicEffects.LESSER_POWER_AIR_WATER;
        var title = effect.title;
        // Air and Water share one value, but the registered effect's modifier map is unordered, so
        // the attribute is named instead of relying on the token's first-modifier fallback.
        var description = "On spell hit: {trigger_chance} chance to increase air and water spell power by "
                + TooltipTokens.effect(effect.id, 0, MoreSpellSchools.AIR.id)
                + " for {effect_duration} seconds.";

        var spell = passiveSpellBase();
        spell.school = MoreSpellSchools.AIR;

        var trigger = new Spell.Trigger();
        trigger.chance = T1_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.visuals = Fx.Visuals.of(
                lesserActivateParticles(MoreParticles.SMALL_GUST, 12),
                lesserActivateParticles(MoreParticles.BIG_SPLASH, 12));

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry lesser_proc_earth_nature = add(lesser_proc_earth_nature());
    private static Entry lesser_proc_earth_nature() {
        var id = Identifier.of(MOD_ID, "lesser_proc_earth_nature");
        var effect = MoreRelicEffects.LESSER_POWER_EARTH_NATURE;
        var title = effect.title;
        // Earth and Nature share one value; the attribute is named for the same reason as above.
        var description = "On spell hit: {trigger_chance} chance to increase earth and nature spell power by "
                + TooltipTokens.effect(effect.id, 0, MoreSpellSchools.EARTH.id)
                + " for {effect_duration} seconds.";

        var spell = passiveSpellBase();
        spell.school = MoreSpellSchools.EARTH;

        var trigger = new Spell.Trigger();
        trigger.chance = T1_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.visuals = Fx.Visuals.of(
                lesserActivateParticles(MoreParticles.STONE_PARTICLE, 12),
                lesserActivateParticles(MoreParticles.LEAF, 12));

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry lesser_use_rage_power = add(lesser_use_rage_power());
    private static Entry lesser_use_rage_power() {
        var id = Identifier.of(MOD_ID, "lesser_use_rage_power");
        var effect = MoreRelicEffects.LESSER_RAGE_POWER;
        var title = effect.title;
        // Single modifier, so the token's blank-attribute fallback is unambiguous.
        var description = "Use: Increases rage by "
                + TooltipTokens.effect(effect.id)
                + " for {effect_duration} seconds.";

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_ground_release");
        spell.release.sound = new Sound(MoreRelicSounds.RAGE_POWDER.id().toString());
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .color(Color.RAGE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.1F, 0.1F)),
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .color(Color.RAGE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(25).speed(0.1F, 0.5F)
                                .extent(2F)));
        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T1_USE_EFFECT_DURATION));
        configureCooldown(spell, T1_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    ///MEDIUM RELICS
    public static Entry medium_use_air_power = add(medium_use_air_power());
    private static Entry medium_use_air_power() {
        var id = Identifier.of(MOD_ID, "medium_use_air_power");
        var effect = MoreRelicEffects.MEDIUM_AIR_POWER;
        var title = effect.title;
        // Single modifier, so the token's blank-attribute fallback is unambiguous.
        var description = "Use: Increases air spell power by "
                + TooltipTokens.effect(effect.id)
                + " for {effect_duration} seconds.";

        var spell = activeSpellBase();
        spell.school = MoreSpellSchools.AIR;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(MoreParticles.SMALL_GUST)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.1F, 0.1F)),
                ParticleGroupBuilder.of(MoreParticles.SMALL_GUST)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.12F, 0.12F)));

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry medium_use_earth_power = add(medium_use_earth_power());
    private static Entry medium_use_earth_power() {
        var id = Identifier.of(MOD_ID, "medium_use_earth_power");
        var effect = MoreRelicEffects.MEDIUM_EARTH_POWER;
        var title = effect.title;
        // Single modifier, so the token's blank-attribute fallback is unambiguous.
        var description = "Use: Increases earth spell power by "
                + TooltipTokens.effect(effect.id)
                + " for {effect_duration} seconds.";

        var spell = activeSpellBase();
        spell.school = MoreSpellSchools.EARTH;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(MoreParticles.STONE_PARTICLE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.1F, 0.1F)),
                ParticleGroupBuilder.of(MoreParticles.STONE_PARTICLE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.12F, 0.12F)));

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry medium_use_water_power = add(medium_use_water_power());
    private static Entry medium_use_water_power() {
        var id = Identifier.of(MOD_ID, "medium_use_water_power");
        var effect = MoreRelicEffects.MEDIUM_WATER_POWER;
        var title = effect.title;
        // Single modifier, so the token's blank-attribute fallback is unambiguous.
        var description = "Use: Increases water spell power by "
                + TooltipTokens.effect(effect.id)
                + " for {effect_duration} seconds.";

        var spell = activeSpellBase();
        spell.school = MoreSpellSchools.WATER;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(MoreParticles.BUBBLE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.1F, 0.1F)),
                ParticleGroupBuilder.of(MoreParticles.BIG_SPLASH)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.12F, 0.12F)));
        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry medium_use_nature_power = add(medium_use_nature_power());
    private static Entry medium_use_nature_power() {
        var id = Identifier.of(MOD_ID, "medium_use_nature_power");
        var effect = MoreRelicEffects.MEDIUM_NATURE_POWER;
        var title = effect.title;
        // Single modifier, so the token's blank-attribute fallback is unambiguous.
        var description = "Use: Increases nature spell power by "
                + TooltipTokens.effect(effect.id)
                + " for {effect_duration} seconds.";

        var spell = activeSpellBase();
        spell.school = MoreSpellSchools.NATURE;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(MoreParticles.LEAF)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.1F, 0.1F)),
                sparkDecelerate()
                        .color(Color.NATURE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.1F, 0.1F)));
        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry medium_perk_rage = add(medium_perk_rage());
    private static Entry medium_perk_rage() {
        var id = Identifier.of(MOD_ID, "medium_perk_rage");
        var title = "Mardroeme Mushroom";
        var description = "On melee hit: {trigger_chance} chance to get into a berserk stage for {effect_duration} seconds.";
        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = T2_PROC_CHANCE;
        spell.passive.triggers = List.of(trigger);

        // V1 count 0.4 on a one-shot release = a 40% chance of a single particle (§9),
        // which is `count(1).chance(0.4)` here - a literal `count(0.4)` would emit every time.
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .color(Color.RAGE)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .anchor(ParticleGroup.Anchor.GROUND)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(1F).chance(0.4F).speed(1.0F, 1.0F)
                                .extent(2F)));

        spell.impacts = List.of(createEffectImpact(MoreRelicEffects.MEDIUM_RAGE_POWER.id.toString(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry medium_proc_lifesteal = add(medium_proc_lifesteal());
    private static Entry medium_proc_lifesteal() {
        var id = Identifier.of(MOD_ID, "medium_proc_lifesteal");
        var effect = MoreRelicEffects.MEDIUM_LIFESTEAL_POWER;
        var title = effect.title;
        // Lifesteal and Spell Vampire share one value; the attribute is named because the effect's
        // modifier map is unordered. Two triggers exist (melee + spell), hence the indexed chance.
        var description = "On melee and spell hit: {trigger_chance_1} chance to increase lifesteal and spell vampire by "
                + TooltipTokens.effect(effect.id, 0, attributeId(MRPGCEntityAttributes.LIFESTEAL_MODIFIER))
                + " for {effect_duration} seconds.";

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        trigger.chance = T2_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;

        var trigger2 = new Spell.Trigger();
        trigger2.chance = T2_PROC_CHANCE;
        trigger2.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        spell.passive.triggers = List.of(trigger, trigger2);

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.sound = new Sound(RelicSounds.BLOODLUST_ACTIVATE.id().toString());
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.dripping_blood)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.2F, 0.8F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.FLOAT)
                        .color(Color.RAGE)
                        // V1 WIDE_PIPE = PIPE at double the entity radius
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .count(15).speed(0.02F, 0.1F)));

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    ///GREATER RELICS
    public static Entry greater_proc_rage = add(greater_proc_rage());
    private static Entry greater_proc_rage() {
        var id = Identifier.of(MOD_ID, "greater_proc_rage");
        var effect = MoreRelicEffects.GREATER_RAGE_POWER;
        var title = "Svablod's Ritual";
        var health_threshold = 0.25F;
        // Three modifiers with three different values, previously read by list position - unsafe,
        // because the registered effect keeps them in an unordered map. Each is now named.
        // `damage_taken` is configured negative (-65%) while the prose says "reduces ... by", so it
        // takes the ABS format; the positional mutator rendered "reduces incoming damage by -65%".
        var description = "Taking damage below " + TooltipTokens.bakedPercent(health_threshold)
                + " health, reduces incoming damage by "
                + TooltipTokens.effect(effect.id, 0, SpellEngineAttributes.DAMAGE_TAKEN.id, TooltipTokens.Format.ABS)
                + ", increases rage by "
                + TooltipTokens.effect(effect.id, 0, attributeId(MRPGCEntityAttributes.RAGE_MODIFIER))
                + " and attack speed by "
                + TooltipTokens.effect(effect.id, 0, attributeId(EntityAttributes.GENERIC_ATTACK_SPEED))
                + " for {effect_duration} seconds.";
        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        var healthCondition = new Spell.TargetCondition();
        healthCondition.health_percent_below = health_threshold;
        trigger.caster_conditions = List.of(healthCondition);
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .color(Color.RAGE)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .anchor(ParticleGroup.Anchor.GROUND)
                                .count(50).speed(1.0F, 1.8F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.FLOAT)
                        .color(Color.RAGE)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(20).speed(1.0F, 1.0F)
                                .extent(2.5F)));

        spell.impacts = List.of(createEffectImpact(MoreRelicEffects.GREATER_RAGE_POWER.id.toString(), T3_TRANCE_DURATION/2));
        configureCooldown(spell, T3_TRANCE_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry greater_frozen_heart = add(greater_frozen_heart());
    private static Entry greater_frozen_heart() {
        var id = Identifier.of(MOD_ID, "greater_frozen_heart");
        var effect = MoreRelicEffects.GREATER_FROZEN_HEART;
        var title = effect.title;
        // Attack speed and movement speed share one value (-30%), but the effect's modifier map is
        // unordered so the attribute is named. ABS because the prose already says "reduce ... by" -
        // the old mutator passed the raw value through and rendered "by -30%".
        var description = "On taking damage: {trigger_chance} chance to reduce the attack and movement speed of nearby enemies by "
                + TooltipTokens.effect(effect.id, 0, attributeId(EntityAttributes.GENERIC_ATTACK_SPEED), TooltipTokens.Format.ABS)
                + " for {effect_duration} seconds.";

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 5.0F;

        spell.release.sound = new Sound(MRPGLibSounds.FROST_CRACKLE.id().toString());

        var trigger = new Spell.Trigger();
        trigger.chance = T4_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 1.0F;

        var debuff = createEffectImpact(effect.id.toString(), T3_PROC_EFFECT_DURATION );
        debuff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.frost_shard)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25).speed(0.55F, 0.8F)),
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        // V1 WIDE_PIPE = PIPE at double the entity radius
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(15).speed(0.2F, 0.2F)));
        spell.impacts = List.of(debuff);

        configureCooldown(spell, T3_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry greater_kircheis_shard = add(greater_kircheis_shard());
    private static Entry greater_kircheis_shard() {
        var id = Identifier.of(MOD_ID, "greater_kircheis_shard");
        var description = "On arrow hit: {trigger_chance} chance to create a electric field for {cloud_duration} seconds dealing {damage} damage.";
        var title = "Kircheis Shard";

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var trigger = new Spell.Trigger();
        trigger.chance = T3_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        spell.passive.triggers = List.of(trigger);

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        spell.deliver.delay = 5;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = T3_ZONE_RANGE;
        cloud.volume.area.vertical_range_multiplier = 0.5F;
        cloud.time_to_live_seconds = T3_ZONE_DURATION;
        cloud.impact_tick_interval = 20;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        // Continuous cloud presence FX - stays a plain list, not an Fx.Visuals.
        // V1 `electric_arc_A/B` were retired; `electricArc(lightning_arc_*)` rebuilds their look.
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.electricArc(SpellEngineParticles.lightning_arc_A)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(5).speed(0F, 0F)),
                ParticleGroupBuilder.electricArc(SpellEngineParticles.lightning_arc_B)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(5).speed(0F, 0F)));
        spell.deliver.clouds = List.of(cloud);



        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.release.sound = new Sound(MoreRelicSounds.KIRCHEIS_SHARD_PROC.id());

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.35F;
        damage.sound = Sound.withVolume(MoreRelicSounds.KIRCHEIS_SHARD_IMPACT.id(), 0.5F);


        spell.impacts = List.of(damage);
        configureCooldown(spell, T3_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry greater_madreds_bloodrazor = add(greater_madreds_bloodrazor());
    private static Entry greater_madreds_bloodrazor() {
        var id = Identifier.of(MOD_ID, "greater_madreds_bloodrazor");
        var title = "Madred's Bloodrazor";
        var effect = MoreRelicEffects.GREATER_MADREDS_BLOODRAZOR;
        // Fraction of the target's max health each hit deals; the tooltip prints the same constant.
        // The old mutator read it off the build-time `spell` object, so baking it is equivalent.
        var maxHealthDamage = 0.03F;
        // Single modifier (attack speed), so the token's blank-attribute fallback is unambiguous.
        // Two triggers (passive melee + stashed melee), hence the indexed chance.
        var description = "On melee hit: {trigger_chance_1} to receive increased attack speed by "
                + TooltipTokens.effect(effect.id)
                + " and deal additional damage according to " + TooltipTokens.bakedPercent(maxHealthDamage)
                + " of the targets max health every hit.";

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger_passive = new Spell.Trigger();
        trigger_passive.chance = T3_PROC_CHANCE;
        trigger_passive.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger_passive.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger_passive);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger_stash = new Spell.Trigger();
        trigger_stash.type = Spell.Trigger.Type.MELEE_IMPACT;
        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        spell.deliver.stash_effect.duration = 10;
        spell.deliver.stash_effect.triggers = List.of(trigger_stash);

        var damage = new Spell.Impact();
        damage.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        damage.attribute_from_target = true;
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = maxHealthDamage;
        damage.sound = Sound.withVolume(MoreRelicSounds.MADREDS_BLOODRAZOR_IMPACT.id(), 1.25F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.dripping_blood)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.2F, 0.8F)),
                ParticleGroupBuilder.of(MoreParticles.BLOOD_DROP)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(20).speed(0.2F, 0.8F)));


        spell.impacts = List.of(damage);
        configureCooldown(spell, T3_TRANCE_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry greater_liandrys_torment = add(greater_liandrys_torment());
    private static Entry greater_liandrys_torment() {
        var id = Identifier.of(MOD_ID, "greater_liandrys_torment");
        var title = "Liandry's Torment";
        var description = "On spell hit: {trigger_chance} chance to torment the target for {effect_duration} seconds, damaging according to 3%% of the targets max health per second.";
        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        trigger.chance = T3_PROC_CHANCE;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var debuff = createEffectImpact(MoreRelicEffects.GREATER_LIANDRYS_TORMENT.id.toString(),T3_PROC_EFFECT_DURATION);
        debuff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
        debuff.action.status_effect.amplifier = 1;
        debuff.action.status_effect.amplifier_cap = 4;
        debuff.sound = new Sound(MoreRelicSounds.LIANDRYS_TORMENT_PROC.id());
        debuff.action.status_effect.refresh_duration = false;
        spell.impacts = List.of(debuff);

        configureCooldown(spell, T3_TRANCE_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry greater_sunfire_cape = add(greater_sunfire_cape());
    private static Entry greater_sunfire_cape() {
        var id = Identifier.of(MOD_ID, "greater_sunfire_cape");
        var effect = MoreRelicEffects.GREATER_SUNFIRE_CAPE;
        var title = effect.title;
        // Fraction of the caster's max health the burst deals; the old mutator read it off the
        // build-time `spell` object, so baking the same constant is equivalent.
        var maxHealthDamage = 0.04F;
        // Four triggers (2 passive + 2 stashed), hence the indexed chance.
        var description = "On damage taken and dealt: {trigger_chance_1} to deal "
                + TooltipTokens.bakedPercent(maxHealthDamage)
                + " damage of your max health around you. ";

        var spell = passiveSpellBase();
        spell.range = 4.0F;
        spell.school = SpellSchools.FIRE;

        var trigger_damage_taken = new Spell.Trigger();
        trigger_damage_taken.chance = T3_PROC_CHANCE;
        trigger_damage_taken.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger_damage_taken.target_override = Spell.Trigger.TargetSelector.CASTER;
        var trigger_damage_dealt = new Spell.Trigger();
        trigger_damage_dealt.chance = T3_PROC_CHANCE;
        trigger_damage_dealt.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger_damage_dealt.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger_damage_taken,trigger_damage_dealt);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger_stash_damage_dealt = new Spell.Trigger();
        trigger_stash_damage_dealt.type = Spell.Trigger.Type.MELEE_IMPACT;
        var trigger_stash_damage_taken = new Spell.Trigger();
        trigger_stash_damage_taken.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.duration = T3_PROC_EFFECT_DURATION;
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        spell.deliver.stash_effect.triggers = List.of(trigger_stash_damage_dealt,trigger_stash_damage_taken);

        var damage = new Spell.Impact();
        damage.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        damage.attribute_from_target = false;
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = maxHealthDamage;
        damage.sound = Sound.withVolume(SpellEngineSounds.GENERIC_FIRE_IGNITE.id(), 1.0F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.2F, 0.8F)));

        spell.impacts = List.of(damage);
        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 4.0F;
        configureCooldown(spell, T3_TRANCE_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    ///SUPERIOR RELICS
    public static final Color GOLD = Color.from(0xffd700);
    public static Entry superior_zhonyas_hourglass = add(superior_zhonyas_hourglass());
    private static Entry superior_zhonyas_hourglass() {
        var id = Identifier.of(MOD_ID, "superior_zhonyas_hourglass");
        var description = "Use: Become invulnerable for {effect_duration} seconds, you cant move, attack or cast spells.";
        var effect = MoreRelicEffects.SUPERIOR_ZHONYAS_HOURGLASS;
        var title = effect.title;

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.sound = Sound.withVolume(MoreRelicSounds.ZHONYAS_HOURGLASS.id(), 0.75F);

        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.DECELERATE)
                        .color(GOLD)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.1F, 0.4F)),
                ParticleGroupBuilder.of(SpellEngineParticles.sign_hourglass)
                        .scale(1.8F)
                        .color(GOLD)
                        .attached()
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL)
                                .count(1).speed(0.75F, 0.75F)));
        spell.impacts = List.of(createEffectImpact(effect.id.toString(), 3));
        configureCooldown(spell, T4_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry superior_mejais_soulstealer = add(superior_mejais_soulstealer());
    private static Entry superior_mejais_soulstealer() {
        var id = Identifier.of(MOD_ID, "superior_mejais_soulstealer");
        var effect = MoreRelicEffects.SUPERIOR_MEJAIS_SOULSTEALER;
        var title = effect.title;
        // Every school in `allOffensiveMagicSchools()` gets the same modifier value, so the token's
        // first-modifier fallback is unambiguous - and no single school could stand in for the
        // generic "spell power" the prose names. ABS mirrors the old mutator's `Math.abs`.
        var description = "Defeating enemies grants you spell power by "
                + TooltipTokens.effect(effect.id, 0, null, TooltipTokens.Format.ABS)
                + ", stacking up to {effect_amplifier_cap} times for {effect_duration} seconds.";
        var spell = passiveSpellBase();

        spell.school = SpellSchools.ARCANE;

        var trigger = killedBySpellTrigger();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        var stashTrigger = killedBySpellTrigger();
        stashTrigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        stashTrigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.deliver.stash_effect.triggers = List.of(stashTrigger);

        var buff = createEffectImpact(MoreRelicEffects.SUPERIOR_MEJAIS_SOULSTEALER.id.toString(),T4_PROC_EFFECT_DURATION + 5);
        buff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
        buff.action.status_effect.amplifier = 1;
        buff.action.status_effect.amplifier_cap = 9;
        buff.action.status_effect.refresh_duration = false;
        spell.impacts = List.of(buff);

        configureCooldown(spell, T4_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry superior_shurelyas_battlesong = add(superior_shurelyas_battlesong());
    private static Entry superior_shurelyas_battlesong() {
        var id = Identifier.of(MOD_ID, "superior_shurelyas_battlesong");
        var effect = MoreRelicEffects.SUPERIOR_SHURELYAS_BATTLESONG;
        var title = effect.title;
        // Single modifier, so the token's blank-attribute fallback is unambiguous. Only one of the
        // two impacts is a STATUS_EFFECT, so `{effect_duration}` stays un-indexed.
        var description = "Use: Increase movement speed by "
                + TooltipTokens.effect(effect.id)
                + " for {effect_duration} seconds and reduces spell cooldowns for allies.";

        var spell = activeSpellBase();
        spell.range = 10;
        spell.school = SpellSchools.HEALING;
        spell.release.sound = new Sound(MoreRelicSounds.SHURELYAS_BATTLESONG_ACTIVATE.id());

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 1.0F;
        spell.target.area.include_caster = true;

        var buff = createEffectImpact(effect.id.toString(), T4_USE_EFFECT_DURATION / 2 );
        buff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.sign_speed)
                        .scale(1.8F)
                        .color(Color.WHITE)
                        .attached()
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL)
                                .count(1).speed(0.75F, 0.75F)));

        var impact = new Spell.Impact();
        impact.action = new Spell.Impact.Action();
        impact.action.type = Spell.Impact.Action.Type.COOLDOWN;
        impact.action.cooldown = new Spell.Impact.Action.Cooldown();
        impact.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        impact.action.cooldown.actives.duration_multiplier = 0.8F;
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.sign_speed)
                        .scale(1.2F)
                        .color(Color.WHITE)
                        .attached()
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL)
                                .count(1).speed(0.75F, 0.75F)));

        spell.impacts = List.of(buff, impact);

        configureCooldown(spell, T4_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry superior_mikaels_blessing = add(superior_mikaels_blessing());
    private static Entry superior_mikaels_blessing() {
        var id = Identifier.of(MOD_ID, "superior_mikaels_blessing");
        var effect = MoreRelicEffects.SUPERIOR_MIKAELS_BLESSING;
        var title = effect.title;
        // `{heal_percent}` is the HEAL impact's coefficient read off the live spell - see
        // `registerTooltipTokens()`. No declarative token expresses it.
        var description = "Use: Heals you or the targeted ally for {heal_percent} of your max health and cleanses all harmful effects.";

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.range = 16;

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = new Sound(MoreRelicSounds.MIKAELS_BLESSING_ACTIVATE.id());

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.use_caster_as_fallback = true;

        var buff = createEffectImpact(effect.id.toString(), 1 );

        var heal = createHeal(0.15F);
        heal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());
        heal.visuals = Fx.Visuals.of(
                sparkDecelerate()
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(20).speed(0.02F, 0.15F)),
                sparkDecelerate()
                        .color(Color.HOLY)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(40).speed(0.3F, 0.3F)));

        spell.impacts = List.of(heal,buff);

        configureCooldown(spell, T4_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }
    public static Entry superior_guardian_angel = add(superior_guardian_angel());
    private static Entry superior_guardian_angel() {
        var id = Identifier.of(MOD_ID, "superior_guardian_angel");
        var title = "Guardian Angel";
        var effect = MoreRelicEffects.SUPERIOR_GUARDIAN_ANGEL;
        var health_threshold = 0.15F;
        // The threshold is a local constant, so it is baked straight into the lang value (`%%`
        // because the description is fed through `String.format` on translation). `{health_percent}`
        // is the HEAL impact's coefficient read off the live spell - see `registerTooltipTokens()`.
        var description = "Taking damage below " + TooltipTokens.bakedPercent(health_threshold)
                + " health, makes you invulnerable for {effect_duration} seconds and heals you for {health_percent} of your maximum health.";

        var spell = passiveSpellBase();
        spell.school = SpellSchools.HEALING;

        spell.release.animation = PlayerAnimation.of("more_relics:guardian_angel");
        spell.release.sound = Sound.withVolume(MoreRelicSounds.GUARDIAN_ANGEL_ACTIVATE.id(), 1.25F);

        var trigger = new Spell.Trigger();
        var healthCondition = new Spell.TargetCondition();
        healthCondition.health_percent_below = health_threshold;
        trigger.caster_conditions = List.of(healthCondition);
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var heal = createHeal(0.65F);
        heal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());
        heal.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_circle_1)
                        .attached()
                        .scale(0.8F)
                        .playbackSpeed(1.25F) // V1 maxAge 0.8 -> playback_speed 1 / 0.8
                        .color(Color.WHITE)
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(1).speed(0.2F, 0.2F)));

        var buff = createEffectImpact(effect.id.toString(), 4);
        spell.impacts = List.of(heal, buff);

        configureCooldown(spell, T4_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description);
    }

    /// Description values that no declarative `{token}` expresses, registered through the
    /// server-safe `TooltipTokens` (the deprecated, client-only `SpellTooltip.DescriptionMutator`
    /// is gone). Called from client init; every other tooltip value in this mod is a plain token.
    public static void registerTooltipTokens() {
        healPercent(superior_mikaels_blessing.id(), "{heal_percent}");
        healPercent(superior_guardian_angel.id(), "{health_percent}");
    }

    /// Resolves `token` to the spell's own HEAL coefficient as a percentage. Both callers heal for a
    /// *fraction of max health* (`heal.attribute` is `generic.max_health`), which the built-in
    /// `{heal}` token cannot say - it estimates an absolute number instead. Read off the live
    /// registry entry, so a datapack override of the spell is reflected. Uses `percent`, not
    /// `bakedPercent`: the value is spliced in after translation, so `%` must not be doubled.
    private static void healPercent(Identifier spellId, String token) {
        TooltipTokens.registerCustom(spellId, args -> {
            var description = args.description();
            for (var impact : args.spellEntry().value().impacts) {
                if (impact.action != null && impact.action.heal != null) {
                    return description.replace(token,
                            TooltipTokens.percent(impact.action.heal.spell_power_coefficient));
                }
            }
            return description;
        });
    }
}
