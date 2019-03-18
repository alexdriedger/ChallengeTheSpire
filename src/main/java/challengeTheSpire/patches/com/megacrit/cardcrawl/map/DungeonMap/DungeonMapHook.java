package challengeTheSpire.patches.com.megacrit.cardcrawl.map.DungeonMap;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.map.DungeonMap;

public class DungeonMapHook {

    @SpirePatch(clz = DungeonMap.class, method = "renderBossIcon")
    public static class stopBossIconRender {
        public static SpireReturn Prefix() {
            if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = SpirePatch.CONSTRUCTOR)
    public static class minimizeBossHitbox {
        public static void Postfix(DungeonMap __instance) {
            if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
                __instance.bossHb = new Hitbox(0, 0);
            }
        }
    }
}
