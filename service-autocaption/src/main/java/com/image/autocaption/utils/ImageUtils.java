package com.image.autocaption.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * The Utility class that helps in processing an image input stream for resizing and converting to target format types.
 */
public class ImageUtils {

    /**
     * Downscales an image by resizing to target maxWidth and maxHeight.
     *
     * @param inputStream the image input stream to be resized
     * @param maxWidth target maximum width after resizing
     * @param maxHeight target maximum height after resizing
     * @return resource descriptor for the downscaled image
     *
     * @throws Exception exception occurred while processing the input image
     */
    public static Resource resizeIfNeeded(InputStream inputStream, int maxWidth, int maxHeight) throws Exception {
        BufferedImage resizedImage = null;
        BufferedImage originalImage = ImageIO.read(inputStream);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        System.out.println("original size---" + width + " " + height);
        if (width <= maxWidth && height <= maxHeight) resizedImage = originalImage;
        else {
            // Resize with aspect ratio preserved
            resizedImage = Thumbnails.of(originalImage)
                    .size(maxWidth, maxHeight)
                    .asBufferedImage();

            System.out.println("resized image size---" + resizedImage.getWidth() + " " + resizedImage.getHeight());
            System.out.println(resizedImage.getType());
        }

        return bufferedImageToResource(resizedImage);
    }

    /**
     * Converts a Buffered Image to a ByteArrayResource
     *
     * @param image the resized buffered image to be converted
     *
     * @return resource descriptor for the downscaled image
     * @throws Exception Exception occurred while processing the input image
     */
    private static Resource bufferedImageToResource(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos); // TODO format: "jpg", "png", etc.
        baos.flush();
        return new ByteArrayResource(baos.toByteArray());
    }
}
