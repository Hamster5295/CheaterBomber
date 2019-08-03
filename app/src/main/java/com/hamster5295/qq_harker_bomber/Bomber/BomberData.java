package com.hamster5295.qq_harker_bomber.Bomber;

import java.util.ArrayList;

public class BomberData {
    private static ArrayList<Bomber> bombers = new ArrayList<>();

    public static void putBomber(Bomber b) {
        bombers.add(b);
    }

    public static void setBomber(int index, Bomber b) {
        bombers.set(index, b);
    }

    public static Bomber getBomber(int index) {
        return bombers.get(index);
    }

    public static ArrayList<Bomber> getBombers() {
        return bombers;
    }

    public static void setBombers(ArrayList<Bomber> bombers) {
        BomberData.bombers = bombers;
    }
}
