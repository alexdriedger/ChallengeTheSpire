package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;

import challengeTheSpire.ChallengeModeScreen;
import challengeTheSpire.MenuEnumPatch;
import challengeTheSpire.PaginatedMenuPanelScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;

public class MainMenuScreenHooks {

    @SpirePatch(clz = MainMenuScreen.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {boolean.class})
    public static class DefaultConstructorPatch {
        @SpireInsertPatch(rloc = 1)
        public static void Insert(MainMenuScreen __instance, boolean bool) {
            __instance.panelScreen = new PaginatedMenuPanelScreen();
        }
    }

    @SpirePatch(clz = MainMenuScreen.class, method = "update")
    public static class Update {
        public static void Postfix(MainMenuScreen __instance) {
            if (__instance.screen.equals(MenuEnumPatch.CHALLENGE)) {
                ChallengeModeScreenField.challengeModeScreen.get(CardCrawlGame.mainMenuScreen).update();
            }
        }
    }

    @SpirePatch(clz = MainMenuScreen.class, method = "render")
    public static class Render {
        public static void Postfix(MainMenuScreen __instance, SpriteBatch sb) {
            if (__instance.screen.equals(MenuEnumPatch.CHALLENGE)) {
                ChallengeModeScreenField.challengeModeScreen.get(CardCrawlGame.mainMenuScreen).render(sb);
            }
        }
    }

}
