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
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nicktgn.mvp.IIMvpFragment;
import com.github.nicktgn.mvp.IMvpFragment;
import com.github.nicktgn.mvp.MvpBundle;
import com.github.nicktgn.mvp.annotations.MVPFragmentCompat;
import com.github.nicktgn.mvp_sample.R;
import com.github.nicktgn.mvp_sample.models.NotebooksProvider;
import com.github.nicktgn.mvp_sample.presensters.NotebookPresenter;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */

@MVPFragmentCompat(Fragment.class)
public class NotebookFragment extends Fragment//MvpFragmentCompat<NotebookPresenter.NotebookView, NotebookPresenter>
										implements IMvpFragment<NotebookPresenter>,
													NotebookPresenter.NotebookView,
													NotebookPresenter.NotebookCtx{

	private static final Logger logger = LoggerManager.getLogger(MainActivity.class.getName());

	@BindView(R.id.add_new_note_btn) View mAddNoteBtn;
	@BindView(R.id.notes_list_empty_view) View mNotesListEmptyView;
	@BindView(R.id.notes_list) RecyclerView mNotesList;

	@BindString(R.string.default_note_name) String defaultNoteName;
	@BindString(R.string.default_notebook_name) String defaultNotebookName;
	@BindDimen(R.dimen.notes_grid_spacing) int notesGridSpacing;


	private NotesListAdapter mAdapter;
	private Unbinder unbinder;

	private NotebookPresenter presenter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
									 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_notebook, container, false);

		unbinder = ButterKnife.bind(this, v);

		final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		mNotesList.setLayoutManager(layoutManager);
		mNotesList.addItemDecoration(new SpacesItemDecoration(notesGridSpacing));

		mAdapter = new NotesListAdapter(getContext());
		mNotesList.setAdapter(mAdapter);

		setHasOptionsMenu(true);

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		logger.d("adding actions");
		// Inflate the menu items for use in the action bar
		inflater.inflate(R.menu.menu_notebook, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_new_note:
				getPresenter().createNote(defaultNoteName);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public NotebookPresenter getPresenter() {
		if(presenter == null){
			presenter = new NotebookPresenter(this, NotebooksProvider.getInstance());
		}
		return presenter;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	public <T extends android.app.Fragment & IMvpFragment> T getMvpFragment(Class<T> targetView, MvpBundle arguments) {
		return null;
	}

	@Override
	public <T extends Fragment & IMvpFragment> T getMvpFragmentCompat(Class<T> targetView, MvpBundle arguments) {
		return null;
	}

	@OnClick(R.id.add_new_note_btn)
	public void onAddNewNote(View view) {
		getPresenter().createNote(defaultNoteName);
	}

	@Override
	public String getDefaultNotebookTitle() {
		return defaultNotebookName;
	}


	public class EventShowNotebookTitle{
		public String title;
		public EventShowNotebookTitle(String title){
			this.title = title;
		}
	}

	public class EventOpenNote{
		public NoteFragment noteView;
		public EventOpenNote(NoteFragment noteView){
			this.noteView = noteView;
		}
	}

	@Override
	public void showTitle(String title) {
		EventBus.getDefault().post(new EventShowNotebookTitle(title));
	}

	@Override
	public void showNotes(int numNotes) {
		if(numNotes == 0){
			mNotesList.setVisibility(View.GONE);
			mNotesListEmptyView.setVisibility(View.VISIBLE);
		}
		else {
			mNotesListEmptyView.setVisibility(View.GONE);
			mNotesList.setVisibility(View.VISIBLE);

			// update the number of notebooks in the list and let the adapter do the rest
			mAdapter.update(numNotes);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void gotoNoteView(MvpBundle noteModel) {
		EventBus.getDefault().post(new EventOpenNote(getMvpFragmentCompat(NoteFragment.class, noteModel)));
	}

	public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
		private int space;

		public SpacesItemDecoration(int space) {
			this.space = space;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view,
											RecyclerView parent, RecyclerView.State state) {
			outRect.left = space;
			outRect.bottom = space;

			if(parent.getChildLayoutPosition(view) / 2 == 0){
				outRect.top = space;
			}
			if(parent.getChildLayoutPosition(view) % 2 == 1){
				outRect.right = space;
			}

		}
	}

	class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.SimpleViewHolder> {

		// view holder can act as a presenter's "subview" and implement its
		// interfaces to present each piece of data designated for this "subview"
		public class SimpleViewHolder extends RecyclerView.ViewHolder
			implements NotebookPresenter.NoteView,
			View.OnClickListener{

			public int index;

			@BindView(R.id.title_txt) TextView title;
			@BindView(R.id.content_txt) TextView content;

			public SimpleViewHolder(View itemView) {
				super(itemView);

				ButterKnife.bind(this, itemView);

				itemView.setOnClickListener(this);
			}

			@Override
			public void onClick(View view) {
				getPresenter().openNote(index);
			}

			@Override
			public void showTitle(String title) {
				this.title.setText(title);
			}

			@Override
			public void showContent(String content) {
				this.content.setText(content);
			}
		}

		private Context mContext;
		private int mCount;

		public NotesListAdapter(Context context) {
			this.mContext = context;
		}

		public void update(int count){
			mCount = count;
		}

		@Override
		public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_note, parent, false);
			return new SimpleViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
			viewHolder.index = position;

			// every time this adapter is called to present a list item view,
			// just call presenter to present this list item as "subview" and
			// presenter will provide all necessary data to the viewHolder
			// (if it implements "subview" interface)
			getPresenter().presentSubView(viewHolder, position);
		}

		@Override
		public int getItemCount() {
			return mCount;
		}

	}
}
