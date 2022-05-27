package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

import java.util.Locale;

public class OptionsLowerCaseLanguage implements IFixableData {
    public int getFixVersion() {
        return 816;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if (compound.hasKey("lang", 8)) {
            compound.setString("lang", compound.getString("lang").toLowerCase(Locale.ROOT));
        }

        return compound;
    }
}
