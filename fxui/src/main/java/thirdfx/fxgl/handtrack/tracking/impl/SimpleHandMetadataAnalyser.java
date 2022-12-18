package thirdfx.fxgl.handtrack.tracking.impl;

import thirdfx.fxgl.handtrack.tracking.Hand;
import thirdfx.fxgl.handtrack.tracking.HandMetadata;
import thirdfx.fxgl.handtrack.tracking.HandMetadataAnalyser;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SimpleHandMetadataAnalyser implements HandMetadataAnalyser {

    @Override
    public HandMetadata analyse(Hand hand) {
        return new HandMetadata(false);
    }
}
