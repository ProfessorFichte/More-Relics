package more_relics;

import more_relics.item.Group;
import more_relics.item.ItemCompat;
import more_relics.item.MoreRelicsItems;
import more_relics.spell.MoreRelicEffects;
import more_relics.spell.MoreRelicSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.relics_rpgs.config.ItemConfig;
import net.spell_engine.api.config.ConfigFile;
import net.tinyconfig.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreRelics implements ModInitializer {
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


	@Override
	public void onInitialize() {
		itemConfig.refresh();
		effectConfig.refresh();
		ItemCompat.register();
		MoreRelicSounds.register();
		Group.GROUP = FabricItemGroup.builder()
				.icon(Group.ICON)
				.displayName(Text.translatable(Group.translationKey))
				.build();
		Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);
		MoreRelicsItems.register(itemConfig.value.entries);
		itemConfig.save();
		MoreRelicEffects.register(effectConfig.value);
		effectConfig.save();
	}
}