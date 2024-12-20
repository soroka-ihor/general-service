package es.localchat.utils;

import es.localchat.component.profile.avatar.model.AllowedProfileImageExtension;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;


public class MultipartUtils {

    public static byte[] shrinkUp(MultipartFile multipart) {
        if (!isImage(multipart)) {
            throw new IllegalArgumentException("The multipart file is not an image.");
        }
        try {
            // Resize the image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(multipart.getInputStream())
                    .size(800, 600) // Change the size according to your needs
                    .outputFormat(getFileExtension(multipart))
                    .outputQuality(0.8)
                    .toOutputStream(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean extensionIsAllowed(MultipartFile image) {
        return AllowedProfileImageExtension.contains(getFileExtension(image));
    }

    public static String getFileExtension(MultipartFile image) {
        if (Objects.isNull(image) ||
                image.getOriginalFilename() == null ||
                image.getOriginalFilename().isEmpty() ||
                image.getOriginalFilename().lastIndexOf(".") == -1
        ) {
            return "";
        }
        return image.getOriginalFilename()
               .substring(image.getOriginalFilename().lastIndexOf(".") + 1);
    }

    public static boolean moreThan(MultipartFile image, int megabytes) {
        if (convertBytesToMegabytes(image.getSize()) < megabytes) {
            return false;
        }
        return true;
    }

    public static int convertBytesToMegabytes(long bytes) {
        return (int) (bytes / (1024 * 1024));
    }

    public static boolean isImage(MultipartFile multipartFile) {
        return multipartFile.getContentType().startsWith("image/");
    }
}
