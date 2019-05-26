package challengeTheSpire.patches.com.megacrit.cardcrawl.helpers.ModHelper;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.RunModStrings;

import java.util.Collection;
import java.util.List;

public class ModHelperHooks {

    @SpirePatch(clz = ModHelper.class, method = "initialize")
    public static class InitializeCustomMods {
        public static void Postfix() {
            for (String mod : ChallengeTheSpire.CTSChallengemods) {
                initializeMod(mod);
            }
            for (String mod : ChallengeTheSpire.CTSDifficultymods) {
                initializeMod(mod);
            }
            for (List<String> challenges : ChallengeTheSpire.OtherModChallengemods.values()) {
                for (String chal : challenges) {
                    initializeMod(chal);
                }
            }
        }

        private static void initializeMod(String mod) {
            RunModStrings modStrings = CardCrawlGame.languagePack.getRunModString(mod);
            ChallengeTheSpire.moddedMods.put(mod, new AbstractDailyMod(mod, modStrings.NAME, modStrings.DESCRIPTION, "all_star.png", true));
        }
    }

    @SpirePatch(clz = ModHelper.class, method = "getMod")
    public static class GetMod {
        public static AbstractDailyMod Postfix(AbstractDailyMod __result, String key) {
            if (__result == null) {
                return ChallengeTheSpire.moddedMods.get(key);
            }
            return __result;
        }
    }

}
