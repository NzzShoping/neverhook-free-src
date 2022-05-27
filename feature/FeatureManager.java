package ru.neverhook.feature;

import net.minecraft.client.Minecraft;
import ru.neverhook.feature.combat.*;
import ru.neverhook.feature.hud.*;
import ru.neverhook.feature.movement.*;
import ru.neverhook.feature.other.*;
import ru.neverhook.feature.player.*;
import ru.neverhook.feature.visual.*;
import ru.neverhook.feature.world.*;
import ru.neverhook.utils.other.MinecraftHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureManager implements MinecraftHelper {

    public ArrayList<Feature> featureList = new ArrayList<>();

    public FeatureManager() {

        //Other
        featureList.add(new MiddleClickPearl());
        featureList.add(new SeeBarriers());
        featureList.add(new ModuleSoundAlert());
        featureList.add(new ViewModel());

        //Combat
        featureList.add(new HitBox());
        featureList.add(new Reach());
        featureList.add(new TargetStrafe());
        featureList.add(new Criticals());
        featureList.add(new AutoGapple());
        featureList.add(new Velocity());
        featureList.add(new KillAura());
        featureList.add(new AutoArmor());
        featureList.add(new ChestStealer());
        featureList.add(new AntiBot());
        featureList.add(new FastBow());
        featureList.add(new AutoTotem());
        featureList.add(new TriggerBot());
        featureList.add(new Clip());
        featureList.add(new Timer());
        featureList.add(new AntiAim());
        featureList.add(new AutoPot());
        featureList.add(new KeepSprint());

        //Movement
        featureList.add(new AutoSprint());
        featureList.add(new Speed());
        featureList.add(new Jesus());
        featureList.add(new Fly());
        featureList.add(new NoSlowDown());
        featureList.add(new XCarry());
        featureList.add(new ElytraFly());
        featureList.add(new WallClimb());
        featureList.add(new OldPositionTeleport());
        featureList.add(new WaterSpeed());

        //Player
        featureList.add(new InventoryWalk());
        featureList.add(new FastPlace());
        featureList.add(new NoClip());
        featureList.add(new NoFall());
        featureList.add(new FastWorldLoad());
        featureList.add(new AutoAuth());
        featureList.add(new NoDamageTeam());
        featureList.add(new AntiTrapka());
        featureList.add(new AntiAFK());
        featureList.add(new FastUse());
        featureList.add(new AutoTool());
        featureList.add(new NoEntityHit());

        //World
        featureList.add(new FlagDetector());
        featureList.add(new SafeWalk());
        featureList.add(new NoWeb());
        featureList.add(new Step());
        featureList.add(new ESP());
        featureList.add(new MCF());
        featureList.add(new NoInteract());
        featureList.add(new ChatAppend());
        featureList.add(new XrayBypass());
        featureList.add(new AutoFish());
        featureList.add(new DeathCoords());
        featureList.add(new StreamerMode());
        featureList.add(new DiscordRPC());

        //Visual
        featureList.add(new NoRender());
        featureList.add(new NameTags());
        featureList.add(new Ambience());
        featureList.add(new ScoreBoard());
        featureList.add(new CameraClip());
        featureList.add(new HurtCam());
        featureList.add(new FullBright());
        featureList.add(new Tracers());
        featureList.add(new EnchantEffect());
        featureList.add(new NoJumpDelay());
        featureList.add(new Chams());
        featureList.add(new Spammer());
        featureList.add(new NoBreakDelay());
        featureList.add(new FreeCam());
        featureList.add(new AntiPush());
        featureList.add(new NoServerRotation());
        featureList.add(new Animations());
        featureList.add(new BlockESP());
        featureList.add(new ItemPhysics());
        featureList.add(new ChatFeatures());
        featureList.add(new ArmorHUD());
        featureList.add(new AntiLagMachine());

        //Hud
        featureList.add(new ArreyList());
        featureList.add(new ClickGUI());
        featureList.add(new HUD());
        featureList.add(new FakeHack());
        featureList.add(new Notifications());
        featureList.add(new ClientFont());

        featureList.sort(Comparator.comparingInt(feature -> -Minecraft.getMinecraft().robotoRegular.getStringWidth(feature.getName())));
    }

    public ArrayList<Feature> getFeatureList() {
        return featureList;
    }

    public List<Feature> getFeaturesInCategory(Category category) {
        List<Feature> featureList = new ArrayList<>();
        for (Feature feature : getFeatureList()) {
            if (feature.getCategory() == category) {
                featureList.add(feature);
            }
        }
        return featureList;
    }

    public Feature getFeaturesByName(String name) {
        for (Feature feature : getFeatureList()) {
            if (feature.getName().equalsIgnoreCase(name))
                return feature;
        }
        return null;
    }

    public Feature getFeatureByClass(Class<? extends Feature> classModule) {
        for (Feature feature : getFeatureList()) {
            if (feature.getClass() == classModule) {
                return feature;
            }
        }
        return null;
    }

    public List<Feature> getStatesFeatures() {
        return getFeatureList().stream().filter(Feature::getState).collect(Collectors.toCollection(ArrayList::new));
    }
}
