package more_relics.item;


import com.google.common.base.Suppliers;
import more_relics.spell.MoreRelicSpells;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.relics_rpgs.config.ItemConfig;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigUtil;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_engine.api.spell.container.SpellContainers;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static more_relics.MoreRelics.MOD_ID;

public class MoreRelicsItems {
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public record ItemArgs(Item.Settings settings, @Nullable AttributeModifiersComponent attributes) { }
    public static Function<ItemArgs, Item> factory = args -> {
        var settings = args.settings;
        if (args.attributes != null) {
            settings.attributeModifiers(args.attributes);
        }
        return new Item(settings);
    };
    private static Function<ItemArgs, Item> getFactory() { return factory; }

    public static final class Entry {
        private final int tier;
        public String lootTheme;
        private final String name;
        private final String translatedName;
        private ItemConfig.Entry config;
        public ItemConfig.Entry defaults;
        private final Supplier<Item> item;
        private SpellContainer spellContainer;

        public Entry(int tier, String name, String translatedName) {
            this(tier, name, translatedName, ItemConfig.Entry.EMPTY);
        }

        public Entry(int tier, String name, String translatedName, ItemConfig.Entry config) {
            this.tier = tier;
            this.name = name;
            this.translatedName = translatedName;
            this.config = config;
            this.defaults = config;

            this.item = Suppliers.memoize(() -> {
                var settings = new Item.Settings()
                        .maxCount(1);
                var attributes = (config().attributes != null && !config().attributes.isEmpty())
                        ? ConfigUtil.attributesComponent(Identifier.of(MOD_ID, name), config().attributes).build()
                        : null;
                var spellContainer = spellContainer();
                if (spellContainer != null) {
                    settings = settings.component(SpellDataComponents.SPELL_CONTAINER, spellContainer);
                }
                if (config().durability > 0) {
                    settings = settings.maxDamage(config().durability);
                }

                var rarity = rarityFrom(tier);
                if (rarity != Rarity.COMMON) {
                    settings = settings.rarity(rarity);
                }
                return MoreRelicsFactory.getFactory().apply(new MoreRelicsFactory.ItemArgs(settings, attributes));
            });
        }

        private static Rarity rarityFrom(int tier) {
            return switch (tier) {
                case 0, 1 -> Rarity.COMMON;
                case 2 -> Rarity.UNCOMMON;
                case 3 -> Rarity.RARE;
                default -> Rarity.EPIC;
            };
        }

        public int tier() {
            return tier;
        }

        public Identifier id() {
            return Identifier.of(MOD_ID, name);
        }

        public String name() {
            return name;
        }

        public String translatedName() {
            return translatedName;
        }

        public ItemConfig.Entry config() {
            return config;
        }

        public Supplier<Item> item() {
            return item;
        }

        @Nullable public SpellContainer spellContainer() {
            return spellContainer;
        }

        public Entry config(ItemConfig.Entry config) {
            this.config = config;
            return this;
        }

        public Entry spell(SpellContainer spellContainer) {
            this.spellContainer = spellContainer;
            return this;
        }

        public Entry lootTheme(String lootTheme) {
            this.lootTheme = lootTheme;
            return this;
        }

        public boolean isEnabled() {
            return true;
        }
    }

    private static final float tier_0_multiplier = 0.05F;

    public static final Entry JEWEL_FIGURINE_MALACHITE = add(new Entry(1, "jewel_figurine_malachite", "Malachite Honeybadger Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MoreSpellSchools.EARTH.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(MoreSpellSchools.NATURE.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_AQUAMARINE = add(new Entry(1, "jewel_figurine_aquamarine", "Aquamarine Koi-Carp Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MoreSpellSchools.AIR.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(MoreSpellSchools.WATER.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_CHAIN = add(new Entry(1, "jewel_figurine_chain", "Metallic Wolf Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MRPGCEntityAttributes.RAGE_MODIFIER.getIdAsString(), tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), tier_0_multiplier/2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );

    public static final Entry LESSER_PROC_AIR_WATER= add(new Entry(1, "lesser_proc_air_water", "Bottled Typhoon"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.lesser_proc_air_water.id()));
    public static final Entry LESSER_PROC_EARTH_NATURE = add(new Entry(1, "lesser_proc_earth_nature", "Glowing Moss Stone"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.lesser_proc_earth_nature.id()));
    public static final Entry LESSER_USE_RAGE = add(new Entry(1, "lesser_use_rage", "Rage Powder"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.lesser_use_rage_power.id()));

    public static final Entry MEDIUM_USE_AIR_POWER = add(new Entry(2, "medium_use_air_power", "Air Orb"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.medium_use_air_power.id()));
    public static final Entry MEDIUM_USE_EARTH_POWER = add(new Entry(2, "medium_use_earth_power", "Earth Orb"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.medium_use_earth_power.id()));
    public static final Entry MEDIUM_USE_WATER_POWER = add(new Entry(2, "medium_use_water_power", "Water Orb"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.medium_use_water_power.id()));
    public static final Entry MEDIUM_USE_NATURE_POWER = add(new Entry(2, "medium_use_nature_power", "Nature Orb"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.medium_use_nature_power.id()));
    public static final Entry MEDIUM_PROC_RAGE = add(new Entry(2, "medium_proc_rage", "Mardroeme Mushroom"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.medium_perk_rage.id()));
    public static final Entry MEDIUM_PROC_LIFESTEAL = add(new Entry(2, "medium_proc_lifesteal", "Vampiric Scepter"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.medium_proc_lifesteal.id()));


    public static final Entry GREATER_FROZEN_HEART = add(new Entry(3, "greater_frozen_heart", "Frozen Heart"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.greater_frozen_heart.id()));
    public static final Entry GREATER_PROC_RAGE = add(new Entry(3, "greater_proc_rage", "Svablod's Totem"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.greater_proc_rage.id()));
    public static final Entry GREATER_KIRCHEIS_SHARD = add(new Entry(3, "greater_kircheis_shard", "Kircheis Shard"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.greater_kircheis_shard.id()));
    public static final Entry GREATER_MADREDS_BLOODRAZOR = add(new Entry(3, "greater_madreds_bloodrazor", "Madred's Bloodrazor"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.greater_madreds_bloodrazor.id()));
    public static final Entry GREATER_LIANDRYS_TORMENT = add(new Entry(3, "greater_liandrys_torment", "Liandry's Torment"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.greater_liandrys_torment.id()));
    public static final Entry GREATER_SUNFIRE_CAPE = add(new Entry(3, "greater_sunfire_cape", "Sunfire Cape"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.greater_sunfire_cape.id()));

    public static final Entry SUPERIOR_MEJAIS_SOULSTEALER = add(new Entry(4, "superior_mejais_soulstealer", "Mejai's Soulstealer"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.superior_mejais_soulstealer.id()));
    public static final Entry SUPERIOR_ZHONYAS_HOURGLASS = add(new Entry(4, "superior_zhonyas_hourglass", "Zhonya's Hourglass"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.superior_zhonyas_hourglass.id()));
    public static final Entry SUPERIOR_SHURELYAS_BATTLESONG = add(new Entry(4, "superior_shurelyas_battlesong", "Shurelya's Battlesong"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.superior_shurelyas_battlesong.id()));
    public static final Entry SUPERIOR_MIKAELS_BLESSING = add(new Entry(4, "superior_mikaels_blessing", "Mikael's Blessing"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.superior_mikaels_blessing.id()));
    public static final Entry SUPERIOR_GUARDIAN_ANGEL = add(new Entry(4, "superior_guardian_angel", "Guardian Angel"))
            .spell(SpellContainers.forRelic(MoreRelicSpells.superior_guardian_angel.id()));

    public static void register(Map<String, ItemConfig.Entry> config) {
        for (var entry : entries) {
            var key = entry.id().toString();
            var configEntry = config.get(key);
            if (configEntry != null) {
                entry.config(configEntry);
            } else {
                config.put(key, entry.config());
            }
        }

        for(var entry: entries) {
            if (entry.isEnabled()) {
                Registry.register(Registries.ITEM, entry.id(), entry.item().get());
            }
        }
    }

    public static void addToGroup(ItemGroup.Entries entries) {
        for (var entry : MoreRelicsItems.entries) {
            if (entry.isEnabled()) {
                entries.add(entry.item().get());
            }
        }
    }
}
