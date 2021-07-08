package org.liquidengine.legui.image;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.liquidengine.legui.exception.LeguiException;
import org.liquidengine.legui.exception.LeguiExceptionTemplate;
import org.liquidengine.legui.util.IOUtil;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by ShchAlexander on 2/6/2017.
 */
public class StbBackedLoadableImage extends LoadableImage {

    private static final Logger LOGGER = LogManager.getLogger();
    private int width;
    private int height;
    private ImageChannels channels;
    private ByteBuffer imageData;

    /**
     * This constructor should be used with {@link #setPath(Identifier)} and {@link #load()} methods.
     */
    public StbBackedLoadableImage() {
    }

    /**
     * Used to create buffered image object and load it.
     *
     * @param path path to image source.
     */
    public StbBackedLoadableImage(Identifier path) {
        super(path);
        try {
            load();
        } catch (LeguiException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(e);
            }
        }
    }

    /**
     * Should be used to load image data from source.
     */
    @Override
    public void load() {
        try {
            InputStream stream = MinecraftClient.getInstance().getResourcePackProvider().getPack().open(ResourceType.CLIENT_RESOURCES, getPath());
            ByteBuffer byteBuffer = IOUtil.resourceToByteBuffer(stream);
            int[] width = {0};
            int[] height = {0};
            int[] channels = {0};
            ByteBuffer imageData = STBImage.stbi_load_from_memory(byteBuffer, width, height, channels, 4);

            if (imageData != null) {
                this.width = width[0];
                this.height = height[0];
                this.channels = ImageChannels.instance(channels[0]);
                this.imageData = imageData;
            } else { // if error occurs
                throw LeguiExceptionTemplate.FAILED_TO_LOAD_IMAGE.create(STBImage.stbi_failure_reason());
            }
        } catch (IOException e) {
            throw LeguiExceptionTemplate.FAILED_TO_LOAD_IMAGE.create(e, e.getMessage());
        }
    }

    /**
     * Returns image width.
     *
     * @return image width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns image height.
     *
     * @return image height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns image data.
     *
     * @return image data.
     */
    public ByteBuffer getImageData() {
        return imageData;
    }

    /**
     * Returns image channels.
     *
     * @return image channels.
     */
    public ImageChannels getChannels() {
        return channels;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("path", getPath())
            .append("width", width)
            .append("height", height)
            .append("channels", channels)
            .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        StbBackedLoadableImage image = (StbBackedLoadableImage) obj;

        return new EqualsBuilder()
            .append(width, image.width)
            .append(height, image.height)
            .append(getPath(), image.getPath())
            .append(channels, image.channels)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getPath())
            .append(width)
            .append(height)
            .append(channels)
            .toHashCode();
    }

}
