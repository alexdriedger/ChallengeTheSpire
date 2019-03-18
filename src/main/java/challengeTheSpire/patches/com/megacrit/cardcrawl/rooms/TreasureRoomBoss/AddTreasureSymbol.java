package challengeTheSpire.patches.com.megacrit.cardcrawl.rooms.TreasureRoomBoss;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;

@SpirePatch(clz = TreasureRoomBoss.class, method = SpirePatch.CONSTRUCTOR)
public class AddTreasureSymbol {

    public static void Postfix(TreasureRoomBoss __instance) {
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
            __instance.setMapSymbol("T");

            // TODO : CHANGE TO INSTRUMENT PATCH TO DELETE THE LINE THAT CHANGES THIS
            CardCrawlGame.nextDungeon = "Exordium";
        }
    }
}
