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

public class AddUserActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button buttonAdd;
    private EditText editTextName;
    private EditText editTextJob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        progressBar = findViewById(R.id.progressBarAddUser);
        buttonAdd = findViewById(R.id.buttonAddUser);
        editTextName = findViewById(R.id.editTextName);
        editTextJob = findViewById(R.id.editTextJob);

        buttonAdd.setOnClickListener(buttonAddOnClickListener);
    }

    private View.OnClickListener buttonAddOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonAddClicked();
        }
    };

    private void buttonAddClicked() {
        new AddUserTask().execute();
    }

    private class AddUserTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL restApiEndpoint = new URL(RestApiHelper.apiURL + "/users");
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                Map<String, String> data = new HashMap<>();
                data.put("name", editTextJob.getText().toString());
                data.put("job", editTextName.getText().toString());
                JSONObject postData = new JSONObject(data);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.getOutputStream().write(postData.toString().getBytes());

                return httpsURLConnection.getResponseCode();
            } catch (Exception exception) {
                Toast.makeText(AddUserActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 201) {
                Toast.makeText(AddUserActivity.this, "Success", Toast.LENGTH_SHORT).show();
                editTextName.setText("");
                editTextJob.setText("");
            } else {
                Toast.makeText(AddUserActivity.this, "Error " + responseCode, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
