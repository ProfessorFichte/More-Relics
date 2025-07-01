package more_relics.spell;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.relics_rpgs.spell.RelicSounds;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
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

    private static final float T4_USE_EFFECT_DURATION = 15;
    private static final float T4_USE_EFFECT_COOLDOWN = 90;
    private static final float T4_PROC_EFFECT_COOLDOWN = 90;
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

    private static @NotNull ParticleBatch lesserActivateParticles(SpellEngineParticles.MagicParticleFamily family, int count) {
        return lesserActivateParticles(SpellEngineParticles.getMagicParticleVariant(
                        family,
                        SpellEngineParticles.MagicParticleFamily.Shape.SPARK,
                        SpellEngineParticles.MagicParticleFamily.Motion.DECELERATE).id().toString(),
                count);
    }

    private static @NotNull ParticleBatch lesserActivateParticles(String particleId, int count) {
        return new ParticleBatch(
                particleId,
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                count, 0.14F, 0.15F);
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
                lesserActivateParticles(SpellEngineParticles.FROST, 12)
        };

        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

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
    public static Entry medium_use_rage_power = add(medium_use_rage_power());
    private static Entry medium_use_rage_power() {
        var id = Identifier.of(MOD_ID, "medium_use_rage_power");
        var description = "Use: Increases rage by {bonus} for {effect_duration} seconds.";
        var effect = MoreRelicEffects.MEDIUM_RAGE_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = "spell_engine:one_handed_area_release";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.1F, 0.1F)
                        .color(Color.RAGE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.12F, 0.12F)
                        .color(Color.RAGE.toRGBA()),
        };
        spell.impacts = List.of(createEffectImpact(effect.id.toString(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
    ///GREATER RELICS
    public static Entry greater_perk_rage = add(greater_perk_rage());
    private static Entry greater_perk_rage() {
        var id = Identifier.of(MOD_ID, "greater_perk_rage");
        var title = "Mardroeme Mushroom";
        var description = "On melee hit: {trigger_chance} chance to get into a berserk stage for {effect_duration} seconds.";
        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = 0.15F;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        ParticleBatch.Rotation.LOOK, 0.4F, 1.0F,1,0,2)
                        .color(Color.RAGE.toRGBA()),
        };

        spell.impacts = List.of(createEffectImpact(MoreRelicEffects.GREATER_RAGE_POWER.id.toString(), T3_TRANCE_DURATION));
        configureCooldown(spell, T3_TRANCE_COOLDOWN);

        return new Entry(id, spell, title, description, null);
    }
    ///SUPERIOR RELICS
    public static Entry superior_proc_rage = add(superior_proc_rage());
    private static Entry superior_proc_rage() {
        var id = Identifier.of(MOD_ID, "superior_proc_rage");
        var effect = MoreRelicEffects.SUPERIOR_RAGE_POWER;
        var title = "Svablod's Ritual";
        var health_threshold = 0.25F;
        var description = "Taking damage below {health_threshold} health, reduces damage by {bonus}, increases rage by {bonus2} and attack speed by {bonus3} for {effect_duration} seconds.";
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

        spell.impacts = List.of(createEffectImpact(MoreRelicEffects.SUPERIOR_RAGE_POWER.id.toString(), 5));
        configureCooldown(spell, T4_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }
}
