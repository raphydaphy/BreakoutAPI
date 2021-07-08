package org.liquidengine.legui.style.font;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.liquidengine.legui.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.liquidengine.legui.exception.LeguiExceptionTemplate.FAILED_TO_LOAD_FONT;

/**
 * Representation of font. Used by text components to specify font to use by renderer.
 */
public class Font {

    /**
     * Font data.
     */
    private ByteBuffer data;

    /**
     * Path to font.
     */
    private Identifier path;

    /**
     * Default constructor. Should be used with path setter and {@link #load()} method.
     */
    public Font() {
    }

    /**
     * Used to create font by specified path. Loads font from specified path.
     *
     * @param path path to font data.
     */
    public Font(Identifier path) {
        this.path = path;
        load();
    }

    /**
     * Used to create font with specified path and data.
     *
     * @param path path to font.
     * @param data font data.
     */
    public Font(Identifier path, ByteBuffer data) {
        this.path = path;
        this.data = data;
    }

    public void load() {
        try {
            InputStream stream = MinecraftClient.getInstance().getResourcePackProvider().getPack().open(ResourceType.CLIENT_RESOURCES, path);
            data = IOUtil.resourceToByteBuffer(stream);
        } catch (IOException e) {
            throw FAILED_TO_LOAD_FONT.create(e, path.toString());
        }
    }

    /**
     * Returns font data.
     *
     * @return font data.
     */
    public ByteBuffer getData() {
        return data;
    }

    /**
     * Returns font path.
     *
     * @return font path.
     */
    public Identifier getPath() {
        return path;
    }

    /**
     * Used to set path. Can be used only once. Used with default constructor.
     *
     * @param path path to set.
     */
    public Font setPath(Identifier path) {
        if (this.path == null && path != null) {
            this.path = path;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Font font = (Font) o;

        return new EqualsBuilder()
            .append(getData(), font.getData())
            .append(getPath(), font.getPath())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getData())
            .append(getPath())
            .toHashCode();
    }
}