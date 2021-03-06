package org.liquidengine.legui.image.loader;

import net.minecraft.util.Identifier;
import org.liquidengine.legui.image.StbBackedLoadableImage;
import org.liquidengine.legui.image.LoadableImage;

/**
 * Created by ShchAlexander on 3/2/2017.
 */
public class DefaultImageLoader extends ImageLoader {

    @Override
    protected LoadableImage createImage(Identifier path) {
        return new StbBackedLoadableImage(path);
    }
}
