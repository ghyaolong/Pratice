package item.com.pratice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ProgressBar mProgressBar;

    public static final String URI =
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545939570586&di=7e58db2fea6fb6c0d08315aabfa56092&imgtype=0&src=http%3A%2F%2Fpic.xoyo.com%2Fbbs%2F2010%2F11%2F30%2F10113010300bdf68f9f96b70e4.jpg";

    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mImageView = findViewById(R.id.iv_image);
        mProgressBar = findViewById(R.id.progress_circular);
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(URI);
    }

    class MyAsyncTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            URLConnection connection;
            try {
                connection = new URL(url).openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                bitmap = BitmapFactory.decodeStream(bis);
                Thread.sleep(3000);
                inputStream.close();
                bis.close();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mProgressBar.setVisibility(View.GONE);
            mImageView.setImageBitmap(bitmap);
        }
    }
}
