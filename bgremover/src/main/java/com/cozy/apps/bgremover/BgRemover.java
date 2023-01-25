package com.cozy.apps.bgremover;

import static com.cozy.apps.bgremover.SmoothMask.tfResizeBilinear;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.segmentation.Segmentation;
import com.google.mlkit.vision.segmentation.SegmentationMask;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions;

import java.nio.ByteBuffer;

public class BgRemover {

    Segmenter segmenter;

    public BgRemover() {
        SelfieSegmenterOptions options =
                new SelfieSegmenterOptions.Builder()
                        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
                        .enableRawSizeMask()
                        .build();
        segmenter = Segmentation.getClient(options);
    }

    public void bitmapSegmentation(Context context, Bitmap bitmap, BgRemoverListener bgRemoverListener) {
        Bitmap finalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        InputImage image = InputImage.fromBitmap(finalBitmap, 0);
        segmenter.process(image)
                .addOnSuccessListener(
                        segmentationMask -> {
                            // Task completed successfully
                            ByteBuffer buffer = segmentationMask.getBuffer();
                            int maskWidth = segmentationMask.getWidth();
                            int maskHeight = segmentationMask.getHeight();
                            Bitmap mask = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888);

                            for (int y = 0; y < maskHeight; y++) {
                                for (int x = 0; x < maskWidth; x++) {
                                    // Gets the confidence of the (x,y) pixel in the mask being in the foreground.
                                    float maskConfidence = 1 - buffer.getFloat();
                                    if (maskConfidence > 0) {
                                        mask.setPixel(x, y, Color.BLACK);
                                    } else {
                                        mask.setPixel(x, y, Color.WHITE);
                                    }
                                }
                            }
                            buffer.rewind();
                            mask = tfResizeBilinear(mask, finalBitmap.getWidth(), finalBitmap.getHeight());

                            mask = SmoothMask.blur(context, mask);

                            for (int y = 0; y < finalBitmap.getHeight(); y++) {
                                for (int x = 0; x < finalBitmap.getWidth(); x++) {
                                    if (mask.getPixel(x, y) == Color.BLACK) {
                                        finalBitmap.setPixel(x, y, Color.TRANSPARENT);
                                    }
                                }
                            }
                            bgRemoverListener.onSuccess(finalBitmap);
                        })
                .addOnFailureListener(
                        e -> {
                            // Task failed with an exception
                            // ...
                            bgRemoverListener.onFailed(e);
                        });
    }

}
