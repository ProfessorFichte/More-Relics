package more_relics.spell;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.sounds.ModSounds;
import net.relics_rpgs.spell.RelicSounds;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static more_relics.MoreRelics.MOD_ID;

public class MoreRelicSpells {
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator) {
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
        spell.tier = 7;

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

    private static @NotNull ParticleBatch lesserActivateParticles(Color color, int count) {
        return lesserActivateParticles(SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                count)
                .color(color.toRGBA());
    }
    private static final Identifier SPARK_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPARK,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE
    ).id();

    private static @NotNull ParticleBatch lesserActivateParticles(String particleId, int count) {
        return new ParticleBatch(
                particleId,
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                count, 0.14F, 0.15F);
    }

    private static final Identifier HEALING_PARTICLES = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.NATURE,
            SpellEngineParticles.MagicParticleFamily.Shape.IMPACT,
            SpellEngineParticles.MagicParticleFamily.Motion.ASCEND
    ).id();

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

    public static Entry lesser_proc_air_lightning = add(lesser_proc_air_lightning());
    private static Entry lesser_proc_air_lightning() {
        var id = Identifier.of(MOD_ID, "lesser_proc_air_lightning");
        var description = "On spell hit: {trigger_chance} chance to increase air and lightning spell power by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.LESSER_POWER_AIR_LIGHTNING;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = passiveSpellBase();
        spell.school = MoreSpellSchools.AIR;

        var trigger = new Spell.Trigger();
        trigger.chance = T1_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:one_handed_healing_release";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[]{
                lesserActivateParticles("more_rpg_classes:small_gust", 12),
                lesserActivateParticles(SpellEngineParticles.electric_arc_A.id().toString(), 12)
        };

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry lesser_proc_earth_fire = add(lesser_proc_earth_fire());
    private static Entry lesser_proc_earth_fire() {
        var id = Identifier.of(MOD_ID, "lesser_proc_earth_fire");
        var description = "On spell hit: {trigger_chance} chance to increase earth and fire spell power by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.LESSER_POWER_EARTH_FIRE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = passiveSpellBase();
        spell.school = MoreSpellSchools.EARTH;

        var trigger = new Spell.Trigger();
        trigger.chance = T1_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:one_handed_healing_release";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[]{
                lesserActivateParticles("more_rpg_classes:stone_particle", 12),
                lesserActivateParticles(SpellEngineParticles.flame_spark.id().toString(), 12)
        };

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry lesser_proc_water_frost = add(lesser_proc_water_frost());
    private static Entry lesser_proc_water_frost() {
        var id = Identifier.of(MOD_ID, "lesser_proc_water_frost");
        var description = "On spell hit: {trigger_chance} chance to increase water and frost spell power by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.LESSER_POWER_WATER_FROST;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = passiveSpellBase();
        spell.school = MoreSpellSchools.WATER;

        var trigger = new Spell.Trigger();
        trigger.chance = T1_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:one_handed_healing_release";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[]{
                lesserActivateParticles("more_rpg_classes:big_splash", 12),
                lesserActivateParticles(Color.FROST, 12)
        };

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry lesser_use_rage_power = add(lesser_use_rage_power());
    private static Entry lesser_use_rage_power() {
        var id = Identifier.of(MOD_ID, "lesser_use_rage_power");
        var description = "Use: Increases rage by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.LESSER_RAGE_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = "spell_engine:dual_handed_ground_release";
        spell.release.sound = new Sound(MoreRelicSounds.RAGE_POWDER.id().toString());
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.1F, 0.1F)
                        .color(Color.RAGE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK,25 ,0.1F, 0.5F,0,2)
                        .color(Color.RAGE.toRGBA()),
        };
        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T1_USE_EFFECT_DURATION));
        configureCooldown(spell, T1_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    ///MEDIUM RELICS
    public static Entry medium_use_air_power = add(medium_use_air_power());
    private static Entry medium_use_air_power() {
        var id = Identifier.of(MOD_ID, "medium_use_air_power");
        var description = "Use: Increases air spell power by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.MEDIUM_AIR_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = activeSpellBase();
        spell.school = MoreSpellSchools.AIR;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "more_rpg_classes:small_gust",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.1F, 0.1F),
                new ParticleBatch(
                        "more_rpg_classes:small_gust",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.12F, 0.12F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry medium_use_earth_power = add(medium_use_earth_power());
    private static Entry medium_use_earth_power() {
        var id = Identifier.of(MOD_ID, "medium_use_earth_power");
        var description = "Use: Increases earth spell power by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.MEDIUM_EARTH_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = activeSpellBase();
        spell.school = MoreSpellSchools.EARTH;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "more_rpg_classes:stone_particle",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.1F, 0.1F),
                new ParticleBatch(
                        "more_rpg_classes:stone_particle",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.12F, 0.12F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry medium_use_water_power = add(medium_use_water_power());
    private static Entry medium_use_water_power() {
        var id = Identifier.of(MOD_ID, "medium_use_water_power");
        var description = "Use: Increases water spell power by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.MEDIUM_WATER_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = activeSpellBase();
        spell.school = MoreSpellSchools.WATER;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "more_rpg_classes:bubble",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.1F, 0.1F),
                new ParticleBatch(
                        "more_rpg_classes:big_splash",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.12F, 0.12F)
        };
        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
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

        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        ParticleBatch.Rotation.LOOK, 0.4F, 1.0F,1,0,2)
                        .color(Color.RAGE.toRGBA()),
        };

        spell.impacts = List.of(createEffectImpact(MoreRelicEffects.MEDIUM_RAGE_POWER.id.toString(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, null);
    }
    public static Entry medium_proc_lifesteal = add(medium_proc_lifesteal());
    private static Entry medium_proc_lifesteal() {
        var id = Identifier.of(MOD_ID, "medium_proc_lifesteal");
        var description = "On melee and spell hit: {trigger_chance_1} chance to increase lifesteal and spell vampire by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.MEDIUM_LIFESTEAL_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        trigger.chance = T2_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;

        var trigger2 = new Spell.Trigger();
        trigger2.chance = T2_PROC_CHANCE;
        trigger2.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        spell.passive.triggers = List.of(trigger, trigger2);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.BLOODLUST_ACTIVATE.id().toString());
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.dripping_blood.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.2F, 0.8F),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.FLOAT
                        ).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.02F, 0.1F)
                        .color(Color.RAGE.toRGBA()),
        };

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    ///GREATER RELICS
    public static Entry greater_proc_rage = add(greater_proc_rage());
    private static Entry greater_proc_rage() {
        var id = Identifier.of(MOD_ID, "greater_proc_rage");
        var effect = MoreRelicEffects.GREATER_RAGE_POWER;
        var title = "Svablod's Ritual";
        var health_threshold = 0.25F;
        var description = "Taking damage below {health_threshold} health, reduces incoming damage by {bonus}, increases rage by {bonus2} and attack speed by {bonus3} for {effect_duration} seconds.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().attributes().get(1);
            var modifier2 = effect.config().attributes().get(0);
            var modifier3 = effect.config().attributes().get(2);
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            var bonus2 = SpellTooltip.bonus(modifier2.value, modifier2.operation);
            var bonus3 = SpellTooltip.bonus(modifier3.value, modifier3.operation);
            return args.description()
                    .replace("{health_threshold}", SpellTooltip.percent(health_threshold))
                    .replace("{bonus}", bonus)
                    .replace("{bonus2}", bonus2)
                    .replace("{bonus3}", bonus3);
        };
        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        var healthCondition = new Spell.TargetCondition();
        healthCondition.health_percent_below = health_threshold;
        trigger.caster_conditions = List.of(healthCondition);
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        50, 1.0F, 1.8F)
                        .color(Color.RAGE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.RAGE.toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        ParticleBatch.Rotation.LOOK,20,1.0F,1.0F,0,2.5F)
                        .color(Color.RAGE.toRGBA()),
        };

        spell.impacts = List.of(createEffectImpact(MoreRelicEffects.GREATER_RAGE_POWER.id.toString(), T3_TRANCE_DURATION/2));
        configureCooldown(spell, T3_TRANCE_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry greater_frozen_heart = add(greater_frozen_heart());
    private static Entry greater_frozen_heart() {
        var id = Identifier.of(MOD_ID, "greater_frozen_heart");
        var effect = MoreRelicEffects.GREATER_FROZEN_HEART;
        var title = effect.title;
        var description = "On taking damage: {trigger_chance} chance to reduce the attack and movement speed of nearby enemies by {bonus} for {effect_duration} seconds.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 5.0F;

        spell.release.sound = new Sound(ModSounds.FROST_CRACKLE_ID.toString());

        var trigger = new Spell.Trigger();
        trigger.chance = T4_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 1.0F;

        var debuff = createEffectImpact(effect.id.toString(), T3_PROC_EFFECT_DURATION );
        debuff.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.frost_shard.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.55F, 0.8F),
                new ParticleBatch(SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.2F, 0.2F)
        };
        spell.impacts = List.of(debuff);

        configureCooldown(spell, T3_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
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
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.electric_arc_A.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5, 0, 0),
                new ParticleBatch(
                        SpellEngineParticles.electric_arc_B.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5, 0, 0)
        };
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

        return new Entry(id, spell, title, description, null);
    }
    public static Entry greater_madreds_bloodrazor = add(greater_madreds_bloodrazor());
    private static Entry greater_madreds_bloodrazor() {
        var id = Identifier.of(MOD_ID, "greater_madreds_bloodrazor");
        var description = "On melee hit: {trigger_chance_1} to receive increased attack speed by {bonus} and deal additional damage according to {max_health_damage} of the targets max health every hit.";
        var title = "Madred's Bloodrazor";
        var effect = MoreRelicEffects.GREATER_MADREDS_BLOODRAZOR;

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier_effect = effect.config().firstModifier();
            var bonus_effect = SpellTooltip.bonus(modifier_effect.value, modifier_effect.operation);
            var modifiedDescription = args.description();
            var max_health_damage = spell.impacts.get(0).action.damage;
            if (max_health_damage != null) {
                modifiedDescription = modifiedDescription.replace("{max_health_damage}", SpellTooltip.percent(max_health_damage.spell_power_coefficient));
            }
            return modifiedDescription
                    .replace("{bonus}", bonus_effect);
        };

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
        damage.action.damage.spell_power_coefficient = 0.03F;
        damage.sound = Sound.withVolume(MoreRelicSounds.MADREDS_BLOODRAZOR_IMPACT.id(), 1.25F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.dripping_blood.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.2F, 0.8F),
                new ParticleBatch(
                        "more_rpg_classes:blood_drop",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        20, 0.2F, 0.8F)
        };


        spell.impacts = List.of(damage);
        configureCooldown(spell, T3_TRANCE_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
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

        return new Entry(id, spell, title, description, null);
    }
    public static Entry greater_sunfire_cape = add(greater_sunfire_cape());
    private static Entry greater_sunfire_cape() {
        var id = Identifier.of(MOD_ID, "greater_sunfire_cape");
        var description = "On damage taken and dealt: {trigger_chance_1} to deal {max_health_damage} damage of your max health around you. ";
        var effect = MoreRelicEffects.GREATER_SUNFIRE_CAPE;
        var title = effect.title;

        var spell = passiveSpellBase();
        spell.range = 4.0F;
        spell.school = SpellSchools.FIRE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifiedDescription = args.description();
            var max_health_damage = spell.impacts.get(0).action.damage;
            if (max_health_damage != null) {
                modifiedDescription = modifiedDescription.replace("{max_health_damage}", SpellTooltip.percent(max_health_damage.spell_power_coefficient));
            }
            return modifiedDescription;
        };

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
        damage.action.damage.spell_power_coefficient = 0.04F;
        damage.sound = Sound.withVolume(SpellEngineSounds.GENERIC_FIRE_IGNITE.id(), 1.0F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.flame.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.2F, 0.8F)
        };

        spell.impacts = List.of(damage);
        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 4.0F;
        configureCooldown(spell, T3_TRANCE_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    ///SUPERIOR RELICS
    public static Entry superior_zhonyas_hourglass = add(superior_zhonyas_hourglass());
    private static Entry superior_zhonyas_hourglass() {
        var id = Identifier.of(MOD_ID, "superior_zhonyas_hourglass");
        var description = "Use: Become invulnerable for {effect_duration} seconds, you cant move, attack or cast spells.";
        var effect = MoreRelicEffects.SUPERIOR_ZHONYAS_HOURGLASS;
        var title = effect.title;

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = Sound.withVolume(MoreRelicSounds.ZHONYAS_HOURGLASS.id(), 0.75F);

        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.getMagicParticleVariant(
                                SpellEngineParticles.HOLY,
                                SpellEngineParticles.MagicParticleFamily.Shape.IMPACT,
                                SpellEngineParticles.MagicParticleFamily.Motion.DECELERATE
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.1F, 0.4F)
        };
        spell.impacts = List.of(createEffectImpact(effect.id.toString(), 3));
        configureCooldown(spell, T4_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, null);
    }
    public static Entry superior_mejais_soulstealer = add(superior_mejais_soulstealer());
    private static Entry superior_mejais_soulstealer() {
        var id = Identifier.of(MOD_ID, "superior_mejais_soulstealer");
        var effect = MoreRelicEffects.SUPERIOR_MEJAIS_SOULSTEALER;
        var title = effect.title;
        var description = "Defeating enemies grants you spell power by {bonus}, stacking up to {effect_amplifier_cap} times for {effect_duration} seconds.";
        var spell = passiveSpellBase();
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(Math.abs(modifier.value), modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

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

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry superior_shurelyas_battlesong = add(superior_shurelyas_battlesong());
    private static Entry superior_shurelyas_battlesong() {
        var id = Identifier.of(MOD_ID, "superior_shurelyas_battlesong");
        var effect = MoreRelicEffects.SUPERIOR_SHURELYAS_BATTLESONG;
        var title = effect.title;
        var description = "Use: Increase movement speed by {bonus} for {effect_duration} seconds and reduces spell cooldowns for allies.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };

        var spell = activeSpellBase();
        spell.range = 10;
        spell.school = SpellSchools.HEALING;
        spell.release.sound = new Sound(MoreRelicSounds.SHURELYAS_BATTLESONG_ACTIVATE.id());

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 1.0F;
        spell.target.area.include_caster = true;

        var buff = createEffectImpact(effect.id.toString(), T4_USE_EFFECT_DURATION / 2 );
        buff.particles = new ParticleBatch[]{
        };

        var impact = new Spell.Impact();
        impact.action = new Spell.Impact.Action();
        impact.action.type = Spell.Impact.Action.Type.COOLDOWN;
        impact.action.cooldown = new Spell.Impact.Action.Cooldown();
        impact.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        impact.action.cooldown.actives.duration_multiplier = 0.8F;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.sign_speed.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.CENTER,
                        1, 0.75F, 0.75F)
                        .scale(1.2F)
                        .color(Color.WHITE.toRGBA())
                        .followEntity(true)
        };

        spell.impacts = List.of(buff, impact);

        configureCooldown(spell, T4_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry superior_mikaels_blessing = add(superior_mikaels_blessing());
    private static Entry superior_mikaels_blessing() {
        var id = Identifier.of(MOD_ID, "superior_mikaels_blessing");
        var effect = MoreRelicEffects.SUPERIOR_MIKAELS_BLESSING;
        var title = effect.title;
        var description = "Use: Heals you or the targeted ally for {heal_percent} of your max health and cleanses all harmful effects.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifiedDescription = args.description();
            var spell = args.spellEntry().value();
            var heal = spell.impacts.get(0).action.heal;
            if (heal != null) {
                modifiedDescription = modifiedDescription.replace("{heal_percent}", SpellTooltip.percent(heal.spell_power_coefficient));
            }
            return modifiedDescription;
        };

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.range = 16;

        spell.release.animation = "spell_engine:one_handed_healing_release";
        spell.release.sound = new Sound(MoreRelicSounds.MIKAELS_BLESSING_ACTIVATE.id());

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.use_caster_as_fallback = true;

        var buff = createEffectImpact(effect.id.toString(), 1 );

        var heal = createHeal(0.15F);
        heal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        20, 0.02F, 0.15F),
                new ParticleBatch(SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.3F, 0.3F)
                        .color(Color.HOLY.toRGBA())
        };

        spell.impacts = List.of(heal,buff);

        configureCooldown(spell, T4_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry superior_guardian_angel = add(superior_guardian_angel());
    private static Entry superior_guardian_angel() {
        var id = Identifier.of(MOD_ID, "superior_guardian_angel");
        var title = "Guardian Angel";
        var effect = MoreRelicEffects.SUPERIOR_GUARDIAN_ANGEL;
        var health_threshold = 0.15F;
        var description = "Taking damage below {health_threshold} health, makes you invulnerable for {effect_duration} seconds and heals you for {health_percent} of your maximum health.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifiedDescription = args.description();
            var spell = args.spellEntry().value();
            var heal = spell.impacts.get(0).action.heal;
            if (heal != null) {
                modifiedDescription = modifiedDescription.replace("{health_percent}", SpellTooltip.percent(heal.spell_power_coefficient));
            }
            return modifiedDescription
                    .replace("{health_threshold}", SpellTooltip.percent(health_threshold));
        };

        var spell = passiveSpellBase();
        spell.school = SpellSchools.HEALING;

        spell.release.animation = "more_relics:guardian_angel";
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
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.area_circle_1.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.FEET,
                        1, 0.2F, 0.2F)
                        .followEntity(true)
                        .scale(0.8F)
                        .maxAge(0.8F)
                        .color(Color.WHITE.toRGBA()),
        };

        var buff = createEffectImpact(effect.id.toString(), 4);
        spell.impacts = List.of(heal, buff);

        configureCooldown(spell, T4_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
}
