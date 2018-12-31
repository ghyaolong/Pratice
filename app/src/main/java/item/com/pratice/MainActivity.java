package item.com.pratice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import item.com.pratice.notification.NotificationActivity;
import item.com.pratice.utils.HttpUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TAKE_PHOTO = 1;
    private Button btn_sendRequest;
    private TextView tv_responseText;

    private Button btnChange;
    private TextView text;

    private Button btnAsyncTest;

    private Button mProgressBar;

    private Button mBtnService;

    private Button mBtnNotification;

    private Button mBtnTakePhoto;
    private ImageView mIvPhoto;

    private Button mBtnGoPhoto;


    private Uri imageUri;

    public static final int UPDATE_TEXT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sendRequest = findViewById(R.id.btn_http);
        tv_responseText = findViewById(R.id.response_text);

        btnChange = findViewById(R.id.btn_change);
        text = findViewById(R.id.tv_text);

        btn_sendRequest.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        btnAsyncTest = findViewById(R.id.asyncTest);
        btnAsyncTest.setOnClickListener(this);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setOnClickListener(this);

        mBtnService = findViewById(R.id.btn_service);
        mBtnService.setOnClickListener(this);

        mBtnNotification = findViewById(R.id.btn_notification);
        mBtnNotification.setOnClickListener(this);


        mBtnTakePhoto = findViewById(R.id.btn_take_photo);
        mBtnTakePhoto.setOnClickListener(this);

        mIvPhoto = findViewById(R.id.iv_photo);

        mBtnGoPhoto = findViewById(R.id.btn_go_photo);
        mBtnGoPhoto.setOnClickListener(this);



    }

    private Handler handler  =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    //在这里进行UI操作
                    text.setText("在handler中进行UI操作");
                    break;
                    default:
                        break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_http:
                sendHttpRequest("http://www.baidu.com");
                break;
            case R.id.btn_change:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            case R.id.asyncTest:
                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                startActivity(intent);
                break;
            case R.id.progressBar:
                startActivity(new Intent(MainActivity.this,ProgressBarActivity.class));
                break;
            case R.id.btn_service:
                startActivity(new Intent(this,ServiceActivity.class));
                    break;
            case R.id.btn_notification:
                startActivity(new Intent(this,NotificationActivity.class));
                break;
            case R.id.btn_take_photo:
                //创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try {
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            "item.com.pratice.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }

                //启动相机程序
                Intent openPhotoIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                openPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(openPhotoIntent,TAKE_PHOTO);

                break;

            case R.id.btn_go_photo:
                Log.d("yao","go photo");
                startActivity(new Intent(this,PhotoActivity.class));
                break;
            default:
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(imageUri));
                        mIvPhoto.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                    break;
        }
    }

    private void sendHttpRequest(String address){
        HttpUtils.sendHttpRequest(address,new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                showResponse(response.body().string());
            }
        });
    }

    private void sendRuquestWithHttpUrlConnection() {

        //开启线程发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    /*URL url = new URL("http://www.baiud.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    //dos.writeBytes("username=123&password=123456");
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line ;
                    while((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());*/

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")
                            .build();
                    Response response = client.newCall(request).execute();
                    String resposneData = response.body().string();
                    showResponse(resposneData);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    /*if(reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(connection!=null){
                        connection.disconnect();
                    }*/
                }
            }


        }).start();
    }

    private void showResponse(final String resposne) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //这里进行UI操作，将结果显示到页面上
                tv_responseText.setText(resposne);
            }
        });
    }
}
