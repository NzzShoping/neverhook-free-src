package ru.neverhook.utils.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import ru.neverhook.Main;
import ru.neverhook.feature.Feature;

import java.util.ArrayList;
import java.util.Random;

public interface MinecraftHelper {

    Minecraft mc = Minecraft.getMinecraft();
    Random rand = new Random();
    Gui gui = new Gui();
    ArrayList<Feature> feature = new ArrayList<>(Main.instance.featureManager.getFeatureList());

}
