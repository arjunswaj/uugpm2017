package com.asb.elections;

import fj.F;
import fj.data.IO;
import fj.data.IOFunctions;
import fj.data.Stream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Consolidator. Don't judge.
 * Created by arjun on 11/03/17.
 */
public class Consolidator implements F<Stream<Path>, IO<Path>> {
    @Override
    public IO<Path> f(Stream<Path> paths) {
        return IOFunctions.fromTry(() -> {
            Path consolidatedFile = Paths.get("res", "consolidated.csv");
            try (BufferedWriter wr = Files.newBufferedWriter(
                    consolidatedFile)) {
                paths.forEach(constituencyFile -> {
                    String fileName = constituencyFile.toFile()
                                                      .getName();
                    int endIndex = fileName.length() - 4;
                    fileName = fileName.substring(0, endIndex);
                    String[] splits = fileName.split(" - ");
                    String state = splits[0].trim();
                    String constituency = splits[1].trim();
                    consolidateData(wr, constituencyFile, state, constituency);
                });
            }
            return consolidatedFile;
        });
    }

    private void consolidateData(BufferedWriter bwReportFile,
                                 Path constituencyFile,
                                 String state, String constituency) {
        try (BufferedReader br = Files.newBufferedReader(constituencyFile)) {
            String row = br.readLine();
            while (null != (row = br.readLine())) {
                StringTokenizer st = new StringTokenizer(row, ",");
                String candidate = st.nextToken()
                                     .trim();
                String party = st.nextToken()
                                 .trim();
                String votes = st.nextToken()
                                 .trim();
                bwReportFile.write(state.replaceAll("\"", "")
                                        .toUpperCase(Locale.ENGLISH));
                bwReportFile.write(",");
                bwReportFile.write(constituency.replaceAll("\"", "")
                                               .toUpperCase(Locale.ENGLISH));
                bwReportFile.write(",");
                bwReportFile.write(candidate.replaceAll("\"", "")
                                            .toUpperCase(Locale.ENGLISH));
                bwReportFile.write(",");
                bwReportFile.write(party.replaceAll("\"", "")
                                        .toUpperCase(Locale.ENGLISH));
                bwReportFile.write(",");
                bwReportFile.write(votes);
                bwReportFile.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
