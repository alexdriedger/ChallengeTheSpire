package challengeTheSpire.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.green.SneakyStrike;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static basemod.BaseMod.logger;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
public class PreNextRoomTransitionHook {
    public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
        logger.debug("Transitioning room to floor #" + AbstractDungeon.floorNum);
        // Treasure Rooms and Campfires before boss and beginning of act 4
        Set<Integer> singleCardFloors = new HashSet<>(Arrays.asList(9, 15, 26, 32, 43, 49, 52));
        // These floors get an extra sneaky strike on higher difficulties
        Set<Integer> extraCardFloors = new HashSet<>(Arrays.asList(15, 32, 49));

        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.SNEAKY_STRIKE_ID)) {
            if (singleCardFloors.contains(AbstractDungeon.floorNum)) {
                addSneakyStrikeToDeck();
            }
            if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.GOLD_DIFFICULTY_ID) ||
                    ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.PLATINUM_DIFFICULTY_ID)) {
                if (extraCardFloors.contains(AbstractDungeon.floorNum)) {
                    addSneakyStrikeToDeck();
                }
            }
        }
    }

    private static void addSneakyStrikeToDeck() {
        SneakyStrike ss = new SneakyStrike();
        if (AbstractDungeon.player.hasRelic(MoltenEgg2.ID)) {
            ss.upgrade();
        }
        AbstractDungeon.effectList.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(ss, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }
}
