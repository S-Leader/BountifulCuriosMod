package keletu.bountifulcurios.entity;

import keletu.bountifulcurios.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class FlareEntity extends ThrowableProjectile {
    private boolean inGround;
    private int lifeTicks;
    private @Nullable BlockPos stuckPos;
    private @Nullable Block stuckBlock;

    public FlareEntity(EntityType<? extends FlareEntity> type, Level level) { super(type, level); }

    public FlareEntity(Level level, LivingEntity owner) {
        this(ModEntities.FLARE.get(), level);
        setOwner(owner);
        setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        boolean stuck = inGround && stuckPos != null && stuckBlock != null && level().getBlockState(stuckPos).is(stuckBlock);
        Vec3 fixed = position();
        if (!stuck) { inGround = false; stuckPos = null; stuckBlock = null; }
        super.tick();
        if (stuck) { setPos(fixed); setDeltaMovement(Vec3.ZERO); }
        if (level().isClientSide() && tickCount % (inGround ? 6 : 2) == 1) {
            Vec3 trail = Vec3.directionFromRotation(getXRot(), getYRot()).scale(-1);
            level().addParticle(ParticleTypes.FLAME, getX() + trail.x * 0.3, getY() + trail.y * 0.3, getZ() + trail.z * 0.3, trail.x * 0.055, trail.y * 0.055, trail.z * 0.055);
        }
        if (!level().isClientSide() && ++lifeTicks > 12000) discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Vec3 movement = getDeltaMovement();
        Vec3 location = result.getLocation();
        if (movement.lengthSqr() > 1.0E-7) location = location.subtract(movement.normalize().scale(0.05));
        setPos(location);
        setDeltaMovement(Vec3.ZERO);
        inGround = true;
        stuckPos = result.getBlockPos().immutable();
        stuckBlock = level().getBlockState(stuckPos).getBlock();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("InGround", inGround);
        output.putInt("LifeTicks", lifeTicks);
        if (stuckPos != null) output.putLong("StuckPos", stuckPos.asLong());
        if (stuckBlock != null) output.store("StuckBlock", Identifier.CODEC, BuiltInRegistries.BLOCK.getKey(stuckBlock));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        inGround = input.getBooleanOr("InGround", false);
        lifeTicks = input.getIntOr("LifeTicks", 0);
        stuckPos = input.getLong("StuckPos").map(BlockPos::of).orElse(null);
        stuckBlock = input.read("StuckBlock", Identifier.CODEC).flatMap(BuiltInRegistries.BLOCK::getOptional).orElse(null);
    }
}
