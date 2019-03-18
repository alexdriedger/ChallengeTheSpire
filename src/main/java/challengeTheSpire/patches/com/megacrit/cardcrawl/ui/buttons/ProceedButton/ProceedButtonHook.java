package challengeTheSpire.patches.com.megacrit.cardcrawl.ui.buttons.ProceedButton;

import challengeTheSpire.ChallengeTheSpire;
import challengeTheSpire.MonsterRoomEliteHunting;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static basemod.BaseMod.logger;

public class ProceedButtonHook {

    @SpirePatch(clz = ProceedButton.class, method = "update")
    public static class goToVictoryHook {
        public static final Logger logger = LogManager.getLogger(ProceedButton.class.getName());

        @SpireInsertPatch(rloc = 25)
        public static void Insert(ProceedButton __instance) {
            if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
                logger.debug("Proceed Button clicked at y:" + AbstractDungeon.getCurrMapNode().y);
                AbstractRoom currentRoom = AbstractDungeon.getCurrRoom();
                if (currentRoom instanceof MonsterRoomEliteHunting &&
                        AbstractDungeon.getCurrMapNode().y == AbstractDungeon.map.size() - 2) {
                    logger.debug("Going to Victory Room");
                    goToVictoryRoomOrTheDoor(__instance);
                }
            }
        }

        private static void goToVictoryRoomOrTheDoor(ProceedButton pb) {
            CardCrawlGame.music.fadeOutBGM();
            CardCrawlGame.music.fadeOutTempBGM();
            MapRoomNode node = new MapRoomNode(-1, 15);
            node.room = new VictoryRoom(VictoryRoom.EventType.HEART);
            AbstractDungeon.nextRoom = node;
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.nextRoomTransitionStart();
            pb.hide();
        }
    }

    @SpirePatch(clz = ProceedButton.class, method = "update")
    public static class goToNextRoom {

        @SpireInsertPatch(rloc = 111)
        public static SpireReturn Insert(ProceedButton __instance) {
            if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
                AbstractDungeon.dungeonMapScreen.open(false);
                __instance.hide();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

    }

}
