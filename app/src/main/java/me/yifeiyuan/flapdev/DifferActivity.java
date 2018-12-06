package me.yifeiyuan.flapdev;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flapdev.simpletext.SimpleTextItem;
import me.yifeiyuan.flapdev.simpletext.SimpleTextModel;

/**
 * todo support AsyncListDiffer
 */
public class DifferActivity extends AppCompatActivity {

    private static final String TAG = "DifferActivity";

    private FlapAdapter flapAdapter;
    private AsyncListDiffer<SimpleTextModel> differ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differ);

        RecyclerView recyclerView = findViewById(R.id.rv_items);

        flapAdapter = new FlapAdapter();
        flapAdapter.registerItemFactory(new SimpleTextItem.SimpleTextItemFactory());

        List<SimpleTextModel> models = new ArrayList<>();

        models.add(new SimpleTextModel("Android"));
        models.add(new SimpleTextModel("Java"));
        models.add(new SimpleTextModel("Kotlin"));

        flapAdapter.setModels(models);

        recyclerView.setAdapter(flapAdapter);

        differ = new AsyncListDiffer<SimpleTextModel>(flapAdapter, new DiffUtil.ItemCallback<SimpleTextModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull final SimpleTextModel simpleTextModel, @NonNull final SimpleTextModel t1) {
                Log.d(TAG, "areItemsTheSame() called with: simpleTextModel = [" + simpleTextModel + "], t1 = [" + t1 + "]");
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull final SimpleTextModel simpleTextModel, @NonNull final SimpleTextModel t1) {
                Log.d(TAG, "areContentsTheSame() called with: simpleTextModel = [" + simpleTextModel + "], t1 = [" + t1 + "]");
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeModels();
    }

    private void changeModels() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<SimpleTextModel> newModels = new ArrayList<>();
                newModels.add(new SimpleTextModel("iOS"));
                newModels.add(new SimpleTextModel("OC"));
                newModels.add(new SimpleTextModel("Kotlin"));
                flapAdapter.setModels(newModels);
                differ.submitList(newModels);
            }
        }, 5000);
    }
}
