package challengeTheSpire;

import basemod.abstracts.CustomReward;
import challengeTheSpire.patches.com.megacrit.cardcrawl.screens.select.GridCardSelectScreen.GridCardSelectScreenFields;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.List;

public class LargeCardReward extends CustomReward {

    private static final Texture ICON = ImageMaster.REWARD_CARD_NORMAL;

    // Add a card to your deck
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("The Library");
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String TEXT = OPTIONS[4];

    // Cancel
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CombatRewardScreen");
    public static final String[] UI_STRINGS = uiStrings.TEXT;
    private static final String CANCEL = UI_STRINGS[6];

    private CardGroup group;
    private int numCards;

    public LargeCardReward(int numCards) {
        super(ICON, TEXT, RushCardRewardEnum.CTS_LARGE_CARD_REWARD);
        this.numCards = numCards;
        this.group = generateCardGroup(numCards);
    }

    public LargeCardReward(CardGroup group) {
        super(ICON, TEXT, RushCardRewardEnum.CTS_LARGE_CARD_REWARD);
        this.numCards = group.size();
        this.group = group;
    }

    public LargeCardReward(String challengeMode, String difficulty) {
        super(ICON, TEXT, RushCardRewardEnum.CTS_LARGE_CARD_REWARD);
        if (challengeMode.equals(ChallengeTheSpire.BOSS_RUSH_ID)) {
            switch (difficulty) {
                case ChallengeTheSpire.BRONZE_DIFFICULTY_ID:
                    this.numCards = 20;
                    break;
                case ChallengeTheSpire.SILVER_DIFFICULTY_ID:
                    this.numCards = 15;
                    break;
                case ChallengeTheSpire.GOLD_DIFFICULTY_ID:
                    this.numCards = 15;
                    break;
                case ChallengeTheSpire.PLATINUM_DIFFICULTY_ID:
                    this.numCards = 10;
                    break;
                default:
                    ChallengeTheSpire.logger.error("Unknown difficulty being played in Boss Rush");
            }
        }
        else if (challengeMode.equals(ChallengeTheSpire.ELITE_RUSH_ID)) {
            switch (difficulty) {
                case ChallengeTheSpire.BRONZE_DIFFICULTY_ID:
                    this.numCards = 15;
                    break;
                case ChallengeTheSpire.SILVER_DIFFICULTY_ID:
                    this.numCards = 10;
                    break;
                case ChallengeTheSpire.GOLD_DIFFICULTY_ID:
                    this.numCards = 10;
                    break;
                case ChallengeTheSpire.PLATINUM_DIFFICULTY_ID:
                    this.numCards = 5;
                    break;
                default:
                    ChallengeTheSpire.logger.error("Unknown difficulty being played in Elite Rush");
            }
        }
        this.group = generateCardGroup(numCards);
    }

    public int getNumCards() {
        return numCards;
    }

    @Override
    public boolean claimReward() {
        ChallengeTheSpire.logger.debug("Claiming card reward");
        AbstractDungeon.gridSelectScreen.open(group, 1, TEXT, false);
        AbstractDungeon.overlayMenu.cancelButton.show(CANCEL);
        AbstractDungeon.dynamicBanner.hide();
        GridCardSelectScreenFields.rewardItem.set(AbstractDungeon.gridSelectScreen, this);
        GridCardSelectScreenFields.forCardReward.set(AbstractDungeon.gridSelectScreen, true);

        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        return false;
    }

    private CardGroup generateCardGroup(int numCards) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (int i = 0; i < numCards; i++) {
            AbstractCard card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
            boolean containsDupe = true;
            while (containsDupe) {
                containsDupe = false;

                for (AbstractCard c : group.group) {
                    if (c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
                        break;
                    }
                }
            }

            if (!group.contains(card)) {
                if ((card.type == AbstractCard.CardType.ATTACK) && (AbstractDungeon.player.hasRelic("Molten Egg 2"))) {
                    card.upgrade();
                } else if ((card.type == AbstractCard.CardType.SKILL) && (AbstractDungeon.player.hasRelic("Toxic Egg 2"))) {
                    card.upgrade();
                } else if ((card.type == AbstractCard.CardType.POWER) && (AbstractDungeon.player.hasRelic("Frozen Egg 2"))) {
                    card.upgrade();
                }
                group.addToBottom(card);
            } else {
                i--;
            }
        }

        return group;
    }
}
