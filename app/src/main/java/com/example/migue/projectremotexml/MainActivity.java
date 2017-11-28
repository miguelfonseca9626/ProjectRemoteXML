package com.example.migue.projectremotexml;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity { // AppCompatActivity {

    // url
    static final String URL = "http://www.w3schools.com/xml/simple.xml";

    // chaves que identificam os nodos no documento XML
    static final String KEY_ITEM = "breakfast_menu"; // main item
    static final String KEY_FOOD = "food";
    static final String KEY_NAME = "name";
    static final String KEY_PRICE = "price";

    // variável com o reconhecedor XML (parser)
    XMLParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_main);

        // avaliar o estado da ligação de rede...
        ConnectivityManager connMngr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            parser = new XMLParser();
            new DownloadXML().execute(URL); // método definido em baixo
        }

    }

    private void setListView(String xml){

        ArrayList <HashMap<String, String>> itemsMenu  =
                new ArrayList<HashMap<String, String>>();

        // obter o documento relativo ao item principal

        Document doc = parser.getDomElement(xml);
        NodeList nodeList = doc.getElementsByTagName(KEY_FOOD);
        // percorrer todos os nodos <food>
        for(int i=0; i < nodeList.getLength(); i++ ) {
            // construir o HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nodeList.item(i);
            // adicionar ao map, cada nodo dentro de <food>
            map.put(KEY_NAME, parser.getValue(e,KEY_NAME));
            map.put(KEY_PRICE, parser.getValue(e,KEY_PRICE));
            // acrescentar o map ao ArrayList
            itemsMenu.add(map);
        }

        // associar os itemsMenu ao ListView
        ListAdapter adapter = new SimpleAdapter(this,
                itemsMenu,
                R.layout.list_item,
                new String[] {KEY_NAME, KEY_PRICE},
                new int[] {R.id.name, R.id.price}
        );
        setListAdapter(adapter);
        //considerando o seguinte:   public class MainActivity extends ListActivity {
    }



    private class DownloadXML extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls){
            return parser.getXmlFromUrl(urls[0]);
        }

        // @Override
        protected void onProgressUpdate(Integer... progress)        {
            // info sobre progresso da execução da tarefa
        }

        @Override
        protected void onPostExecute(String result){
            setListView(result);  // método definido acima
        }
    }



}