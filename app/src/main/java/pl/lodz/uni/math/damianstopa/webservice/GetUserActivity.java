package pl.lodz.uni.math.damianstopa.webservice;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetUserActivity extends AppCompatActivity {

    private Button buttonGet;
    private TextView textView;
    private EditText editTextUserId;
    private ImageView imageViewAvatar;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user);

        buttonGet = findViewById(R.id.buttonGet);
        textView = findViewById(R.id.textView);
        editTextUserId = findViewById(R.id.editTextUserId);
        imageViewAvatar = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        buttonGet.setOnClickListener(buttonGetOnClickListener);
    }

    private View.OnClickListener buttonGetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonGetClicked();
        }
    };

    private void buttonGetClicked() {
        clearViews();
        progressBar.setVisibility(View.VISIBLE);
        new GetUserTask().execute();
    }

    private void clearViews() {
        textView.setText("");
        imageViewAvatar.setImageDrawable(null);
    }

    private class GetUserTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpsURLConnection httpsURLConnection = null;
            try {
                URL restApiEndpoint = new URL(RestApiHelper.apiURL + "/users/" + editTextUserId.getText().toString());
                httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();

                if (httpsURLConnection.getResponseCode() == 200) {
                    InputStream responseBody = httpsURLConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    int data = responseBodyReader.read();
                    while (data != -1) {
                        stringBuilder.append((char) data);
                        data = responseBodyReader.read();
                    }
                    return stringBuilder.toString();
                }

            } catch (Exception e) {
                Toast.makeText(GetUserActivity.this, "Error", Toast.LENGTH_SHORT).show();
            } finally {
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String dataFromGet) {
            super.onPostExecute(dataFromGet);
            try {
                JSONObject jsonObject = new JSONObject(dataFromGet);
                JSONObject dataObject = jsonObject.getJSONObject("data");

                String firstName = dataObject.getString("first_name");
                String lastName = dataObject.getString("last_name");
                String avatarURL = dataObject.getString("avatar");
                String fullName = firstName + " " + lastName;
                textView.setText(fullName);
                loadImagePreview(avatarURL);

            } catch (Exception e) {
                Toast.makeText(GetUserActivity.this, "Error JSON", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadImagePreview(final String url) {
        //start a background thread for networking
        new Thread(new Runnable() {
            public void run() {
                try {
                    //download the drawable
                    final Drawable drawable = Drawable.createFromStream((InputStream) new URL(url).getContent(), "src");
                    //edit the view in the UI thread
                    imageViewAvatar.post(new Runnable() {
                        public void run() {
                            imageViewAvatar.setImageDrawable(drawable);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
