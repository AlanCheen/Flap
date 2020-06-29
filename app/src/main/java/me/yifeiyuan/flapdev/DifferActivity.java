package me.yifeiyuan.flapdev;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.extensions.DifferFlapAdapter;
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel;

/**
 * Testing for DifferFlapAdapter
 */
public class DifferActivity extends AppCompatActivity {

    private static final String TAG = "DifferActivity";

    private DifferFlapAdapter<SimpleTextModel> flapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differ);

        RecyclerView recyclerView = findViewById(R.id.rv_items);

        flapAdapter = new DifferFlapAdapter<>(new DiffUtil.ItemCallback<SimpleTextModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull final SimpleTextModel simpleTextModel, @NonNull final SimpleTextModel t1) {
                return true;
            }

            @Override
            public boolean areContentsTheSame(@NonNull final SimpleTextModel simpleTextModel, @NonNull final SimpleTextModel t1) {
                return simpleTextModel.equals(t1);
            }

            @Nullable
            @Override
            public Object getChangePayload(@NonNull final SimpleTextModel oldItem, @NonNull final SimpleTextModel newItem) {
                return super.getChangePayload(oldItem, newItem);
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
//        testClear();
    }

    private void testClear() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                flapAdapter.getData().clear(); will throw UnsupportedOperationException
                flapAdapter.setData(new ArrayList<>());
            }
        }, 3000);
    }

    private void changeModels() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<SimpleTextModel> newModels = new ArrayList<>();

                for (int i = 0; i < 20; i++) {
//                    newModels.add(new SimpleTextModel("Android :" + i));
                    newModels.add(new SimpleTextModel("Android :" + (i % 2 == 0 ? i : (666))));
                }

                flapAdapter.setData(newModels);
            }
        }, 5000);
    }
}
