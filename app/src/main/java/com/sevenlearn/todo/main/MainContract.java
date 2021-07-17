package com.sevenlearn.todo.main;

import com.sevenlearn.todo.BasePresenter;
import com.sevenlearn.todo.BaseView;
import com.sevenlearn.todo.model.Task;

import java.util.List;

public interface MainContract {

     interface View extends BaseView {
        void showTasks(List<Task> taskList);

        void clearTasks();

        void updateTask(Task task);

        void addTask(Task task);

        void deleteTask(Task task);

        void setEmptyStateVisibility(Boolean visible);
    }

    interface Presenter extends BasePresenter<View> {

         void onDeleteAllBtnClick();

         void onSearch(String q);

         void onTaskItemClick(Task task);


    }

}
