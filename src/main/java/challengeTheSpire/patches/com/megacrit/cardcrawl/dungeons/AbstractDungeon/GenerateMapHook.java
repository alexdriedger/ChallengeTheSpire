package challengeTheSpire.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import challengeTheSpire.ChallengeTheSpire;
import challengeTheSpire.MonsterRoomBossRush;
import challengeTheSpire.MonsterRoomEliteHunting;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;

import java.lang.reflect.Constructor;
import java.util.*;

import static basemod.BaseMod.logger;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.fadeIn;

@SpirePatch(clz=AbstractDungeon.class, method="generateMap")
public class GenerateMapHook {

    public static SpireReturn Prefix() {
        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.ELITE_RUSH_ID)) {
            AbstractDungeon.map = generateEliteHuntingMap();
            return SpireReturn.Return(null);
        } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.BOSS_RUSH_ID)) {
            AbstractDungeon.map = generateBossRushMap();
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static final int MAP_CENTER_X = 3;

    private static void addNode(ArrayList<ArrayList<MapRoomNode>> map, AbstractRoom room) {
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

        map.add(row);

    }

    private static ArrayList<ArrayList<MapRoomNode>> generateMonsterRooms(List<String> keys, Class<? extends MonsterRoom> cls) {
        ArrayList<ArrayList<MapRoomNode>> map = new ArrayList<>();
        Collections.shuffle(keys, new Random(Settings.seed));
        try {
            // Get the type of room that is being created
            Constructor<? extends MonsterRoom> con = cls.getConstructor(String.class);
            for (String k : keys) {
                AbstractRoom room = con.newInstance(k);
                addNode(map, room);
            }
        } catch (Exception e) {
            throw new RuntimeException("Tried to create a non supported room");
        }

        return map;
    }

    private static ArrayList<ArrayList<MapRoomNode>> generateEliteRooms(List<String> keys) {
        ArrayList<ArrayList<MapRoomNode>> partialMap = generateMonsterRooms(keys, MonsterRoomEliteHunting.class);

        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.GOLD_DIFFICULTY_ID)) {
            partialMap.get(0).get(MAP_CENTER_X).hasEmeraldKey = true;
            Collections.shuffle(partialMap, new Random(Settings.seed));
        }

        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.PLATINUM_DIFFICULTY_ID)) {
            for (ArrayList<MapRoomNode> row : partialMap) {
                row.get(MAP_CENTER_X).hasEmeraldKey = true;
            }
        }

        return partialMap;
    }

    private static ArrayList<ArrayList<MapRoomNode>> generateEliteHuntingMap() {
        long startTime = System.currentTimeMillis();

        ArrayList<ArrayList<MapRoomNode>> map = new ArrayList<>();

        // Act 1
        addNode(map, new ShopRoom());
        addNode(map, new RestRoom());
        map.addAll(generateEliteRooms(Arrays.asList("Gremlin Nob", "Lagavulin", "3 Sentries")));
        addNode(map, new TreasureRoomBoss());

        // Act 2
        addNode(map, new ShopRoom());
        addNode(map, new RestRoom());
        map.addAll(generateEliteRooms(Arrays.asList("Gremlin Leader", "Slavers", "Book of Stabbing")));
        addNode(map, new TreasureRoomBoss());

        // Act 3
        addNode(map, new ShopRoom());
        addNode(map, new RestRoom());
        map.addAll(generateEliteRooms(Arrays.asList("Giant Head", "Nemesis", "Reptomancer")));

        // Add act 4 elite
        map.addAll(generateEliteRooms(Arrays.asList("Shield and Spear")));
        addNode(map, new VictoryRoom(VictoryRoom.EventType.HEART));

        adjustNodes(map);
        connectNodes(map);

        logger.info("Generated the following dungeon map:");
        logger.info(MapGenerator.toString(map, Boolean.valueOf(true)));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        AbstractDungeon.firstRoomChosen = false;
        fadeIn();
        return map;
    }

    private static ArrayList<ArrayList<MapRoomNode>> generateBossRushMap() {
        long startTime = System.currentTimeMillis();

        ArrayList<ArrayList<MapRoomNode>> map = new ArrayList<>();

        // Act 1
        addNode(map, new ShopRoom());
        addNode(map, new ShopRoom());
        map.addAll(generateMonsterRooms(Arrays.asList("The Guardian", "Hexaghost", "Slime Boss"), MonsterRoomBossRush.class));
        addNode(map, new TreasureRoomBoss());

        // Act 2
        addNode(map, new ShopRoom());
        addNode(map, new RestRoom());
        map.addAll(generateMonsterRooms(Arrays.asList("Automaton", "Collector", "Champ"), MonsterRoomBossRush.class));
        addNode(map, new TreasureRoomBoss());

        // Act 3
        addNode(map, new ShopRoom());
        addNode(map, new RestRoom());
        map.addAll(generateMonsterRooms(Arrays.asList("Awakened One", "Time Eater", "Donu and Deca"), MonsterRoomBossRush.class));
        // Add act 4 elite
        addNode(map, new MonsterRoomBossRush("The Heart"));
        addNode(map, new VictoryRoom(VictoryRoom.EventType.HEART));

        adjustNodes(map);
        connectNodes(map);

        logger.info("Generated the following dungeon map:");
        logger.info(MapGenerator.toString(map, Boolean.valueOf(true)));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        AbstractDungeon.firstRoomChosen = false;
        fadeIn();
        return map;
    }

    private static void connectNodes(ArrayList<ArrayList<MapRoomNode>> map) {
        for (int i = 0; i < map.size(); i++) {
            if (i == 0) {
                // Nothing to connect to
            } else if (i == map.size() - 1) {
                connectNode(map.get(i - 1).get(MAP_CENTER_X), map.get(i).get(MAP_CENTER_X), true);
            } else {
                connectNode(map.get(i - 1).get(MAP_CENTER_X), map.get(i).get(MAP_CENTER_X), false);
            }
        }
    }

    private static void connectNode(MapRoomNode src, MapRoomNode dst, boolean isFinalNode) {
        if (isFinalNode) {
            src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, src.x, src.y, src.offsetX + .01f, src.offsetY + .01f, false));
        } else {
            src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
        }
    }

    private static void adjustNodes(ArrayList<ArrayList<MapRoomNode>> map) {
        for (int i = 0; i < map.size(); i++) {
            for (MapRoomNode room : map.get(i)) {
                room.y = i;
            }
        }
    }
}
