package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.CombatRewardScreen;

import challengeTheSpire.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

public class LargeCardRewardDonePatch {

    @SpirePatch(clz = CombatRewardScreen.class, method = "rewardViewUpdate")
    public static class rewardViewUpdatePatch {

        @SpireInsertPatch(rloc = 18, localvars = {"r"})
        public static void Insert(CombatRewardScreen __instance, RewardItem r) {
            if (r.type == RushCardRewardEnum.CTS_LARGE_CARD_REWARD && r.isDone) {
                r.claimReward();
                r.isDone = false;
            }
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class removeCardReward {

        @SpireInsertPatch(rloc = 33)
        public static void Insert(CombatRewardScreen __instance) {
            if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.BOSS_RUSH_ID) ||
                    ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID) ||
                    ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_ELITE_RUSH_ID) ||
                    ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_BOSS_RUSH_ID)) {
                if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomEliteHunting ||
                        AbstractDungeon.getCurrRoom() instanceof MonsterRoomBossRush) {
                    __instance.rewards.removeIf(r -> r.type == RewardItem.RewardType.CARD);
                    __instance.rewards.add(new LargeCardReward(ChallengeTheSpire.getActiveChallenge(), ChallengeTheSpire.getActiveDifficulty()));
                }
                if (AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss) {
                    __instance.rewards.removeIf(r -> r.type == RewardItem.RewardType.CARD);
                }
            }
        }
    }
}
