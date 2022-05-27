package ru.neverhook.feature.world;

import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.Sys;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Spammer extends Feature {

    public ArrayList<String> playerNames = new ArrayList<>();
    int t, i;
    Setting delay;

    public Spammer() {
        super("Spammer", Category.World);
        ArrayList<String> Mode = new ArrayList<>();
        Main.instance.setmgr.addSetting(new Setting("Spammer Mode", this, "Default", Mode));
        Mode.add("Default");
        Mode.add("Absurd");
        Mode.add("HvH?");
        Mode.add("Custom");
        Main.instance.setmgr.addSetting(delay = new Setting("Message Delay", this, 100, 10, 500, 10));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) throws IOException {
        String mode = Main.instance.setmgr.getSettingByName("Spammer Mode").getValString();
        if (mode.equalsIgnoreCase("Default")) {
            try {
                String str1 = RandomStringUtils.randomAlphabetic(3);
                String str2 = RandomStringUtils.randomPrint(5);
                String str4 = RandomStringUtils.randomPrint(1);
                String str3 = "";
                double dlld = delay.getValFloat();
                this.setSuffix("" + (int) dlld);
                if (t++ % (int) dlld == 0) {
                    switch (i) {
                        case 0:
                            mc.player.sendChatMessage("![" + str1 + "] " + "Free N" + str3 + "everHook" + " better than" + " R"
                                    + str3 + "ageR10 " + "`vk.com/neverhook ` " + "[" + str2 + "]");
                            i++;
                            break;
                        case 1:
                            mc.player.sendChatMessage("![" + str1 + "] " + "Free N" + str3 + "everHook" + " better than" + " S"
                                    + str3 + "tressClient " + "`vk.com/neverhook ` " + "[" + str2 + "]");
                            i++;
                            break;
                        case 2:
                            mc.player.sendChatMessage("![" + str1 + "] " + "Free N" + str3 + "everHook" + " better than" + " S"
                                    + str3 + "igma " + "`vk.com/neverhook ` " + "[" + str2 + "]");
                            i++;
                            break;
                        case 3:
                            mc.player.sendChatMessage("![" + str1 + "] " + "Free N" + str3 + "everHook" + " better than" + " C"
                                    + str3 + "lown " + "`vk.com/neverhook ` " + "[" + str2 + "]");
                            i++;
                            break;
                        case 4:
                            mc.player.sendChatMessage("![" + str1 + "] " + "Free N" + str3 + "everHook" + " better than" + " A"
                                    + str3 + "krien " + "`vk.com/neverhook ` " + "[" + str2 + "]");
                            i = 0;
                            break;
                    }
                }
            } catch (Exception e) {
            }
        } else if (mode.equalsIgnoreCase("Absurd")) {
            String str1 = RandomStringUtils.randomAlphabetic(3);
            String str2 = RandomStringUtils.randomPrint(5);
            String str3 = "";
            double dlld = delay.getValFloat();
            this.setSuffix("" + (int) dlld);
            if (t++ % (int) dlld == 0) {
                switch (i) {
                    case 0:
                        mc.player.sendChatMessage(str3 + "!Diluted with a fair amount of empathy, rational thinking unambiguously defines each participant as capable of making their own decisions regarding the forms of influence." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 1:
                        mc.player.sendChatMessage(str3 + "!In general, of course, synthetic testing provides ample opportunities for clustering efforts." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 2:
                        mc.player.sendChatMessage(str3 + "!In our efforts to improve the user experience, we miss that basic user behavior scenarios are equally left to their own devices." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 3:
                        mc.player.sendChatMessage(str3 + "!Your actions are meaningless, your words are absurd." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 4:
                        mc.player.sendChatMessage(str3 + "!You have to work not for 12 hours, but with your head." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 5:
                        mc.player.sendChatMessage(str3 + "!Taking into account the key scenarios of behavior, the beginning of the daily work on the formation of a position ensures the relevance of experiments that are striking in their scale and grandeur." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 6:
                        mc.player.sendChatMessage(str3 + "!Break break break, we'll buy a new one." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 7:
                        mc.player.sendChatMessage(str3 + "!Oh no! It seems... I won." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 8:
                        mc.player.sendChatMessage(str3 + "!Come on! Press Alt + F4, get out of this f$cking hell." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 9:
                        mc.player.sendChatMessage(str3 + "!As part of the specification of modern standards, the key features of the project structure are verified in a timely manner." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 10:
                        mc.player.sendChatMessage(str3 + "!And again I got caught..." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 11:
                        mc.player.sendChatMessage(str3 + "!Every day is the same, how long will it last?" + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 12:
                        mc.player.sendChatMessage(str3 + "!Here is a vivid example of modern trends - a high-quality prototype of a future project for the preparation and implementation of clustering efforts." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 13:
                        mc.player.sendChatMessage(str3 + "!In their quest to improve the quality of life, they forget that the economic agenda of today allows them to complete important tasks to develop the timely implementation of the super task." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 14:
                        mc.player.sendChatMessage(str3 + "!Some features of domestic policy, overcoming the current difficult economic situation, are equally left to their own devices." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 15:
                        mc.player.sendChatMessage(str3 + "!The importance of these problems is so obvious that the solidarity of a team of professionals allows us to complete important tasks of developing deep reasoning." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 16:
                        mc.player.sendChatMessage(str3 + "!By the way, entrepreneurs on the Internet are limited solely by the way of thinking." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 17:
                        mc.player.sendChatMessage(str3 + "!The task of the organization, in particular the new model of organizational activity, is a qualitatively new stage of the corresponding conditions of activation." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 18:
                        mc.player.sendChatMessage(str3 + "!Taking into account the current international situation, diluted with a fair amount of empathy, rational thinking allows you to perform important tasks in the development of innovative methods of process management." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 19:
                        mc.player.sendChatMessage(str3 + "!By the way, the elements of the political process only add to factional differences and are made public." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 20:
                        mc.player.sendChatMessage(str3 + "!In general, of course, the strengthening and development of the internal structure ensures the relevance of the phased and consistent development of society." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 21:
                        mc.player.sendChatMessage(str3 + "!Diverse and rich experience tells us that the further development of various forms of activity provides ample opportunities for rethinking foreign economic policies." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 22:
                        mc.player.sendChatMessage(str3 + "!And also conclusions made on the basis of Internet analytics can be associatively distributed by industry." + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 23:
                        mc.player.sendChatMessage(str3 + "!Taking into account the indicators of success, the strengthening and development of the internal structure creates the preconditions for a system of mass participation." + " [" + str1 + "]" + " [" + str2 + "]");
                        i = 0;
                        break;
                }
            }
        } else if (mode.equalsIgnoreCase("HvH?")) {
            String str1 = RandomStringUtils.randomAlphabetic(3);
            String str2 = RandomStringUtils.randomPrint(5);
            String str3 = "";
            double dlld = delay.getValFloat();
            this.setSuffix("" + (int) dlld);
            if (t++ % (int) dlld == 0) {
                switch (i) {
                    case 0:
                        mc.player.sendChatMessage(str3 + "!Твой клиент зaлупa ебaная)) Кид@й мнe дyэль: \"/duel " + mc.player.getName() + "\".  Карта: Пляж " + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 1:
                        mc.player.sendChatMessage(str3 + "!Правда думаешь твой клиент лучше?) Кидaй мне дуэль: \"/duel " + mc.player.getName() + "\". Карта: Пляж " + " [" + str1 + "]" + " [" + str2 + "]");
                        i++;
                        break;
                    case 2:
                        mc.player.sendChatMessage(str3 + "!Ты как себя ведешь бл9дина eбaнaя? Кiдай мне дуэль: \"/duel " + mc.player.getName() + "\".  Карта: Пляж " + " [" + str1 + "]" + " [" + str2 + "]");
                        i = 0;
                        break;
                }
            }
        } else if (mode.equalsIgnoreCase("Custom")) {
            String str1 = RandomStringUtils.randomAlphabetic(3);
            String str2 = RandomStringUtils.randomPrint(5);
            File file = new File(mc.mcDataDir + "\\NeverHook", "spammer.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner scanner = new Scanner(file);
            double dlld = delay.getValFloat();
            while (scanner.hasNextLine()) {
                if (t++ % (int) dlld == 0) {
                    switch (i) {
                        case 0:
                            mc.player.sendChatMessage("! '" + scanner.nextLine() + "' " + str2 + str1 + "\\");
                            break;
                        case 1:
                            mc.player.sendChatMessage("! '" + scanner.nextLine() + "' " + str2 + str1);
                            break;
                    }
                }
                scanner.close();
            }
        }
    }

    @Override
    public void onEnable() {
        String mode = Main.instance.setmgr.getSettingByName("Spammer Mode").getValString();
        if (mode.equalsIgnoreCase("Custom")) {
            Sys.openURL(mc.mcDataDir + "\\NeverHook\\spammer.txt");
        }
        super.onEnable();
    }
}