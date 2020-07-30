package project.final_year.opkomstmanager.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import project.final_year.opkomstmanager.R;

public class SemResult extends AppCompatActivity {

    public static String uRoll;
    ImageView back;
    private WebView semesterResult;
    ProgressDialog progDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_sem_result);

        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        progDailog = new ProgressDialog(this);
        progDailog.setMessage("Loading...");
        progDailog.setCancelable(false);
        progDailog.show();

        Window window1 = this.getWindow();
        window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window1.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        TextView semResultTitle = toolbar.findViewById(R.id.chat_name_title);
        semResultTitle.setText(uRoll);
        setSupportActionBar(toolbar);

        back = toolbar.findViewById(R.id.back);
        back.setOnClickListener(v -> SemResult.super.onBackPressed());

        semesterResult = findViewById(R.id.semesterResult);
        semesterResult.getSettings().setJavaScriptEnabled(true);
        semesterResult.getSettings().setLoadWithOverviewMode(true);
        semesterResult.getSettings().setUseWideViewPort(true);
        semesterResult.getSettings().setBuiltInZoomControls(true);
        semesterResult.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });

        semesterResult.loadUrl("https://result.ietlucknow.ac.in/");
    }

    @Override
    public void onBackPressed() {
        if(semesterResult.canGoBack()){
            semesterResult.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}
