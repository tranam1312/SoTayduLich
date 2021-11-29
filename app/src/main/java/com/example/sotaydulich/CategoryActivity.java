package com.example.sotaydulich;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sotaydulich.model.ViTri;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DanhSachAdapter danhSachAdapter;
    private Toolbar toolbar;
    private MyDatabase db;
    private static int  STATUS_OK = 200;
    private ArrayList<ViTri> viTris = new ArrayList<>(); ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.rv_danh_sach);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_danh_muc, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchByLocationOrByDescription(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchByLocationOrByDescription(query);
                    return false;
            }

        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                insert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void  init(){
        danhSachAdapter = new DanhSachAdapter(new onClickItem() {
            @Override
            public void onClick(ViTri viTri) {
                Toast.makeText(getApplicationContext(), "helllo" , Toast.LENGTH_SHORT).show();
                activity(viTri);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(danhSachAdapter);
        getAllData();
    }

    public  void  insert(){
        Intent intent = new Intent(this, ContentActivity.class);
        startActivityForResult(intent,STATUS_OK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STATUS_OK) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                if (result.equals( "Ok")){
                    getAllData();
                }
            }

        }
    }

    public void  getAllData(){
        db =  new MyDatabase(this);
        Cursor cursor = db.getAllData();
        while (cursor.  moveToNext()) {
            String id = cursor.getString(0);
            String diaiem = cursor.getString(1);
            String moTa = cursor.getString(2);
            byte[] uilImg = cursor.getBlob(3);
            viTris.add(new ViTri(id, diaiem, moTa, uilImg));
            Log.d("item", ""+viTris.size());
            danhSachAdapter.setAdpater(viTris);
        }
        db.close();
    }

     public  void activity(ViTri viTri){
        Intent intent = new Intent(this, EditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("vitri",viTri);
        intent.putExtras(bundle);
        startActivity(intent);

     }
     public void searchByLocationOrByDescription(String query){
         Cursor cursor = db.getAllData();
         viTris.clear();
         recyclerView.setAdapter(danhSachAdapter);
         while (cursor.moveToNext()) {
             String id = cursor.getString(0);
             String diaiem = cursor.getString(1);
             String moTa = cursor.getString(2);
             byte[] uilImg = cursor.getBlob(3);
             if (diaiem.contains(query)|| moTa.contains(query)){
                 viTris.add(new ViTri(id, diaiem, moTa, uilImg));
                 Log.d("item", ""+viTris.size());
                 danhSachAdapter.setAdpater(viTris);
             }
         }
    }



}