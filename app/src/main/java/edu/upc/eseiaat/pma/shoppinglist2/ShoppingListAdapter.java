package edu.upc.eseiaat.pma.shoppinglist2;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Usuari on 23/10/2017.
 */

public class ShoppingListAdapter extends ArrayAdapter<ShoppingItem> {
    public ShoppingListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //funcion que llama el listview cuando le quiere pedir al adaptador una de las pastillitas
        //convertView es lo de las escaleras mecanicas
        View result = convertView;
        if (result==null){//si no hay convertview nos tenemos que crear nosotros una de las pastillitas de la lista
            LayoutInflater inflater = LayoutInflater.from(getContext());//el inflador devuelve un view
            result = inflater.inflate(R.layout.shopping_item, null);
        }

        CheckBox checkBox = (CheckBox) result.findViewById(R.id.shopping_item);
        ShoppingItem item = getItem(position);//n la posicion que sea los datos de la lista
        checkBox.setText(item.getText());
        checkBox.setChecked(item.isChecked());//les paso el booleano para saber si esta marcado o no
        return result;
    }
}
