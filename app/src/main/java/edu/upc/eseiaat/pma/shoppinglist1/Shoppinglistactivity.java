package edu.upc.eseiaat.pma.shoppinglist1;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Shoppinglistactivity extends AppCompatActivity {

    //una lista siempre necesita unos datos, un array por lo tanto:
    private ArrayList<String> itemlist;
    //una lista siempre necesita un adaptador tambien por lo tanto:
    private ArrayAdapter<String> adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglistactivity);
        list = (ListView) findViewById(R.id.list);//Con control+alt+f los convierto en campos
        btn_add = (Button) findViewById(R.id.btn_añadir);
        edit_item = (EditText) findViewById(R.id.edititem);//cajetilla
        itemlist = new ArrayList<>();
        itemlist.add("Patatas");
        itemlist.add("Papel WC");
        itemlist.add("Zanahoria");
        itemlist.add("CopasDanone");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemlist);//this por esta clase, simple list item es el que queremos, itemlist es como hemos llamado a los datos que van a ir dentro

        //mirar contenido de la cajita, y meterlo en la lista cuando le de al botón(listener)
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creamos un metodo  que te añade un item en la lista
                addItem();
            }
        });

        //mirar contenido de la cajita, y meterlo en la lista igual que antes pero esta vez cuando le dé al tick
        edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addItem();  //metodo  que te añade un item en la lista
                return true;
            }
        });

        list.setAdapter(adapter);//con este adapter meteremos lo que hay en el arraylist(itemlist) en la lista(list)

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //creamos un listener en la misma lista para cuando cliquemos durante unos segundos
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int pos, long id) {
                maybeRemoveItem(pos); //creamos un metodo para borrar un elemento de la lista i pasamos la posicion para saber quien el que borro
                return true;
            }
        });
    }



    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//creamos un cuadro de dialogo
        builder.setTitle(R.string.confirm); //Le ponemos un titulo a través de un recurso string a la confirmación de borrado
        String fmt = getResources().getString(R.string.confirmmessage);
        builder.setMessage(String.format(fmt, itemlist.get(pos))); //hacemos que salga el mensaje junto al elemento que queremos borrar con un format pasandole el pos de ese elemento
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemlist.remove(pos); //borramos ese elemento en el que hemos hecho un click largo
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, null); //si le damos al boton de cancel pues vuelve hacia atras(cancela)
        builder.create().show(); //para que funcione el dialogo


    }

    private void addItem() {
        //creo un string con item text i le digo que lo coja de edit_item(cajetilla)
        String item_text = edit_item.getText().toString();
        if (!item_text.isEmpty()) { //si item_text NO esta vacio ( !isEmpty ) añadelo
            itemlist.add(item_text); //añado lo escrito en la cajetilla a la lista
            adapter.notifyDataSetChanged(); //aviso al adaptador que hay algo que ha cambiado(he añadido uno a la lista
            edit_item.setText(""); //cuando ya haya añadido lo que quiero borro lo que hay en la cajetilla
        }
    }
}

//si queremos que el editext donde escribimos tenga atributos especificos
//como una " si estamos escribiendo un mail o un boton de send en vez
//de return cuando enviamos un mensjae de texto o un return que simplemente
//baje una linea el texto y no envie nada tenemos que recurrir en el text XML
//del layout dentro del editext a : android:imeOptions"actionDone" ñor ejemplo