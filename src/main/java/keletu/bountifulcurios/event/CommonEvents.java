package keletu.bountifulcurios.event;

import java.util.Set;
import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.compat.CurioUtil;
import keletu.bountifulcurios.registry.ModEffects;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemUseAnimation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = BountifulCurios.MODID)
public final class CommonEvents {
    public static final Identifier BROKEN_HEART_ID = BountifulCurios.id("broken_heart_health_drain");
    private static final String PRIDE_MARKER = "bountifulcurios:pride_sinful_active";
    private static final Set<Holder<MobEffect>> ANKH_IMMUNITIES = Set.of(MobEffects.BLINDNESS, MobEffects.NAUSEA, MobEffects.DARKNESS, MobEffects.HUNGER, MobEffects.MINING_FATIGUE, MobEffects.WEAKNESS, MobEffects.SLOWNESS, MobEffects.LEVITATION, MobEffects.POISON, MobEffects.WITHER);

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player && CurioUtil.isAnyEquipped(player, ModItems.BALLOON.get(), ModItems.HORSESHOE_BALLOON.get())) {
            player.setDeltaMovement(player.getDeltaMovement().add(0, 0.3, 0));
            player.fallDistance = Math.max(0, player.fallDistance - 5);
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (CurioUtil.isAnyEquipped(player, ModItems.LUCKY_HORSESHOE.get(), ModItems.HORSESHOE_BALLOON.get())) event.setCanceled(true);
        else if (CurioUtil.isEquipped(player, ModItems.BALLOON.get())) event.setDistance(Math.max(0, event.getDistance() - 5));
    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;
        Holder<MobEffect> effect = event.getEffectInstance().getEffect();
        boolean shield = CurioUtil.isEquipped(player, ModItems.SHIELD_ANKH.get()) || CurioUtil.isHeld(player, ModItems.SHIELD_ANKH.get());
        if (CurioUtil.hasImmunity(player, effect) || shield && ANKH_IMMUNITIES.contains(effect)) event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }

    @SubscribeEvent
    public static void onFireDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player) || !event.getSource().is(DamageTypeTags.IS_FIRE)) return;
        if (CurioUtil.isAnyEquipped(player, ModItems.OBSIDIAN_SKULL.get(), ModItems.SHIELD_OBSIDIAN.get(), ModItems.SHIELD_ANKH.get()) || CurioUtil.isHeld(player, ModItems.SHIELD_OBSIDIAN.get(), ModItems.SHIELD_ANKH.get())) {
            event.setAmount(event.getAmount() * 0.5F);
            player.clearFire();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamageIFrames(LivingDamageEvent.Post event) {
        if (event.getHealthDamage() > 0 && event.getEntity() instanceof Player player && CurioUtil.isEquipped(player, ModItems.AMULET_CROSS.get())) {
            player.invulnerableTime = Math.max(player.invulnerableTime, 36);
        }
    }

    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player && (CurioUtil.isAnyEquipped(player, ModItems.SHIELD_COBALT.get(), ModItems.SHIELD_OBSIDIAN.get(), ModItems.SHIELD_ANKH.get()) || CurioUtil.isHeld(player, ModItems.SHIELD_COBALT.get(), ModItems.SHIELD_OBSIDIAN.get(), ModItems.SHIELD_ANKH.get()))) event.setStrength(0);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLethalDamage(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player) || player.getHealth() - event.getNewDamage() >= 1 || !CurioUtil.isEquipped(player, ModItems.BROKEN_HEART.get())) return;
        double loss = 1 - (player.getHealth() - event.getNewDamage());
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        if (health == null || health.getValue() <= loss + 1) return;
        AttributeModifier old = health.getModifier(BROKEN_HEART_ID);
        double oldLoss = old == null ? 0 : old.amount();
        health.removeModifier(BROKEN_HEART_ID);
        health.addPermanentModifier(new AttributeModifier(BROKEN_HEART_ID, oldLoss - loss, AttributeModifier.Operation.ADD_VALUE));
        event.setNewDamage(Math.max(0, event.getNewDamage() - (float) loss));
        player.level().playSound(null, player.blockPosition(), SoundEvents.IRON_GOLEM_HURT, SoundSource.PLAYERS, 0.7F, 0.85F);
    }

    @SubscribeEvent
    public static void onWake(PlayerWakeUpEvent event) {
        AttributeInstance health = event.getEntity().getAttribute(Attributes.MAX_HEALTH);
        if (health != null) health.removeModifier(BROKEN_HEART_ID);
    }

    @SubscribeEvent
    public static void onUseStart(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player && CurioUtil.isEquipped(player, ModItems.AMULET_SIN_GLUTTONY.get())) {
            ItemUseAnimation animation = event.getItem().getUseAnimation();
            if ((animation == ItemUseAnimation.EAT || animation == ItemUseAnimation.DRINK) && event.getDuration() > 7) event.setDuration(7);
        }
    }

    @SubscribeEvent
    public static void onUseTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof Player player && CurioUtil.isEquipped(player, ModItems.AMULET_SIN_GLUTTONY.get())) {
            ItemUseAnimation animation = event.getItem().getUseAnimation();
            if ((animation == ItemUseAnimation.EAT || animation == ItemUseAnimation.DRINK) && event.getDuration() > 7) event.setDuration(7);
        }
    }

    @SubscribeEvent
    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player && CurioUtil.isEquipped(player, ModItems.AMULET_SIN_GLUTTONY.get()) && event.getItem().getUseAnimation() == ItemUseAnimation.EAT) {
            FoodProperties food = event.getItem().get(DataComponents.FOOD);
            double saturationModifier = food == null ? 0 : food.saturation() / Math.max(1.0, food.nutrition() * 2.0);
            int amplifier = food == null ? 0 : (int) Math.floor(food.nutrition() / 4.0 + saturationModifier / 6.0);
            applySinfulBuff(player, amplifier, 200, true);
        }
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        if (CurioUtil.isEquipped(event.getEntity(), ModItems.AMULET_SIN_WRATH.get())) applySinfulBuff(event.getEntity(), 3, 120, true);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        boolean active = CurioUtil.isEquipped(player, ModItems.AMULET_SIN_PRIDE.get()) && player.getHealth() + 0.5F > player.getMaxHealth();
        boolean granted = player.getPersistentData().getBooleanOr(PRIDE_MARKER, false);
        MobEffectInstance sinful = player.getEffect(ModEffects.SINFUL);
        if (active) {
            if (sinful == null || sinful.getAmplifier() == 0 && sinful.getDuration() < 40) applySinfulBuff(player, 0, -1, false);
            player.getPersistentData().putBoolean(PRIDE_MARKER, true);
        } else if (granted && (sinful == null || sinful.getAmplifier() == 0)) {
            player.removeEffect(ModEffects.SINFUL);
            player.getPersistentData().remove(PRIDE_MARKER);
        }
    }

    private static void applySinfulBuff(Player player, int amplifier, int duration, boolean particles) {
        player.addEffect(new MobEffectInstance(ModEffects.SINFUL, duration, amplifier, false, particles, true));
    }

    private CommonEvents() {
    }
}
