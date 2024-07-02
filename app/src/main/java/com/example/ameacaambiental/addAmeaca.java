package com.example.ameacaambiental;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class addAmeaca extends AppCompatActivity {
    EditText txtDescricao, txtEndereco, txtData;
    Button btnSalvar, btnCapturarImagem;
    ImageView imageView;
    DatabaseReference mDatabase;
    Bitmap imagemCapturada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addameaca);

        txtDescricao = findViewById(R.id.descricao);
        txtData = findViewById(R.id.txtData);
        txtEndereco = findViewById(R.id.txtEndereco);
        imageView = findViewById(R.id.imageView);
        btnSalvar = findViewById(R.id.btAdicionar);
        btnCapturarImagem = findViewById(R.id.btnCapturarImagem);

        mDatabase = FirebaseDatabase.getInstance().getReference("ameaca");

        btnSalvar.setOnClickListener(v -> adicionarAmeaca());
        btnCapturarImagem.setOnClickListener(v -> capturarImagem());
    }

    public void adicionarAmeaca() {

        String descricao = txtDescricao.getText().toString();
        String endereco = txtEndereco.getText().toString();
        String data = txtData.getText().toString();
        if (descricao.isEmpty() || data.isEmpty() || endereco.isEmpty()) {

            makeText(this, "Por favor, preencha todos os campos", LENGTH_SHORT).show();
            return;
        }
        String imageBase64 = converterImageParaBase64();
        Ameaca ameaca = new Ameaca();
        ameaca.setData(data);
        ameaca.setDescricao(descricao);
        ameaca.setEndereco(endereco);
        ameaca.setImage(imageBase64);

        String id = mDatabase.push().getKey();
        if (id != null) {
            ameaca.setId(id);
            mDatabase.child(id).setValue(ameaca)
                    .addOnSuccessListener(unused -> {
                        makeText(addAmeaca.this, "Ameaça adicionada com sucesso!", LENGTH_SHORT).show();
                        txtDescricao.setText("");
                        txtEndereco.setText("");
                        txtData.setText("");

                        Intent it = new Intent(getBaseContext(), MainActivity.class);
                        setResult(RESULT_OK, it);
                        finish();
                    })
                    .addOnFailureListener(e -> makeText(addAmeaca.this, "Erro ao adicionar ameaça: " + e.getMessage(), LENGTH_SHORT).show());
        }




    }

    public void capturarImagem() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        abreActivity.launch(intent);
    }
    ActivityResultLauncher<Intent> abreActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {


                      Bitmap imagemBit = result.getData().getExtras().getParcelable("data", Bitmap.class);

           if(imagemBit!= null){imagemCapturada = imagemBit;

               imageView.setImageBitmap(imagemCapturada);

           }
                                         }
                }
            }
    );



    private String converterImageParaBase64() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imagemCapturada.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
