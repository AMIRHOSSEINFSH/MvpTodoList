package com.sevenlearn.todo.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sevenlearn.todo.R;
import com.sevenlearn.todo.detail.TaskDetailActivity;
import com.sevenlearn.todo.model.AppDatabase;
import com.sevenlearn.todo.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View, TaskAdapter.TaskItemEventListener {

    private static final int REQUEST_CODE = 430;
    public static final int RESULT_CODE_ADD_TASK = 1001;
    public static final int RESULT_CODE_DELETE_TASK = 1003;
    public static final int RESULT_CODE_UPDATE_TASK = 1002;
    private static final String EXTRA_KEY_TASK = "task";
    private TaskAdapter taskAdapter;
    private MainContract.Presenter presenter;
    private View emptyState;
    private View deleteAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(AppDatabase.getAppDatabase(this).getTaskDao());
        taskAdapter = new TaskAdapter(this, this);
        deleteAllBtn=findViewById(R.id.deleteAllBtn);
        EditText searchEt = findViewById(R.id.searchEt);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            presenter.onSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDeleteAllBtnClick();
            }
        });

        View view = findViewById(R.id.addNewTaskBtn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, TaskDetailActivity.class), REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.taskListRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(taskAdapter);

        presenter.onAttach(this);
    }


    @Override
    public void showTasks(List<Task> taskList) {
        taskAdapter.setTasks(taskList);
    }

    @Override
    public void clearTasks() {
        taskAdapter.clearItems();
    }

    @Override
    public void updateTask(Task task) {
        taskAdapter.updateItem(task);
    }

    @Override
    public void addTask(Task task) {
        taskAdapter.addItem(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskAdapter.deleteItem(task);
    }

    @Override
    public void setEmptyStateVisibility(Boolean visible) {
        emptyState = findViewById(R.id.emptyState);
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if ( (resultCode == RESULT_CODE_ADD_TASK || resultCode == RESULT_CODE_UPDATE_TASK || resultCode == RESULT_CODE_DELETE_TASK) && data != null) {
                Task task = data.getParcelableExtra(EXTRA_KEY_TASK);
                if (task != null) {
                    if (resultCode == RESULT_CODE_ADD_TASK)
                    taskAdapter.addItem(task);
                    else if(resultCode == RESULT_CODE_UPDATE_TASK)
                        taskAdapter.updateItem(task);
                    else
                        taskAdapter.deleteItem(task);

                    setEmptyStateVisibility(taskAdapter.getItemCount() == 0);
                }
            }

        }
    }

    @Override
    public void onClick(Task task) {
    presenter.onTaskItemClick(task);
    }

    @Override
    public void onLongClick(Task task) {
        Intent intent=new Intent(MainActivity.this,TaskDetailActivity.class);
        intent.putExtra(EXTRA_KEY_TASK,task);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
