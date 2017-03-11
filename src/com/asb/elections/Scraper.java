package com.asb.elections;

import fj.F;
import fj.data.IO;
import fj.data.IOFunctions;
import fj.data.IOW;
import fj.data.Stream;

import java.nio.file.Path;
import java.util.Map;

/**
 * Scrapes the election data.
 * Created by arjun on 11/03/17.
 */
public class Scraper implements F<Map<String, Integer>, IO<Path>> {
    private static final String SITE =
            "http://eciresults.nic.in/Constituencywise";
    private static final String HTM_AC = ".htm?ac=";

    private WebPageFetcher webPageFetcher = new WebPageFetcher();
    private TransformSourceToFile transformSourceToFile = new
            TransformSourceToFile();
    private Consolidator consolidator = new Consolidator();

    @Override
    public IO<Path> f(Map<String, Integer> stateSeatsMap) {
        return IOW.lift(IOFunctions.sequence(
                Stream.iterableStream(stateSeatsMap.entrySet())
                      .bind(this::computeLinks)
                      .map(webPageFetcher)
                      .map(s -> IOFunctions.bind(s,
                                                 t -> transformSourceToFile.f(
                                                         t)))))
                  .bind(consolidator);
    }

    private Stream<String> computeLinks(Map.Entry<String, Integer> entry) {
        return Stream.range(1, entry.getValue())
                     .map(constituencyNumber -> SITE + entry.getKey() +
                             constituencyNumber + HTM_AC + constituencyNumber);
    }


}
