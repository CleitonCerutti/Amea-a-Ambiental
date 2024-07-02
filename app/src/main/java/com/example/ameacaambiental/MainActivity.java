package com.example.ameacaambiental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listAmeaca;
    List<Ameaca> ameacas;
    DatabaseReference mDatabase;

    private AmeacaAdapter ameacaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        mDatabase = FirebaseDatabase.getInstance().getReference("ameaca");

        listAmeaca = findViewById(R.id.containerList);
        ameacas = new ArrayList<>();


        ameacaAdapter = new AmeacaAdapter(MainActivity.this, R.layout.activity_ameaca_list_item, ameacas);
        listAmeaca.setAdapter(ameacaAdapter);

        listAmeaca.setOnItemClickListener((parent, view, position, id) -> {
            Ameaca ameaca = ameacas.get(position);
            changeToUpdate(ameaca.getId());
        });

        listAmeaca.setOnItemLongClickListener((parent, view, position, id) -> {
            Ameaca ameaca = ameacas.get(position);
            mDatabase.child(ameaca.getId()).removeValue();
            return true;
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ameacas.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Ameaca value = dataSnapshot.getValue(Ameaca.class);
                    ameacas.add(value);
                }

                ameacaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Erro ao carregar ameaças do banco de dados: " + error.getMessage());
            }
        });

        loadAmeacasFromDatabase();

        Button btnAdicionar = findViewById(R.id.novaAmeaca);
        btnAdicionar.setOnClickListener(v -> {
            Intent it = new Intent(getBaseContext(), addAmeaca.class);
            startActivity(it);
        });
    }

    public void changeToUpdate(String id) {
        Intent it = new Intent(getBaseContext(), editAmeaca.class);
        it.putExtra("ID", id);
        startActivity(it);
    }

    private void loadAmeacasFromDatabase() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ameacas.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Ameaca value = dataSnapshot.getValue(Ameaca.class);
                    ameacas.add(value);
                }

                ameacaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Erro ao carregar ameaças do banco de dados: " + error.getMessage());
            }
        });
    }
}
