package com.example.fichadeguerreiro;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class JaPossuePersonagem extends AppCompatActivity {

    private static final int REQUEST_CRIACAO = 1;
    private static final int REQUEST_VER = 2;

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private List<Personagem> listaPersonagens = new ArrayList<>();
    private PersonagemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ja_possue_personagem);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PersonagemAdapter(listaPersonagens);
        recyclerView.setAdapter(adapter);

        carregarPersonagens();

        adapter.setOnItemClickListener(personagem -> {
            Toast.makeText(this, "Selecionou: " + personagem.getNome(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(JaPossuePersonagem.this, VerPersonagem.class);
            intent.putExtra("personagem", personagem);
            startActivityForResult(intent, REQUEST_VER);
        });

        Button buttonCriarPersonagem = findViewById(R.id.button3);
        buttonCriarPersonagem.setOnClickListener(view -> {
            Intent intent = new Intent(JaPossuePersonagem.this, CriacaoDoPersonagem.class);
            startActivityForResult(intent, REQUEST_CRIACAO);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CRIACAO || requestCode == REQUEST_VER) && resultCode == RESULT_OK) {
            carregarPersonagens();
        }
    }


    private void carregarPersonagens() {
        db.collection("personagens")
                .get()
                .addOnSuccessListener(query -> {
                    listaPersonagens.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        Personagem p = doc.toObject(Personagem.class);
                        p.setId(doc.getId());
                        listaPersonagens.add(p);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao carregar personagens", Toast.LENGTH_SHORT).show();
                    Log.e("FIREBASE", "Erro ao carregar personagens", e);
                });
    }
}

