/**
 * Copyright (c) 2016-present, Facebook, Inc.
 * All rights reserved.
 * <p>
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package in.ajm.sb_library.keyframes.model.keyframedmodels;


import java.util.List;

import in.ajm.sb_library.keyframes.KFPath;
import in.ajm.sb_library.keyframes.model.KFFeature;
import in.ajm.sb_library.keyframes.model.KFFeatureFrame;

/**
 * A {@link KeyFramedObject} which houses information for a key framed feature object.  This
 * includes the commands to draw a shape at each given key frame.  This is a post-process object
 * used for KFFeature.
 */
public class KeyFramedPath extends KeyFramedObject<KFFeatureFrame, KFPath> {

    private KeyFramedPath(List<KFFeatureFrame> featureFrames, float[][][] timingCurves) {
        super(featureFrames, timingCurves);
    }

    /**
     * Constructs a KeyFramedPath from a {@link KFFeature}.
     */
    public static KeyFramedPath fromFeature(KFFeature feature) {
        return new KeyFramedPath(feature.getKeyFrames(), feature.getTimingCurves());
    }

    /**
     * Applies the current state, given by interpolationValue, to the supplied Path object.
     * @param stateA Initial state
     * @param stateB End state
     * @param interpolationValue Progress [0..1] between stateA and stateB
     * @param modifiable The modifiable object to apply the values to
     */
    @Override
    protected void applyImpl(KFFeatureFrame stateA, KFFeatureFrame stateB, float interpolationValue, KFPath modifiable) {
        if (stateB == null || interpolationValue == 0) {
            stateA.getShapeData().applyFeature(modifiable);
            return;
        }
        KFFeatureFrame.ShapeMoveListData thisMoveList = stateA.getShapeData();
        KFFeatureFrame.ShapeMoveListData nextMoveList = stateB.getShapeData();
        for (int i = 0, len = thisMoveList.getVectorCommands().size(); i < len; i++) {
            thisMoveList.getVectorCommands().get(i).interpolate(
                    nextMoveList.getVectorCommands().get(i),
                    interpolationValue,
                    modifiable);
        }
    }
}
