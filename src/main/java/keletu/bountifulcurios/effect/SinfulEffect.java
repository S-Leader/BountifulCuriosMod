package keletu.bountifulcurios.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class SinfulEffect extends MobEffect {
    public SinfulEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x101317);
        addAttributeModifier(Attributes.ATTACK_DAMAGE,
                "d0b248eb-3abb-4584-9cc5-2aaa06146300", 0.25D,
                AttributeModifier.Operation.MULTIPLY_BASE);
        addAttributeModifier(Attributes.ARMOR,
                "d01deb3c-0e03-4d1f-b402-a9a47db42ccd", 3.0D,
                AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.ARMOR_TOUGHNESS,
                "beaf841f-4962-4bb8-8952-43f4c1e0de76", 1.0D,
                AttributeModifier.Operation.ADDITION);
    }
}
