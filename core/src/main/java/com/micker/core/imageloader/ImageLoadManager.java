package com.micker.core.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.micker.core.R;
import com.micker.core.imageloader.util.UriHelper;
import com.micker.core.imageloader.util.Util;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


/**
 * Created by zhangyang on 16/1/15.
 */
public class ImageLoadManager {

    public static void loadBlurImage(final String url, final WscnImageView imageView, final int iterations, final int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(imageView.getController())
                    .setImageRequest(request)
                    .build();
            imageView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBlurImage(final int resId, final WscnImageView imageView, final int iterations, final int blurRadius) {
        try {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(String.valueOf(resId))
                    .build();
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(imageView.getController())
                    .setImageRequest(request)
                    .build();
            imageView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(final String url, final WscnImageView imageView, final int loading, final boolean isNeedPlaceHolder) {
        Observable.just(url)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !Util.checkFilter(imageView, url);
                    }
                })
                .map(new Function<String, Uri>() {
                    @Override
                    public Uri apply(String url1) throws Exception {
                        return UriHelper.createUri(url);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        if (isNeedPlaceHolder)
                            setPlaceHolder(loading, imageView);
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE, ByteConstants.MAX_BITMAP_SIZE))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setAutoPlayAnimations(true)
                                .setImageRequest(request)
                                .build();
                        imageView.setController(controller);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public static void loadImage(final String url, final WscnImageView imageView, final int loading) {
        Observable.just(url)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !Util.checkFilter(imageView, url);
                    }
                })
                .map(new Function<String, Uri>() {
                    @Override
                    public Uri apply(String url1) throws Exception {
                        return UriHelper.createUri(url);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        setPlaceHolder(loading, imageView);
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE, ByteConstants.MAX_BITMAP_SIZE))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setAutoPlayAnimations(true)
                                .setImageRequest(request)
                                .build();
                        imageView.setController(controller);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private static void setPlaceHolder(int loading, WscnImageView imageView) {
        if (loading != R.drawable.default_img && loading != 0) {
            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            hierarchy.setPlaceholderImage(loading);
        } else {
            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            hierarchy.setPlaceholderImage(R.drawable.default_img);
        }
    }

    public static void loadImage(final String url, final WscnImageView imageView, final int loading, ControllerListener listener) {
        Uri uri;
        if (url.contains("http") || url.contains("https")) {
            uri = Uri.parse(url);
        } else {
            uri = Uri.fromFile(new File(url));
        }
        setPlaceHolder(loading, imageView);
        if (Util.getIsNoImage(imageView.getContext()) && !Util.isConnectWIFI(imageView.getContext())) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE, ByteConstants.MAX_BITMAP_SIZE))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setImageRequest(request)
                .setControllerListener(listener)
                .build();
        imageView.setController(controller);
    }

    public static void loadImage(final int resId, final WscnImageView imageView, final int loading) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        setPlaceHolder(loading, imageView);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int width = params != null && params.width > 0 ? params.width : (int) Util.dipToPx(300, imageView.getContext());
        int height = params != null && params.height > 0 ? params.height : (int) (width * imageView.getAspectRatio());
        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (width > 0 && height > 0) {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(width, height, ByteConstants.MAX_BITMAP_SIZE));
        } else {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE,
                    ByteConstants.MAX_BITMAP_SIZE));
        }
        ImageRequest imageRequest = imageRequestBuilder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setImageRequest(imageRequest)
                .build();
        imageView.setController(controller);
    }

    public static void loadRoundImage(final String url, final WscnImageView imageView, final int loading, final int rad) {
        Observable.just(url).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return !Util.checkFilter(imageView, url);
            }
        }).map(new Function<String, Uri>() {
            @Override
            public Uri apply(String url1) throws Exception {
                return UriHelper.createUri(url1);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
                        setPlaceHolder(loading, imageView);
                        RoundingParams params = hierarchy.getRoundingParams();
                        if (params == null) {
                            params = new RoundingParams();
                            params.setBorderColor(Color.WHITE);
                        }

                        params.setCornersRadius(rad);
                        hierarchy.setRoundingParams(params);
                        imageView.setHierarchy(hierarchy);
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE,
                                        ByteConstants.MAX_BITMAP_SIZE))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setAutoPlayAnimations(true)
                                .setImageRequest(request)
                                .build();
                        imageView.setController(controller);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public static void loadRoundImage(final String url, final WscnImageView imageView, final int loading, final int radLeft, final int radTop, final int radRight, final int radBottom) {
        Observable.just(url).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return !Util.checkFilter(imageView, url);
            }
        }).map(new Function<String, Uri>() {
            @Override
            public Uri apply(String url1) throws Exception {
                return UriHelper.createUri(url1);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
                        if (loading != R.drawable.default_img && loading != 0) {
                            hierarchy.setPlaceholderImage(loading);
                        }
                        RoundingParams params = hierarchy.getRoundingParams();
                        if (params == null) {
                            params = new RoundingParams();
                            params.setBorderColor(Color.WHITE);
                        }

                        params.setCornersRadii(radLeft, radTop, radRight, radBottom);
                        hierarchy.setRoundingParams(params);
                        imageView.setHierarchy(hierarchy);
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE,
                                        ByteConstants.MAX_BITMAP_SIZE))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setAutoPlayAnimations(true)
                                .setImageRequest(request)
                                .build();
                        imageView.setController(controller);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }


    public static void loadCircleImage(final String url, final WscnImageView imageView, final int loading, final int rad) {
        Observable.just(url).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return !Util.checkFilter(imageView, url);
            }
        }).map(new Function<String, Uri>() {
            @Override
            public Uri apply(String url1) throws Exception {
                return UriHelper.createUri(url1);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
                        setPlaceHolder(loading, imageView);
                        RoundingParams params = hierarchy.getRoundingParams();
                        if (params == null) {
                            params = new RoundingParams();
                            params.setBorderColor(Color.WHITE);
                        }
                        params.setRoundAsCircle(true);
                        params.setBorderWidth(rad);
                        hierarchy.setRoundingParams(params);
                        imageView.setHierarchy(hierarchy);
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE,
                                        ByteConstants.MAX_BITMAP_SIZE))
                                //.setProgressiveRenderingEnabled(true)
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setAutoPlayAnimations(true)
                                .setImageRequest(request)
                                .build();
                        imageView.setController(controller);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public static void loadCircleImage(final String url, final WscnImageView imageView, final int loading, final int rad, final ControllerListener listener) {
        Observable.just(url).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return !Util.checkFilter(imageView, url);
            }
        }).map(new Function<String, Uri>() {
            @Override
            public Uri apply(String url1) throws Exception {
                return UriHelper.createUri(url1);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
                        setPlaceHolder(loading, imageView);
                        RoundingParams params = hierarchy.getRoundingParams();
                        if (params == null) {
                            params = new RoundingParams();
                            params.setBorderColor(Color.WHITE);
                        }
                        params.setRoundAsCircle(true);
                        params.setBorderWidth(rad);
                        hierarchy.setRoundingParams(params);
                        imageView.setHierarchy(hierarchy);
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                .setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE,
                                        ByteConstants.MAX_BITMAP_SIZE))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setAutoPlayAnimations(true)
                                .setImageRequest(request)
                                .setControllerListener(listener)
                                .build();
                        imageView.setController(controller);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public static void loadCircleImage(int id, WscnImageView imageView, int loading, int rad) {
        try {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(String.valueOf(id))
                    .build();
            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            RoundingParams params = hierarchy.getRoundingParams();
            if (params == null) {
                params = new RoundingParams();
                params.setBorderColor(Color.WHITE);
            }
            params.setRoundAsCircle(true);
            params.setBorderWidth(rad);
            hierarchy.setRoundingParams(params);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setUri(uri)
                    .build();
            imageView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadBitmap(String url, final ProgressResponseListener<Bitmap> callBack) {
        try {
            Uri uri;
            if (url.contains("http") || url.contains("https")) {
                uri = Uri.parse(url);
            } else {
                uri = Uri.fromFile(new File(url));
            }
            final ImageRequest imageRequest = ImageRequest.fromUri(uri);
            final ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(imageRequest, "loadBitmap");
            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                     @Override
                                     public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                         try {
                                             callBack.onComplete(bitmap);
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }
                                     }

                                     @Override
                                     public void onFailureImpl(DataSource dataSource) {
                                         callBack.onComplete(null);
                                     }
                                 },
                    UiThreadImmediateExecutorService.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onComplete(null);
        }
    }

    public static void loadBitmap(int res, final ProgressResponseListener<Bitmap> callBack) {
        try {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(String.valueOf(res))
                    .build();
            ImageRequest imageRequest = ImageRequest.fromUri(uri);
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(imageRequest, "loadBitmap");
            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                     @Override
                                     public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                         try {
                                             callBack.onComplete(bitmap);
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }
                                     }

                                     @Override
                                     public void onFailureImpl(DataSource dataSource) {

                                     }
                                 },
                    UiThreadImmediateExecutorService.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void requestDownloadOnly(Context context, String url) {
        Uri uri = Uri.parse(url);
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.prefetchToDiskCache(imageRequest, url);
    }

    public static void loadSelector(final String normalUrl, final String selectUrl, final ImageView imageView) {
        final HashMap<String, Drawable> hashMap = new HashMap<>();
        Observable<BitmapDrawable> normalObservable = getDrawable(normalUrl, imageView.getContext())
                .doOnNext(new Consumer<BitmapDrawable>() {
                    @Override
                    public void accept(BitmapDrawable bitmapDrawable) throws Exception {
                        hashMap.put(normalUrl, bitmapDrawable);
                    }
                });
        Observable<BitmapDrawable> selectObservable = getDrawable(selectUrl, imageView.getContext())
                .doOnNext(new Consumer<BitmapDrawable>() {
                    @Override
                    public void accept(BitmapDrawable bitmapDrawable) throws Exception {
                        hashMap.put(selectUrl, bitmapDrawable);
                    }
                });
        Observable.merge(normalObservable, selectObservable).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BitmapDrawable>() {
                    @Override
                    public void accept(BitmapDrawable drawable) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        try {
                            Drawable drawable = getSelector(hashMap.get(normalUrl), hashMap.get(selectUrl));
                            imageView.setImageDrawable(drawable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private static Drawable getSelector(Drawable gd_normal, Drawable gd_press) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, gd_press);
        states.addState(new int[]{android.R.attr.state_selected}, gd_press);
        states.addState(new int[]{}, gd_normal);
        return states;
    }

    private static Observable<BitmapDrawable> getDrawable(String uri, final Context context) {
        final ImageDrawableDataSource dataSource = new ImageDrawableDataSource(uri, context);
        return Observable.create(dataSource);
    }

    public static boolean checkIsInFileCache(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);
        return imagePipeline.isInDiskCacheSync(uri);
    }

    public static void getImageFileFromCache(final String url, final ProgressResponseListener<File> listener) {
        final ImageRequest imageRequest = ImageRequest.fromUri(url);
        final ImageRequest newImageRequest = ImageRequestBuilder.fromRequest(imageRequest)
                .setResizeOptions(new ResizeOptions(ByteConstants.IMAGE_MAX_SIZE, ByteConstants.IMAGE_MAX_SIZE,
                        ByteConstants.MAX_BITMAP_SIZE)).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<Void> dataSource = imagePipeline.prefetchToDiskCache(newImageRequest, "loadBitmap");
        dataSource.subscribe(new BaseDataSubscriber<Void>() {
            @Override
            protected void onNewResultImpl(DataSource<Void> dataSource) {
                Observable.interval(200, TimeUnit.MILLISECONDS).takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return getCacheFile(imageRequest) != null;
                    }
                }).
                        observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        try {
                            listener.onComplete(getCacheFile(imageRequest));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void onFailureImpl(DataSource<Void> dataSource) {

            }
        }, UiThreadImmediateExecutorService.getInstance());
    }


    protected static File getCacheFile(ImageRequest imageRequest) {
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest
                , imageRequest.getSourceUri());
        BinaryResource resource =
                ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        if (resource == null) {
            resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
        }
        if (resource == null) {
            return null;
        }
        return ((FileBinaryResource) resource).getFile();
    }

    public static long getCacheSize() {
        return Fresco.getImagePipelineFactory().getMainFileCache().getSize() +
                Fresco.getImagePipelineFactory().getSmallImageFileCache().getSize();
    }

    public static void clearCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearDiskCaches();
    }

}