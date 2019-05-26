package challengeTheSpire;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.*;
import challengeTheSpire.util.IDCheckDontTouchPls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.green.SneakyStrike;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.RunModStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@SpireInitializer
public class ChallengeTheSpire implements
        AddCustomModeModsSubscriber,
        EditStringsSubscriber,
        PostCreateStartingRelicsSubscriber,
        PostCreateStartingDeckSubscriber,
        PostDungeonInitializeSubscriber,
        PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(ChallengeTheSpire.class.getName());
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Challenge The Spire";
    private static final String AUTHOR = "alexdriedger"; // And pretty soon - You!
    private static final String DESCRIPTION = "Challenges for Slay The Spire";

    public static final String ELITE_RUSH_ID = "challengethespire:Elite Rush";
    public static final int ELITE_RUSH_STARTING_GOLD = 1000;
    public static final int ELITE_RUSH_STARTING_GOLD_REDUCED = 750;

    public static final String BOSS_RUSH_ID = "challengethespire:Boss Rush";
    public static final int BOSS_RUSH_STARTING_GOLD = 1000;
    public static final int BOSS_RUSH_STARTING_GOLD_REDUCED = 750;

    public static final String SNEAKY_STRIKE_ID = "challengethespire:Sneaky Strike";

    public static final String BRONZE_DIFFICULTY_ID = "challengethespire:Bronze Difficulty";
    public static final String SILVER_DIFFICULTY_ID = "challengethespire:Silver Difficulty";
    public static final String GOLD_DIFFICULTY_ID = "challengethespire:Gold Difficulty";
    public static final String PLATINUM_DIFFICULTY_ID = "challengethespire:Platinum Difficulty";

    public static final String CHALLENGE_MENU_PANEL_ID = "challengethespire:Challenge Panel";
    public static final String CHALLENGE_MENU_SCREEN_ID = "challengethespire:Challenge Screen";

    public static List<String> CTSChallengemods;
    public static List<String> CTSDifficultymods;
    public static Map<String, List<String>> OtherModChallengemods;
    public static Map<String, AbstractDailyMod> moddedMods;

    private List<String> rushRelicsToRemove;
    private List<String> eliteRushRelicsToRemove;
    private List<String> bossRushRelicsToRemove;
    private List<String> replayRushRelicsToRemove;
    private List<String> hubrisRushRelicsToRemove;
    private List<String> conspireRushRelicsToRemove;
    private List<String> aspirationRushRelicsToRemove;
    private List<String> halationRushRelicsToRemove;
    private List<String> jediRushRelicsToRemove;
    private List<String> vexRushRelicsToRemove;

    public ChallengeTheSpire() {
        logger.info("Subscribe to BaseMod hooks");
        initializeCustomMods();
        initializeRelicsToRemove();

        BaseMod.subscribe(this);
        setModID("challengethespire");
    }

    private static void initializeCustomMods() {
        CTSChallengemods = new ArrayList<>(Arrays.asList(ELITE_RUSH_ID, BOSS_RUSH_ID, SNEAKY_STRIKE_ID));
        CTSDifficultymods = new ArrayList<>(Arrays.asList(BRONZE_DIFFICULTY_ID, SILVER_DIFFICULTY_ID, GOLD_DIFFICULTY_ID, PLATINUM_DIFFICULTY_ID));
        OtherModChallengemods = new HashMap<>();
        moddedMods = new HashMap<>();
    }

    private void initializeRelicsToRemove() {
        rushRelicsToRemove = new ArrayList<>(Arrays.asList(
                // No ? Nodes
                JuzuBracelet.ID,
                TinyChest.ID,

                // Single Path
                WingBoots.ID,

                // No Normal Chests
                Matryoshka.ID,

                // No Normal Enemies
                PrayerWheel.ID,

                // Custom Card Rewards
                PrismaticShard.ID,
                BustedCrown.ID,
                SingingBowl.ID,
                QuestionCard.ID,
                DreamCatcher.ID,
                TinyHouse.ID
        ));

        eliteRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                // No Bosses
                Pantograph.ID
        ));

        bossRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                // Necessary to remove relic from its default relic pool
                // It is added to Boss relic pool in `MakePantographBossRelic` patch
                Pantograph.ID,

                // No Elites
                BlackStar.ID,
                Sling.ID,
                PreservedInsect.ID
        ));

        replayRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                "Golden Egg",
                "Honey Jar",
                "Ring of Chaos",
                "ReplayTheSpireMod:ShopPack",
                "Replay:Mystery Machine"
        ));

        hubrisRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                "hubris:TenFootPole",
                "hubris:OldNail",
                "hubris:DisguiseKit",
                "hubris:TinFlute",
                "hubris:64BitClover",
                "hubris:Teleporter",
                "hubris:TerracottaHorce"
        ));

        conspireRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                "conspire:Flyswatter",
                "conspire:TreasureMap"
        ));

        aspirationRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                "aspiration:SeaSaltIceCream",
                "aspiration:PoetsPen",
                "aspiration:PoetsPen_weak"
        ));

        halationRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                "halation:LostSnail",
                "halation:Komachan",
                "halation:Jellyphish",
                "halation:LettersToHer",
                "halation:BlackBeret",
                "halation:QuantumPhysicsTextbook",
                "halation:SmartPhone",
                "halation:Convergence",
                "halation:Diary",
                "halation:KoshaPiece",
                "halation:SelfBoilingWater"
        ));

        jediRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                "jedi:arcanewood",
                "jedi:shrinkray"
        ));

        vexRushRelicsToRemove = new ArrayList<>(Arrays.asList(
                "vexMod:BetterTron",
                "vexMod:HeadHunter",
                "vexMod:PlagueVial",
                "vexMod:RedPlottingStone",
                "vexMod:CursedCompass",
                "vexMod:MallPass",
                "vexMod:TreasureMap",
                "vexMod:BrokenBowl",
                "vexMod:ChompingNoodles",
                "vexMod:GildedClover"
        ));
    }

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = ChallengeTheSpire.class.getResourceAsStream("/IDCheckStrings.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.debug("setModID is:\t" + ID);
        logger.debug("EXCEPTION_STRINGS.DEFAULTID is:\t" + EXCEPTION_STRINGS.DEFAULTID);

        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        }
        logger.info("Success! ID is " + modID);
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = ChallengeTheSpire.class.getResourceAsStream("/IDCheckStrings.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT

        String packageName = ChallengeTheSpire.class.getPackage().getName(); // STILL NOT EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO

    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Challenge The Spire Mod =========================");
        ChallengeTheSpire mod = new ChallengeTheSpire();
        logger.info("========================= /Challenge The Spire Mod Initialized. Hello World./ =========================");
    }

    @Override
    public void receiveCustomModeMods(List<CustomMod> list) {
        CustomMod eliteRush = new CustomMod(ELITE_RUSH_ID, "p", true);
        CustomMod bossRush = new CustomMod(BOSS_RUSH_ID, "p", true);
        CustomMod sneakyStrike = new CustomMod(SNEAKY_STRIKE_ID, "p", true);
        list.add(eliteRush);
        list.add(bossRush);
        list.add(sneakyStrike);
    }

    @Override
    public void receiveEditStrings() {
        String path = getResourceDir() + "localization/";
        String language;
        if (Settings.language.toString().equals("ZHS")) {
            language = "zhs";
        } else {
            // Load English by default
            language = "eng";
        }
        path += language;
        loadStringbyLanguage(path);

        loadExternalChallenges(language);
    }

    private static void processExternalChallengeFile(String path, String modName) {
        logger.info("Loading Challenge file:\t" + path);
        BaseMod.loadCustomStringsFile(RunModStrings.class, path);

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, RunModStrings>>(){}.getType();
        try {
            // Get challenge ids from file
            Map<String, RunModStrings> challenges = gson.fromJson(loadJson(path), type);

            // Store challenge ids for creating challenges in ChallengeModeScreen
            List<String> challengesIDs = new ArrayList<>(challenges.keySet());
            OtherModChallengemods.put(modName, challengesIDs);

            logger.info("Loaded challenge file successfully:\t" + path);
        } catch (JsonParseException e) {
            logger.error("Could not load challenges file (incorrect challenge file format):\t" + path);
            e.printStackTrace();
        }
    }

    /**
     * Get mods running
     * @return Map of Mod Name to URL for the mod jar
     */
    private static Map<String, URL> getRunningModsPathMTS() {
        Map<String, URL> map = new HashMap<>();
        ModInfo[] modInfos = Loader.MODINFOS;
        for (ModInfo m : modInfos) {
            map.put(m.Name, m.jarURL);
        }

        return map;
    }

    private static void loadExternalChallenges(String language) {

        Map<String, URL> mods = getRunningModsPathMTS();

        for (String modName : mods.keySet()) {
            try {
                URL jarURL = mods.get(modName);
                JarFile jarFile = new JarFile(Paths.get(jarURL.toURI()).toFile());
                jarFile.stream()
                        // Map jar entry to file path
                        .map(ZipEntry::getName)
                        .filter(filePath -> filePath.contains("localization") && filePath.contains(language) && filePath.endsWith("Challenges.json"))
                        .forEach(path -> ChallengeTheSpire.processExternalChallengeFile(path, modName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String loadJson(String jsonPath) {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }

    private static void loadStringbyLanguage(String path) {
        BaseMod.loadCustomStringsFile(RunModStrings.class,
                path + "/ChallengeTheSpire-CustomMod-Strings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class,
                path + "/ChallengeTheSpire-MenuPanel-Strings.json");
    }

    @Override
    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> relics) {
        if (isCustomModActive(ELITE_RUSH_ID)) {
            relics.add(BlackStar.ID);
            relics.add(Sling.ID);
            relics.add(PreservedInsect.ID);

            AbstractDungeon.relicsToRemoveOnStart.addAll(eliteRushRelicsToRemove);
        }

        if (isCustomModActive(ChallengeTheSpire.BOSS_RUSH_ID)) {
            relics.add(Courier.ID);
            relics.add(MoltenEgg2.ID);
            relics.add(ToxicEgg2.ID);
            relics.add(FrozenEgg2.ID);

            AbstractDungeon.relicsToRemoveOnStart.addAll(bossRushRelicsToRemove);
        }

        if (isCustomModActive(ELITE_RUSH_ID) || isCustomModActive(BOSS_RUSH_ID)) {
            AbstractDungeon.relicsToRemoveOnStart.addAll(rushRelicsToRemove);

            removeModRelicsIfLoaded("ReplayTheSpireMod", replayRushRelicsToRemove);
            removeModRelicsIfLoaded("hubris", hubrisRushRelicsToRemove);
            removeModRelicsIfLoaded("conspire", conspireRushRelicsToRemove);
            removeModRelicsIfLoaded("aspiration", aspirationRushRelicsToRemove);
            removeModRelicsIfLoaded("Halation", halationRushRelicsToRemove);
            removeModRelicsIfLoaded("jedi", jediRushRelicsToRemove);
            removeModRelicsIfLoaded("vexMod", vexRushRelicsToRemove);
        }
    }

    private void removeModRelicsIfLoaded(String ID, List<String> relicsToRemove) {
        if (Loader.isModLoaded(ID)) {
            logger.info(ID + " detected! Removing " + relicsToRemove.size() + " useless relics");
            AbstractDungeon.relicsToRemoveOnStart.addAll(relicsToRemove);
        }
    }

    @Override
    public void receivePostDungeonInitialize() {
        if (isCustomModActive(ELITE_RUSH_ID)) {
            if (isCustomModActive(GOLD_DIFFICULTY_ID) || isCustomModActive(PLATINUM_DIFFICULTY_ID)) {
                AbstractDungeon.player.gold = ELITE_RUSH_STARTING_GOLD_REDUCED;
                AbstractDungeon.player.displayGold = ELITE_RUSH_STARTING_GOLD_REDUCED;
            } else {
                AbstractDungeon.player.gold = ELITE_RUSH_STARTING_GOLD;
                AbstractDungeon.player.displayGold = ELITE_RUSH_STARTING_GOLD;
            }
        }

        if (isCustomModActive(BOSS_RUSH_ID)) {
            if (isCustomModActive(GOLD_DIFFICULTY_ID) || isCustomModActive(PLATINUM_DIFFICULTY_ID)) {
                AbstractDungeon.player.gold = BOSS_RUSH_STARTING_GOLD_REDUCED;
                AbstractDungeon.player.displayGold = BOSS_RUSH_STARTING_GOLD_REDUCED;
            } else {
                AbstractDungeon.player.gold = BOSS_RUSH_STARTING_GOLD;
                AbstractDungeon.player.displayGold = BOSS_RUSH_STARTING_GOLD;
            }
        }
    }

    @Override
    public void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass playerClass, CardGroup cardGroup) {
        if (isCustomModActive(SNEAKY_STRIKE_ID)) {
            AbstractDungeon.effectList.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(new SneakyStrike(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.registerCustomReward(
                RushCardRewardEnum.CTS_LARGE_CARD_REWARD,
                (rewardSave) -> {
                    return new LargeCardReward(rewardSave.amount);
                },
                (customReward) -> {
                    return new RewardSave(customReward.type.toString(), null, ((LargeCardReward)customReward).getNumCards(), 0);
                }
        );

        BaseMod.registerModBadge(ImageMaster.loadImage(getImagePath("modBadge.png")), "Challenge The Spire", "alexdriedger", "Challenge the Spire rocks!", new ModPanel());
    }

    public static boolean isCustomModActive(String ID) {
        return (CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(ID)) || ModHelper.isModEnabled(ID);
    }

    public static String getActiveChallenge() {
        for (String mod : CTSChallengemods) {
            if (isCustomModActive(mod)) {
                logger.debug("active challenge:\t" + mod);
                return mod;
            }
        }
        logger.error("No active challenge");
        return null;
    }

    public static String getActiveDifficulty() {
        for (String mod : CTSDifficultymods) {
            if (isCustomModActive(mod)) {
                logger.debug("active difficulty:\t" + mod);
                return mod;
            }
        }
        logger.error("No active difficulty");
        return null;
    }

    public static String getImagePath(String path) {
        return getResourceDir() + "images/" + path;
    }

    public static String getResourceDir() {
        return "challengeTheSpireResources/";
    }
}