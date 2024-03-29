package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.JsonUtils;

import javax.annotation.Nullable;

public class NBTPredicate {
    public static final NBTPredicate field_193479_a = new NBTPredicate(null);
    @Nullable
    private final NBTTagCompound field_193480_b;

    public NBTPredicate(@Nullable NBTTagCompound p_i47536_1_) {
        this.field_193480_b = p_i47536_1_;
    }

    public boolean func_193478_a(ItemStack p_193478_1_) {
        return this == field_193479_a || this.func_193477_a(p_193478_1_.getTagCompound());
    }

    public boolean func_193475_a(Entity p_193475_1_) {
        return this == field_193479_a || this.func_193477_a(CommandBase.entityToNBT(p_193475_1_));
    }

    public boolean func_193477_a(@Nullable NBTBase p_193477_1_) {
        if (p_193477_1_ == null) {
            return this == field_193479_a;
        } else {
            return this.field_193480_b == null || NBTUtil.areNBTEquals(this.field_193480_b, p_193477_1_, true);
        }
    }

    public static NBTPredicate func_193476_a(@Nullable JsonElement p_193476_0_) {
        if (p_193476_0_ != null && !p_193476_0_.isJsonNull()) {
            NBTTagCompound nbttagcompound;

            try {
                nbttagcompound = JsonToNBT.getTagFromJson(JsonUtils.getString(p_193476_0_, "nbt"));
            } catch (NBTException nbtexception) {
                throw new JsonSyntaxException("Invalid nbt tag: " + nbtexception.getMessage());
            }

            return new NBTPredicate(nbttagcompound);
        } else {
            return field_193479_a;
        }
    }
}
