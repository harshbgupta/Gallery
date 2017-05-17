package corporation.hell.gallery;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** The images. */
    private ArrayList<String> images;

    private GridView gallery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gallery = (GridView) findViewById(R.id.galleryGridView);

        gallery.setAdapter(new ImageAdapter(this));

    }

    // Swarup added
    public View getGridViewItemByPosition(int position, GridView gridView) {
        final int firstListItemPosition = gridView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + gridView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return gridView.getAdapter().getView(position, null, gridView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return gridView.getChildAt(childIndex);
        }
    }

    /**
     * The Class ImageAdapter.
     */
    private class ImageAdapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public long getItemId(int position) {
            return position;
        }

        public Object getItem(int position) {
            return position;
        }

        // Imp should be added
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        // Imp should be added
        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        public int getCount() {
            return images.size();
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));
                picturesView.setPadding(10,10,10,10);

            } else {
                picturesView = (ImageView) convertView;
            }

            //Setting images to grid view
            Glide.with(context).load(images.get(position))
                    .placeholder(R.drawable.cert1).centerCrop()
                    .into(picturesView);

            gallery.setOnItemClickListener(new OnItemClickListener() {

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    if (null != images && !images.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Images selectd of " + position, Toast.LENGTH_SHORT).show();
                        // Swarup added
                        View view = getGridViewItemByPosition(position, gallery);
                        view.setBackgroundColor(Color.BLUE);
                    }
                }
            });

            return picturesView;
        }



        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }
    }

}
