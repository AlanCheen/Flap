package me.yifeiyuan.flapdev;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.ext.FlapDiffAdapter;
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel;

/**
 * Testing for DifferFlapAdapter
 */
public class DifferActivity extends AppCompatActivity {

    private static final String TAG = "DifferActivity";

    private FlapDiffAdapter<SimpleTextModel> flapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differ);

        RecyclerView recyclerView = findViewById(R.id.rv_items);

        flapAdapter = new FlapDiffAdapter<>(new DiffUtil.ItemCallback<SimpleTextModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull final SimpleTextModel simpleTextModel, @NonNull final SimpleTextModel t1) {
                return true;
            }

            @Override
            public boolean areContentsTheSame(@NonNull final SimpleTextModel simpleTextModel, @NonNull final SimpleTextModel t1) {
                Log.d(TAG, "areContentsTheSame() called with: simpleTextModel = [" + simpleTextModel + "], t1 = [" + t1 + "]");
                return false;
            }

            @Nullable
            @Override
            public Object getChangePayload(@NonNull final SimpleTextModel oldItem, @NonNull final SimpleTextModel newItem) {
                return newItem.content;
            }
        });

        List<SimpleTextModel> models = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            models.add(new SimpleTextModel("Android :" + i));
        }

        flapAdapter.submitList(models);

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

                for (int i = 0; i < 6; i++) {
//                    newModels.add(new SimpleTextModel("Android :" + i));
                    newModels.add(new SimpleTextModel("Android :" + (i % 2 == 0 ? i : (666))));
                }

                flapAdapter.submitList(newModels, () -> Log.d(TAG, "submitList run() called"));
                flapAdapter.notifyItemChanged(3,"ssssss ");
            }
        }, 2000);
    }

//    private void changeModels2() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                for (SimpleTextModel datum : flapAdapter.getData()) {
//                    datum.content = "asdf";
//                }
//
//                flapAdapter.notifyDataSetChanged();
////
////                List<SimpleTextModel> newModels = new ArrayList<>();
////
////                for (int i = 0; i < 20; i++) {
//////                    newModels.add(new SimpleTextModel("Android :" + i));
////                    newModels.add(new SimpleTextModel("Android :" + (i % 2 == 0 ? i : (666))));
////                }
////
////                flapAdapter.setData(newModels);
//            }
//        }, 2000);
//    }
}
