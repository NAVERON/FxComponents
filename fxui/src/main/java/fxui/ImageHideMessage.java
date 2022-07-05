package fxui;

import java.util.stream.IntStream;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;

/**
 * 将message信息隐藏入图片当中 编码和解码计算 
 * @author Almas Baimagambetov (almaslvl@gmail.com) 
 * 工具类，学习如何编码message进入image 
 */
public class ImageHideMessage {

    private static interface Encoder {
        Image encode(Image image, String message);
    }
    
    public interface Decoder {
        String decode(Image image);
    }
    
    private static class BasicEncoder implements Encoder {
        @Override
        public Image encode(Image image, String message) {
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();

            WritableImage copy = new WritableImage(image.getPixelReader(), width, height);
            PixelWriter writer = copy.getPixelWriter();
            PixelReader reader = image.getPixelReader();

            boolean[] bits = encode(message);

            IntStream.range(0, bits.length)
                    .mapToObj(i -> new Pair<>(i, reader.getArgb(i % width, i / width)))
                    .map(pair -> new Pair<>(pair.getKey(), bits[pair.getKey()] ? pair.getValue() | 1 : pair.getValue() &~ 1))
                    .forEach(pair -> {
                        int x = pair.getKey() % width;
                        int y = pair.getKey() / width;

                        writer.setArgb(x, y, pair.getValue());
                    });

            return copy;
        }

        private boolean[] encode(String message) {
            byte[] data = message.getBytes();

            // int = 32 bits
            // byte = 8 bits
            boolean[] bits = new boolean[32 + data.length * 8];

            // encode length
            String binary = Integer.toBinaryString(data.length);
            while (binary.length() < 32) {
                binary = "0" + binary;
            }

            for (int i = 0; i < 32; i++) {
                bits[i] = binary.charAt(i) == '1';
            }

            // [7, 6, 5 ... 0]
            // encode message
            for (int i = 0; i < data.length; i++) {
                byte b = data[i];

                for (int j = 0; j < 8; j++) {
                    bits[32 + i*8 + j] = ((b >> (7 - j)) & 1) == 1;
                }
            }

            return bits;
        }
    }
    
    private static class BasicDecoder implements Decoder {
        @Override
        public String decode(Image image) {
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();

            PixelReader reader = image.getPixelReader();

            boolean[] bits = new boolean[width * height];

            IntStream.range(0, width * height)
                    .mapToObj(i -> new Pair<>(i, reader.getArgb(i % width, i / width)))
                    .forEach(pair -> {
                        String binary = Integer.toBinaryString(pair.getValue());
                        bits[pair.getKey()] = binary.charAt(binary.length() - 1) == '1';
                    });

            // decode length
            int length = 0;
            for (int i = 0; i < 32; i++) {
                if (bits[i]) {
                    length |= (1 << (31 - i));
                }
            }

            byte[] data = new byte[length];
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bits[32 + i*8 + j]) {
                        data[i] |= (1 << (7 - j));
                    }
                }
            }

            return new String(data);
        }
    }
    
    
}






