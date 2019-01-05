package me.yifeiyuan.flapdev;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.DifferFlapAdapter;
import me.yifeiyuan.flapdev.simpletext.SimpleTextModel;

/**
 * todo support
 * AsyncListDiffer
 * ListAdapter
 */
public class DifferActivity extends AppCompatActivity {

    private static final String TAG = "DifferActivity";

    private DifferFlapAdapter flapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differ);

        RecyclerView recyclerView = findViewById(R.id.rv_items);

        flapAdapter = new DifferFlapAdapter();
        flapAdapter.setDiffItemCallback(new DiffUtil.ItemCallback<SimpleTextModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull final SimpleTextModel simpleTextModel, @NonNull final SimpleTextModel t1) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull final SimpleTextModel simpleTextModel, @NonNull final SimpleTextModel t1) {
                return false;
            }
        });

        List<SimpleTextModel> models = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            models.add(new SimpleTextModel("Android :" + i));
        }

        flapAdapter.setData(models);

        recyclerView.setAdapter(flapAdapter);
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
                flapAdapter.setData(newModels);
            }
        }, 5000);
    }
}
