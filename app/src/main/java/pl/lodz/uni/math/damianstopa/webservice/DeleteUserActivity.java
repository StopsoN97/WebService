package pl.lodz.uni.math.damianstopa.webservice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DeleteUserActivity extends AppCompatActivity {

    private Button buttonDelete;
    private EditText editTextUserIdToDelete;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        buttonDelete = findViewById(R.id.buttonDelete);
        editTextUserIdToDelete = findViewById(R.id.editTextUserIdToDelete);
        progressBar = findViewById(R.id.progressBarOnDelete);

        buttonDelete.setOnClickListener(buttonDeleteOnClickListener);
    }

    private View.OnClickListener buttonDeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonDeleteClicked();
        }
    };

    private void buttonDeleteClicked() {
        new DeleteUserTask().execute();
    }

    private class DeleteUserTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL restApiEndpoint = new URL(RestApiHelper.apiURL + "/users/" + editTextUserIdToDelete.getText().toString());
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) restApiEndpoint.openConnection();
                httpsURLConnection.setRequestMethod("DELETE");
                return httpsURLConnection.getResponseCode();

            } catch (Exception e) {
                Toast.makeText(DeleteUserActivity.this, "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 204) {
                Toast.makeText(DeleteUserActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DeleteUserActivity.this, "Error, something went wrong", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);

        }
    }


}