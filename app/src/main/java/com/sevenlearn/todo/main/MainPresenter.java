package com.sevenlearn.todo.main;

import com.sevenlearn.todo.model.Task;
import com.sevenlearn.todo.model.TaskDao;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private TaskDao taskDao;
    private List<Task> taskList;
    private MainContract.View view;

    public MainPresenter(TaskDao taskDao) {
        this.taskDao = taskDao;
        this.taskList = taskDao.getAll();
    }

    @Override
    public void onDeleteAllBtnClick() {
        taskDao.deleteAll();
        view.clearTasks();
        view.setEmptyStateVisibility(true);
    }

    @Override
    public void onSearch(String q) {
        List<Task> taskList;
        if (!q.isEmpty()){
            taskList = taskDao.search(q);
        }
        else{
            taskList=taskDao.getAll();
        }

        view.showTasks(taskList);

    }

    @Override
    public void onTaskItemClick(Task task) {
        task.setCompleted(!task.isCompleted());
        int result = taskDao.update(task);
        if (result > 0)
            view.updateTask(task);

    }

    @Override
    public void onAttach(MainContract.View view) {
        this.view = view;

        if (!taskList.isEmpty()) {
            view.showTasks(taskList);
            view.setEmptyStateVisibility(false);
        } else
            view.setEmptyStateVisibility(true);
    }

    @Override
    public void onDetach() {
        view = null;
    }
}
