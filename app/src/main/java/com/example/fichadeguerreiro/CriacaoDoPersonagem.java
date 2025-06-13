package com.example.fichadeguerreiro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CriacaoDoPersonagem extends AppCompatActivity {

    private EditText etNome, etForca, etDestreza, etConstituicao, etInteligencia, etSabedoria, etCarisma;
    private Spinner spRaca, spClasse;
    private List<CheckBox> checkBoxesPericias = new ArrayList<>();
    private FirebaseFirestore db;
    private Personagem personagemEmEdicao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_criacao_do_personagem);

        // Ajuste do padding para sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização do Firebase
        db = FirebaseFirestore.getInstance();

        // Vincular views
        etNome = findViewById(R.id.etNomePersonagem);
        etForca = findViewById(R.id.etForca);
        etDestreza = findViewById(R.id.etDestreza);
        etConstituicao = findViewById(R.id.etConstituicao);
        etInteligencia = findViewById(R.id.etInteligencia);
        etSabedoria = findViewById(R.id.etSabedoria);
        etCarisma = findViewById(R.id.etCarisma);
        spRaca = findViewById(R.id.spinnerRaca);
        spClasse = findViewById(R.id.spinnerClasse);

        // Configurar spinner de Raças
        List<String> racas = List.of("Humano", "Elfo", "Orc", "Anão");
        ArrayAdapter<String> racaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, racas);
        racaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRaca.setAdapter(racaAdapter);

        // Configurar spinner de Classes
        List<String> classes = List.of("Guerreiro", "Ladino", "Paladino", "Mago");
        ArrayAdapter<String> classeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        classeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClasse.setAdapter(classeAdapter);

        // Inicializar CheckBoxes de perícias
        checkBoxesPericias.add(findViewById(R.id.cbAcrobacia));
        checkBoxesPericias.add(findViewById(R.id.cbArcanismo));
        checkBoxesPericias.add(findViewById(R.id.cbAtletismo));
        checkBoxesPericias.add(findViewById(R.id.cbEnganacao));
        checkBoxesPericias.add(findViewById(R.id.cbHistoria));
        checkBoxesPericias.add(findViewById(R.id.cbIntimidacao));
        checkBoxesPericias.add(findViewById(R.id.cbInvestigacao));
        checkBoxesPericias.add(findViewById(R.id.cbNatureza));
        checkBoxesPericias.add(findViewById(R.id.cbReligiao));

        // Verifica se veio personagem para edição via intent
        String personagemId = getIntent().getStringExtra("personagem_id");
        if (personagemId != null) {
            carregarPersonagemParaEdicao(personagemId);
        }
    }

    private void carregarPersonagemParaEdicao(String nome) {
        db.collection("personagens")
                .whereEqualTo("nome", nome)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        var document = queryDocumentSnapshots.getDocuments().get(0);
                        Personagem personagem = document.toObject(Personagem.class);
                        if (personagem != null) {
                            personagem.setId(document.getId());
                            personagemEmEdicao = personagem;
                            preencherCamposComPersonagem(personagem);
                        }
                    } else {
                        Toast.makeText(this, "Personagem não encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erro ao buscar personagem por nome", Toast.LENGTH_SHORT).show());
    }

    private void preencherCamposComPersonagem(Personagem personagem) {
        etNome.setText(personagem.getNome());

        // Selecionar raça e classe nos spinners
        ArrayAdapter<String> racaAdapter = (ArrayAdapter<String>) spRaca.getAdapter();
        int racaPos = racaAdapter.getPosition(personagem.getRaca());
        spRaca.setSelection(racaPos);

        ArrayAdapter<String> classeAdapter = (ArrayAdapter<String>) spClasse.getAdapter();
        int classePos = classeAdapter.getPosition(personagem.getClasse());
        spClasse.setSelection(classePos);

        etForca.setText(String.valueOf(personagem.getForca()));
        etDestreza.setText(String.valueOf(personagem.getDestreza()));
        etConstituicao.setText(String.valueOf(personagem.getConstituicao()));
        etInteligencia.setText(String.valueOf(personagem.getInteligencia()));
        etSabedoria.setText(String.valueOf(personagem.getSabedoria()));
        etCarisma.setText(String.valueOf(personagem.getCarisma()));

        // Marcar perícias
        for (CheckBox cb : checkBoxesPericias) {
            cb.setChecked(personagem.getPericias().contains(cb.getText().toString()));
        }
    }

    public void salvarPersonagem(View view) {
        String nome = etNome.getText().toString().trim();
        String raca = spRaca.getSelectedItem().toString();
        String classe = spClasse.getSelectedItem().toString();

        try {
            int forca = Integer.parseInt(etForca.getText().toString());
            int destreza = Integer.parseInt(etDestreza.getText().toString());
            int constituicao = Integer.parseInt(etConstituicao.getText().toString());
            int inteligencia = Integer.parseInt(etInteligencia.getText().toString());
            int sabedoria = Integer.parseInt(etSabedoria.getText().toString());
            int carisma = Integer.parseInt(etCarisma.getText().toString());

            List<String> periciasSelecionadas = new ArrayList<>();
            for (CheckBox cb : checkBoxesPericias) {
                if (cb.isChecked()) {
                    periciasSelecionadas.add(cb.getText().toString());
                }
            }

            if (periciasSelecionadas.size() > 2) {
                Toast.makeText(this, "Selecione no máximo 2 perícias!", Toast.LENGTH_SHORT).show();
                return;
            }

            Personagem personagem = new Personagem(
                    nome, raca, classe,
                    forca, destreza, constituicao,
                    inteligencia, sabedoria, carisma,
                    periciasSelecionadas
            );

            if (personagemEmEdicao != null) {
                // Atualizar personagem existente
                db.collection("personagens")
                        .document(personagemEmEdicao.getId())
                        .set(personagem)
                        .addOnSuccessListener(doc -> {
                            Toast.makeText(this, "Personagem atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, JaPossuePersonagem.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Erro ao atualizar personagem.", Toast.LENGTH_SHORT).show()
                        );
            } else {
                // Criar novo personagem
                db.collection("personagens")
                        .add(personagem)
                        .addOnSuccessListener(doc -> {
                            Toast.makeText(this, "Personagem criado com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Erro ao salvar personagem.", Toast.LENGTH_SHORT).show()
                        );
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, preencha todos os atributos com números válidos.", Toast.LENGTH_SHORT).show();
        }
    }
}
