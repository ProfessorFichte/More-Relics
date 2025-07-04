package more_relics;

import more_relics.item.Group;
import more_relics.item.MoreRelicsItems;
import more_relics.spell.MoreRelicEffects;
import more_relics.spell.MoreRelicSounds;
import more_relics.spell.MoreRelicSpells;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.relics_rpgs.item.RelicItemTags;
import net.spell_engine.api.datagen.SimpleSoundGenerator;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static more_relics.MoreRelics.MOD_ID;

public class MoreRelicsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(LangGenerator::new);
		pack.addProvider(ModelProvider::new);
		pack.addProvider(MoreRelicsSpellGen::new);
		pack.addProvider(SoundGen::new);
	}
	public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {

		public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			var all = getOrCreateTagBuilder(RelicItemTags.ALL);
			MoreRelicsItems.entries.forEach(entry -> all.addOptional(entry.id()));

			HashMap<Identifier, Equipment.LootProperties> relicEntries = new HashMap<>();
			for (var entry: MoreRelicsItems.entries) {
				var id = entry.id();
				var lootProperties = Equipment.LootProperties.of(entry.tier(), entry.lootTheme);
				relicEntries.put(id, lootProperties);
			}
			generateRelicTags(relicEntries);
		}
	}

	public static class LangGenerator extends FabricLanguageProvider {
		protected LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, "en_us", registryLookup);
		}

		@Override
		public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
			translationBuilder.add(Group.translationKey, "More Relics");
			MoreRelicsItems.entries.forEach(entry ->
					translationBuilder.add(entry.item().get().getTranslationKey(), entry.translatedName())
			);
			MoreRelicSpells.entries.forEach(entry -> {
				var id = entry.id();
				translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name" , entry.title());
				translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description" , entry.description());
			});
			MoreRelicEffects.entries.forEach(entry -> {
				translationBuilder.add(entry.effect.getTranslationKey(), entry.title);
				translationBuilder.add(entry.effect.getTranslationKey() + ".description", entry.description);
			});
		}
	}

	public static class ModelProvider extends FabricModelProvider {
		public ModelProvider(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			MoreRelicsItems.entries.forEach(entry -> {
				itemModelGenerator.register(entry.item().get(), Models.GENERATED);
			});
		}
	}

	public static class MoreRelicsSpellGen extends SpellGenerator {
		public MoreRelicsSpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateSpells(Builder builder) {
			for (var entry: MoreRelicSpells.entries) {
				builder.add(entry.id(), entry.spell());
			}
		}
	}

	public static class SoundGen extends SimpleSoundGenerator {
		public SoundGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateSounds(Builder builder) {
			builder.entries.add(new Entry(MOD_ID,
					MoreRelicSounds.entries.stream().map( MoreRelicSounds.Entry::name).toList()));
		}
	}
}
