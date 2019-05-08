package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.CombatRewardScreen;

import challengeTheSpire.ChallengeTheSpire;
import challengeTheSpire.LargeCardReward;
import challengeTheSpire.RushCardRewardEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rewards.RewardItem;
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
                    ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
                __instance.rewards.removeIf(r -> r.type == RewardItem.RewardType.CARD);
            }
            if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.BOSS_RUSH_ID)) {
                if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.BRONZE_DIFFICULTY_ID)) {
                    __instance.rewards.add(new LargeCardReward(20));
                } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.SILVER_DIFFICULTY_ID)) {
                    __instance.rewards.add(new LargeCardReward(15));
                } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.GOLD_DIFFICULTY_ID)) {
                    __instance.rewards.add(new LargeCardReward(15));
                } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.PLATINUM_DIFFICULTY_ID)) {
                    __instance.rewards.add(new LargeCardReward(10));
                } else {
                    ChallengeTheSpire.logger.error("Unknown difficulty being played");
                }
            }
            if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
                if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.BRONZE_DIFFICULTY_ID)) {
                    __instance.rewards.add(new LargeCardReward(15));
                } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.SILVER_DIFFICULTY_ID)) {
                    __instance.rewards.add(new LargeCardReward(10));
                } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.GOLD_DIFFICULTY_ID)) {
                    __instance.rewards.add(new LargeCardReward(10));
                } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.PLATINUM_DIFFICULTY_ID)) {
                    __instance.rewards.add(new LargeCardReward(5));
                } else {
                    ChallengeTheSpire.logger.error("Unknown difficulty being played");
                }
            }
        }
    }
}
