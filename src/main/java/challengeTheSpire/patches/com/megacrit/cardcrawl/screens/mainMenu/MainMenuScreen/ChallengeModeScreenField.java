package challengeTheSpire.patches.com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;

import challengeTheSpire.ChallengeModeScreen;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;

@SpirePatch(clz = MainMenuScreen.class, method = SpirePatch.CLASS)
public class ChallengeModeScreenField {
    public static SpireField<ChallengeModeScreen> challengeModeScreen = new SpireField<>(() -> new ChallengeModeScreen());
}
