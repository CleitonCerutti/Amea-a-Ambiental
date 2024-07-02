package com.example.ameacaambiental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editAmeaca extends AppCompatActivity {
    Button btnSalvar;
    DatabaseReference mDatabase;

    EditText txtDescricao, txtEndereco, txtData;
    Ameaca current;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editameaca);

        txtDescricao = findViewById(R.id.descricao);
        txtEndereco = findViewById(R.id.txtEndereco);
        txtData = findViewById(R.id.txtData);
        btnSalvar = findViewById(R.id.btAtualizar);

        _id = getIntent().getStringExtra("ID");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ameaca").child(String.valueOf(_id));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    current = snapshot.getValue(Ameaca.class);
                    txtDescricao.setText(current.getDescricao());
                    txtEndereco.setText(current.getEndereco());
                    txtData.setText(current.getData());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tratar erro de leitura do Firebase
            }
        });

        btnSalvar.setOnClickListener(v -> updateAmeaca());
    }

    public void updateAmeaca() {
        String descricao = txtDescricao.getText().toString();
        String endereco = txtEndereco.getText().toString();
        String data = txtData.getText().toString();

        if (descricao.isEmpty() || data.isEmpty() || endereco.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Ameaca ameaca = new Ameaca();
        ameaca.setId(_id);
        ameaca.setDescricao(descricao);
        ameaca.setEndereco(endereco);
        ameaca.setData(data);

        mDatabase.setValue(ameaca)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Atualização bem-sucedida
                        Toast.makeText(editAmeaca.this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent();
                        setResult(RESULT_OK, it);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Tratamento de erro ao atualizar o registro
                        Toast.makeText(editAmeaca.this, "Erro ao atualizar registro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
