import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

public class XpmMonoConverter {

    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out.println("No file input selected!");
            return;
        }

        for (String f:args) {
            try {
                final File file = new File(f);
                if (!file.exists() || file.isDirectory()) {
                    System.out.println("File did not find: " + f);
                    continue;
                }

                final BufferedImage image = ImageIO.read(file);

                final ByteArrayOutputStream baos = new ByteArrayOutputStream(image.getWidth() * image.getHeight());
                int b = 0;
                int count = 0;

                for (int j = 0; j < image.getHeight(); j++) {
                    for (int i = 0, w = image.getWidth(); i < w; i++) {
                        final int color = image.getRGB(i, j);

                        final int bit = (byte) (color == Color.WHITE.getRGB() ? 0 : 1 << count);
                        b = (b | bit);

                        count++;
                        if (count == 8 || i == w - 1) {
                            count = 0;
                            baos.write(b);
                            b = 0;
                        }
                    }
                }

                final int dot = file.getName().lastIndexOf('.');
                final File dir = file.getParentFile();

                final File out;
                if (dot > 0) {
                    out = new File(dir, file.getName().substring(0, dot) + ".MONO");
                } else {
                    out = new File(dir, file.getName());
                }

                Files.write(out.toPath(), baos.toByteArray());

                System.out.println(String.format("File converted: %s > %s", f, out));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

