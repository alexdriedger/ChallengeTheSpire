package challengeTheSpire;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

import static basemod.BaseMod.logger;

public class MonsterRoomBossRush extends MonsterRoom {

    private final String encounterID;

    public MonsterRoomBossRush(String encounterID) {
        super();
        this.encounterID = encounterID;
//        this.mapSymbol = "E";
    }

    @Override
    public void onPlayerEntry() {
        playBGM(null);

        logger.debug("MONSTER: " + encounterID);
        this.setMonster(MonsterHelper.getEncounter(encounterID));
        AbstractDungeon.lastCombatMetricKey = encounterID;
        this.monsters.init();

        waitTimer = 0.1F;
    }

    @Override
    public void dropReward() {
        super.dropReward();
        for (RewardItem r : this.rewards) {
            if (r.type == RewardItem.RewardType.GOLD) {
                r.incrementGold(100);
            }
        }
        addRelicToRewards(returnRandomRelicTier());
    }

    private AbstractRelic.RelicTier returnRandomRelicTier() {
        int roll = AbstractDungeon.relicRng.random(0, 99);

        if (roll < 50) {
            return AbstractRelic.RelicTier.COMMON;
        } else if (roll > 82) {
            return AbstractRelic.RelicTier.RARE;
        } else {
            return AbstractRelic.RelicTier.UNCOMMON;
        }
    }
}
