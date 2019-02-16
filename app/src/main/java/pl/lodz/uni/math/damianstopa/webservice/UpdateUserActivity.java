package pl.lodz.uni.math.damianstopa.webservice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UpdateUserActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button buttonUpdate;
    private EditText editTextName;
    private EditText editTextJob;
    private EditText editTextUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        progressBar = findViewById(R.id.progressBarOnUpdateUser);
        buttonUpdate = findViewById(R.id.buttonUpdateUser);
        editTextName = findViewById(R.id.editTextNameToUpdate);
        editTextJob = findViewById(R.id.editTextJobToUpdate);
        editTextUserId = findViewById(R.id.editTextUserIdToUpdate);

        buttonUpdate.setOnClickListener(buttonUpdateOnClickListener);
    }

    private View.OnClickListener buttonUpdateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonUpdateClicked();
        }
    };

    private void buttonUpdateClicked() {
        if (!isdEditTextUserIdEmpty()) {
            new UpdateUserTask().execute();
        }
        else {
            Toast.makeText(UpdateUserActivity.this, "Please input id!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isdEditTextUserIdEmpty() {
        if (editTextUserId.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private class UpdateUserTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL restApiEndpoint = new URL(RestApiHelper.apiURL + "/users/" + editTextUserId.getText().toString());
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();
                httpsURLConnection.setRequestMethod("PUT");
                Map<String, String> data = new HashMap<>();
                data.put("name", editTextJob.getText().toString());
                data.put("job", editTextName.getText().toString());
                JSONObject postData = new JSONObject(data);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.getOutputStream().write(postData.toString().getBytes());

                return httpsURLConnection.getResponseCode();
            } catch (Exception exception) {
                Toast.makeText(UpdateUserActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 200) {
                Toast.makeText(UpdateUserActivity.this, "Success, user updated", Toast.LENGTH_SHORT).show();
                clearEditTexts();
            } else {
                Toast.makeText(UpdateUserActivity.this, "Error " + responseCode, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void clearEditTexts() {
        editTextName.setText("");
        editTextJob.setText("");
        editTextUserId.setText("");
    }
}
