package com.example.fichadeguerreiro;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class VerPersonagem extends AppCompatActivity {

    private EditText etNomePersonagem;
    private Spinner spinnerRaca, spinnerClasse;
    private EditText etForca, etDestreza, etConstituicao, etInteligencia, etSabedoria, etCarisma;
    private CheckBox cbAcrobacia, cbArcanismo, cbAtletismo, cbEnganacao, cbHistoria, cbIntimidacao, cbInvestigacao, cbNatureza, cbReligiao;
    private Button btnVoltar, btnEditar, btnDeletar;

    private boolean editando = false;
    private Personagem personagem;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_personagem);

        // Inicialização das Views
        etNomePersonagem = findViewById(R.id.etNomePersonagem);
        spinnerRaca = findViewById(R.id.spinnerRaca);
        spinnerClasse = findViewById(R.id.spinnerClasse);
        etForca = findViewById(R.id.etForca);
        etDestreza = findViewById(R.id.etDestreza);
        etConstituicao = findViewById(R.id.etConstituicao);
        etInteligencia = findViewById(R.id.etInteligencia);
        etSabedoria = findViewById(R.id.etSabedoria);
        etCarisma = findViewById(R.id.etCarisma);

        cbAcrobacia = findViewById(R.id.cbAcrobacia);
        cbArcanismo = findViewById(R.id.cbArcanismo);
        cbAtletismo = findViewById(R.id.cbAtletismo);
        cbEnganacao = findViewById(R.id.cbEnganacao);
        cbHistoria = findViewById(R.id.cbHistoria);
        cbIntimidacao = findViewById(R.id.cbIntimidacao);
        cbInvestigacao = findViewById(R.id.cbInvestigacao);
        cbNatureza = findViewById(R.id.cbNatureza);
        cbReligiao = findViewById(R.id.cbReligiao);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnEditar = findViewById(R.id.btnEditar);
        btnDeletar = findViewById(R.id.btnDeletar);

        // ** Aqui: popula os spinners com os dados **
        List<String> racas = List.of("Humano", "Elfo", "Orc", "Anão");
        ArrayAdapter<String> racaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, racas);
        racaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRaca.setAdapter(racaAdapter);

        List<String> classes = List.of("Guerreiro", "Ladino", "Paladino", "Mago");
        ArrayAdapter<String> classeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        classeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasse.setAdapter(classeAdapter);

        personagem = (Personagem) getIntent().getSerializableExtra("personagem");

        if (personagem != null) {
            preencherCampos(personagem);
        }

        setCamposEditaveis(false);

        btnVoltar.setOnClickListener(v -> finish());

        btnEditar.setOnClickListener(v -> {
            editando = !editando;
            setCamposEditaveis(editando);
            btnEditar.setText(editando ? "Salvar" : "Editar Personagem");

            if (!editando) {
                salvarPersonagem();
            }
        });

        btnDeletar.setOnClickListener(v -> deletarPersonagem());
    }


    private void preencherCampos(Personagem p) {
        etNomePersonagem.setText(p.getNome());
        setSpinnerSelection(spinnerRaca, p.getRaca());
        setSpinnerSelection(spinnerClasse, p.getClasse());

        etForca.setText(String.valueOf(p.getForca()));
        etDestreza.setText(String.valueOf(p.getDestreza()));
        etConstituicao.setText(String.valueOf(p.getConstituicao()));
        etInteligencia.setText(String.valueOf(p.getInteligencia()));
        etSabedoria.setText(String.valueOf(p.getSabedoria()));
        etCarisma.setText(String.valueOf(p.getCarisma()));

        cbAcrobacia.setChecked(p.getPericias().contains("Acrobacia"));
        cbArcanismo.setChecked(p.getPericias().contains("Arcanismo"));
        cbAtletismo.setChecked(p.getPericias().contains("Atletismo"));
        cbEnganacao.setChecked(p.getPericias().contains("Enganacao"));
        cbHistoria.setChecked(p.getPericias().contains("Historia"));
        cbIntimidacao.setChecked(p.getPericias().contains("Intimidacao"));
        cbInvestigacao.setChecked(p.getPericias().contains("Investigacao"));
        cbNatureza.setChecked(p.getPericias().contains("Natureza"));
        cbReligiao.setChecked(p.getPericias().contains("Religiao"));
    }

    private void salvarPersonagem() {
        personagem.setNome(etNomePersonagem.getText().toString());
        personagem.setRaca(spinnerRaca.getSelectedItem().toString());
        personagem.setClasse(spinnerClasse.getSelectedItem().toString());

        personagem.setForca(parseIntSafe(etForca.getText().toString()));
        personagem.setDestreza(parseIntSafe(etDestreza.getText().toString()));
        personagem.setConstituicao(parseIntSafe(etConstituicao.getText().toString()));
        personagem.setInteligencia(parseIntSafe(etInteligencia.getText().toString()));
        personagem.setSabedoria(parseIntSafe(etSabedoria.getText().toString()));
        personagem.setCarisma(parseIntSafe(etCarisma.getText().toString()));

        personagem.setPericias(new ArrayList<>());
        if (cbAcrobacia.isChecked()) personagem.getPericias().add("Acrobacia");
        if (cbArcanismo.isChecked()) personagem.getPericias().add("Arcanismo");
        if (cbAtletismo.isChecked()) personagem.getPericias().add("Atletismo");
        if (cbEnganacao.isChecked()) personagem.getPericias().add("Enganacao");
        if (cbHistoria.isChecked()) personagem.getPericias().add("Historia");
        if (cbIntimidacao.isChecked()) personagem.getPericias().add("Intimidacao");
        if (cbInvestigacao.isChecked()) personagem.getPericias().add("Investigacao");
        if (cbNatureza.isChecked()) personagem.getPericias().add("Natureza");
        if (cbReligiao.isChecked()) personagem.getPericias().add("Religiao");

        db.collection("personagens")
                .document(personagem.getId())
                .set(personagem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Personagem salvo!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );

    }

    private void deletarPersonagem() {
        db.collection("personagens")
                .document(personagem.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Personagem deletado!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erro ao deletar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void setCamposEditaveis(boolean habilitar) {
        etNomePersonagem.setEnabled(habilitar);
        spinnerRaca.setEnabled(habilitar);
        spinnerClasse.setEnabled(habilitar);

        etForca.setEnabled(habilitar);
        etDestreza.setEnabled(habilitar);
        etConstituicao.setEnabled(habilitar);
        etInteligencia.setEnabled(habilitar);
        etSabedoria.setEnabled(habilitar);
        etCarisma.setEnabled(habilitar);

        cbAcrobacia.setEnabled(habilitar);
        cbArcanismo.setEnabled(habilitar);
        cbAtletismo.setEnabled(habilitar);
        cbEnganacao.setEnabled(habilitar);
        cbHistoria.setEnabled(habilitar);
        cbIntimidacao.setEnabled(habilitar);
        cbInvestigacao.setEnabled(habilitar);
        cbNatureza.setEnabled(habilitar);
        cbReligiao.setEnabled(habilitar);
    }

    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
