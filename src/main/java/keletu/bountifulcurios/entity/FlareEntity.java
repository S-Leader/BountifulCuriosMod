package keletu.bountifulcurios.entity;

import keletu.bountifulcurios.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FlareEntity extends ThrowableProjectile {
    private static final int MAX_LIFETIME = 12_000;

    private boolean inGround;
    @Nullable
    private BlockPos stuckPos;
    @Nullable
    private Block stuckBlock;

    public FlareEntity(EntityType<? extends FlareEntity> type, Level level) {
        super(type, level);
    }

    public FlareEntity(Level level, LivingEntity owner) {
        super(ModEntities.FLARE.get(), owner, level);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        boolean stillStuck = inGround && stuckPos != null && stuckBlock != null
                && level().getBlockState(stuckPos).is(stuckBlock);
        Vec3 fixedPosition = position();

        if (!stillStuck) {
            inGround = false;
            stuckPos = null;
            stuckBlock = null;
        }

        super.tick();

        if (stillStuck) {
            setPos(fixedPosition);
            setDeltaMovement(Vec3.ZERO);
        }

        if (level().isClientSide && tickCount % (inGround ? 6 : 2) == 1) {
            Vec3 trail = Vec3.directionFromRotation(getXRot(), getYRot()).scale(-1.0D);
            level().addParticle(ParticleTypes.FLAME,
                    getX() + trail.x * 0.3D,
                    getY() + trail.y * 0.3D,
                    getZ() + trail.z * 0.3D,
                    trail.x * 0.055D,
                    trail.y * 0.055D,
                    trail.z * 0.055D);
        }

        if (!level().isClientSide && tickCount > MAX_LIFETIME) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        // The original flare is a light source and marker, not a weapon.
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Vec3 movement = getDeltaMovement();
        Vec3 location = result.getLocation();
        if (movement.lengthSqr() > 1.0E-7D) {
            location = location.subtract(movement.normalize().scale(0.05D));
        }
        setPos(location);
        setDeltaMovement(Vec3.ZERO);
        inGround = true;
        stuckPos = result.getBlockPos().immutable();
        stuckBlock = level().getBlockState(stuckPos).getBlock();
    }

    @Override
    protected float getGravity() {
        return 0.03F;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("InGround", inGround);
        if (stuckPos != null) {
            tag.putLong("StuckPos", stuckPos.asLong());
        }
        if (stuckBlock != null) {
            tag.putString("StuckBlock", BuiltInRegistries.BLOCK.getKey(stuckBlock).toString());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        inGround = tag.getBoolean("InGround");
        stuckPos = tag.contains("StuckPos") ? BlockPos.of(tag.getLong("StuckPos")) : null;
        if (tag.contains("StuckBlock")) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("StuckBlock"));
            stuckBlock = id == null ? null : BuiltInRegistries.BLOCK.get(id);
        } else {
            stuckBlock = null;
        }
    }
}
