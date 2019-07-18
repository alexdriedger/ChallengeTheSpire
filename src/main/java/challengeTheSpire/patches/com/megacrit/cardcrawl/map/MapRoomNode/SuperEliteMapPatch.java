package challengeTheSpire.patches.com.megacrit.cardcrawl.map.MapRoomNode;

import basemod.ReflectionHacks;
import challengeTheSpire.ChallengeTheSpire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.vfx.FlameAnimationEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SuperEliteMapPatch {

    @SpirePatch(clz = MapRoomNode.class, method = "update")
    public static class updatePatch {

        @SpireInsertPatch(rloc = 33)
        public static void Insert(MapRoomNode __instance) {
            if ((ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID) ||
                    ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_ELITE_RUSH_ID))
                    && __instance.hasEmeraldKey) {
                updateEmerald(__instance);
            }
        }

        private static void updateEmerald(MapRoomNode node) {
            Iterator<FlameAnimationEffect> i;
            float flameVfxTimer = (float) ReflectionHacks.getPrivate(node, MapRoomNode.class, "flameVfxTimer");
            flameVfxTimer -= Gdx.graphics.getDeltaTime();
            ReflectionHacks.setPrivate(node, MapRoomNode.class, "flameVfxTimer", flameVfxTimer);

            List<FlameAnimationEffect> fEffects = (List<FlameAnimationEffect>) ReflectionHacks.getPrivate(node, MapRoomNode.class, "fEffects");
            if (flameVfxTimer < 0.0F) {
                ReflectionHacks.setPrivate(node, MapRoomNode.class, "flameVfxTimer", MathUtils.random(0.2F, 0.4F));
                fEffects.add(new FlameAnimationEffect(node.hb));
            }

            for (i = fEffects.iterator(); i.hasNext(); ) {
                FlameAnimationEffect e = i.next();
                if (e.isDone) {
                    e.dispose();
                    i.remove();
                }
            }

            for (FlameAnimationEffect e : fEffects) {
                e.update();
            }
        }
    }

    @SpirePatch(clz = MapRoomNode.class, method = "render")
    public static class renderPatch {
        @SpireInsertPatch(rloc = 6)
        public static void Insert(MapRoomNode __instance, SpriteBatch sb) {
            if ((ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)
                || ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_ELITE_RUSH_ID))
                    && __instance.hasEmeraldKey) {
                renderEmeraldVfx(__instance, sb);
            }
        }

        private static void renderEmeraldVfx(MapRoomNode __instance, SpriteBatch sb) {
            List<FlameAnimationEffect> fEffects = (List<FlameAnimationEffect>) ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "fEffects");
            float scale = (float) ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "scale");

            for (FlameAnimationEffect e : fEffects) {
                e.render(sb, scale);
            }
        }
    }
}
