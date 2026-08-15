package keletu.bountifulcurios.event;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.compat.CurioUtil;
import keletu.bountifulcurios.registry.ModEffects;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = BountifulCurios.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CommonEvents {
    public static final UUID BROKEN_HEART_UUID =
            UUID.fromString("554f3929-4193-4ae5-a4da-4b528a89ca32");
    private static final int CROSS_INVULNERABLE_TICKS = 36;
    private static final String PRIDE_SINFUL_MARKER =
            "bountifulcurios:pride_sinful_active";
    private static final Set<MobEffect> ANKH_IMMUNITIES = Set.of(
            MobEffects.BLINDNESS, MobEffects.CONFUSION, MobEffects.DARKNESS, MobEffects.HUNGER,
            MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.LEVITATION, MobEffects.POISON, MobEffects.WITHER);

    @SubscribeEvent
    public static void verifyDevelopmentServer(ServerStartedEvent event) {
        if (!Boolean.getBoolean("bountifulcurios.verify")) {
            return;
        }
        List<String> requiredRecipes = new java.util.ArrayList<>(List.of(
                "bountifulcurios:amulet_sin_gluttony",
                "bountifulcurios:wormhole_mirror",
                "bountifulcurios:disintegration/amulet_cross",
                "bountifulcurios:iron_ring",
                "bountifulcurios:ring_flywheel",
                "bountifulcurios:ring_flywheel_advanced",
                "bountifulcurios:crown_gold",
                "bountifulcurios:flare_red",
                "bountifulcurios:flare_gun",
                "bountifulcurios:spectral_silt/balloon",
                "bountifulcurios:spectral_silt/shield_cobalt",
                "bountifulcurios:smithing/ring_free_action",
                "bountifulcurios:smithing/mixed_dragon_scale",
                "bountifulcurios:smithing/shield_obsidian",
                "bountifulcurios:smithing/shield_ankh",
                "bountifulcurios:smithing/vitamins_alpha"));
        if (!ModList.get().isLoaded("botania")) {
            requiredRecipes.add("bountifulcurios:phantom_prism");
        }
        List<String> missing = requiredRecipes.stream().filter(id -> event.getServer()
                .getRecipeManager().byKey(ResourceLocation.tryParse(id)).isEmpty()).toList();
        if (!missing.isEmpty()) {
            throw new IllegalStateException("Missing Bountiful Curios recipes: " + missing);
        }
        ItemStack flywheel = new ItemStack(ModItems.RING_FLYWHEEL.get());
        if (!flywheel.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
            throw new IllegalStateException("Flywheel ring energy capability is unavailable");
        }
        if (ModEffects.SINFUL.get().getAttributeModifiers().size() != 3) {
            throw new IllegalStateException("Sinful effect is missing its 1.12.2 attributes");
        }

        Recipe<?> shieldRecipe = event.getServer().getRecipeManager().byKey(
                        new ResourceLocation("bountifulcurios", "smithing/shield_obsidian"))
                .orElseThrow();
        if (!(shieldRecipe instanceof SmithingRecipe smithingRecipe)
                || shieldRecipe.getSerializer() != RecipeSerializer.SMITHING_TRANSFORM) {
            throw new IllegalStateException("Shield upgrade is not a vanilla JSON smithing recipe");
        }
        SimpleContainer smithing = new SimpleContainer(3);
        smithing.setItem(0, new ItemStack(ModItems.RESPLENDENT_TOKEN.get()));
        ItemStack damagedShield = new ItemStack(ModItems.SHIELD_COBALT.get());
        damagedShield.setDamageValue(37);
        smithing.setItem(1, damagedShield);
        smithing.setItem(2, new ItemStack(ModItems.OBSIDIAN_SKULL.get()));
        ItemStack upgradedShield = smithingRecipe.assemble(smithing,
                event.getServer().registryAccess());
        if (!smithingRecipe.matches(smithing, event.getServer().overworld())
                || !upgradedShield.is(ModItems.SHIELD_OBSIDIAN.get())
                || upgradedShield.getDamageValue() != 37) {
            throw new IllegalStateException("Legacy shield smithing upgrade did not preserve NBT");
        }
        smithing.setItem(0, ItemStack.EMPTY);
        if (smithingRecipe.matches(smithing, event.getServer().overworld())) {
            throw new IllegalStateException("Smithing upgrade accepted an empty template slot");
        }

        List<String> removedTokenRecipes = List.of(
                "amulet_cross", "amulet_sin_empty", "balloon",
                "broken_black_dragon_scale", "broken_heart", "lucky_horseshoe",
                "magic_mirror", "shield_cobalt", "sunglasses");
        List<String> tokenRecipesStillPresent = removedTokenRecipes.stream()
                .map(id -> new ResourceLocation(BountifulCurios.MODID, "rtoken/" + id))
                .filter(id -> event.getServer().getRecipeManager().byKey(id).isPresent())
                .map(ResourceLocation::toString).toList();
        if (!tokenRecipesStillPresent.isEmpty()) {
            throw new IllegalStateException("Removed Resplendent Token recipes remain: "
                    + tokenRecipesStillPresent);
        }
        if (!chestLootContains(event.getServer(), BuiltInLootTables.ANCIENT_CITY,
                ModItems.VITAMINS_A.get(), 512)) {
            throw new IllegalStateException("Ancient city Vitamin A loot injection is inactive");
        }
        if (!chestLootContains(event.getServer(), BuiltInLootTables.SIMPLE_DUNGEON,
                ModItems.POTION_RECALL.get(), 128)) {
            throw new IllegalStateException("Original dungeon loot injection is inactive");
        }
        if (!chestLootContains(event.getServer(), BuiltInLootTables.NETHER_BRIDGE,
                ModItems.IRON_RING.get(), 512)) {
            throw new IllegalStateException("Original nether fortress loot injection is inactive");
        }
        FakePlayer testPlayer = FakePlayerFactory.getMinecraft(event.getServer().overworld());
        ICuriosItemHandler testCurios = CuriosApi.getCuriosInventory(testPlayer)
                .orElseThrow(() -> new IllegalStateException(
                        "Curios inventory unavailable on verification player"));
        testPlayer.fallDistance = 0.0F;
        if (PowderSnowBlock.canEntityWalkOnPowderSnow(testPlayer)) {
            throw new IllegalStateException(
                    "Powder snow was walkable without vanilla equipment or Free Action ring");
        }
        testCurios.setEquippedCurio("ring", 0,
                new ItemStack(ModItems.RING_FREE_ACTION.get()));
        if (!PowderSnowBlock.canEntityWalkOnPowderSnow(testPlayer)) {
            throw new IllegalStateException("Ring of Free Action powder snow mixin is inactive");
        }
        testCurios.setEquippedCurio("ring", 0, ItemStack.EMPTY);
        if (PowderSnowBlock.canEntityWalkOnPowderSnow(testPlayer)) {
            throw new IllegalStateException(
                    "Powder snow remained walkable after removing Ring of Free Action");
        }
        testPlayer.setHealth(testPlayer.getMaxHealth());
        testCurios.setEquippedCurio("necklace", 0,
                new ItemStack(ModItems.AMULET_SIN_PRIDE.get()));
        onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, testPlayer));
        MobEffectInstance prideSinful = testPlayer.getEffect(ModEffects.SINFUL.get());
        if (prideSinful == null || prideSinful.getAmplifier() != 0) {
            throw new IllegalStateException("Pride Pendant did not grant Sinful I");
        }
        testCurios.setEquippedCurio("necklace", 0, ItemStack.EMPTY);
        onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, testPlayer));
        if (testPlayer.hasEffect(ModEffects.SINFUL.get())) {
            throw new IllegalStateException("Pride Pendant left Sinful I after unequipping");
        }
        testCurios.setEquippedCurio("necklace", 0,
                new ItemStack(ModItems.AMULET_CROSS.get()));
        testPlayer.invulnerableTime = 20;
        onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, testPlayer));
        if (testPlayer.invulnerableTime != 20) {
            throw new IllegalStateException("Cross Necklace continuously reset invulnerability");
        }
        onDamageIFrames(new LivingDamageEvent(testPlayer,
                testPlayer.damageSources().generic(), 2.0F));
        if (testPlayer.invulnerableTime != CROSS_INVULNERABLE_TICKS) {
            throw new IllegalStateException("Cross Necklace did not extend a damage event once");
        }
        testPlayer.invulnerableTime = 19;
        onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, testPlayer));
        if (testPlayer.invulnerableTime != 19) {
            throw new IllegalStateException("Cross Necklace prevented invulnerability countdown");
        }
        testCurios.setEquippedCurio("necklace", 0, ItemStack.EMPTY);

        AttributeInstance testMaxHealth = testPlayer.getAttribute(Attributes.MAX_HEALTH);
        if (testMaxHealth == null) {
            throw new IllegalStateException("Verification player has no max health attribute");
        }
        testMaxHealth.removeModifier(BROKEN_HEART_UUID);
        testCurios.setEquippedCurio("charm", 0,
                new ItemStack(ModItems.BROKEN_HEART.get()));
        testPlayer.setHealth(5.0F);
        LivingDamageEvent brokenHeartSave = new LivingDamageEvent(testPlayer,
                testPlayer.damageSources().generic(), 10.0F);
        onLethalDamage(brokenHeartSave);
        AttributeModifier brokenHeartDrain = testMaxHealth.getModifier(BROKEN_HEART_UUID);
        if (brokenHeartDrain == null || brokenHeartDrain.getAmount() >= 0.0D
                || brokenHeartSave.getAmount() <= 0.0F
                || testPlayer.getHealth() >= testPlayer.getMaxHealth()) {
            throw new IllegalStateException(
                    "Broken Heart did not consume maximum health to prevent lethal damage");
        }
        testMaxHealth.removeModifier(BROKEN_HEART_UUID);
        testCurios.setEquippedCurio("charm", 0, ItemStack.EMPTY);
        testPlayer.setHealth(testPlayer.getMaxHealth());

        if (ModList.get().isLoaded("botania")) {
            assertRecipeUses(event, "bountifulcurios:amulet_sin_wrath",
                    "botania:rune_wrath");
            assertRecipeUses(event, "bountifulcurios:amulet_sin_gluttony",
                    "botania:rune_gluttony");
            assertRecipeUses(event, "bountifulcurios:amulet_sin_pride",
                    "botania:rune_pride");
            assertRecipeUses(event, "bountifulcurios:spectral_silt/balloon",
                    "botania:rune_air");
        }
        BountifulCurios.LOGGER.info(
                "Bountiful Curios development verification passed (effects, recipes, smithing, "
                        + "compatibility and energy capability)");
    }

    private static void assertRecipeUses(ServerStartedEvent event, String recipeId,
                                         String itemId) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(itemId));
        Recipe<?> recipe = event.getServer().getRecipeManager().byKey(
                ResourceLocation.tryParse(recipeId)).orElseThrow();
        boolean found = recipe.getIngredients().stream()
                .anyMatch(ingredient -> ingredient.test(new ItemStack(item)));
        if (!found) {
            throw new IllegalStateException(recipeId + " did not activate integration item "
                    + itemId);
        }
    }

    private static boolean chestLootContains(MinecraftServer server, ResourceLocation tableId,
                                             Item item, int attempts) {
        LootTable table = server.getLootData().getLootTable(tableId);
        LootParams params = new LootParams.Builder(server.overworld())
                .withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                .create(LootContextParamSets.CHEST);
        for (long seed = 0; seed < attempts; seed++) {
            if (table.getRandomItems(params, seed).stream().anyMatch(stack -> stack.is(item))) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player
                && CurioUtil.isAnyEquipped(player, ModItems.BALLOON.get(),
                ModItems.HORSESHOE_BALLOON.get())) {
            player.setDeltaMovement(player.getDeltaMovement().add(0.0D, 0.3D, 0.0D));
            player.fallDistance = Math.max(0.0F, player.fallDistance - 5.0F);
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (CurioUtil.isAnyEquipped(player, ModItems.LUCKY_HORSESHOE.get(),
                ModItems.HORSESHOE_BALLOON.get())) {
            event.setCanceled(true);
        } else if (CurioUtil.isEquipped(player, ModItems.BALLOON.get())) {
            event.setDistance(Math.max(0.0F, event.getDistance() - 5.0F));
        }
    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        MobEffect effect = event.getEffectInstance().getEffect();
        boolean ankhShield = CurioUtil.isEquipped(player, ModItems.SHIELD_ANKH.get())
                || CurioUtil.isHeld(player, ModItems.SHIELD_ANKH.get());
        if (CurioUtil.hasImmunity(player, effect)
                || (ankhShield && ANKH_IMMUNITIES.contains(effect))) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onFireDamage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || !event.getSource().is(DamageTypeTags.IS_FIRE)) {
            return;
        }
        Item obsidianShield = ModItems.SHIELD_OBSIDIAN.get();
        Item ankhShield = ModItems.SHIELD_ANKH.get();
        boolean protectedFromFire = CurioUtil.isAnyEquipped(player,
                ModItems.OBSIDIAN_SKULL.get(), obsidianShield, ankhShield)
                || CurioUtil.isHeld(player, obsidianShield, ankhShield);
        if (protectedFromFire) {
            event.setAmount(event.getAmount() * 0.5F);
            player.clearFire();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamageIFrames(LivingDamageEvent event) {
        if (event.getAmount() > 0.0F
                && event.getEntity() instanceof Player player
                && CurioUtil.isEquipped(player, ModItems.AMULET_CROSS.get())) {
            player.invulnerableTime = Math.max(player.invulnerableTime,
                    CROSS_INVULNERABLE_TICKS);
        }
    }

    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player
                && (CurioUtil.isAnyEquipped(player, ModItems.SHIELD_COBALT.get(),
                ModItems.SHIELD_OBSIDIAN.get(), ModItems.SHIELD_ANKH.get())
                || CurioUtil.isHeld(player, ModItems.SHIELD_COBALT.get(),
                ModItems.SHIELD_OBSIDIAN.get(), ModItems.SHIELD_ANKH.get()))) {
            event.setStrength(0.0F);
        }
    }

    @SubscribeEvent
    public static void onLethalDamage(LivingDamageEvent event) {
        if (ModList.get().isLoaded("firstaid"))
            return; //we can improve this later by calling for a constant variable that is defined once instead of calling for this method each time it happens.

        if (!(event.getEntity() instanceof Player player)
                || player.getHealth() - event.getAmount() >= 1.0F) {
            return;
        }

        if (!CurioUtil.isEquipped(player, ModItems.BROKEN_HEART.get())) {
            return;
        }

        double maxHealthLoss = 1.0D - (player.getHealth() - event.getAmount());
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null || maxHealth.getValue() <= maxHealthLoss + 1.0D) {
            return;
        }

        AttributeModifier old = maxHealth.getModifier(BROKEN_HEART_UUID);
        double oldLoss = old == null ? 0.0D : old.getAmount();
        if (old != null) {
            maxHealth.removeModifier(old);
        }
        maxHealth.addPermanentModifier(new AttributeModifier(BROKEN_HEART_UUID,
                "Broken Heart max health drain", oldLoss - maxHealthLoss,
                AttributeModifier.Operation.ADDITION));
        event.setAmount(Math.max(0.0F, event.getAmount() - (float) maxHealthLoss));
        player.setHealth(Math.max(1.0F, player.getHealth()));
        player.level().playSound(null, player.blockPosition(), SoundEvents.IRON_GOLEM_HURT,
                SoundSource.PLAYERS, 0.7F, 0.85F);
    }

    @SubscribeEvent
    public static void onWake(PlayerWakeUpEvent event) {
        AttributeInstance maxHealth = event.getEntity().getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.removeModifier(BROKEN_HEART_UUID);
        }
    }

    @SubscribeEvent
    public static void onUseStart(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player
                && CurioUtil.isEquipped(player, ModItems.AMULET_SIN_GLUTTONY.get())) {
            UseAnim animation = event.getItem().getUseAnimation();
            if ((animation == UseAnim.EAT || animation == UseAnim.DRINK)
                    && event.getDuration() > 7) {
                event.setDuration(7);
            }
        }
    }

    @SubscribeEvent
    public static void onUseTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof Player player
                && CurioUtil.isEquipped(player, ModItems.AMULET_SIN_GLUTTONY.get())) {
            UseAnim animation = event.getItem().getUseAnimation();
            if ((animation == UseAnim.EAT || animation == UseAnim.DRINK)
                    && event.getDuration() > 7) {
                event.setDuration(7);
            }
        }
    }

    @SubscribeEvent
    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player
                && CurioUtil.isEquipped(player, ModItems.AMULET_SIN_GLUTTONY.get())
                && event.getItem().getUseAnimation() == UseAnim.EAT) {
            FoodProperties food = event.getItem().getFoodProperties(player);
            int amplifier = food == null ? 0 : (int) Math.floor(
                    food.getNutrition() / 4.0D + food.getSaturationModifier() / 6.0D);
            applySinfulBuff(player, amplifier, 10 * 20, true);
        }
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        if (CurioUtil.isEquipped(event.getEntity(), ModItems.AMULET_SIN_WRATH.get())) {
            applySinfulBuff(event.getEntity(), 3, 6 * 20, true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }
        Player player = event.player;
        boolean prideActive = CurioUtil.isEquipped(player, ModItems.AMULET_SIN_PRIDE.get())
                && player.getHealth() + 0.5F > player.getMaxHealth();
        boolean prideGrantedSinful = player.getPersistentData().getBoolean(PRIDE_SINFUL_MARKER);
        MobEffectInstance sinful = player.getEffect(ModEffects.SINFUL.get());
        if (prideActive) {
            if (sinful == null || sinful.getAmplifier() == 0 && sinful.getDuration() < 40) {
                applySinfulBuff(player, 0, Integer.MAX_VALUE, false);
            }
            player.getPersistentData().putBoolean(PRIDE_SINFUL_MARKER, true);
        } else if (prideGrantedSinful) {
            if (sinful == null) {
                player.getPersistentData().remove(PRIDE_SINFUL_MARKER);
            } else if (sinful.getAmplifier() == 0) {
                player.removeEffect(ModEffects.SINFUL.get());
                player.getPersistentData().remove(PRIDE_SINFUL_MARKER);
            }
        }
    }

    private static void applySinfulBuff(Player player, int amplifier, int duration,
                                        boolean particles) {
        player.addEffect(new MobEffectInstance(ModEffects.SINFUL.get(), duration, amplifier,
                false, particles, true));
    }

    private CommonEvents() {
    }
}
