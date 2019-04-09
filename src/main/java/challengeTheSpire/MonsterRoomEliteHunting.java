package challengeTheSpire;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
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

    @Override
    public void applyEmeraldEliteBuff() {
        if (AbstractDungeon.getCurrMapNode().hasEmeraldKey) {
            switch (AbstractDungeon.mapRng.random(0, 3)) {
                case 0:
                    for (AbstractMonster m : this.monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, AbstractDungeon.actNum + 1), AbstractDungeon.actNum + 1));
                    }
                    break;
                case 1:
                    for (AbstractMonster m : this.monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction(m, 0.25F, true));
                    }
                    break;
                case 2:
                    for (AbstractMonster m : this.monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MetallicizePower(m, AbstractDungeon.actNum * 2 + 2), AbstractDungeon.actNum * 2 + 2));
                    }
                    break;
                case 3:
                    for (AbstractMonster m : this.monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new com.megacrit.cardcrawl.powers.RegenerateMonsterPower(m, 1 + AbstractDungeon.actNum * 2), 1 + AbstractDungeon.actNum * 2));
                    }
                    break;
            }
        }
    }
}
