package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public class EntityDamageSourceIndirect extends EntityDamageSource {
    private final Entity indirectEntity;

    public EntityDamageSourceIndirect(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
        super(damageTypeIn, source);
        this.indirectEntity = indirectEntityIn;
    }

    @Nullable
    public Entity getSourceOfDamage() {
        return this.damageSourceEntity;
    }

    @Nullable
    public Entity getEntity() {
        return this.indirectEntity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
        ITextComponent itextcomponent = this.indirectEntity == null ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
        ItemStack itemstack = this.indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase) this.indirectEntity).getHeldItemMainhand() : ItemStack.field_190927_a;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".item";
        return !itemstack.func_190926_b() && itemstack.hasDisplayName() && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName(), itextcomponent, itemstack.getTextComponent()) : new TextComponentTranslation(s, entityLivingBaseIn.getDisplayName(), itextcomponent);
    }
}
