package com.example.myungjin.withusplanet.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myungjin.withusplanet.DataConnect.ServerConnect;
import com.example.myungjin.withusplanet.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class User_profile extends AppCompatActivity {
    private Context mContext = this;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private int PICK_IMAGE_REQUEST = 1;

    private ServerConnect serverConnect;

    private Uri mImageCaptureUri;
    private ImageView userImage;
    private String absolutePath;
    int MY_PERMISSION_REQUEST_STORAGE;
    private String filePath;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        serverConnect = new ServerConnect();




       userID = getIntent().getStringExtra("id");

        Button closeBtn = (Button)findViewById(R.id.close_btn);
        userImage = (ImageView)findViewById(R.id.user_image);
        TextView userId = (TextView)findViewById(R.id.user_id);
        userId.setText(userID);
        Button selectPhotoBtn = (Button)findViewById(R.id.selectphotobtn);
        Button cameraBtn = (Button)findViewById(R.id.camerabtn);



        selectPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionCheck();
                doTakeAlbumAction();

            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionCheck();
                doTakePhotoAction();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               Intent intentUserInfo = new Intent(mContext, UserInfo.class);

           }
        });


    }

    //사진촬영 메소드
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //임시로 사용할 파일 경로 생성
        String url = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    //앨범 사진선택 메소드
    public void doTakeAlbumAction(){
        //앨범호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;
        switch(requestCode){
            case PICK_FROM_ALBUM:
            {
                mImageCaptureUri = data.getData();
                Log.d("smartWheel",mImageCaptureUri.getPath().toString());
            }
            case PICK_FROM_CAMERA:
            {
                //이미지를 가져운 이후 리사이즈할 이미지 크기 결정
                //이후에 이미지 크롭 어플리케이션 호출
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri,"image/*");

                //CROP할 이미지를 200*200 크기로 저장
                intent.putExtra("outputX",200);
                intent.putExtra("outputY",200);
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("scale",true);
                intent.putExtra("return-data",true);
                startActivityForResult(intent,CROP_FROM_IMAGE);
                break;
            }
            case CROP_FROM_IMAGE:
            {
                //크롭이 된 이후의 이미지 넘겨받기
                //이미지뷰에 이미지를 보여주는 등 부가적인 작업 이후에 임시파일 삭제
                if(resultCode != RESULT_OK) return;
                final Bundle extras = data.getExtras();

                //CROP된 이미지를 저장하기 위한 파일 경로
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ServerAnalyzer/"+userID+".jpg";
                System.out.println("Crop 파일 경로 : "+filePath);
                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");//CROP된 BITMAP
                    userImage.setImageBitmap(photo);//CROP된 BITMAP 보여주기
                    System.out.println("Crop된 이미지 앨범에 저장~!~");
                    storeCropImage(photo,filePath);//CROP된 이미지를 외부저장소, 앨범에 저장
                    absolutePath = filePath;
                    break;
                }

                //임시파일삭제(problem : 지금 삭제가 안됨.......8ㅅ8)8ㅅ8ㅅ8ㅅ8ㅅ8ㅅ8
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
            }
        }
    }

    //BITMAP저장~!~
    private void storeCropImage(Bitmap bitmap, String filePath){
        //폴더를 생성해 이미지 저장하는 방식
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ServerAnalyzer";
        System.out.println("dirPath : "+ dirPath);
        File directory_SmartWheel = new File(dirPath);

        //SmartWheel 디렉토리에 폴더가 없다면
        if(!directory_SmartWheel.exists()){
            System.out.println("SmartWhell 디렉토리에 폴더가 없다면 -> 만들기~!~");
            directory_SmartWheel.mkdirs(); //problem : 얘도 안만들어짐......권한문제 해결~~~~~
        }
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            //sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));


            System.out.println("-----------------filepath : "+filePath);

            out.flush();
            out.close();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void PermissionCheck(){ //저장소 읽기/쓰기 접근 권한!
        PermissionListener permissionlistener = new PermissionListener(){
            @Override
            public void onPermissionGranted() {
                Toast.makeText(mContext,"권한허가",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(mContext,"권한거부\n"+deniedPermissions.toString(),Toast.LENGTH_SHORT).show();
            }

        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("거부하면 사용X")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

}
