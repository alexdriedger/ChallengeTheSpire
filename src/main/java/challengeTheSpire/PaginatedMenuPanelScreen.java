package challengeTheSpire;

import basemod.IUIElement;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;
import com.megacrit.cardcrawl.screens.mainMenu.MenuPanelScreen;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;

public class PaginatedMenuPanelScreen extends MenuPanelScreen {

    private static final float PANEL_Y = Settings.HEIGHT / 2.0F;
    private ArrowOptionsButton leftArrow;
    private ArrowOptionsButton rightArrow;
    private int index;

    public PaginatedMenuPanelScreen() {
        this.index = 0;
        this.rightArrow = new ArrowOptionsButton(
                "challengeTheSpireResources/images/smallRightArrow.png",
                (int)(Settings.WIDTH / 2.0F + 700.0F * Settings.scale),
                (int)(Settings.HEIGHT / 2.0F - 50.0F * Settings.scale),
                () -> {
                    this.index = 1;
                    overridePanels();
                });
        this.leftArrow = new ArrowOptionsButton(
                "challengeTheSpireResources/images/smallLeftArrow.png",
                (int)(Settings.WIDTH / 2.0F - 775.0F * Settings.scale),
                (int)(Settings.HEIGHT / 2.0F - 50.0F * Settings.scale),
                () -> {
                    this.index = 0;
                    overridePanels();
                });

    }

    @Override
    public void open(PanelScreen screenType) {
        super.open(screenType);
        if (screenType.equals(PanelScreen.PLAY)) {
            overridePanels();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        MenuPanelScreen.PanelScreen screen = (MenuPanelScreen.PanelScreen) ReflectionHacks.getPrivate(this, MenuPanelScreen.class, "screen");
        if (screen.equals(PanelScreen.PLAY)) {
            if (index == 1) {
                leftArrow.render(sb);
            } else if (index == 0) {
                rightArrow.render(sb);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        MenuPanelScreen.PanelScreen screen = (MenuPanelScreen.PanelScreen) ReflectionHacks.getPrivate(this, MenuPanelScreen.class, "screen");
        if (screen.equals(PanelScreen.PLAY)) {
            if (index == 1) {
                leftArrow.update();
            } else if (index == 0) {
                rightArrow.update();
            }
        }
    }

    private void overridePanels() {
        this.panels.clear();
        if (this.index == 0) {
            this.panels.add(new MainMenuPanelButton(MainMenuPanelButton.PanelClickResult.PLAY_NORMAL, MainMenuPanelButton.PanelColor.BLUE, Settings.WIDTH / 2.0F - 450.0F * Settings.scale, PANEL_Y));
            this.panels.add(new MainMenuPanelButton(MenuEnumPatch.PLAY_CHALLENGE, MainMenuPanelButton.PanelColor.RED, Settings.WIDTH / 2.0F, PANEL_Y));
            if (CardCrawlGame.mainMenuScreen.statsScreen.statScreenUnlocked()) {
                this.panels.add(new MainMenuPanelButton(MainMenuPanelButton.PanelClickResult.PLAY_DAILY, MainMenuPanelButton.PanelColor.BEIGE, Settings.WIDTH / 2.0F + 450.0F * Settings.scale, PANEL_Y));
            } else {
                this.panels.add(new MainMenuPanelButton(MainMenuPanelButton.PanelClickResult.PLAY_DAILY, MainMenuPanelButton.PanelColor.GRAY, Settings.WIDTH / 2.0F + 450.0F * Settings.scale, PANEL_Y));
            }
        } else if (this.index == 1) {
            if (StatsScreen.all.highestDaily > 0) {
                this.panels.add(new MainMenuPanelButton(MainMenuPanelButton.PanelClickResult.PLAY_CUSTOM, MainMenuPanelButton.PanelColor.RED, Settings.WIDTH / 2.0F, PANEL_Y));
            } else {
                this.panels.add(new MainMenuPanelButton(MainMenuPanelButton.PanelClickResult.PLAY_CUSTOM, MainMenuPanelButton.PanelColor.GRAY, Settings.WIDTH / 2.0F + 450.0F, PANEL_Y));
            }
        }
    }
}
