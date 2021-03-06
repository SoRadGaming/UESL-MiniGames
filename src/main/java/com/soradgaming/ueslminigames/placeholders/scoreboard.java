package com.soradgaming.ueslminigames.placeholders;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.managment.gameManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class scoreboard {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    public static final HashMap<UUID, Integer> playerList = new HashMap<>();

    public static @NotNull LinkedHashMap<UUID, Integer> grabData() {
        ArrayList<UUID> uuidList = gameManager.getPlayerList("All");
        for (UUID uuid : uuidList) {
            int points = plugin.data.getInt(uuid + ".points");
            playerList.put(uuid, points);
        }
        return sortHashMapByValues(playerList);
    }

    public static @NotNull LinkedHashMap<UUID, Integer> sortHashMapByValues(@NotNull HashMap<UUID, Integer> passedMap) {
        List<UUID> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<UUID, Integer> sortedMap = new LinkedHashMap<>();

        for (Integer val : mapValues) {
            Iterator<UUID> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                UUID key = keyIt.next();
                Integer comp1 = passedMap.get(key);

                if (comp1.equals(val)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
