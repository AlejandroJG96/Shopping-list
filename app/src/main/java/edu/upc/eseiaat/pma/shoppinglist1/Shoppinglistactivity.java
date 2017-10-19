package edu.upc.eseiaat.pma.shoppinglist1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
        edit_item = (EditText) findViewById(R.id.edititem);
        itemlist = new ArrayList<>();
        itemlist.add("Patatas");
        itemlist.add("Papel WC");
        itemlist.add("Zanahoria");
        itemlist.add("CopasDanone");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemlist);//this por esta clase, simple list item es el que queremos, itemlist es como hemos llamado a los datos que van a ir dentro







        list.setAdapter(adapter);


    }
}
