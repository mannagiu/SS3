package com.giulia.miapplicazionediprova;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int i;
    int j;
    // definisco un ArrayList con il nome delle cartelle e/o file
    ArrayList<String> listp;


    //ARRAY DI IMMAGINI
    ArrayList<Integer> list_i;
    ;
    ArrayList<String> list_items;
    ArrayList<Myobject> arrayobj;
    int numoggetti;
    JSONArray jsonRootObj = new JSONArray();
    TextView tv;
    FileInputStream stream = null;
    FileOutputStream fOut = null;
    File yourFile = null;
    FileChannel fc;
    MappedByteBuffer bb;
    String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView im = (ImageView) findViewById(R.id.ima);
        im.setImageResource(R.drawable.addfolder);
        final ListView mylist = (ListView) findViewById(R.id.listv);
        listp = new ArrayList<>();
        list_i = new ArrayList<>();
        list_items = new ArrayList<>();
        arrayobj = new ArrayList<>();
        tv = (TextView) findViewById(R.id.view);
        tv.setText("lili");

        Myadapter myAdapter = new Myadapter(MainActivity.this, list_i, listp);

        Myobject o1 = new Myobject("dir p", "dir", false);
        Myobject o2 = new Myobject("dir2", "dir", false);
        Myobject o3 = new Myobject("file1", "file", false);


        //use >  int id = c.getInt("duration"); if you want get an int
        arrayobj.add(o1);
        arrayobj.add(o2);
        arrayobj.add(o3);

        //String[] nameproducts = new String[] { "dir1", "dir2", "dir3","file","dir4" };

        numoggetti = arrayobj.size();

        for (i = 0; i < numoggetti; i++) {
            JSONObject newone = new JSONObject();
            try {
                newone.put("tipo", arrayobj.get(i).getTipo());
                newone.put("name", arrayobj.get(i).getNome());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listp.add(arrayobj.get(i).getNome());

            if (arrayobj.get(i).getTipo() == "dir") {
                list_i.add(R.drawable.folder);
                jsonRootObj.put(newone);
            } else
                list_i.add(R.drawable.file);

        }


        // recupero la lista dal
        // layout
        // creo e istruisco l'adattatore


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listp);

        // inietto i dati
        mylist.setAdapter(myAdapter);

        jsonStr = jsonRootObj.toString();
        //tv.setText(jsonStr);
        //adesso creo il file
        yourFile = new File(Environment.getExternalStorageDirectory(), "Documents/giulia4.json");
        try {

            fOut = new FileOutputStream(yourFile);

            fOut.write(jsonStr.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            stream = new FileInputStream(yourFile);

            try {
                fc = stream.getChannel();
                bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
                tv.setText(jsonStr);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//POSSO FARE LA LETTURA DEL FILE ANCHE COSÌ, forse è anche meglio anzichè usare tutte ste cose strane
/*FileInputStream fin=new FileInputStream();
int c;
String temp="";
while( (c = fin.read()) != -1){
   temp = temp + Character.toString((char)c);
}

//string temp contains all the data of the file.
fin.close();
*/

    }
}