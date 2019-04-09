package challengeTheSpire;

import challengeTheSpire.patches.com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen.ChallengeModeScreenField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.custom.CustomModeCharacterButton;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;

public class ChallengeModeCharacterButton extends CustomModeCharacterButton {

    public ChallengeModeCharacterButton(AbstractPlayer c, boolean locked) {
        super(c, locked);
    }

    @Override
    public void update(float x, float y) {
        this.x = x;
        this.y = y;
        this.hb.move(x, y);
        updateHitbox();
    }

    private void updateHitbox() {
        this.hb.update();
        if (this.hb.justHovered) {
            CardCrawlGame.sound.playA("UI_HOVER", -0.3F);
        }
        if ((InputHelper.justClickedLeft) && (!this.locked) && (this.hb.hovered)) {
            CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
            this.hb.clickStarted = true;
        }
        if (this.hb.clicked) {
            this.hb.clicked = false;
            if (!this.selected) {
                ChallengeModeScreen challengeModeScreen = ChallengeModeScreenField.challengeModeScreen.get(CardCrawlGame.mainMenuScreen);
                challengeModeScreen.deselectOtherOptions(this);
                this.selected = true;
                CardCrawlGame.chosenCharacter = this.c.chosenClass;
            }
        }
    }
}
