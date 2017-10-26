package edu.upc.eseiaat.pma.shoppinglist3;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Shoppinglistactivity extends AppCompatActivity {
    private static final String FILENAME = "shopping_list.txt";//ponemos una constante en la clase / static porque solo hay una coia de e/final porque no cambiará

    //una lista siempre necesita unos datos, un array por lo tanto:
    private ArrayList<ShoppingItem> itemlist;
    //una lista siempre necesita un adaptador tambien por lo tanto:
    private ShoppingListAdapter adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;
   private void writeItemlist(){


       try {
           FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
           for(int i=0;i<itemlist.size();i++){//pasamos por todos los objetos de la lista
               ShoppingItem it = itemlist.get(i);//cogemos el item
               String line = String.format("%s;%b\n", it.getText(),it.isChecked());//metemos un string y luego un booleano que son respectivamente it.getText y is Checked
               fos.write(line.getBytes());//grabo la linea en el fichero fos
           }
           fos.close();//lo cerramos
       } catch (FileNotFoundException e) {
           Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();//excepcion de no encontrar
       } catch (IOException e) {
           Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
       }

   }

    @Override
    protected void onStop() {//cuando la aplicacion para por cualquier cosa s
        super.onStop();
        writeItemlist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglistactivity);
        list = (ListView) findViewById(R.id.list);//Con control+alt+f los convierto en campos
        btn_add = (Button) findViewById(R.id.btn_añadir);
        edit_item = (EditText) findViewById(R.id.edititem);//cajetilla
        itemlist = new ArrayList<>();
        itemlist.add( new ShoppingItem("Patatas",  true));
        itemlist.add(new ShoppingItem("zanahoria", true));
        itemlist.add(new ShoppingItem("papael"));
        itemlist.add(new ShoppingItem("copas"));
        adapter = new ShoppingListAdapter(this, R.layout.shopping_item, itemlist);//this por esta clase, simple list item es el que queremos, itemlist es como hemos llamado a los datos que van a ir dentro

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                itemlist.get(position).togglechecked();
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //creamos un listener en la misma lista para cuando cliquemos durante unos segundos
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int pos, long id) {
                maybeRemoveItem(pos); //creamos un metodo para borrar un elemento de la lista i pasamos la posicion para saber el que borro con cuadro de dialogo
                return true;
            }
        });
    }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//creamos un cuadro de dialogo
        builder.setTitle(R.string.confirm); //Le ponemos un titulo a través de un recurso string a la confirmación de borrado
        String fmt = getResources().getString(R.string.confirmmessage);
        builder.setMessage(String.format(fmt, itemlist.get(pos).getText())); //hacemos que salga el mensaje junto al elemento que queremos borrar con un format pasandole el pos de ese elemento
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
            itemlist.add(new ShoppingItem(item_text)); //añado lo escrito en la cajetilla a la lista
            adapter.notifyDataSetChanged(); //aviso al adaptador que hay algo que ha cambiado(he añadido uno a la lista
            edit_item.setText(""); //cuando ya haya añadido lo que quiero borro lo que hay en la cajetilla
        }
        list.smoothScrollToPosition(itemlist.size()-1);
    }
}

//si queremos que el editext donde escribimos tenga atributos especificos
//como una " si estamos escribiendo un mail o un boton de send en vez
//de return cuando enviamos un mensjae de texto o un return que simplemente
//baje una linea el texto y no envie nada tenemos que recurrir en el text XML
//del layout dentro del editext a : android:imeOptions"actionDone" ñor ejemplo