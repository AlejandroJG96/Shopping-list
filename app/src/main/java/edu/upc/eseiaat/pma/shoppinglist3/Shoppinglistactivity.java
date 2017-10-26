package edu.upc.eseiaat.pma.shoppinglist3;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Shoppinglistactivity extends AppCompatActivity {
    private static final String FILENAME = "shopping_list.txt";//ponemos una constante en la clase / static porque solo hay una coia de e/final porque no cambiará
    private static final int MAX_BYTES = 8000;
    //una lista siempre necesita unos datos, un array por lo tanto:
    private ArrayList<ShoppingItem> itemlist;
    //una lista siempre necesita un adaptador tambien por lo tanto:
    private ShoppingListAdapter adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;

    private void writeItemlist() {


        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i < itemlist.size(); i++) {//pasamos por todos los objetos de la lista
                ShoppingItem it = itemlist.get(i);//cogemos el item
                String line = String.format("%s;%b\n", it.getText(), it.isChecked());//metemos un string y luego un booleano que son respectivamente it.getText y is Checked
                fos.write(line.getBytes());//grabo la linea en el fichero fos
            }
            fos.close();//lo cerramos
        } catch (FileNotFoundException e) {
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();//excepcion de no encontrar
        } catch (IOException e) {
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        }

    }

    private void ReadItemlist() {
        itemlist = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];//definimos el tamaño de la tabla de bytes que crearemos para poder leer
            int nread = fis.read(buffer);//leemos i nos devuelve el numero de cosas leidas
            if(nread>0){
            String content = new String(buffer, 0, nread);//pasamos este buffer a un string
            String[] lines = content.split("\n");//por donde veas un barra n parte
            for (int i = 0; i < lines.length; i++) {
                String[] parts = lines[i].split(";");
                itemlist.add(new ShoppingItem(parts[0], parts[1].equals("true")));//esta expersion es cierta cuando parts1 es true
            }
            }
            fis.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            Toast.makeText(this, R.string.cannot_read, Toast.LENGTH_SHORT).show();//si falla leer el fichero sale un toast
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
        ReadItemlist();
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
        list.smoothScrollToPosition(itemlist.size() - 1);
    }

    @Override//sobrecargamos el método para que aparezca el menu
    public boolean onCreateOptionsMenu(Menu menu) {//quieres rellenar el menu
        MenuInflater inflater = getMenuInflater();//pides el inflador de menus
        inflater.inflate(R.menu.options, menu);//mete la sopciones que te dan en el menu ahi
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.clear_checked://una opcion del menu
                clearchecked();
                return true;
            case R.id.clear_all://otra opcion del menu
                clearAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearAll() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.confirm_clear_All);
        builder.setPositiveButton(R.string.clear_All, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//si clicamos al boton se borra
                itemlist.clear();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void clearchecked() {//aqui borramos los que estan marcados
        int i=0;
        while (i < itemlist.size()){//recorre todos los elementos de itemlist
            if(itemlist.get(i).isChecked()){//si esta seleccionado
                itemlist.remove(i);//borramos el item por el que pasamos que este marcado(utilizamos un while con el else porke si borramos nos saltaria un indice con el for
            }else{
                i++;
            }

        }
        adapter.notifyDataSetChanged();//si lo borramos que cambie
    }
}


//si queremos que el editext donde escribimos tenga atributos especificos
//como una " si estamos escribiendo un mail o un boton de send en vez
//de return cuando enviamos un mensjae de texto o un return que simplemente
//baje una linea el texto y no envie nada tenemos que recurrir en el text XML
//del layout dentro del editext a : android:imeOptions"actionDone" ñor ejemplo