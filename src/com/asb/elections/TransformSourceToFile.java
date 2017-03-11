package com.asb.elections;

import fj.F;
import fj.data.IO;
import fj.data.IOFunctions;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Transforms Source To File.
 * Ugly shit. Don't judge.
 * Created by arjun on 11/03/17.
 */
public class TransformSourceToFile implements F<Source, IO<Path>> {
    @Override
    public IO<Path> f(Source source) {
        return IOFunctions.fromTry(() -> {
            Element result = source.getAllElements("div id=\"div1\"")
                                   .get(0);
            Element table = result.getAllElements("table")
                                  .get(0);
            List<Element> rows = table.getAllElements("tr");
            Element constituency = rows.get(0);
            String constituencyName = constituency.getTextExtractor()
                                                  .toString();
            Path res = Paths.get("res", constituencyName + ".csv");
            try (BufferedWriter bwResultFile = Files.newBufferedWriter(res)) {
                bwResultFile.write("Candidate,Party,Votes\n");
                int rowSize = rows.size();
                for (int index = 3; index < rowSize; index += 1) {
                    Element row = rows.get(index);
                    List<Element> cols = row.getAllElements("td");
                    bwResultFile.write(cols.get(0)
                                           .getTextExtractor()
                                           .toString());
                    bwResultFile.write(",");
                    bwResultFile.write(cols.get(1)
                                           .getTextExtractor()
                                           .toString());
                    bwResultFile.write(",");
                    bwResultFile.write(cols.get(2)
                                           .getTextExtractor()
                                           .toString());
                    bwResultFile.write("\n");
                }
                return res;
            }
        });
    }
}
