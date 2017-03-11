package com.asb.elections;

import com.google.common.collect.ImmutableMap;
import fj.data.IOFunctions;

import java.util.Map;

public class Main {

    private static final Map<String, Integer> STATE_SEATS_MAP =
            ImmutableMap.<String, Integer>builder()
                    .put("S05", 40)
                    .put("S14", 60)
                    .put("S19", 117)
                    .put("S24", 403)
                    .put("S28", 70)
                    .build();

    public static void main(String[] args) {
        Scraper scraper = new Scraper();
        IOFunctions.runSafe(scraper.f(STATE_SEATS_MAP));
    }
}
