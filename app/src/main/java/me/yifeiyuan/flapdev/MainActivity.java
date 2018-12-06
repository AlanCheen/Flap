package me.yifeiyuan.flapdev;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flapdev.simpleimage.SimpleImageModel;
import me.yifeiyuan.flapdev.simpleimage.SimpleImageVH;
import me.yifeiyuan.flapdev.simpletext.SimpleTextItem;
import me.yifeiyuan.flapdev.simpletext.SimpleTextModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv_items);

        FlapAdapter adapter = new FlapAdapter();

        long t1 = System.currentTimeMillis();
        adapter.registerItemFactory(new SimpleTextItem.SimpleTextItemFactory())
                .registerItemFactory(new SimpleImageVH.SimpleImageItemFactory());

        long t2 = System.currentTimeMillis();

        Log.e("Flap", "time cost :" + (t2 - t1));

        List<Object> models = new ArrayList<>();

        models.add(new SimpleTextModel("Android"));
        models.add(new SimpleTextModel("Java"));
        models.add(new SimpleTextModel("Kotlin"));

        models.add(new SimpleImageModel());

        adapter.setModels(models);

        recyclerView.setAdapter(adapter);

//        List<Object> models = mockModels();
    }

    private List<Object> mockModels() {

        List<Object> models = new ArrayList<>();
        models.add(new SimpleTextModel("Android"));
        models.add(new SimpleTextModel("Java"));
        models.add(new SimpleTextModel("Kotlin"));

        return models;
    }

}
