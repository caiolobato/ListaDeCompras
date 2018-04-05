package pa.senac.br.listadecompras;

import android.content.DialogInterface;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pa.senac.br.listadecompras.model.Lista;

public class MainActivity extends AppCompatActivity {

    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef;
    private EditText campoLista;
    private String TAG;
    private List<Lista> listaList;
    private ListView listaListView;
    private Lista lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campoLista = findViewById(R.id.campoListaId);
        listaListView = findViewById(R.id.listViewId);

        iniciarFirebase();
        getLista();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerForContextMenu(listaListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Excluir").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                lista = new Lista();

                AdapterView.AdapterContextMenuInfo menuInfo =
                        (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                int position = menuInfo.position;
                lista = (Lista) listaListView.getItemAtPosition(position);


                //Dialog box começa aqui
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Excluir");
                dialog.setMessage("Tem certeza que deseja excluir este registro?");
                //dialog.setCancelable(false);
                dialog.setIcon(android.R.drawable.ic_delete);

                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"Registro mantido",Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //remover valor
                        myRef.child(lista.getNomeLista()).removeValue();
                        //myRef.child(lista.getNomeLista()).setValue(null);

                        Toast.makeText(MainActivity.this,"Registro excluido",Toast.LENGTH_SHORT).show();
                        carregaLista();

                    }
                });

                dialog.create();
                dialog.show();

                return false;
            }
        });
    }

    public void iniciarFirebase(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Lista");

    }

    public Lista geraLista(){
        Lista lista = new Lista();
        lista.setNomeLista(campoLista.getText().toString());
        lista.setDataLista("04/04/2018");
        lista.setQuantidade("2");

        return lista;
    }

    protected void carregaLista() {
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,listaList);
        listaListView.setAdapter(adapter);
    }

    public void getLista(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaList = new ArrayList<>();
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Lista lista = d.getValue(Lista.class);
                    listaList.add(lista);
                }
                carregaLista();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });
    }

    public void insereTexto(View view) {
        Lista lista = geraLista();
        myRef.child(lista.getNomeLista()).setValue(lista);
        campoLista.setText(null);
        carregaLista();
    }

}
