package me.yifeiyuan.flapdev;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.Flap;
import me.yifeiyuan.flapdev.gallery.GalleryItemFactory;
import me.yifeiyuan.flapdev.simpleimage.SimpleImageItemFactory;
import me.yifeiyuan.flapdev.simpleimage.SimpleImageModel;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_items);
        initFlap();

        SampleAdapter adapter = new SampleAdapter();

        List<Object> models = mockModels();
        adapter.setModels(models);

//        adapter.registerItemFactory(new SimpleTextItemFactory());

        recyclerView.setAdapter(adapter);
    }

    private List<Object> mockModels() {
        List<Object> models = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            models.add(new SimpleImageModel());
        }

        return models;
    }

    private void initFlap() {
        Flap.getDefault().registerItemFactory(new SimpleImageItemFactory());
        Flap.getDefault().registerItemFactory(new GalleryItemFactory());
    }
}
