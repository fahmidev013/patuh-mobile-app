package app.patuhmobile.module;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import app.patuhmobile.R;
import app.patuhmobile.module.Login.LoginActivity;

public class LupaSandiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pro_lupa_sandi);
        Button btnSimpan = (Button) findViewById(R.id.simpan);
        Button btnBatal = (Button) findViewById(R.id.batal);
        btnSimpan.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
        btnBatal.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }
}
