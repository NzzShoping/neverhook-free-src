package net.minecraft.client.gui.toasts;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public interface IToast {
    ResourceLocation field_193654_a = new ResourceLocation("textures/gui/toasts.png");
    Object field_193655_b = new Object();

    IToast.Visibility func_193653_a(GuiToast p_193653_1_, long p_193653_2_);

    default Object func_193652_b() {
        return field_193655_b;
    }

    enum Visibility {
        SHOW(SoundEvents.field_194226_id),
        HIDE(SoundEvents.field_194227_ie);

        private final SoundEvent field_194170_c;

        Visibility(SoundEvent p_i47607_3_) {
            this.field_194170_c = p_i47607_3_;
        }

        public void func_194169_a(SoundHandler p_194169_1_) {
            p_194169_1_.playSound(PositionedSoundRecord.func_194007_a(this.field_194170_c, 1.0F, 1.0F));
        }
    }
}
