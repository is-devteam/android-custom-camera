package pl.gatti.dgcam;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DgCamActivity extends Activity {
	private Camera mCamera;
	private CameraPreview mPreview;
	private Button ibRetake;
	private Button ibUse;
	private Button ibCapture;
	private File sdRoot;
	private final String dir = "/DCIM/Camera/";
	private String fileName;
    private FrameLayout previewLayout;
    private ImageView previewImageView;

    private int deviceHeight;

    private PictureCallback mPicture = new ExamplePictureCallback();

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ibRetake = (Button) findViewById(R.id.ibRetake);
		ibUse = (Button) findViewById(R.id.ibUse);
		ibCapture = (Button) findViewById(R.id.ibCapture);
        previewImageView = (ImageView) findViewById(R.id.preview_image_view);
        previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
	}

	@Override
	protected void onResume() {
		super.onResume();

        sdRoot = Environment.getExternalStorageDirectory();

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        deviceHeight = display.getHeight();

        ibCapture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
            }
        });

        ibRetake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File discardedPhoto = new File(sdRoot, dir + fileName);
                discardedPhoto.delete();

                mCamera.startPreview();

                ibRetake.setVisibility(LinearLayout.GONE);
                ibUse.setVisibility(LinearLayout.GONE);
                previewLayout.setVisibility(View.VISIBLE);
                previewImageView.setVisibility(View.GONE);
            }
        });

        ibUse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

		createCamera();
	}

	@Override
	protected void onPause() {
		super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        previewLayout.removeViewAt(0);
	}

    private void createCamera() {
        mCamera = getCameraInstance();
        Camera.Parameters params = mCamera.getParameters();

        params.setPictureSize(640, 480);
        params.setPreviewSize(640, 480);
        params.setPictureFormat(PixelFormat.JPEG);
        params.setJpegQuality(50);
        mCamera.setParameters(params);

        mPreview = new CameraPreview(this, mCamera);

        float widthFloat = (float) (deviceHeight) * 4 / 3;
        int width = Math.round(widthFloat);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, deviceHeight);
        layoutParams.gravity = Gravity.CENTER;
        previewLayout.setLayoutParams(layoutParams);

        layoutParams = new FrameLayout.LayoutParams(width, deviceHeight);
        layoutParams.gravity = Gravity.CENTER;
        previewImageView.setLayoutParams(layoutParams);

        previewLayout.addView(mPreview, 0);
    }

	public static Camera getCameraInstance() {
        int cameraIndex = 0;

        for (int i = 0; i <= Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraIndex = i;
                break;
            }
        }

        try {
			return Camera.open(cameraIndex);
		} catch (Exception e) {
            return null;
		}
	}

   class ExamplePictureCallback implements PictureCallback {
		public void onPictureTaken(byte[] data, Camera camera) {
			ibRetake.setVisibility(View.VISIBLE);
			ibUse.setVisibility(View.VISIBLE);
            previewLayout.setVisibility(View.GONE);
            previewImageView.setVisibility(View.VISIBLE);
            mCamera.stopPreview();
            ibCapture.setVisibility(View.GONE);

			fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";
			File mkDir = new File(sdRoot, dir);
			mkDir.mkdirs();
			File pictureFile = new File(sdRoot, dir + fileName);

			try {
				FileOutputStream purge = new FileOutputStream(pictureFile);
				purge.write(data);
				purge.close();
			} catch (FileNotFoundException e) {
				Log.d("DG_DEBUG", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("DG_DEBUG", "Error accessing file: " + e.getMessage());
			}

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            Matrix flipVerticalMatrix = new Matrix();
            flipVerticalMatrix.setScale(-1, 1);
            flipVerticalMatrix.postTranslate(bitmap.getWidth(), 0);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), flipVerticalMatrix, true);
            previewImageView.setImageBitmap(bitmap);
		}
	};
}