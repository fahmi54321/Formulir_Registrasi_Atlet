package com.mobile.formulirregistrasiatlet.Utils;

import android.graphics.Bitmap;
import android.net.Uri;

public interface ImageAttachmentListener {
    public void image_attachment(int from, String filename, Bitmap file, Uri uri);
}
