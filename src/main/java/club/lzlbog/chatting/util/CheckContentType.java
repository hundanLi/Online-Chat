package club.lzlbog.chatting.util;

import org.apache.http.entity.ContentType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-23 10:45
 **/
public class CheckContentType {
    private static Set<String> imageTypeSet = new HashSet<>();
    static {
        imageTypeSet.add(ContentType.IMAGE_BMP.getMimeType());
        imageTypeSet.add(ContentType.IMAGE_GIF.getMimeType());
        imageTypeSet.add(ContentType.IMAGE_JPEG.getMimeType());
        imageTypeSet.add(ContentType.IMAGE_PNG.getMimeType());
        imageTypeSet.add(ContentType.IMAGE_SVG.getMimeType());
        imageTypeSet.add(ContentType.IMAGE_TIFF.getMimeType());
        imageTypeSet.add(ContentType.IMAGE_WEBP.getMimeType());
    }
    public static boolean checkImage(String contentType) {
        return imageTypeSet.contains(contentType);
    }
}
