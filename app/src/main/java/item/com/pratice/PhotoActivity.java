package item.com.pratice;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity  implements View.OnClickListener{

    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照

    public static final int CROP_PHOTO = 2;

    private Uri imageUri;

    private Button goPhoto;

    private ImageView ivShowPhoto;

    private static File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        goPhoto = findViewById(R.id.go_poto);
        ivShowPhoto = findViewById(R.id.iv_image1);

        goPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_poto:
                //openCamera(this);
                openGallery();
                break;
        }
    }


    /**
     * 打开相册
     */
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CROP_PHOTO);
    }

    private void openCamera(Activity activity) {
        //获取系统版本
        int currApiVersion = Build.VERSION.SDK_INT;
        //激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断存储卡是否可用
        if(hasSdcard()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileName =  sdf.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory()+fileName+".jpg");
            if(currApiVersion<24){
                //从文件中创建uri
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            }else {
                //兼容android7.0 使用共享文件的形式
                ContentValues cv = new ContentValues(1);
                cv.put(MediaStore.Images.Media.DATA,tempFile.getAbsolutePath());
                //检查是否有存储权限，以免崩溃
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"请开启存储权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            }

        }
        activity.startActivityForResult(intent,PHOTO_REQUEST_CAREMA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case PHOTO_REQUEST_CAREMA:
                if(resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO); // 启动裁剪程序
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        if(data != null) {
                            Uri uri = data.getData();
                            imageUri = uri;
                        }

                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        ivShowPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /*
     * 判断sdcard是否被挂载
     */
    private boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
