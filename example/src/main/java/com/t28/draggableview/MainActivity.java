package com.t28.draggableview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.t28.draggablelistview.DraggableListView;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity implements ItemAdapter.OnItemClickListener {
    private DraggableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (DraggableListView) findViewById(R.id.main_container);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new GridLayoutManager(this, 3));

        final List<String> dataSet = Arrays.asList(getResources().getStringArray(R.array.lineups));
        final ItemAdapter adapter = new ItemAdapter(dataSet);
        adapter.setOnItemClickListener(this);
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {
        if (mListView.isDragging()) {
            return;
        }

        mListView.startDrag(view, new DraggableListView.ShadowBuilder(view));
    }
}
