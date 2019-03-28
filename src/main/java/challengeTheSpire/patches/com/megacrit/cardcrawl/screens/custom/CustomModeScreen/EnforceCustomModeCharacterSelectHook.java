package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.custom.CustomModeScreen;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.trials.CustomTrial;

import static basemod.BaseMod.logger;

@SpirePatch(clz = CustomModeScreen.class, method = "updateEmbarkButton")
public class EnforceCustomModeCharacterSelectHook {

    @SpireInsertPatch(rloc = 37, localvars = {"trial"})
    public static void Insert(CustomModeScreen __instance, CustomTrial trial) {
        for (String s : trial.dailyModIDs()) {
            logger.debug(s);
        }
        if (trial.dailyModIDs().contains(ChallengeTheSpire.SNEAKY_STRIKE_ID)) {
            logger.debug("Setting player to Silent for Sneaky Strike Run");
            CardCrawlGame.chosenCharacter = AbstractPlayer.PlayerClass.THE_SILENT;
        }
    }
}
