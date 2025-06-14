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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_criacao_do_personagem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

            db.collection("personagens")
                    .add(personagem)
                    .addOnSuccessListener(doc -> {
                        Toast.makeText(this, "Personagem criado com sucesso!", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erro ao salvar personagem.", Toast.LENGTH_SHORT).show()
                    );

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, preencha todos os atributos com números válidos.", Toast.LENGTH_SHORT).show();
        }
    }
}
