package com.happs.ximand.ringcontrol.view.fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentAddTimetableBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.view.adapter.EditTimetableRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.fragment.AddTimetableViewModel;

import java.util.List;

public class AddTimetableFragment
        extends BaseFragment<AddTimetableViewModel, FragmentAddTimetableBinding> {

    private RecyclerView lessonsRecyclerView;

    public AddTimetableFragment() {
        super(R.layout.fragment_add_timetable, R.menu.menu_toolbar_editing);
    }

    public static AddTimetableFragment newInstance() {
        return new AddTimetableFragment();
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull FragmentAddTimetableBinding binding) {
        lessonsRecyclerView = binding.lessonsRecyclerView;
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull AddTimetableViewModel viewModel) {
        viewModel.getLessonsLiveData().observe(
                getViewLifecycleOwner(), this::initAdapter
        );
        viewModel.getDetailEditingLiveData().observe(getViewLifecycleOwner(), detailed -> {
            EditTimetableRecyclerViewAdapter adapter = getEditTimetableAdapter();
            if (adapter != null) {
                adapter.setDetailEditingStatus(detailed);
            }
        });
        viewModel.setCorrectnessCheck(() -> {
            EditTimetableRecyclerViewAdapter adapter = getEditTimetableAdapter();
            if (adapter != null) {
                return adapter.isAllLinesCorrect();
            }
            return false;
        });
    }

    private EditTimetableRecyclerViewAdapter getEditTimetableAdapter() {
        return (EditTimetableRecyclerViewAdapter) lessonsRecyclerView.getAdapter();
    }

    protected void initAdapter(List<Lesson> lessons) {
        EditTimetableRecyclerViewAdapter adapter =
                new EditTimetableRecyclerViewAdapter(lessons);
        lessonsRecyclerView.setAdapter(adapter);
        getViewModel().getAddLessonEvent().observe(getViewLifecycleOwner(),
                aVoid -> adapter.addEmptyLesson()
        );
        getViewModel().getRemoveLessonEvent().observe(getViewLifecycleOwner(),
                aVoid -> adapter.removeLastLesson()
        );
    }
}
