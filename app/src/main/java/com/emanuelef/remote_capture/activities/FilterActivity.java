package com.emanuelef.remote_capture.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.emanuelef.remote_capture.R;
import com.emanuelef.remote_capture.model.ConnectionDescriptor.Status;
import com.emanuelef.remote_capture.model.FilterDescriptor;
import com.google.android.material.chip.Chip;

public class FilterActivity extends AppCompatActivity {
    public static final String FILTER_DESCRIPTOR = "filter";
    private static final String TAG = "FilterActivity";
    private FilterDescriptor mFilter;
    private CheckBox mShowMasked;
    private Chip mStatusOpen;
    private Chip mStatusClosed;
    private Chip mStatusUnreachable;
    private Chip mStatusError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_filter_dialog);
        setTitle(R.string.edit_filter);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }

        Intent intent = getIntent();
        if(intent != null) {
            FilterDescriptor desc = (FilterDescriptor)intent.getSerializableExtra(FILTER_DESCRIPTOR);
            if(desc != null)
                mFilter = desc;
        }
        if(mFilter == null)
            mFilter = new FilterDescriptor();

        mShowMasked = findViewById(R.id.show_masked);
        mStatusOpen = findViewById(R.id.status_open);
        mStatusClosed = findViewById(R.id.status_closed);
        mStatusUnreachable = findViewById(R.id.status_unreachable);
        mStatusError = findViewById(R.id.status_error);

        model2view();
    }

    private void model2view() {
        mShowMasked.setChecked(mFilter.showMasked);

        Chip selected_status = null;
        switch(mFilter.status) {
            case STATUS_OPEN: selected_status = mStatusOpen; break;
            case STATUS_CLOSED: selected_status = mStatusClosed; break;
            case STATUS_UNREACHABLE: selected_status = mStatusUnreachable; break;
            case STATUS_ERROR: selected_status = mStatusError; break;
        }
        if(selected_status != null)
            selected_status.setChecked(true);
    }

    private void view2model() {
        mFilter.showMasked = mShowMasked.isChecked();

        if(mStatusOpen.isChecked())
            mFilter.status = Status.STATUS_OPEN;
        else if(mStatusClosed.isChecked())
            mFilter.status = Status.STATUS_CLOSED;
        else if(mStatusUnreachable.isChecked())
            mFilter.status = Status.STATUS_UNREACHABLE;
        else if(mStatusError.isChecked())
            mFilter.status = Status.STATUS_ERROR;
        else
            mFilter.status = Status.STATUS_INVALID;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_filter_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.apply_changes) {
            view2model();
            Intent intent = new Intent();
            intent.putExtra(FILTER_DESCRIPTOR, mFilter);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}