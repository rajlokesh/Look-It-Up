package com.example.raj.myapplication;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    boolean mediaMan = false;

    TextView textView;
    ProgressBar progressBar;
    ProgressBar progressBarR;
    Bitmap bbBitmap;
    Canvas canvas;
    Paint paint;
    Paint paintText;
    ImageView imageView;
    GifImageView ivscan;
    //int wDiff,hDiff;

    ViewGroup.LayoutParams params;
    ImageView bbiv;
    Integer CAM_REQ = 1, FILE_REQ = 0;
    String filePath;
    Map config;
    private String takenPicFileName;
    private Uri uriCamPic;
    private String urlToDeepAi;
    private List<Caption> selected = new ArrayList<Caption>();
    private int ih,iw;
    private int iW,iH;

    //    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Map config = new HashMap();
        config.put("cloud_name", "dgxykz1au");
//
        if (mediaMan == false) {
            MediaManager.init(this, config);
            mediaMan = true;

        }
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        bbiv = findViewById(R.id.bb);
        ivscan = findViewById(R.id.scan);
        params = ivscan.getLayoutParams();
        ivscan.setVisibility(View.INVISIBLE);
        ivscan.setAlpha((float) 0.1);
        progressBar = findViewById(R.id.progressBar);
        progressBarR = findViewById(R.id.progressBarR);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelect();
                textView.setTextSize(30);
            }
        });
    }

    private void imageSelect() {
        final CharSequence[] options = {"Camera", "Storage", "Exit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(("Camera"))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("-mm-ss");
                    String picFileName = "look" + df.format(date) + ".jpg";
                    File outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File capturedFile = null;
                    try {
                        capturedFile = File.createTempFile(picFileName, ".jpg", outputPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    takenPicFileName = capturedFile.getAbsolutePath();
                    uriCamPic = Uri.fromFile(capturedFile);

                    String authorities = getApplicationContext().getPackageName() + ".provider";
                    uriCamPic = FileProvider.getUriForFile(MainActivity.this, authorities, capturedFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCamPic);

                    Log.i("onActivityRESULT", "im before cam req if");
                    startActivityForResult(intent, CAM_REQ);


                } else if (options[which].equals(("Storage"))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select Image"), FILE_REQ);

                } else if (options[which].equals(("Exit"))) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        textView.setText("Processing...");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAM_REQ) {
                Log.i("onActivityRESULT", "im inside cam req if");
                Uri uri = null;
                if (data != null) {

                    uri = data.getData();
                }
                if (uri == null && takenPicFileName != null) {
                    uri = Uri.fromFile(new File(takenPicFileName));
                }
                File file = new File(takenPicFileName);
                if (!file.exists()) {
                    file.mkdir();
                }
                /*Bundle bundle = data.getExtras();
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds=true;

                Bitmap bitmap = (Bitmap) bundle.get("data");*/
                int targetImageViewWidth = imageView.getWidth();
                int targetImageViewHeight = imageView.getHeight();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(takenPicFileName, bmOptions);
                int cameraImageWidth = bmOptions.outHeight;
                int cameraImageHeight = bmOptions.outWidth;

                int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inJustDecodeBounds = false;

                Bitmap processedBitmp = BitmapFactory.decodeFile(takenPicFileName, bmOptions);

                imageView.setImageBitmap(processedBitmp);
                //imageView.setImageDrawable(Drawable.createFromPath(takenPicFileName));
                filePath = takenPicFileName;
            } else {
                Uri imageUri = data.getData();
                imageView.setImageURI(imageUri);
                filePath = getRealPathFromURI(imageUri);
            }
        } else
            Log.i("onActivityRESULT", "im inside onactR result not ok");

        ih=imageView.getMeasuredHeight();//height of imageView
        iw=imageView.getMeasuredWidth();//width of imageView
        iH=imageView.getDrawable().getIntrinsicHeight();//original height of underlying image
        iW=imageView.getDrawable().getIntrinsicWidth();//original width of underlying image

        if (ih/iH<=iw/iW) iw=iW*ih/iH;//rescaled width of image within ImageView
        else ih= iH*iw/iW;//rescaled height of image within ImageView
        params.height = ih;
        params.width = iw;
        //ivscan.setMaxWidth(imageView.getDrawable().getMinimumWidth());
        ivscan.setLayoutParams(params);

        goingForward();

    }

    private String getRealPathFromURI(Uri imageUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(MainActivity.this, imageUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void goingForward() {
        Log.i("goingForward", "here we start tomo");
        //editText = findViewById(R.id.editText);
        //editText.setVisibility(View.INVISIBLE);

        couldinary();


    }

    private void callDeepAi() {
        Retrofit.Builder builder = new Retrofit.Builder().
                baseUrl("https://api.deepai.org/").
                addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        DeepAiClient client = retrofit.create(DeepAiClient.class);
        Call<CaptionItem> call = client.captionForUrl(urlToDeepAi);
        call.enqueue(new Callback<CaptionItem>() {
            @Override
            public void onResponse(Call<CaptionItem> call, Response<CaptionItem> response) {
                CaptionItem repos = response.body();
                // if(!repos.isEmpty())
                //Toast.makeText(MainActivity.this, repos.size(),Toast.LENGTH_SHORT).show();
                //Log.i("return ai tomo response",response.body()+"       "+repos.toString() );
                Log.i("tomo pretty  gson", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                manipulateResults(repos);

            }

            @Override
            public void onFailure(Call<CaptionItem> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("return from ai tomo", t.getMessage());
            }
        });
    }

    private void manipulateResults(CaptionItem repos) {
        Output reposOutput = repos.getOutput();
        List<Caption> captions = reposOutput.getCaptions();
        int i = 0;
        Log.i("return list tomo", "" + captions.size());
        for (Caption c : captions
                ) {

            if (i > 10 || c.getConfidence() < 0)
                break;
            selected.add(c);
            i++;
            Log.i("return list tomo", "" + c.getConfidence());

        }
        showInTextView();
    }

    private void showInTextView() {

        bbiv.setVisibility(View.VISIBLE);
        bbBitmap = Bitmap.createBitmap(
                iw, // Width
                ih,
                Bitmap.Config.ARGB_8888 // Config
        );
        canvas = new Canvas(bbBitmap);
        paint = new Paint();
        paintText = new Paint();
        paintText.setColor(Color.YELLOW);
        paintText.setTextSize(40f);
        paint.setStrokeWidth(6f);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);

        textView = findViewById(R.id.textView);
        String text = "";
        for (Caption c : selected
                ) {
            //text = text + "\n" + c.getCaption();
            showBoundingBox(c.getBoundingBox(), c.getCaption());
            //Log.i("boundry ",c.getBoundingBox().toString() );
        }

        bbiv.setLayoutParams(imageView.getLayoutParams());
        bbiv.setImageBitmap(bbBitmap);

        progressBar.setVisibility(View.INVISIBLE);
        progressBarR.setVisibility(View.INVISIBLE);
        ivscan.setVisibility(View.INVISIBLE);
        textView.setTextSize(15);
        textView.setText(text);


    }

    private void showBoundingBox(List<Double> boundingBox, String caption) {

        Rect rectangle = new Rect(
                (int)(boundingBox.get(0).intValue()*iw/iW*1.6), // Left
                (int)(boundingBox.get(1).intValue()*ih/iH*1.6), // Top
                (int)(((boundingBox.get(0).intValue() + boundingBox.get(2).intValue()))*iw/iW*1.6), // Right
                (int)(((boundingBox.get(1).intValue() + boundingBox.get(3).intValue()))*ih/iH*1.6)// Bottom
        );
//        Rect rectangle = new Rect(
//                0, // Left
//                0, // Top
//                iw, // Right
//                ih// Bottom
//        );
        canvas.drawRect(rectangle, paint);
        canvas.drawText(caption, (int)(boundingBox.get(0).intValue()*iw/iW*1.6)+8, // Left
                (int)(boundingBox.get(1).intValue()*ih/iH*1.6)-4, paintText);

    }

    private void couldinary() {
//        Map config = new HashMap();
//        config.put("cloud_name", "dgxykz1au");
//
//        MediaManager.init(this, config);

        //  Toast.makeText(MainActivity.this, " before upload Start "+filePath,Toast.LENGTH_SHORT).show();
        Log.i("before upload tomo ", filePath);
        //String decodedFilePath=decodeFile(filePath);
        //Log.i("decoded tomo",decodedFilePath );
        String requestId = MediaManager.get().upload(filePath).unsigned("nwjvlo14").callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarR.setVisibility(View.VISIBLE);
                ivscan.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Double progress = (double) bytes / totalBytes;

                progressBar.setProgress((int) Math.rint(progress * 100));
                progressBarR.setProgress((int) Math.rint(progress * 100));

                //Toast.makeText(MainActivity.this, "upload on Progress"+progress,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i("return from map tomo", "Success");
                printMap(resultData);

                //urlToDeepAi=(String)resultData.get("url");
                Log.i("return from link tomo", urlToDeepAi + "");
                callDeepAi();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).dispatch();


    }


    private void printMap(Map resultData) {
        Iterator it = resultData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getKey().toString().equals("url"))
                urlToDeepAi = pair.getValue().toString();
            Log.i("return from map tomo", pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
