import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.XMLFormatter;

public class CurrencyConverter {
    public static void downloadFile(URL url, String outputFileName) throws IOException
    {
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    public static void main(String[] args) {
        try {
            downloadFile(new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"),"exchangeRate.txt");
            XMLFormatter xmlFormatter = new XMLFormatter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
