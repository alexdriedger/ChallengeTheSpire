package challengeTheSpire.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.BaseMod;
import challengeTheSpire.ChallengeTheSpire;
import challengeTheSpire.MonsterRoomBossRush;
import challengeTheSpire.MonsterRoomEliteHunting;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

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
        } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_ELITE_RUSH_ID)) {
            AbstractDungeon.map = generateModdedEliteHuntingMap();
            return SpireReturn.Return(null);
        } else if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.MODDED_BOSS_RUSH_ID)) {
            AbstractDungeon.map = generateModdedBossRushMap();
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static final int MAP_CENTER_X = 3;
    private static final int MAX_ENEMIES_PER_ACT = 3;
    private static final int MAX_ENEMIES_FINAL_ACT = 1;

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

    private static void addNodes(ArrayList<ArrayList<MapRoomNode>> map, List<AbstractRoom> rooms) {
        Map<Integer, AbstractRoom> positions = new HashMap<>();

        if (rooms.size() > 2 || rooms.size() == 0) {
            String error = "addNodes called with incorrect number of room. Number of rooms:\t" + rooms.size();
            ChallengeTheSpire.logger.error(error);
            throw new RuntimeException(error);
        } else if (rooms.size() == 1 ) {
            addNode(map, rooms.get(0));
        } else if (rooms.size() == 2) {
            positions.put(2, rooms.get(0));
            positions.put(4, rooms.get(1));
        }

        int nodeHeight = map.size();
        ArrayList<MapRoomNode> row = new ArrayList<>();

        for (int i = 0; i <=6; i++) {
            if (positions.get(i) != null) {
                MapRoomNode node = new MapRoomNode(i, nodeHeight);
                node.room = positions.get(i);
                row.add(node);
            } else {
                row.add(new MapRoomNode(i, nodeHeight));
            }
        }
        map.add(row);


    }

    private static ArrayList<ArrayList<MapRoomNode>> generateMonsterRooms(List<String> keys, Class<? extends MonsterRoom> cls, Random mapRNG) {
        ArrayList<ArrayList<MapRoomNode>> map = new ArrayList<>();
        Collections.shuffle(keys, mapRNG);
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

    private static ArrayList<ArrayList<MapRoomNode>> generateEliteRooms(List<String> keys, Random mapRNG) {
        ArrayList<ArrayList<MapRoomNode>> partialMap = generateMonsterRooms(keys, MonsterRoomEliteHunting.class, mapRNG);

        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.GOLD_DIFFICULTY_ID)) {
            partialMap.get(0).get(MAP_CENTER_X).hasEmeraldKey = true;
            Collections.shuffle(partialMap, mapRNG);
        }

        if (ChallengeTheSpire.isCustomModActive(ChallengeTheSpire.PLATINUM_DIFFICULTY_ID)) {
            for (ArrayList<MapRoomNode> row : partialMap) {
                row.get(MAP_CENTER_X).hasEmeraldKey = true;
            }
        }

        return partialMap;
    }

    private static List<String> getModdedElites(String dungeonID, int numElites, Random mapRNG) {
        List<String> moddedElites = BaseMod.getEliteEncounters(dungeonID)
                .stream()
                .map(e -> e.name)
                .collect(Collectors.toList());
        List<String> baseGameElites = new ArrayList<>(ChallengeTheSpire.eliteIDs.get(dungeonID));
        List<String> allElites = normalizeRushEnemies(moddedElites, baseGameElites, numElites, mapRNG);

        logger.info("Elites for " + dungeonID);
        allElites.forEach(logger::info);
        return allElites;
    }

    private static List<String> getModdedBosses(String dungeonID, int numBosses, Random mapRNG) {
        List<String> moddedBosses = BaseMod.getBossIDs(dungeonID);
        List<String> baseGameBosses = new ArrayList<>(ChallengeTheSpire.bossIDs.get(dungeonID));
        List<String> allBosses = normalizeRushEnemies(moddedBosses, baseGameBosses, numBosses, mapRNG);

        logger.info("Bosses for " + dungeonID);
        allBosses.forEach(logger::info);
        return allBosses;
    }

    private static List<String> normalizeRushEnemies(List<String> moddedEnemies, List<String> baseGameEnemies, int numTotalEnemies, Random mapRNG) {
        if (numTotalEnemies > MAX_ENEMIES_PER_ACT) {
            String errMsg = "Exceeded max elites per act. Max:\t" + MAX_ENEMIES_PER_ACT + "\tValue:\t" + numTotalEnemies;
            logger.debug(errMsg);
            throw new RuntimeException(errMsg);
        }

        Collections.shuffle(moddedEnemies, mapRNG);

        if (moddedEnemies.size() > numTotalEnemies) {
            // If there are more modded enemies than number needed
            return moddedEnemies.subList(0, numTotalEnemies);
        } else {
            // Fill remaining number of enemies with base game type of enemy from same act
            int remaining = numTotalEnemies - moddedEnemies.size();
            Collections.shuffle(baseGameEnemies, mapRNG);
            moddedEnemies.addAll(baseGameEnemies.subList(0, remaining));
            Collections.shuffle(moddedEnemies, mapRNG);
            return moddedEnemies;
        }
    }

    private static ArrayList<ArrayList<MapRoomNode>> generateRushMap(List<ArrayList<ArrayList<MapRoomNode>>> monsters) {
        if (monsters.size() > 4) {
            throw new RuntimeException("Incorrect Map Format");
        }

        long startTime = System.currentTimeMillis();
        ArrayList<ArrayList<MapRoomNode>> map = new ArrayList<>();

        // Act 1
        addNode(map, new ShopRoom());
        addNodes(map, Arrays.asList(new ShopRoom(), new RestRoom()));
        map.addAll(monsters.get(0));
        addNode(map, new TreasureRoomBoss());

        // Act 2
        addNodes(map, Arrays.asList(new ShopRoom(), new RestRoom()));
        addNodes(map, Arrays.asList(new ShopRoom(), new RestRoom()));
        map.addAll(monsters.get(1));
        addNode(map, new TreasureRoomBoss());

        // Act 3
        addNodes(map, Arrays.asList(new ShopRoom(), new RestRoom()));
        addNodes(map, Arrays.asList(new ShopRoom(), new RestRoom()));
        map.addAll(monsters.get(2));

        // Add act 4 elite
        map.addAll(monsters.get(3));
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

    private static ArrayList<ArrayList<MapRoomNode>> generateModdedEliteHuntingMap() {
        Random mapRNG = new Random(Settings.seed);
        List<ArrayList<ArrayList<MapRoomNode>>> monsters = new ArrayList<>();
        monsters.add(generateEliteRooms(getModdedElites(Exordium.ID, MAX_ENEMIES_PER_ACT, mapRNG), mapRNG));
        monsters.add(generateEliteRooms(getModdedElites(TheCity.ID, MAX_ENEMIES_PER_ACT, mapRNG), mapRNG));
        monsters.add(generateEliteRooms(getModdedElites(TheBeyond.ID, MAX_ENEMIES_PER_ACT, mapRNG), mapRNG));
        monsters.add(generateEliteRooms(getModdedElites(TheEnding.ID, MAX_ENEMIES_FINAL_ACT, mapRNG), mapRNG));
        return generateRushMap(monsters);
    }

    private static ArrayList<ArrayList<MapRoomNode>> generateEliteHuntingMap() {
        Random mapRNG = new Random(Settings.seed);
        List<ArrayList<ArrayList<MapRoomNode>>> monsters = new ArrayList<>();
        monsters.add(generateEliteRooms(ChallengeTheSpire.eliteIDs.get(Exordium.ID), mapRNG));
        monsters.add(generateEliteRooms(ChallengeTheSpire.eliteIDs.get(TheCity.ID), mapRNG));
        monsters.add(generateEliteRooms(ChallengeTheSpire.eliteIDs.get(TheBeyond.ID), mapRNG));
        monsters.add(generateEliteRooms(ChallengeTheSpire.eliteIDs.get(TheEnding.ID), mapRNG));

        return generateRushMap(monsters);
    }

    private static ArrayList<ArrayList<MapRoomNode>> generateModdedBossRushMap() {
        Random mapRNG = new Random(Settings.seed);
        List<ArrayList<ArrayList<MapRoomNode>>> monsters = new ArrayList<>();
        monsters.add(generateMonsterRooms(getModdedBosses(Exordium.ID, MAX_ENEMIES_PER_ACT, mapRNG), MonsterRoomBossRush.class, mapRNG));
        monsters.add(generateMonsterRooms(getModdedBosses(TheCity.ID, MAX_ENEMIES_PER_ACT, mapRNG), MonsterRoomBossRush.class, mapRNG));
        monsters.add(generateMonsterRooms(getModdedBosses(TheBeyond.ID, MAX_ENEMIES_PER_ACT, mapRNG), MonsterRoomBossRush.class, mapRNG));
        monsters.add(generateMonsterRooms(getModdedBosses(TheEnding.ID, MAX_ENEMIES_FINAL_ACT, mapRNG), MonsterRoomBossRush.class, mapRNG));

        return generateRushMap(monsters);
    }

    private static ArrayList<ArrayList<MapRoomNode>> generateBossRushMap() {
        Random mapRNG = new Random(Settings.seed);
        List<ArrayList<ArrayList<MapRoomNode>>> monsters = new ArrayList<>();
        monsters.add(generateMonsterRooms(ChallengeTheSpire.bossIDs.get(Exordium.ID), MonsterRoomBossRush.class, mapRNG));
        monsters.add(generateMonsterRooms(ChallengeTheSpire.bossIDs.get(TheCity.ID), MonsterRoomBossRush.class, mapRNG));
        monsters.add(generateMonsterRooms(ChallengeTheSpire.bossIDs.get(TheBeyond.ID), MonsterRoomBossRush.class, mapRNG));
        monsters.add(generateMonsterRooms(ChallengeTheSpire.bossIDs.get(TheEnding.ID), MonsterRoomBossRush.class, mapRNG));

        return generateRushMap(monsters);
    }

    private static void connectNodes(ArrayList<ArrayList<MapRoomNode>> map) {
        for (int i = 0; i < map.size(); i++) {
            if (i == 0) {
                // Nothing to connect to
            } else if (i == map.size() - 1) {
                connectNode(map.get(i - 1).get(MAP_CENTER_X), map.get(i).get(MAP_CENTER_X), true);
            } else {
                ArrayList<MapRoomNode> currentRow = map.get(i);
                ArrayList<MapRoomNode> prevRow = map.get(i - 1);

                for (MapRoomNode currentRowNode : currentRow) {
                    if (currentRowNode.room != null) {
                        for (MapRoomNode prevRowNode : prevRow) {
                            if (prevRowNode.room != null) {
                                connectNode(prevRowNode, currentRowNode, false);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void connectNode(MapRoomNode src, MapRoomNode dst, boolean isFinalNode) {
        if (isFinalNode) {
            src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, src.offsetX + .01f, src.y * Settings.MAP_DST_Y + src.offsetY - dst.y * Settings.MAP_DST_Y - dst.offsetY + .01f, false));
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
