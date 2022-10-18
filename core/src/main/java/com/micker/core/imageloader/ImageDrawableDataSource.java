package com.micker.core.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.Nullable;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.exceptions.Exceptions;

/**
 * Created by Leif Zhang on 2017/3/29.
 * Email leifzhanggithub@gmail.com
 */

public class ImageDrawableDataSource implements ObservableOnSubscribe<BitmapDrawable> {
    private String imageUrl;
    private Context context;

    public ImageDrawableDataSource(String url, Context context) {
        imageUrl = url;
        this.context = context;

    }

    @Override
    public void subscribe(final ObservableEmitter<BitmapDrawable> emitter) throws Exception {
        final ImageRequest imageRequest = ImageRequest.fromUri(imageUrl);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, imageUrl);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap loadedImage) {
                Bitmap newBitmap = loadedImage.copy(loadedImage.getConfig(), false);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), newBitmap);
                bitmapDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                emitter.onNext(bitmapDrawable);
                emitter.onComplete();
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                try {
                    Throwable throwable = dataSource.getFailureCause();
                    Exceptions.throwIfFatal(throwable);
                } catch (Exception ignored) {
                }
                emitter.onComplete();
            }
        }, UiThreadImmediateExecutorService.getInstance());

    }
}
