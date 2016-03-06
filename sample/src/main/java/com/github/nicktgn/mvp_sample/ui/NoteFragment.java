/*
 * Copyright 2016 Nick Tsygankov (nicktgn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright 2016 Nick Tsygankov (nicktgn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nicktgn.mvp_sample.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nicktgn.mvp.MvpFragment;
import com.github.nicktgn.mvp_sample.R;
import com.github.nicktgn.mvp_sample.models.NotebooksProvider;
import com.github.nicktgn.mvp_sample.presensters.NotePresenter;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends MvpFragment<NotePresenter.NoteView, NotePresenter>
	implements NotePresenter.NoteView,
	NotePresenter.NoteCtx{


	private static final Logger logger = LoggerManager.getLogger(MainActivity.class.getName());

	@Bind(R.id.title_edit)
	EditText mTitleEdit;
	@Bind(R.id.content_edit)
	EditText mContentEdit;
	@Bind(R.id.edit_tools)
	Toolbar mEditTools;
	@Bind(R.id.edit_tools_menu)
	ActionMenuView mEditToolsMenu;

	@BindString(R.string.default_note_name)
	String mDefaultNoteNameRes;
	@BindInt(R.integer.default_max_editing_history_size)
	int mDefaultMaxEditingHistorySize;

	public NoteFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
									 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_note, container, false);

		ButterKnife.bind(this, v);

		mEditTools.setTitle(null);

		getActivity().getMenuInflater().inflate(R.menu.menu_note_toolbar, mEditToolsMenu.getMenu());
		mEditToolsMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				return onOptionsItemSelected(item);
			}
		});

		return v;
	}

	@Override
	public void onStart(){
		super.onStart();
		// we bind ButterKnife bindings in onCreateView()
		// but attach presenter in onActivityCreated() (which is called after onCreateView())
		// thus we need to attach changes listener even after we attach this View to Presenter
		// so that it does not mess up the logic of Presenter
		// Also we need to detach this listener whenever we "Undo" or "Redo" cause this listener
		// will be triggered on those actions too and it will mess up the edit history.
		mContentEdit.addTextChangedListener(contentWatcher);
 	}

	private TextWatcher contentWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		@Override
		public void afterTextChanged(Editable editable) {}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			logger.d("TEXT CHANGED: " + s + " (" + start + ", " + before + ", " + count + ")");
			presenter.saveContent(s.toString());
		}
	};

	private TextWatcher cursorWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		@Override
		public void afterTextChanged(Editable s) {}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			mContentEdit.setSelection(start + count);
		}
	};

	// we don't care about the timing of the title changes
	// so might as well use ButterKnife binding for this one
	@OnTextChanged(R.id.title_edit)
	public void onTitleChanged(CharSequence s) {
		presenter.saveTitle(s.toString());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(mContentEdit.getWindowToken(), 0);
				break;
			case R.id.action_undo:
				mContentEdit.removeTextChangedListener(contentWatcher);
				mContentEdit.addTextChangedListener(cursorWatcher);
				presenter.undoContentEdit();
				mContentEdit.removeTextChangedListener(cursorWatcher);
				mContentEdit.addTextChangedListener(contentWatcher);
				break;
			case R.id.action_redo:
				mContentEdit.removeTextChangedListener(contentWatcher);
				mContentEdit.addTextChangedListener(cursorWatcher);
				presenter.redoContentEdit();
				mContentEdit.addTextChangedListener(contentWatcher);
				mContentEdit.removeTextChangedListener(cursorWatcher);
				break;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	protected NotePresenter createPresenter() {
		return new NotePresenter(this, NotebooksProvider.getInstance());
	}

	@Override
	public void showTitle(String title) {
		mTitleEdit.setText(title);
	}

	@Override
	public void showContent(String content) {
		mContentEdit.setText(content);
	}

	@Override
	public void startEditingTitle() {
		mTitleEdit.requestFocus();
		((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
			.showSoftInput(mTitleEdit, 0);
	}

	@Override
	public void startEditingContent() {
		mContentEdit.requestFocus();
		((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
			.showSoftInput(mContentEdit, 0);
	}

	@Override
	public void enableUndo(boolean enable) {
		MenuItem undo = mEditToolsMenu.getMenu().findItem(R.id.action_undo);
		undo.setEnabled(enable);
		undo.setIcon(enable ? R.drawable.ic_undo_white_24dp : R.drawable.ic_undo_grey_700_24dp);
	}

	@Override
	public void enableRedo(boolean enable) {
		MenuItem redo = mEditToolsMenu.getMenu().findItem(R.id.action_redo);
		redo.setEnabled(enable);
		redo.setIcon(enable ? R.drawable.ic_redo_white_24dp : R.drawable.ic_redo_grey_700_24dp);
	}


	@Override
	public String getDefaultTitleRes() {
		return mDefaultNoteNameRes;
	}

	@Override
	public int getDefaultMaxHistorySize() {
		return mDefaultMaxEditingHistorySize;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}


}
