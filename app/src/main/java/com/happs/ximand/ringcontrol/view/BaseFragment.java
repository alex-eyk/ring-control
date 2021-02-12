package com.happs.ximand.ringcontrol.view;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.happs.ximand.ringcontrol.BR;
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto;
import com.happs.ximand.ringcontrol.viewmodel.fragment.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public abstract class BaseFragment<VM extends BaseViewModel, B extends ViewDataBinding>
        extends Fragment implements LifecycleOwner {

    protected static final int MENU_NONE = 0;

    private final int layoutId;
    private final int menuResId;
    @Nullable
    private VM viewModel;

    public BaseFragment(int layoutId, int menuResId) {
        this.layoutId = layoutId;
        this.menuResId = menuResId;
    }

    @NonNull
    protected VM getViewModel() {
        if (viewModel == null) {
            throw new IllegalStateException(
                    "viewModel not initialized, check that method is called after onCreate()"
            );
        }
        return viewModel;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (menuResId != MENU_NONE) {
            inflater.inflate(menuResId, menu);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);
        viewModel = createFragmentViewModel();
        getLifecycle().addObserver(viewModel);
    }

    protected VM createFragmentViewModel() {
        Application application = requireActivity().getApplication();
        if (application != null) {
            return new ViewModelProvider(
                    requireActivity(),
                    new ViewModelProvider.AndroidViewModelFactory(application)
            ).get(getGenericClass());
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        B viewDataBinding = DataBindingUtil
                .inflate(inflater, layoutId, container, false);
        onViewDataBindingCreated(viewDataBinding);
        setActionBarTitle();

        onPreViewModelAttaching(getViewModel());
        viewDataBinding.setVariable(BR.viewModel, viewModel);
        viewDataBinding.setLifecycleOwner(getViewLifecycleOwner());

        return viewDataBinding.getRoot();
    }

    protected void setActionBarTitle() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            onSetActionBarTitle(actionBar);
        }
    }

    protected void onSetActionBarTitle(@NonNull ActionBar actionBar) {

    }

    protected void onViewDataBindingCreated(@NonNull B binding) {

    }

    protected void onPreViewModelAttaching(@NonNull VM viewModel) {

    }

    public void onExternalEvent(int eventId) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().getMakeSnackbarEvent().observe(
                getViewLifecycleOwner(), this::showSnackbarBySnackbarDto
        );
    }

    private void showSnackbarBySnackbarDto(SnackbarDto snackbarDto) {
        SnackbarBuilder builder = new SnackbarBuilder(requireView())
                .setDuration(snackbarDto.getDuration());
        if (snackbarDto.getActionResId() != SnackbarDto.ACTION_NONE) {
            builder.setAction(
                    snackbarDto.getActionResId(), snackbarDto.getActionClickListener()
            );
        }
        if (snackbarDto.isFormat()) {
            builder.setFormatText(snackbarDto.getMessageResId(), snackbarDto.getArgs());
        } else {
            builder.setText(snackbarDto.getMessageResId());
        }
        if (snackbarDto.getIconResId() != SnackbarDto.ICON_NONE) {
            builder.setIcon(snackbarDto.getIconResId());
        }
        builder.getSnackbar().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return getViewModel().notifyOptionsMenuItemClicked(item.getItemId());
    }

    public String getDefaultTag() {
        return this.getClass().getSimpleName();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private Class<VM> getGenericClass() {
        return (Class<VM>)
                ((ParameterizedType) Objects.requireNonNull(
                        getClass().getGenericSuperclass())
                ).getActualTypeArguments()[0];
    }
}
