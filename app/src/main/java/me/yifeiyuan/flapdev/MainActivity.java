package me.yifeiyuan.flapdev;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flapdev.simpleimage.SimpleImageModel;
import me.yifeiyuan.flapdev.simpletext.SimpleTextModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv_items);

        createSimpleTestCase(recyclerView);

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
        adapter.setUseFlapItemPool(true)
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

        return models;
    }

}
