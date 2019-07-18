package challengeTheSpire.patches.com.megacrit.cardcrawl.relics.Orrery;

import challengeTheSpire.ChallengeTheSpire;
import challengeTheSpire.LargeCardReward;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Orrery;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = Orrery.class, method = "onEquip")
public class RushCardRewardPatch {

    @SpireInsertPatch(rloc = 4)
    public static void Insert(Orrery __instance) {
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.BOSS_RUSH_ID) ||
                ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID) ||
                ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_ELITE_RUSH_ID) ||
                ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_BOSS_RUSH_ID)) {
            AbstractDungeon.getCurrRoom().rewards.clear();
            for (int i = 1; i <= 5; i++) {
                AbstractDungeon.getCurrRoom().rewards.add(new LargeCardReward(ChallengeTheSpire.getActiveChallenge(), ChallengeTheSpire.getActiveDifficulty()));
            }
        }
    }
}
