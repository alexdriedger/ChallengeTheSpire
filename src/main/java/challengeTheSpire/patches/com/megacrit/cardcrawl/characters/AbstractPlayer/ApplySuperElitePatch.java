package challengeTheSpire.patches.com.megacrit.cardcrawl.characters.AbstractPlayer;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
public class ApplySuperElitePatch {

    @SpireInsertPatch(rloc = 52)
    public static void Insert(AbstractPlayer __instance) {
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID) || ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_ELITE_RUSH_ID)) {
            AbstractDungeon.getCurrRoom().applyEmeraldEliteBuff();
        }
    }
}
