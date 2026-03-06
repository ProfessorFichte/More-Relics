package more_relics.spell.effect;

import net.spell_engine.api.effect.EntityActionsAllowed;

public class MoreRelicsActionImpairing {
    public static final EntityActionsAllowed REVIVING = new EntityActionsAllowed(
            false,
            false,
            new EntityActionsAllowed.PlayersAllowed(
                    false,
                    false,
                    false
            ),
            new EntityActionsAllowed.MobsAllowed(
                    false
            ),
            EntityActionsAllowed.SemanticType.NONE
    );
    public static final EntityActionsAllowed ZHONYAS = new EntityActionsAllowed(
            false,
            false,
            new EntityActionsAllowed.PlayersAllowed(
                    false,
                    false,
                    false
            ),
            new EntityActionsAllowed.MobsAllowed(
                    false
            ),
            EntityActionsAllowed.SemanticType.NONE
    );
}
