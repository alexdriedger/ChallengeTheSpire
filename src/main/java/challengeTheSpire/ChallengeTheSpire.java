package challengeTheSpire;

import basemod.BaseMod;
import basemod.interfaces.*;
import challengeTheSpire.util.IDCheckDontTouchPls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.green.SneakyStrike;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.RunModStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class ChallengeTheSpire implements
        AddCustomModeModsSubscriber,
        EditStringsSubscriber,
        PostCreateStartingRelicsSubscriber,
        PostCreateStartingDeckSubscriber,
        PostDungeonInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(ChallengeTheSpire.class.getName());
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Challenge The Spire";
    private static final String AUTHOR = "alexdriedger"; // And pretty soon - You!
    private static final String DESCRIPTION = "Challenges for Slay The Spire";

    public static final String ELITE_RUSH_ID = "CTS - Elite Rush";
    public static final int ELITE_RUSH_STARTING_GOLD = 1000;
    public static final int ELITE_RUSH_STARTING_GOLD_REDUCED = 750;

    public static final String BOSS_RUSH_ID = "CTS - Boss Rush";
    public static final int BOSS_RUSH_STARTING_GOLD = 1000;
    public static final int BOSS_RUSH_STARTING_GOLD_REDUCED = 750;

    public static final String SNEAKY_STRIKE_ID = "CTS - Sneaky Strike";

    public static final String BRONZE_DIFFICULTY_ID = "CTS - Bronze Difficulty";
    public static final String SILVER_DIFFICULTY_ID = "CTS - Silver Difficulty";
    public static final String GOLD_DIFFICULTY_ID = "CTS - Gold Difficulty";
    public static final String PLATINUM_DIFFICULTY_ID = "CTS - Platinum Difficulty";

    public static final String CHALLENGE_MENU_PANEL_ID = "CTS - Challenge Panel";
    public static final String CHALLENGE_MENU_SCREEN_ID = "CTS - Challenge Screen";

    public static List<String> CTSmods;
    public static Map<String, AbstractDailyMod> moddedMods;

    public ChallengeTheSpire() {
        logger.info("Subscribe to BaseMod hooks");
        initializeCustomMods();

        BaseMod.subscribe(this);
        setModID("challengeTheSpire");
    }

    private static void initializeCustomMods() {
        CTSmods = new ArrayList<>(Arrays.asList(ELITE_RUSH_ID, BOSS_RUSH_ID, SNEAKY_STRIKE_ID, BRONZE_DIFFICULTY_ID, SILVER_DIFFICULTY_ID, GOLD_DIFFICULTY_ID, PLATINUM_DIFFICULTY_ID));
        moddedMods = new HashMap<>();
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
        BaseMod.loadCustomStringsFile(RunModStrings.class,
                getModID() + "Resources/localization/eng/ChallengeTheSpire-CustomMod-Strings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/ChallengeTheSpire-MenuPanel-Strings.json");
    }

    @Override
    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> relics) {
        if (isCustomModActive(ELITE_RUSH_ID)) {
            relics.add(BlackStar.ID);
            relics.add(Sling.ID);
            relics.add(PreservedInsect.ID);

            // No ? Nodes
            AbstractDungeon.relicsToRemoveOnStart.add(JuzuBracelet.ID);
            AbstractDungeon.relicsToRemoveOnStart.add(TinyChest.ID);

            // Single Path
            AbstractDungeon.relicsToRemoveOnStart.add(WingBoots.ID);

            // No Bosses
            AbstractDungeon.relicsToRemoveOnStart.add(Pantograph.ID);

            // No Normal Chests
            AbstractDungeon.relicsToRemoveOnStart.add(Matryoshka.ID);

            // No Normal Enemies
            AbstractDungeon.relicsToRemoveOnStart.add(PrayerWheel.ID);
        }

        if (isCustomModActive(ChallengeTheSpire.BOSS_RUSH_ID)) {
            relics.add(Courier.ID);
            relics.add(MoltenEgg2.ID);
            relics.add(ToxicEgg2.ID);
            relics.add(FrozenEgg2.ID);

            // No ? Nodes
            AbstractDungeon.relicsToRemoveOnStart.add(JuzuBracelet.ID);
            AbstractDungeon.relicsToRemoveOnStart.add(TinyChest.ID);

            // Single Path
            AbstractDungeon.relicsToRemoveOnStart.add(WingBoots.ID);

            // No Normal Chests
            AbstractDungeon.relicsToRemoveOnStart.add(Matryoshka.ID);

            // No Normal Enemies
            AbstractDungeon.relicsToRemoveOnStart.add(PrayerWheel.ID);

            // No Elites
            AbstractDungeon.relicsToRemoveOnStart.add(BlackStar.ID);
            AbstractDungeon.relicsToRemoveOnStart.add(Sling.ID);
            AbstractDungeon.relicsToRemoveOnStart.add(PreservedInsect.ID);
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

    public static boolean isCustomModActive(String ID) {
        return (CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(ID)) || ModHelper.isModEnabled(ID);
    }

    public static String getImagePath(String path) {
        return getModID() + "Resources/images/" + path;
    }
}