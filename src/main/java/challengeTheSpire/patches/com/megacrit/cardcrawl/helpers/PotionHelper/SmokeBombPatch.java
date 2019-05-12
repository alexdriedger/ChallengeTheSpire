package challengeTheSpire.patches.com.megacrit.cardcrawl.helpers.PotionHelper;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;

@SpirePatch(clz = PotionHelper.class, method = "initialize")
public class SmokeBombPatch {
    public static void Postfix(AbstractPlayer.PlayerClass chosenClass) {
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID) ||
                ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.BOSS_RUSH_ID)) {
            PotionHelper.potions.removeIf(p -> p.equals("SmokeBomb"));
        }
    }
}
