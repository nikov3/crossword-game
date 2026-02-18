package com.example.crossword.data.remote;

import com.example.crossword.domain.model.CellModel;
import com.example.crossword.domain.model.CrosswordModel;
import com.example.crossword.domain.model.WordModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CrosswordScraper {

    private final OkHttpClient client = new OkHttpClient();
    private static final Map<String, Character> LETTERS_MAP =
            Map.ofEntries(
                    Map.entry("cac5d60265a111045936e2bf9281c22c", 'А'),
                    Map.entry("c22464e0e60fd5dedb9afa221b9117e2", 'Б'),
                    Map.entry("324c035d6a16af4d56a27ae5e7faefe1", 'В'),
                    Map.entry("57a844e7207e7951384fc058b325abb3", 'Г'),
                    Map.entry("464dbd1aff2eeae9cdd5ae9b84e2b67b", 'Д'),
                    Map.entry("2fae0b26b771cd122f40c4f7db3d40e7", 'Е'),
                    Map.entry("11227d8301328c2148f0c52313e6f04b", 'Ж'),
                    Map.entry("92e11528a9aaed011d449c5cb97b28c9", 'З'),
                    Map.entry("b94d001432c586939ec5c97da699b7c8", 'И'),
                    Map.entry("3dd723209d915e0f8607440d0ac65ed4", 'Й'),
                    Map.entry("b5d268747a0a4c8d4fe2edf5b164b5c4", 'К'),
                    Map.entry("a2e2d2e0a3e9470a6b444365ab00925f", 'Л'),
                    Map.entry("75b56ddef7b6511b6b6d49a7feb09be2", 'М'),
                    Map.entry("30cb826d00a4a2baff11b227a24d5f0a", 'Н'),
                    Map.entry("ae57e26c4dd647f48563e40854866ec2", 'О'),
                    Map.entry("759d23ebb2b710e75108b7547f5ca00d", 'П'),
                    Map.entry("996f45a7499b4f6578820842faaa2f9a", 'Р'),
                    Map.entry("8e0c113c6aeeeb4de492ea17b41ce8e2", 'С'),
                    Map.entry("1e7995234c1adb13a2f1c93b6fd498a0", 'Т'),
                    Map.entry("9aa8fef9b4f807446e58d201194e1899", 'У'),
                    Map.entry("b05ce547d416a593a36418df5e30b795", 'Ф'),
                    Map.entry("30bf5b9b8ccbbc4bb262806b5a15e44e", 'Х'),
                    Map.entry("6709acb1592b305e8cd8061dff45a51d", 'Ц'),
                    Map.entry("b0665a57c1ca40b4e66072b7190e2880", 'Ч'),
                    Map.entry("8fa608a4c4122866163e10e17e656771", 'Ш'),
                    Map.entry("88ec1c55517bb878333a868eb6449439", 'Щ'),
                    Map.entry("f2c05e7a8683640fec199a7ab267ca05", 'Ъ'),
                    Map.entry("c9f428df27cd042d3ee686415ac9e840", 'Ь'),
                    Map.entry("c9dc14bc02d016d6f3a7e6204bcb19cd", 'Ю'),
                    Map.entry("918ed02687178699ca1561ed397f70da", 'Я')
            );

    public CrosswordModel fetch(String url) throws IOException {

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

        Document document = Jsoup.parse(html);
        String pageTitle = document.selectFirst("h1").text();

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

        Pattern sizePattern = Pattern.compile(
                "\\{type:'crossword'.*?width:(\\d+),height:(\\d+)",
                Pattern.DOTALL
        );

        Matcher sizeMatcher = sizePattern.matcher(arrayData);

        if (sizeMatcher.find()) {
            width = Integer.parseInt(sizeMatcher.group(1));
            height = Integer.parseInt(sizeMatcher.group(2));
        }

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

            cells.add(
                    new CellModel(
                            x,
                            y,
                            GetCharFromEncryptedResponseCode(chr),
                            hwid,
                            vwid,
                            svc
                    ));
        }

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

    /// This method work with the LETTERS_MAP.
    /// The response returns MD5 hashed "BGxx" instead of cyrilic letters.
    /// For example "А" is "cac5d60265a111045936e2bf9281c22c". This is MD5 hashed value of "BG01"
    /// For example "Б" is "cac5d60265a111045936e2bf9281c22c". This is MD5 hashed value of "BG02"
    private Character GetCharFromEncryptedResponseCode(String encryptedChr){
        return LETTERS_MAP.get(encryptedChr);
    }
}
