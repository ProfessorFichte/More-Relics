package more_relics.item;


import com.google.common.base.Suppliers;
import more_relics.spell.MoreRelicSpells;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.relics_rpgs.config.ItemConfig;
import net.relics_rpgs.spell.RelicSpells;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigUtil;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
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
                return getFactory().apply(new ItemArgs(settings, attributes));
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

    public static final Entry JEWEL_FIGURINE_WATER = add(new Entry(1, "jewel_figurine_earth", "Citrine Honeybadger Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MoreSpellSchools.EARTH.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_JADE = add(new Entry(1, "jewel_figurine_air", "Jade Pegasus Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MoreSpellSchools.AIR.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_SAPPHIRE = add(new Entry(1, "jewel_figurine_water", "Sapphire Koi-Carp Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MoreSpellSchools.WATER.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_RAGE = add(new Entry(1, "jewel_figurine_rage", "Ruby Wolf Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MRPGCEntityAttributes.RAGE_MODIFIER.getIdAsString(), tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );

    public static final Entry LESSER_PROC_AIR_LIGHTNING = add(new Entry(1, "lesser_proc_air_lightning", "Bottled Storm"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.lesser_proc_air_lightning.id()));
    public static final Entry LESSER_PROC_EARTH_FIRE = add(new Entry(1, "lesser_proc_earth_fire", "Volcanic Crystal"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.lesser_proc_earth_fire.id()));
    public static final Entry LESSER_PROC_WATER_FROST = add(new Entry(1, "lesser_proc_water_frost", "Crystallized Tear"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.lesser_proc_water_frost.id()));

    public static final Entry MEDIUM_USE_AIR_POWER = add(new Entry(2, "medium_use_air_power", "Air Orb"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.medium_use_air_power.id()));
    public static final Entry MEDIUM_USE_EARTH_POWER = add(new Entry(2, "medium_use_earth_power", "Earth Orb"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.medium_use_earth_power.id()));
    public static final Entry MEDIUM_USE_WATER_POWER = add(new Entry(2, "medium_use_water_power", "Water Orb"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.medium_use_water_power.id()));
    public static final Entry MEDIUM_USE_RAGE = add(new Entry(2, "medium_use_rage", "Rage Powder"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.medium_use_rage_power.id()));

    public static final Entry GREATER_PERK_RAGE = add(new Entry(3, "greater_perk_rage", "Mardroeme Mushroom"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.greater_perk_rage.id()));

    public static final Entry SUPERIOR_PROC_RAGE = add(new Entry(4, "superior_proc_rage", "Svablod's Totem"))
            .spell(SpellContainerHelper.createForRelic(MoreRelicSpells.superior_proc_rage.id()));

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
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            for(var entry: entries) {
                if (entry.isEnabled()) {
                    content.add(entry.item().get());
                }
            }
        });
    }
}
