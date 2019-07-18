package challengeTheSpire.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Pantograph;

import java.util.Collections;

@SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
public class MakePantographBossRelic {

    @SpireInsertPatch(rloc = 91)
    public static void Insert(AbstractDungeon __instance) {
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.BOSS_RUSH_ID) || ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_BOSS_RUSH_ID)) {
            // Pantograph should have already been removed from the uncommon relic pool
            AbstractDungeon.bossRelicPool.add(Pantograph.ID);
            Collections.shuffle(AbstractDungeon.bossRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
        }
    }

}
