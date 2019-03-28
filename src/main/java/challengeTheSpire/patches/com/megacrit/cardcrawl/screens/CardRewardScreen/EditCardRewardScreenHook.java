package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.CardRewardScreen;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.SneakyStrike;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;
import java.util.Random;

@SpirePatch(clz = CardRewardScreen.class, method = "open")
public class EditCardRewardScreenHook {
    public static void Prefix(CardRewardScreen __instance, ArrayList<AbstractCard> cards, RewardItem rItem, String header) {
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.SNEAKY_STRIKE_ID)) {
            SneakyStrike ss = new SneakyStrike();
            if (AbstractDungeon.player.hasRelic(MoltenEgg2.ID)) {
                ss.upgrade();
            }
            cards.set(new Random().nextInt(cards.size()), ss);
        }
    }
}
