package pl.lodz.uni.math.damianstopa.webservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAddUser = findViewById(R.id.buttonMenuAddUser);
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        Button buttonGetSingleUser = findViewById(R.id.buttonMenuGetSingleUser);
        buttonGetSingleUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GetUserActivity.class);
                startActivity(intent);
            }
        });

        Button buttonDeleteUser = findViewById(R.id.buttonMenuDeleteUser);
        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteUserActivity.class);
                startActivity(intent);
            }
        });

        Button buttonUpdateUser = findViewById(R.id.buttonMenuUpdateUser);
        buttonUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateUserActivity.class);
                startActivity(intent);
            }
        });

    }
}
