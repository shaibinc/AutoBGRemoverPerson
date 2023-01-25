package com.cozy.apps.bgremover;

import android.graphics.Bitmap;

public  interface BgRemoverListener {
    void onSuccess(Bitmap bitmap);

    void onFailed( Exception exception);

}