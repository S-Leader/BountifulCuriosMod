package keletu.bountifulcurios.loot;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import org.jspecify.annotations.Nullable;

public record TrialRewardContext(boolean ominous, String mob) {
    private static final ThreadLocal<TrialRewardContext> CURRENT = new ThreadLocal<>();

    public static @Nullable TrialRewardContext current() { return CURRENT.get(); }

    public static void duringReward(TrialSpawner spawner, Runnable action) {
        TrialRewardContext previous = CURRENT.get();
        String mob = spawner.getStateData().pack().nextSpawnData()
                .flatMap(data -> data.getEntityToSpawn().getString("id"))
                .map(Identifier::tryParse).map(Identifier::toString).orElse("");
        try {
            CURRENT.set(new TrialRewardContext(spawner.isOminous(), mob));
            action.run();
        } finally {
            if (previous == null) CURRENT.remove();
            else CURRENT.set(previous);
        }
    }
}
