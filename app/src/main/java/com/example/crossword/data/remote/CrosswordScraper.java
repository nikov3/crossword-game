package com.example.crossword.data.remote;

import com.example.crossword.domain.model.CellModel;
import com.example.crossword.domain.model.CrosswordModel;
import com.example.crossword.domain.model.WordModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CrosswordScraper {

    private final OkHttpClient client = new OkHttpClient();

    public CrosswordModel fetch(String url) throws IOException {

        // 1️⃣ HTTP REQUEST
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0")
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("HTTP error: " + response.code());
        }

        String html = response.body().string();

        // 2️⃣ PAGE TITLE
        Document document = Jsoup.parse(html);
        String pageTitle = document.selectFirst("h1").text();

        // 3️⃣ EXTRACT addCrossword(...) BLOCK
        Pattern blockPattern = Pattern.compile("addCrossword\\((.*?)\\);", Pattern.DOTALL);
        Matcher blockMatcher = blockPattern.matcher(html);

        if (!blockMatcher.find()) {
            throw new IOException("Crossword data not found");
        }

        String block = blockMatcher.group(1);

        int start = block.indexOf("[");
        int end = block.lastIndexOf("]");

        String arrayData = block.substring(start, end + 1);

        int width = 0;
        int height = 0;

        List<CellModel> cells = new ArrayList<>();
        List<WordModel> words = new ArrayList<>();

        // 4️⃣ WIDTH + HEIGHT
        Pattern sizePattern = Pattern.compile(
                "\\{type:'crossword'.*?width:(\\d+),height:(\\d+)",
                Pattern.DOTALL
        );

        Matcher sizeMatcher = sizePattern.matcher(arrayData);

        if (sizeMatcher.find()) {
            width = Integer.parseInt(sizeMatcher.group(1));
            height = Integer.parseInt(sizeMatcher.group(2));
        }

        // 5️⃣ CELLS
        Pattern cellPattern = Pattern.compile(
                "\\{type:'cell',x:(\\d+),y:(\\d+),chr:'(.*?)',hwid:(\\d+),vwid:(\\d+),svc:'(.*?)'\\}",
                Pattern.DOTALL
        );

        Matcher cellMatcher = cellPattern.matcher(arrayData);

        while (cellMatcher.find()) {

            int x = Integer.parseInt(cellMatcher.group(1));
            int y = Integer.parseInt(cellMatcher.group(2));
            String chr = cellMatcher.group(3);
            int hwid = Integer.parseInt(cellMatcher.group(4));
            int vwid = Integer.parseInt(cellMatcher.group(5));
            String svc = cellMatcher.group(6);

            cells.add(new CellModel(x, y, chr, hwid, vwid, svc));
        }

        // 6️⃣ WORDS
        Pattern wordPattern = Pattern.compile(
                "\\{type:'word',id:(\\d+),clue:'(.*?)'\\}",
                Pattern.DOTALL
        );

        Matcher wordMatcher = wordPattern.matcher(arrayData);

        while (wordMatcher.find()) {

            int id = Integer.parseInt(wordMatcher.group(1));
            String clue = wordMatcher.group(2);

            words.add(new WordModel(id, clue));
        }

        return new CrosswordModel(
                pageTitle,
                width,
                height,
                cells,
                words
        );
    }
}
