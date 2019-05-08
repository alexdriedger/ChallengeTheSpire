package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

@SpirePatch(clz = GridCardSelectScreen.class, method = SpirePatch.CLASS)
public class GridCardSelectScreenFields {
    public static SpireField<Boolean> forCardReward = new SpireField<>(() -> false);
    public static SpireField<RewardItem> rewardItem = new SpireField<>(() -> null);
}
