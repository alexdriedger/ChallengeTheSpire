package challengeTheSpire.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import challengeTheSpire.ChallengeTheSpire;
import challengeTheSpire.MonsterRoomEliteHunting;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static basemod.BaseMod.logger;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.fadeIn;

@SpirePatch(clz=AbstractDungeon.class, method="generateMap")
public class GenerateMapHook {

    public static SpireReturn Prefix() {
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
            AbstractDungeon.map = generateEliteHuntingMap();
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static final int MAP_CENTER_X = 3;

    private static void addNode(ArrayList<ArrayList<MapRoomNode>> map, AbstractRoom room, boolean isFinalNode) {
        // Create node
        int nodeHeight = map.size();
        MapRoomNode node = new MapRoomNode(MAP_CENTER_X, nodeHeight);
        node.room = room;

        // Create row with empty rooms on either side of node
        ArrayList<MapRoomNode> row = new ArrayList<>();
        row.add(new MapRoomNode(0, nodeHeight));
        row.add(new MapRoomNode(1, nodeHeight));
        row.add(new MapRoomNode(2, nodeHeight));
        row.add(node);
        row.add(new MapRoomNode(4, nodeHeight));
        row.add(new MapRoomNode(5, nodeHeight));
        row.add(new MapRoomNode(6, nodeHeight));

        // Connect node with previous node if it exists
        if (nodeHeight > 0) {
            connectNode(map.get(nodeHeight - 1).get(MAP_CENTER_X), node, isFinalNode);
        }

        map.add(row);

    }

    private static void addNode(ArrayList<ArrayList<MapRoomNode>> map, AbstractRoom room) {
        addNode(map, room, false);
    }

    private static void addAllElites(ArrayList<ArrayList<MapRoomNode>> map, List<String> keys) {
        Collections.shuffle(keys);
        for (String elite : keys) {
            addNode(map, new MonsterRoomEliteHunting(elite));
        }
    }

    private static ArrayList<ArrayList<MapRoomNode>> generateEliteHuntingMap() {
        long startTime = System.currentTimeMillis();

        ArrayList<ArrayList<MapRoomNode>> map = new ArrayList<>();

        // Act 1
        addNode(map, new ShopRoom());
        addNode(map, new RestRoom());
        addAllElites(map, Arrays.asList("Gremlin Nob", "Lagavulin", "3 Sentries"));
        addNode(map, new TreasureRoomBoss());

        // Act 2
        addNode(map, new ShopRoom());
        addNode(map, new RestRoom());
        addAllElites(map, Arrays.asList("Gremlin Leader", "Slavers", "Book of Stabbing"));
        addNode(map, new TreasureRoomBoss());

        // Act 3
        addNode(map, new ShopRoom());
        addNode(map, new RestRoom());
        addAllElites(map, Arrays.asList("Giant Head", "Nemesis", "Reptomancer"));
        // Add act 4 elite
        addNode(map, new MonsterRoomEliteHunting("Shield and Spear"));
        addNode(map, new VictoryRoom(VictoryRoom.EventType.HEART), true);


        logger.info("Generated the following dungeon map:");
        logger.info(MapGenerator.toString(map, Boolean.valueOf(true)));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        AbstractDungeon.firstRoomChosen = false;
        fadeIn();
        return map;
    }

    private static void connectNode(MapRoomNode src, MapRoomNode dst, boolean isFinalNode) {
        if (isFinalNode) {
            src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, src.x, src.y, src.offsetX + .01f, src.offsetY + .01f, false));
        } else {
            src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
        }
    }
}
