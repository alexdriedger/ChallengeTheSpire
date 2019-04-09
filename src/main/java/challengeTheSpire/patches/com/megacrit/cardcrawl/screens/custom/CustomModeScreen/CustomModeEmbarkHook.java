package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.custom.CustomModeScreen;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.CertainFuture;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.trials.CustomTrial;

import static basemod.BaseMod.logger;

@SpirePatch(clz = CustomModeScreen.class, method = "updateEmbarkButton")
public class CustomModeEmbarkHook {

    @SpireInsertPatch(rloc = 37, localvars = {"trial"})
    public static void Insert(CustomModeScreen __instance, CustomTrial trial) {
        for (String s : trial.dailyModIDs()) {
            logger.debug(s);
        }

        addCertainFuture(trial);
        resolveSneakyStrike(trial);
    }

    // Sets certain future mod on boss rush & elite rush to fix crash from interaction
    // with Replay The Spire
    public static void addCertainFuture(CustomTrial trial) {
        if ((!trial.dailyModIDs().contains(CertainFuture.ID)) &&
                (trial.dailyModIDs().contains(ChallengeTheSpire.BOSS_RUSH_ID) ||
                        trial.dailyModIDs().contains(ChallengeTheSpire.ELITE_RUSH_ID))) {
            logger.debug("Setting Certain Future mod to avoid crash with Replay The Spire");
            trial.addDailyMod(CertainFuture.ID);
        }
    }

    public static void resolveSneakyStrike(CustomTrial trial) {
        if (trial.dailyModIDs().contains(ChallengeTheSpire.SNEAKY_STRIKE_ID)) {
            logger.debug("Setting player to Silent for Sneaky Strike Run");
            CardCrawlGame.chosenCharacter = AbstractPlayer.PlayerClass.THE_SILENT;
        }
    }
}
