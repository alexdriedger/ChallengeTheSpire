package challengeTheSpire.patches.com.megacrit.cardcrawl.map.DungeonMap;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.map.DungeonMap;

public class DungeonMapHook {

    @SpirePatch(clz = DungeonMap.class, method = "renderBossIcon")
    public static class stopBossIconRender {
        public static SpireReturn Prefix() {
            // TODO : CHECK IF ELITE RUSH
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = SpirePatch.CONSTRUCTOR)
    public static class minimizeBossHitbox {
        public static void Postfix(DungeonMap __instance) {
            __instance.bossHb = new Hitbox(0, 0);
        }
    }
}
