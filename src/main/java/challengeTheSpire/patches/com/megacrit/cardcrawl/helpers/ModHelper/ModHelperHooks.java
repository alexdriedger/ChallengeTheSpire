package challengeTheSpire.patches.com.megacrit.cardcrawl.helpers.ModHelper;

import challengeTheSpire.ChallengeTheSpire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.RunModStrings;

public class ModHelperHooks {

    @SpirePatch(clz = ModHelper.class, method = "initialize")
    public static class InitializeCustomMods {
        public static void Postfix() {
            for (String mod : ChallengeTheSpire.CTSmods) {
                RunModStrings modStrings = CardCrawlGame.languagePack.getRunModString(mod);
                ChallengeTheSpire.moddedMods.put(mod, new AbstractDailyMod(mod, modStrings.NAME, modStrings.DESCRIPTION, "all_star.png", true));
            }
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
