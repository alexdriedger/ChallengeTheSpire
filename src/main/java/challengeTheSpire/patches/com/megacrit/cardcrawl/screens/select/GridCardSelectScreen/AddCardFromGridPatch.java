package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

public class AddCardFromGridPatch {

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class updatePatch {

        @SpireInsertPatch(rloc = 61)
        public static void Insert(GridCardSelectScreen __instance) {
            if (GridCardSelectScreenFields.forCardReward.get(__instance)) {
                AbstractCard c = __instance.selectedCards.get(0);
                c.stopGlowing();
                AbstractDungeon.effectList.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                __instance.selectedCards.clear();

                AbstractDungeon.combatRewardScreen.rewards.remove(GridCardSelectScreenFields.rewardItem.get(__instance));
                AbstractDungeon.combatRewardScreen.positionRewards();
                if (AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
                    AbstractDungeon.combatRewardScreen.hasTakenAll = true;
                    AbstractDungeon.overlayMenu.proceedButton.show();
                }

                GridCardSelectScreenFields.rewardItem.set(AbstractDungeon.gridSelectScreen, null);
                GridCardSelectScreenFields.forCardReward.set(AbstractDungeon.gridSelectScreen, false);
            }
        }
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "reopen")
    public static class reopenPatch {
        public static void Postfix(GridCardSelectScreen __instance) {
            if (GridCardSelectScreenFields.forCardReward.get(__instance)) {
                AbstractDungeon.overlayMenu.proceedButton.hide();
            }
        }
    }
}
