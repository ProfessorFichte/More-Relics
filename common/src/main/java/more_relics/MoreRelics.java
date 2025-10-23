package more_relics;

import more_relics.item.MoreRelicsItems;
import more_relics.spell.MoreRelicEffects;
import more_relics.spell.MoreRelicSounds;
import net.relics_rpgs.config.ItemConfig;
import net.spell_engine.api.config.ConfigFile;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreRelics {
	public static final String MOD_ID = "more_relics";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<>
			("items", new ItemConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
			("effects", new ConfigFile.Effects())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();


	public static void init() {
		itemConfig.refresh();
		effectConfig.refresh();
		itemConfig.save();
		effectConfig.save();
	}

	public static void registerSounds() {
		MoreRelicSounds.register();
	}

	public static void registerItems() {
		MoreRelicsItems.register(itemConfig.value.entries);
		itemConfig.save();
	}

	public static void registerEffects() {
		MoreRelicEffects.register(effectConfig.value);
		effectConfig.save();
	}
}