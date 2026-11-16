package keletu.bountifulcurios.effect;

import keletu.bountifulcurios.BountifulCurios;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class SinfulEffect extends MobEffect {
    public SinfulEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x101317);
        addAttributeModifier(Attributes.ATTACK_DAMAGE,
                BountifulCurios.id("sinful/d0b248eb-3abb-4584-9cc5-2aaa06146300"), 0.25D,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        addAttributeModifier(Attributes.ARMOR,
                BountifulCurios.id("sinful/d01deb3c-0e03-4d1f-b402-a9a47db42ccd"), 3.0D,
                AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.ARMOR_TOUGHNESS,
                BountifulCurios.id("sinful/beaf841f-4962-4bb8-8952-43f4c1e0de76"), 1.0D,
                AttributeModifier.Operation.ADD_VALUE);
    }
}
