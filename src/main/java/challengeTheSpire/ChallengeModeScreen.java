package challengeTheSpire;

import challengeTheSpire.patches.com.megacrit.cardcrawl.screens.custom.CustomModeScreen.CustomModeEmbarkHook;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.RunModStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.screens.custom.CustomModeCharacterButton;
import com.megacrit.cardcrawl.screens.mainMenu.*;
import com.megacrit.cardcrawl.trials.CustomTrial;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.*;

import static basemod.BaseMod.getModdedCharacters;

public class ChallengeModeScreen implements ScrollBarListener {

    private static final float SHOW_X = 300.0F * Settings.scale;
    private static final int MAX_CHAR_BUTTONS_PER_ROW = 10;
    private float yCharacterAdjust;
    private float screenX = SHOW_X;
    private float startY = -100.0F * Settings.scale;
    private MenuCancelButton cancelButton = new MenuCancelButton();
    public GridSelectConfirmButton confirmButton = new GridSelectConfirmButton(CharacterSelectScreen.TEXT[1]);
    public List<CustomModeCharacterButton> characters = new ArrayList<>();
    public List<ChallengeModeDifficultyButton> difficulties = new ArrayList<>();
    public List<CustomMod> challenges = new ArrayList<>();
    public  List<ChallengeGroup> challengeGroups = new ArrayList<>();
    private UIStrings uiStrings;

    // Titles & headers constants
    private static final float START_POS_Y_TITLE = 950.0F;
    private static final float START_POS_Y_CHAR_HEADER = 850.0F;
    private static final float START_POS_Y_DIFFICULTY_HEADER = 650.0F;
    private static final float START_POS_Y_CHALLENGE_GROUP_START = 350.0F;
    public static final float HEADER_PADDING_BOTTOM = 100.0F;

    // Character button constants
    private static final float CHARACTER_BUTTON_WIDTH = 100.0F;
    private static final float CHARACTER_BUTTON_PADDING = 130.0F;
    private static final float CHARACTER_BUTTON_ROW_HEIGHT = 100.0F;
    private static final float START_POS_Y_CHARACTER_BUTTON_GROUP = START_POS_Y_CHAR_HEADER - HEADER_PADDING_BOTTOM;

    // Difficulty button constants
    private static final float DIFFICULTY_BUTTON_WIDTH = 100.0F;
    private static final float DIFFICULTY_BUTTON_PADDING = 130.0F;
    private static final float START_POS_Y_DIFFICULTY_BUTTON_GROUP = START_POS_Y_DIFFICULTY_HEADER - HEADER_PADDING_BOTTOM;

    private ScrollBar scrollBar;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private boolean grabbedScreen = false;
    private float grabStartY = 0.0F; private float targetY = 0.0F; private float scrollY = 0.0F;

    public ChallengeModeScreen() {
        this.uiStrings = CardCrawlGame.languagePack.getUIString(ChallengeTheSpire.CHALLENGE_MENU_SCREEN_ID);
        initializeCharacters();
        initializeDifficulties();
        initializeChallenges();

        this.scrollBar = new ScrollBar(this, Settings.WIDTH - 340.0F * Settings.scale - ScrollBar.TRACK_W / 2.0F, Settings.HEIGHT / 2.0F, Settings.HEIGHT - 256.0F * Settings.scale);
        calculateScrollBounds();
    }

    private void updateScrolling() {
        int y = InputHelper.mY;

        if (this.scrollUpperBound > 0.0F) {
            if (!this.grabbedScreen) {
                if (InputHelper.scrolledDown) {
                    this.targetY += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    this.targetY -= Settings.SCROLL_SPEED;
                }

                if (InputHelper.justClickedLeft) {
                    this.grabbedScreen = true;
                    this.grabStartY = (y - this.targetY);
                }
            } else if (InputHelper.isMouseDown) {
                this.targetY = (y - this.grabStartY);
            } else {
                this.grabbedScreen = false;
            }
        }

        this.scrollY = MathHelper.scrollSnapLerpSpeed(this.scrollY, this.targetY);

        if (this.targetY < this.scrollLowerBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollLowerBound);
        } else if (this.targetY > this.scrollUpperBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollUpperBound);
        }
        updateBarPosition();
    }

    private void calculateScrollBounds() {
        float upperBound = 0.0F;
        for (ChallengeGroup cg : this.challengeGroups) {
            upperBound += cg.size();
            upperBound += HEADER_PADDING_BOTTOM;
        }
        this.scrollUpperBound = upperBound + yCharacterAdjust + 270.0F * Settings.scale;
        this.scrollLowerBound = (100.0F * Settings.scale);
    }

    @Override
    public void scrolledUsingBar(float newPercent) {
        float newPosition = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.scrollY = newPosition;
        this.targetY = newPosition;
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    public void open() {
        CardCrawlGame.mainMenuScreen.screen = MenuEnumPatch.CHALLENGE;
        CardCrawlGame.mainMenuScreen.darken();
        cancelButton.show(CharacterSelectScreen.TEXT[5]);
    }

    public void update() {
        boolean isDraggingScrollBar = this.scrollBar.update();
        if (!isDraggingScrollBar) {
            updateScrolling();
        }
        updateCancelButton();
        updateCharacterButtons();
        updateDifficultyButtons();
        updateChallenges();
        updateEmbarkButton();
    }

    private void updateCancelButton() {
        this.cancelButton.update();
        if ((this.cancelButton.hb.clicked) || (InputHelper.pressedEscape)) {
            InputHelper.pressedEscape = false;
            this.cancelButton.hb.clicked = false;
            this.cancelButton.hide();
            CardCrawlGame.mainMenuScreen.panelScreen.refresh();
        }
    }

    private void updateCharacterButtons() {
        int row = 0;
        for (int i = 0; i < this.characters.size(); i++) {
            if (i % MAX_CHAR_BUTTONS_PER_ROW == 0 && i != 0) {
                row++;
            }
            float x = this.screenX
                    + (i % MAX_CHAR_BUTTONS_PER_ROW) * CHARACTER_BUTTON_WIDTH * Settings.scale
                    + CHARACTER_BUTTON_PADDING * Settings.scale;
            float y = this.startY
                    + START_POS_Y_CHARACTER_BUTTON_GROUP * Settings.scale
                    - row * CHARACTER_BUTTON_ROW_HEIGHT * Settings.scale
                    + scrollY;
            this.characters.get(i).update(x, y);
        }
    }

    private void updateDifficultyButtons() {
        for (int i = 0; i < this.difficulties.size(); i++) {
            float x = this.screenX
                    + i * DIFFICULTY_BUTTON_WIDTH * Settings.scale
                    + DIFFICULTY_BUTTON_PADDING * Settings.scale;
            float y = this.startY
                    + START_POS_Y_DIFFICULTY_BUTTON_GROUP * Settings.scale
                    - yCharacterAdjust
                    + scrollY;
            this.difficulties.get(i).update(x, y);
        }
    }

    private void updateChallenges() {
        // Show embark button if challenge is selected. Otherwise, hide embark button
        boolean isChallengeSelected = false;
        for (CustomMod c : this.challenges) {
            if (c.selected) {
                isChallengeSelected = true;
                break;
            }
        }
        if (isChallengeSelected) {
            this.confirmButton.show();
            this.confirmButton.isDisabled = false;
        } else {
            this.confirmButton.hide();
            this.confirmButton.isDisabled = true;
        }

        // Update challenge groups
        float yOffset = 0.0F;
        for (ChallengeGroup cg : this.challengeGroups) {
            float y = START_POS_Y_CHALLENGE_GROUP_START * Settings.scale - yOffset + scrollY - yCharacterAdjust;
            cg.update(y);
            yOffset += cg.size() + HEADER_PADDING_BOTTOM * Settings.scale;
        }
    }

    private void updateEmbarkButton() {
        this.confirmButton.update();
        if ((this.confirmButton.hb.clicked) || (CInputActionSet.proceed.isJustPressed())) {
            this.confirmButton.hb.clicked = false;
            for (CustomModeCharacterButton ch : this.characters) {
                if (ch.selected) {
                    CardCrawlGame.chosenCharacter = ch.c.chosenClass;
                }
            }

            CardCrawlGame.mainMenuScreen.isFadingOut = true;
            CardCrawlGame.mainMenuScreen.fadeOutMusic();
            Settings.isTrial = true;
            Settings.isDailyRun = false;
            Settings.isEndless = false;

            CustomTrial trial = new CustomTrial();
            String selectedDiff = "";
            for (ChallengeModeDifficultyButton diff : difficulties) {
                if (diff.selected) {
                    trial.addDailyMod(diff.getId());
                    selectedDiff = diff.getId();
                }
            }

            // Set ascension
            switch (selectedDiff) {
                case ChallengeTheSpire.BRONZE_DIFFICULTY_ID:
                    AbstractDungeon.ascensionLevel = 0;
                    break;
                case ChallengeTheSpire.SILVER_DIFFICULTY_ID:
                    AbstractDungeon.isAscensionMode = true;
                    AbstractDungeon.ascensionLevel = 10;
                    break;
                case ChallengeTheSpire.GOLD_DIFFICULTY_ID:
                    AbstractDungeon.isAscensionMode = true;
                    AbstractDungeon.ascensionLevel = 15;
                    break;
                case ChallengeTheSpire.PLATINUM_DIFFICULTY_ID:
                    AbstractDungeon.isAscensionMode = true;
                    AbstractDungeon.ascensionLevel = 20;
                    break;
                default:
                    String errorMsg = "Challenge the Spire difficulty does not exist";
                    ChallengeTheSpire.logger.error(errorMsg);
                    throw new RuntimeException(errorMsg);
            }

            // Set seeds
            long sourceTime = System.nanoTime();
            Random rng = new Random(Long.valueOf(sourceTime));
            Settings.seed = Long.valueOf(SeedHelper.generateUnoffensiveSeed(rng));
            AbstractDungeon.generateSeeds();

            for (CustomMod chal : challenges) {
                if (chal.selected) {
                    trial.addDailyMod(chal.ID);
                }
            }

            CustomModeEmbarkHook.addCertainFuture(trial);
            CustomModeEmbarkHook.resolveSneakyStrike(trial);

            CardCrawlGame.trial = trial;
            AbstractPlayer.customMods = CardCrawlGame.trial.dailyModIDs();

        }
    }

    public void render(SpriteBatch sb) {
        this.scrollBar.render(sb);
        renderTitle(sb, uiStrings.TEXT[0], startY + START_POS_Y_TITLE * Settings.scale + this.scrollY);
        renderHeader(sb, uiStrings.TEXT[1], startY + START_POS_Y_CHAR_HEADER * Settings.scale + this.scrollY);
        renderHeader(sb, uiStrings.TEXT[2], startY + START_POS_Y_DIFFICULTY_HEADER * Settings.scale - yCharacterAdjust + this.scrollY);
//        renderHeader(sb, uiStrings.TEXT[3], startY + START_POS_Y_CHALLENGE_GROUP_START * Settings.scale - yCharacterAdjust + this.scrollY);
        this.cancelButton.render(sb);
        this.confirmButton.render(sb);

        for (CustomModeCharacterButton o : this.characters) {
            o.render(sb);
        }

        for (ChallengeModeDifficultyButton b : this.difficulties) {
            b.render(sb);
        }

        for (ChallengeGroup cg : this.challengeGroups) {
            cg.render(sb);
        }
    }

    private void renderTitle(SpriteBatch sb, String text, float y) {
        FontHelper.renderSmartText(sb, FontHelper.charTitleFont, text, this.screenX, y, 9999.0F, 32.0F * Settings.scale, Settings.GOLD_COLOR);
    }

    private void renderHeader(SpriteBatch sb, String text, float y) {
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, text, this.screenX + 50.0F * Settings.scale, y, 9999.0F, 32.0F * Settings.scale, Settings.GOLD_COLOR);
    }

    private void initializeCharacters() {
        this.characters.clear();
        this.characters.add(new ChallengeModeCharacterButton(CardCrawlGame.characterManager.setChosenCharacter(AbstractPlayer.PlayerClass.IRONCLAD), false));
        this.characters.add(new ChallengeModeCharacterButton(CardCrawlGame.characterManager.setChosenCharacter(AbstractPlayer.PlayerClass.THE_SILENT), UnlockTracker.isCharacterLocked("The Silent")));
        this.characters.add(new ChallengeModeCharacterButton(CardCrawlGame.characterManager.setChosenCharacter(AbstractPlayer.PlayerClass.DEFECT), UnlockTracker.isCharacterLocked("Defect")));
        for (AbstractPlayer character : getModdedCharacters()) {
            characters.add(new ChallengeModeCharacterButton(CardCrawlGame.characterManager.setChosenCharacter(character.chosenClass), false));
        }

        int count = this.characters.size();
        for (int i = 0; i < count; i++) {
            this.characters.get(i).move(this.screenX + i * 100.0F * Settings.scale - 200.0F * Settings.scale, this.startY - 80.0F * Settings.scale);
        }

        this.yCharacterAdjust = Math.floorDiv(this.characters.size() - 1, MAX_CHAR_BUTTONS_PER_ROW) * 100.0F * Settings.scale;

        this.characters.get(0).hb.clicked = true;
    }

    private void initializeDifficulties() {
        this.difficulties.clear();
        RunModStrings rms = CardCrawlGame.languagePack.getRunModString(ChallengeTheSpire.BRONZE_DIFFICULTY_ID);
        this.difficulties.add(new ChallengeModeDifficultyButton(ImageMaster.loadImage(ChallengeTheSpire.getImagePath("bronzeSquare.png")), rms.NAME, rms.DESCRIPTION, ChallengeTheSpire.BRONZE_DIFFICULTY_ID));
        rms = CardCrawlGame.languagePack.getRunModString(ChallengeTheSpire.SILVER_DIFFICULTY_ID);
        this.difficulties.add(new ChallengeModeDifficultyButton(ImageMaster.loadImage(ChallengeTheSpire.getImagePath("silverSquare.png")), rms.NAME, rms.DESCRIPTION, ChallengeTheSpire.SILVER_DIFFICULTY_ID));
        rms = CardCrawlGame.languagePack.getRunModString(ChallengeTheSpire.GOLD_DIFFICULTY_ID);
        this.difficulties.add(new ChallengeModeDifficultyButton(ImageMaster.loadImage(ChallengeTheSpire.getImagePath("goldSquare.png")), rms.NAME, rms.DESCRIPTION, ChallengeTheSpire.GOLD_DIFFICULTY_ID));
        rms = CardCrawlGame.languagePack.getRunModString(ChallengeTheSpire.PLATINUM_DIFFICULTY_ID);
        this.difficulties.add(new ChallengeModeDifficultyButton(ImageMaster.loadImage(ChallengeTheSpire.getImagePath("platinumSquare.png")), rms.NAME, rms.DESCRIPTION, ChallengeTheSpire.PLATINUM_DIFFICULTY_ID));
        this.difficulties.get(0).hb.clicked = true;
    }

    private void initializeChallenges() {
        challengeGroups.clear();

        // Add Challenge the Spire Challenges
        ChallengeGroup cts_Challenges = new ChallengeGroup(uiStrings.TEXT[3], Arrays.asList(ChallengeTheSpire.ELITE_RUSH_ID, ChallengeTheSpire.MODDED_ELITE_RUSH_ID, ChallengeTheSpire.BOSS_RUSH_ID, ChallengeTheSpire.MODDED_BOSS_RUSH_ID, ChallengeTheSpire.SNEAKY_STRIKE_ID));
        challengeGroups.add(cts_Challenges);

        // Add challenges from other mods
        for (String modName : ChallengeTheSpire.OtherModChallengemods.keySet()) {
            challengeGroups.add(new ChallengeGroup(modName, ChallengeTheSpire.OtherModChallengemods.get(modName)));
        }

        // Set mutual exclusion so only one challenge can be selected at a time
        for (ChallengeGroup cg : challengeGroups) {
            this.challenges.addAll(cg.getCustomMods());
        }
        for (CustomMod c : this.challenges) {
            List<CustomMod> rem = new ArrayList<>(this.challenges);
            rem.remove(c);
            for (CustomMod r : rem) {
                c.setMutualExclusionPair(r);
            }
        }
    }

    public void deselectOtherOptions(CustomModeCharacterButton characterOption) {
        for (CustomModeCharacterButton o : this.characters) {
            if (o != characterOption) {
                o.selected = false;
            }
        }
    }

    public void deselectOtherOptions(ChallengeModeDifficultyButton op) {
        for (ChallengeModeDifficultyButton o : this.difficulties) {
            if (o != op) {
                o.selected = false;
            }
        }
    }
}
