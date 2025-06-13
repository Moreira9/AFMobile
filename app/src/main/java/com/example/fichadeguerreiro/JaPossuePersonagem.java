package com.example.fichadeguerreiro;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class JaPossuePersonagem extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private List<Personagem> listaPersonagens = new ArrayList<>();
    private PersonagemAdapter adapter;
    private Personagem personagemSelecionado = null;

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
            personagemSelecionado = personagem;
            // Se quiser enviar para outra tela, adicione um Intent aqui
        });
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
