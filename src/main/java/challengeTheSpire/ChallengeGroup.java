package challengeTheSpire;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.custom.CustomMod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeGroup {

    private static final float CHALLENGE_START_OFFSET = 225.0F;

    private String header;
    private List<String> challengeIDs;
    private List<CustomMod> customMods;
    private float y;
    private float size;

    public ChallengeGroup(String header, List<String> challengeIDs) {
        this.header = header;
        this.challengeIDs = challengeIDs.stream().map(String::new).collect(Collectors.toList());
        this.customMods = new ArrayList<>();
        for (String c : this.challengeIDs) {
            customMods.add(new CustomMod(c, "p", true));
        }

        this.size = calculateSize();
    }

    private float calculateSize() {
        float totalHeight = 0.0F;
        for (CustomMod chal : this.customMods) {
            float height = (float) ReflectionHacks.getPrivate(chal, CustomMod.class, "height");
            totalHeight += height;
        }

        return totalHeight;
    }

    public float size() {
        return this.size;
    }

    /**
     * Updates {@link ChallengeGroup} location on the screen
     * @param y top of the {@link ChallengeGroup}
     */
    public void update(float y) {
        this.y = y;


        float offset = 0.0F;
        for (CustomMod chal : this.customMods) {
            float height = (float) ReflectionHacks.getPrivate(chal, CustomMod.class, "height");
            float yUpdate = this.y + offset - CHALLENGE_START_OFFSET * Settings.scale;
            chal.update(yUpdate);
            offset -= height;
        }
    }

    public void render(SpriteBatch sb) {
        // Render header
        FontHelper.renderSmartText(sb, FontHelper.deckBannerFont, this.header, 350.0F * Settings.scale, this.y, 9999.0F, 32.0F * Settings.scale, Settings.GOLD_COLOR);

        for (CustomMod c : this.customMods) {
            c.render(sb);
        }
    }

    public List<CustomMod> getCustomMods() {
        return customMods;
    }
}
