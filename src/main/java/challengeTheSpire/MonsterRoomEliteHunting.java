package challengeTheSpire;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import static basemod.BaseMod.logger;

public class MonsterRoomEliteHunting extends MonsterRoomElite {

    private final String encounterID;

    public MonsterRoomEliteHunting(String encounterID) {
        super();
        this.encounterID = encounterID;
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
}
