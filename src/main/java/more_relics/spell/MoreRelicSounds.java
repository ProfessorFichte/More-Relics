package more_relics.spell;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static more_relics.MoreRelics.MOD_ID;

public class MoreRelicSounds {
    public record Entry(String name) {
        public Identifier id() {
            return Identifier.of(MOD_ID, name);
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }
    public static final Entry ZHONYAS_HOURGLASS = add(new Entry("zhonyas_hourglass"));
    public static final Entry RAGE_POWDER = add(new Entry("rage_powder"));
    public static final Entry UPCUT_STONE = add(new Entry("upcut_stone"));
    public static final Entry KIRCHEIS_SHARD_PROC = add(new Entry("kircheis_shard_proc"));
    public static final Entry KIRCHEIS_SHARD_IMPACT = add(new Entry("kircheis_shard_impact"));
    public static final Entry SHURELYAS_BATTLESONG_ACTIVATE = add(new Entry("shurelyas_battlesong_activate"));
    public static final Entry MADREDS_BLOODRAZOR_IMPACT = add(new Entry("madreds_bloodrazor_impact"));
    public static final Entry LIANDRYS_TORMENT_PROC = add(new Entry("liandrys_torment_proc"));
    public static final Entry MIKAELS_BLESSING_ACTIVATE = add(new Entry("mikaels_blesssing_active"));
    public static final Entry GUARDIAN_ANGEL_ACTIVATE = add(new Entry("guardian_angel_active"));

    public static void register() {
        for (var entry: entries) {
            var soundId = entry.id();
            var soundEvent = SoundEvent.of(soundId);
            Registry.register(Registries.SOUND_EVENT, soundId, soundEvent);
        }
    }
}
