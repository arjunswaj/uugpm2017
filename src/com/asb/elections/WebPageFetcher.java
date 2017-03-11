package com.asb.elections;

import fj.F;
import fj.data.IO;
import fj.data.IOFunctions;
import net.htmlparser.jericho.Source;

import java.net.URL;


/**
 * Fetches Web Page.
 * Created by arjun on 11/03/17.
 */
public class WebPageFetcher implements F<String, IO<Source>> {
    @Override
    public IO<Source> f(String url) {
        return IOFunctions.fromTry(() -> new Source(new URL(url)));
    }
}
