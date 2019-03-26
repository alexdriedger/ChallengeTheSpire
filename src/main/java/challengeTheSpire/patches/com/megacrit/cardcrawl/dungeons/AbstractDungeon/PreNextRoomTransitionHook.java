package challengeTheSpire.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.green.SneakyStrike;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import static basemod.BaseMod.logger;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
public class PreNextRoomTransitionHook {
    public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
        logger.debug("Transitioning room to floor #" + AbstractDungeon.floorNum);
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.SNEAKY_STRIKE_ID) &&
                // Treasure Rooms and Campfires before boss and beginning of act 4
                ((AbstractDungeon.floorNum == 9) ||
                (AbstractDungeon.floorNum == 15) ||
                (AbstractDungeon.floorNum == 26) ||
                (AbstractDungeon.floorNum == 43) ||
                (AbstractDungeon.floorNum == 49) ||
                (AbstractDungeon.floorNum == 52))) {
            SneakyStrike ss = new SneakyStrike();
            if (AbstractDungeon.player.hasRelic(MoltenEgg2.ID)) {
                ss.upgrade();
            }
            AbstractDungeon.effectList.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(ss, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
    }
}
