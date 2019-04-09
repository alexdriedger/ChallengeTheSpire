package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;

import basemod.ReflectionHacks;
import challengeTheSpire.ChallengeTheSpire;
import challengeTheSpire.MenuEnumPatch;
import challengeTheSpire.patches.com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen.ChallengeModeScreenField;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;

public class MenuPanelButtonHook {

    @SpirePatch(clz = MainMenuPanelButton.class, method = "setLabel")
    public static class setLabelHook {
        public static SpireReturn Prefix(MainMenuPanelButton __instance) {
            MainMenuPanelButton.PanelClickResult result = (MainMenuPanelButton.PanelClickResult) ReflectionHacks.getPrivate(__instance, MainMenuPanelButton.class, "result");
            if (result.equals(MenuEnumPatch.PLAY_CHALLENGE)) {
                Texture portraitImg = ImageMaster.loadImage("challengeTheSpireResources/images/challengePortrait.png");
                UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ChallengeTheSpire.CHALLENGE_MENU_PANEL_ID);
                ReflectionHacks.setPrivate(__instance, MainMenuPanelButton.class, "header", uiStrings.TEXT[0]);
                ReflectionHacks.setPrivate(__instance, MainMenuPanelButton.class, "description", uiStrings.TEXT[1]);
                ReflectionHacks.setPrivate(__instance, MainMenuPanelButton.class, "portraitImg", portraitImg);
                ReflectionHacks.setPrivate(__instance, MainMenuPanelButton.class, "panelImg", ImageMaster.MENU_PANEL_BG_RED);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MainMenuPanelButton.class, method = "buttonEffect")
    public static class setButtonEffectHook {
        public static SpireReturn Prefix(MainMenuPanelButton __instance) {
            MainMenuPanelButton.PanelClickResult result = (MainMenuPanelButton.PanelClickResult) ReflectionHacks.getPrivate(__instance, MainMenuPanelButton.class, "result");
            if (result.equals(MenuEnumPatch.PLAY_CHALLENGE)) {
                ChallengeModeScreenField.challengeModeScreen.get(CardCrawlGame.mainMenuScreen).open();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}