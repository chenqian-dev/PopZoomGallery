# PopZoomGallery

![Demo](./Untitled.gif)

`PopZoomGallery` is a lightweight Android popup gallery that animates from
thumbnail grids/lists into a fullscreen, swipeable, zoomable image viewer.

## What This Repository Contains

- `library`: the reusable popup gallery component (`PopZoomGallery`)
- `app`: demo app that shows a grid-to-fullscreen transition
- `toolbox` (git submodule): utility module required by the demo app

## Source-Level Project Analysis

This section summarizes the current implementation based on the code in
`library/src/main/java/me/phelps/library`.

1. Popup container
`PopZoomGallery` extends `PopupWindow`, creates a `ZoomGallery`, and exposes one
entrypoint: `showPop(View anchor, int position)`.

2. Gallery composition
`ZoomGallery` is a `RelativeLayout` composed of:
- a dim background view (`ImageView`)
- a `HackyViewPager` for swipe and zoom content
- a bottom `PointIndicator`

3. Transition pipeline
- Open animation: `ZoomImageUtil.zoomImageFromThumb(...)`
- Close animation: `ZoomImageUtil.closeZoomAnim(...)`
- Animation start/end states are driven by `ZoomImageModel.rect`

4. Image loading extension point
`ZoomGalleryAdapter` creates a `PhotoView` per page and delegates real image
loading to:
`ZoomGalleryAdapter.ZoomGalleryInstantiateItem#onItemInstantiate(...)`.
This allows callers to use Glide, Picasso, Fresco, or custom loaders.

5. Close interaction behavior
The viewer closes on `PhotoView` tap (`onPhotoTap` / `onViewTap`), and the
popup is dismissed when the close animation finishes.

## Environment And Compatibility

This is a legacy Android project setup:

- Gradle wrapper: `3.3` (`gradle/wrapper/gradle-wrapper.properties`)
- Android Gradle Plugin: `2.3.2` (`build.gradle`)
- `compileSdkVersion 22`, `targetSdkVersion 22`, `minSdkVersion 14`
- Support library dependency: `com.android.support:appcompat-v7:22.1.0`
- PhotoView dependency: `com.github.chrisbanes.photoview:library:1.2.3`
- Repository source: `jcenter()`

If you are building in a modern environment, migration (AGP/Gradle/repositories
and support libraries) will likely be required.

## Setup

1. Clone with submodules:

```bash
git clone --recursive https://github.com/chenqian2651489/PopZoomGallery.git
```

If already cloned:

```bash
git submodule update --init --recursive
```

2. If you want to run the demo app, verify `settings.gradle` contains:

```gradle
include ':app', ':library', ':lib'
project(':lib').projectDir = new File('toolbox/lib')
```

For library-only integration in an existing app project, only `:library` is
required.

3. Build the demo app:

```bash
./gradlew :app:assembleDebug
```

## Integrating The Library

1. Add module dependency in your app:

```gradle
dependencies {
    compile project(':library') // use implementation(...) in modern Gradle
}
```

2. Prepare a `ZoomImageModel` list for all gallery items:
- `smallImagePath`: thumbnail URL/path
- `bigImagePath`: fullscreen URL/path
- `rect`: thumbnail bounds in window coordinates

3. Create and show `PopZoomGallery`:

```java
ArrayList<ZoomImageModel> zoomImageList = new ArrayList<>();
int firstVisible = gridView.getFirstVisiblePosition();
int lastVisible = firstVisible + gridView.getChildCount() - 1;

for (int i = 0; i < gridView.getCount(); i++) {
    View child = null;
    if (i >= firstVisible && i <= lastVisible) {
        child = gridView.getChildAt(i - firstVisible);
    }
    ZoomImageModel model = new ZoomImageModel();

    if (child != null) {
        int[] xy = new int[2];
        child.getLocationInWindow(xy);
        model.rect = new Rect(
                xy[0],
                xy[1],
                xy[0] + child.getWidth(),
                xy[1] + child.getHeight()
        );
    } else {
        model.rect = new Rect();
    }

    model.smallImagePath = imageList.get(i);
    model.bigImagePath = imageList.get(i);
    zoomImageList.add(model);
}

PopZoomGallery pop = new PopZoomGallery(
        this,
        zoomImageList,
        new ZoomGalleryAdapter.ZoomGalleryInstantiateItem() {
            @Override
            public void onItemInstantiate(
                    ViewGroup container,
                    int position,
                    PhotoView view,
                    ZoomImageModel model
            ) {
                Glide.with(container.getContext())
                        .load(model.bigImagePath)
                        .centerCrop()
                        .into(view);
            }
        }
);

pop.showPop(gridView, startPosition);
```

## Public API

- `PopZoomGallery(Context, ArrayList<ZoomImageModel>, ZoomGalleryInstantiateItem)`
- `showPop(View anchor, int position)`
- `ZoomImageModel`:
  - `String smallImagePath`
  - `String bigImagePath`
  - `Rect rect`

## Important Notes

- `rect` must come from `getLocationInWindow(...)` for correct animation.
- `GridView#getChildAt(index)` uses *visible-child index*, not adapter index.
- If an item is off-screen (no visible child view), using an empty `Rect`
  triggers fade-style close behavior.
- `PointIndicator` is not drawn when there is only one image.
