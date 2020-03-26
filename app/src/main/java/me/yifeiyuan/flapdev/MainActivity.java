package me.yifeiyuan.flapdev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flapdev.components.customviewtype.CustomModel;
import me.yifeiyuan.flapdev.components.databindingsample.SimpleDataBindingModel;
import me.yifeiyuan.flapdev.components.generictest.GenericModel;
import me.yifeiyuan.flapdev.components.simpleimage.SimpleImageModel;
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv_items);

//        createSimpleTestCase(recyclerView);
        createAdvanceTestCase(recyclerView);
    }

    private void createSimpleTestCase(final RecyclerView recyclerView) {
        FlapAdapter adapter = new FlapAdapter();

        List<Object> models = new ArrayList<>();

        models.add(new SimpleTextModel("Android"));
        models.add(new SimpleTextModel("Java"));
        models.add(new SimpleTextModel("Kotlin"));

        models.add(new SimpleImageModel());
        models.add(new SimpleImageModel());
        models.add(new SimpleImageModel());
        models.add(new SimpleImageModel());

        adapter.setData(models);

        recyclerView.setAdapter(adapter);
    }

    private void createAdvanceTestCase(final RecyclerView recyclerView) {

        List<Object> models = mockModels();

        FlapAdapter adapter = new FlapAdapter();
        adapter.setUseComponentPool(true)
                .setLifecycleEnable(true)
                .setLifecycleOwner(this)
                .setData(models);

        recyclerView.setAdapter(adapter);
    }

    private List<Object> mockModels() {

        List<Object> models = new ArrayList<>();
        models.add(new SimpleTextModel("Android"));
        models.add(new SimpleTextModel("Java"));
        models.add(new SimpleTextModel("Kotlin"));

        models.add(new SimpleImageModel());

        models.add(new CustomModel());
        models.add(new GenericModel());
        models.add(new SimpleDataBindingModel());

        return models;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.differ:
                startActivity(new Intent(MainActivity.this,DifferActivity.class));
                break;

            case R.id.kotlin:
                startActivity(new Intent(MainActivity.this,KotlinTestActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
