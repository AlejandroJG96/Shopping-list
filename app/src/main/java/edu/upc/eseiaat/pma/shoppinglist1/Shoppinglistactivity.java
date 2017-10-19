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


    }
}
