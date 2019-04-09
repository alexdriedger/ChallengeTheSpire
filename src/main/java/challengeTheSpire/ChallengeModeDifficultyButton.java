package challengeTheSpire;

import challengeTheSpire.patches.com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen.ChallengeModeScreenField;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class ChallengeModeDifficultyButton {

    private Texture buttonImg;
    private String helpTitle;
    private String helpText;
    public Hitbox hb;
    public float x;
    public float y;
    public boolean selected = false;
    private String id;


    public ChallengeModeDifficultyButton(Texture buttonImg, String helpTitle, String helpText, String id) {
        this.buttonImg = buttonImg;
        this.helpTitle = helpTitle;
        this.helpText = helpText;
        this.id = id;

        this.hb = new Hitbox(80.0F * Settings.scale, 80.0F * Settings.scale);
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
        this.hb.move(x, y);
    }

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

        if ((InputHelper.justClickedLeft) && (this.hb.hovered)) {
            CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
            this.hb.clickStarted = true;
        }

        if (this.hb.clicked) {
        this.hb.clicked = false;

            if (!this.selected) {
                ChallengeModeScreen challengeModeScreen = ChallengeModeScreenField.challengeModeScreen.get(CardCrawlGame.mainMenuScreen);
                challengeModeScreen.deselectOtherOptions(this);
                this.selected = true;
            }
        }
    }

    public void render(SpriteBatch sb) {
        renderOptionButton(sb);
        if (this.hb.hovered) {
            TipHelper.renderGenericTip(InputHelper.mX + 180.0F * Settings.scale, this.hb.cY + 40.0F * Settings.scale, this.helpTitle, this.helpText);
        }
        this.hb.render(sb);
    }

    private void renderOptionButton(SpriteBatch sb) {
        if (this.selected) {
            sb.setColor(new Color(1.0F, 0.8F, 0.2F, 0.25F + (MathUtils.cosDeg((float)(System.currentTimeMillis() / 4L % 360L)) + 1.25F) / 3.5F));
            sb.draw(ImageMaster.FILTER_GLOW_BG, this.hb.cX - 64.0F, this.hb.cY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
        }

        if ((this.selected) || (this.hb.hovered)) {
            sb.setColor(Color.WHITE);
        } else {
            sb.setColor(Color.LIGHT_GRAY);
        }
        sb.draw(this.buttonImg, this.hb.cX - 64.0F, this.y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
    }

    public String getId() {
        return this.id;
    }

}
