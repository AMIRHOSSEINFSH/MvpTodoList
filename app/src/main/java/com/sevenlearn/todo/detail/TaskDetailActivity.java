package com.sevenlearn.todo.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sevenlearn.todo.R;
import com.sevenlearn.todo.main.MainActivity;
import com.sevenlearn.todo.main.MainContract;
import com.sevenlearn.todo.model.AppDatabase;
import com.sevenlearn.todo.model.Task;

import java.util.List;

public class TaskDetailActivity extends AppCompatActivity implements TaskDetailContract.View {

    private int selectedImportance = Task.IMPORTANCE_NORMAL;
    private ImageView lastSelectedImportanceIv;
    private TaskDetailContract.Presenter presenter;
    private EditText taskEt;
    private ImageView deleteBtn;
    static final String EXTRA_KEY_TASK = "task";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        View backBtn = findViewById(R.id.backBtn);
        deleteBtn = findViewById(R.id.deleteTaskBtn);
        taskEt = findViewById(R.id.taskEt);

        Task task=getIntent().getParcelableExtra(EXTRA_KEY_TASK);

        presenter = new TaskDetailPresenter(AppDatabase.getAppDatabase(this).getTaskDao(),
                task);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteTask();
                finish();

            }
        });

        Button saveChangesBtn = findViewById(R.id.saveChangesBtn);
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveChanges(selectedImportance, taskEt.getText().toString());
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View normalImportanceBtn = findViewById(R.id.normalImportanceBtn);
        if (task !=null)
        lastSelectedImportanceIv = normalImportanceBtn.findViewById(R.id.normalImportanceCheckIv);

        View highImportanceBtn = findViewById(R.id.highImportanceBtn);
        highImportanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_HIGH) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.highImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_HIGH;

                    lastSelectedImportanceIv = imageView;
                }
            }
        });
        View lowImportanceBtn = findViewById(R.id.lowImportanceBtn);
        lowImportanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_LOW) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.lowImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_LOW;

                    lastSelectedImportanceIv = imageView;
                }
            }
        });

        normalImportanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_NORMAL) {
                    lastSelectedImportanceIv.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.normalImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_NORMAL;

                    lastSelectedImportanceIv = imageView;
                }
            }
        });


        presenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


    @Override
    public void setDeleteBtnVisibility(boolean visible) {
        deleteBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showTask(Task task) {
        taskEt.setText(task.getTitle());
        //lastSelectedImportanceIv.setImageResource(R.drawable.ic_check_white_24dp);
        ImageView imageView;
        switch (task.getImportance()){
            case Task.IMPORTANCE_HIGH:
                imageView = findViewById(R.id.highImportanceCheckIv);
                imageView.setImageResource(R.drawable.ic_check_white_24dp);
                selectedImportance = Task.IMPORTANCE_HIGH;
                break;
                case Task.IMPORTANCE_NORMAL:
                    imageView = findViewById(R.id.normalImportanceCheckIv);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_NORMAL;
                    break;
                    case Task.IMPORTANCE_LOW:
                        imageView = findViewById(R.id.lowImportanceCheckIv);
                        imageView.setImageResource(R.drawable.ic_check_white_24dp);
                        selectedImportance = Task.IMPORTANCE_LOW;
                        break;
        }

    }

    @Override
    public void showError(String error) {
        Snackbar.make(findViewById(R.id.rootTaskDetail), error, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void returnResult(int resultCode, Task task) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_KEY_TASK, task);
        setResult(resultCode, intent);
        finish();
    }
}
