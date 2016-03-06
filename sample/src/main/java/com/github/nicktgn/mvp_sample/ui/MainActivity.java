/*
 * Copyright 2015 Nick Tsygankov (nicktgn@gmail.com)
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nicktgn.mvp.MvpAppCompatActivity;
import com.github.nicktgn.mvp.MvpBundle;
import com.github.nicktgn.mvp_sample.R;
import com.github.nicktgn.mvp_sample.models.NotebooksProvider;
import com.github.nicktgn.mvp_sample.presensters.MainPresenter;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MvpAppCompatActivity<MainPresenter.MainView, MainPresenter>
									implements MainPresenter.MainView {

	private static final Logger logger = LoggerManager.getLogger(MainActivity.class.getName());

	@Bind(R.id.notebooks_list) RecyclerView mNotebooksList;
	@Bind(R.id.num_notebooks_txt) TextView mNumNotebooksTxt;
	@Bind(R.id.total_num_notes_txt) TextView mTotalNumNotesTxt;
	@Bind(R.id.notebooks_list_empty_view) View mNotebooksListEmptyView;

	@Bind(R.id.add_new_notebook_btn) View mAddNotebookBtn;

	@Bind(R.id.toolbar) Toolbar mToolbar;

	private NotebooksListAdapter mAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		// Set a Toolbar to replace the ActionBar.
		setSupportActionBar(mToolbar);

		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		mNotebooksList.setLayoutManager(layoutManager);

		mAdapter = new NotebooksListAdapter(this);
		mNotebooksList.setAdapter(mAdapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_new_notebook:
				presenter.createNotebook();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();

		logger.d("on Destroy");
	}

	@Override
	protected MainPresenter createPresenter() {
		return new MainPresenter(NotebooksProvider.getInstance());
	}


	@Override
	public void showNumOfNotebooks(int numOfNotebooks) {
		mNumNotebooksTxt.setText(Integer.toString(numOfNotebooks));
	}

	@Override
	public void showNumOfNotes(int numOfNotes) {
		mTotalNumNotesTxt.setText(Integer.toString(numOfNotes));
	}

	@Override
	public void showNotebooksList(int numOfNotebooks) {
		if(numOfNotebooks == 0){
			mNotebooksList.setVisibility(View.GONE);
			mNotebooksListEmptyView.setVisibility(View.VISIBLE);
		}
		else {
			mNotebooksListEmptyView.setVisibility(View.GONE);
			mNotebooksList.setVisibility(View.VISIBLE);

			// update the number of notebooks in the list and let the adapter do the rest
			mAdapter.update(numOfNotebooks);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void gotoNotebookView(MvpBundle notebook) {
		startActivity(getMvpIntent(NotebookActivity.class, notebook));
	}

	@OnClick(R.id.add_new_notebook_btn)
	public void onAddNewNotebook(View view) {
		// Just call presenter action.
		// Because presenter deals with business logic, ideally presenter should be in
		// charge of handling routing between activities. It is preferable especially in case
		// when current presenter passes some data to the presenter in the next activity.
		// But if there is no interaction between presenters involved (and routing between
		// two activities in question is not essential to the presenter's logic), activity
		// can start other activity directly as well.
		// See javadoc for this function
		presenter.createNotebook();
	}


	class NotebooksListAdapter extends RecyclerView.Adapter<NotebooksListAdapter.SimpleViewHolder> {

		// view holder can act as a presenter's "subview" and implement its
		// interfaces to present each piece of data designated for this "subview"
		public class SimpleViewHolder extends RecyclerView.ViewHolder
												implements MainPresenter.NotebookView,
																View.OnClickListener{
			public int index;

			@Bind(R.id.title_txt) TextView title;
			@Bind(R.id.num_notes_txt)TextView numNotes;

			public SimpleViewHolder(View itemView) {
				super(itemView);

				ButterKnife.bind(this, itemView);

				itemView.setOnClickListener(this);
			}

			@Override
			public void showNotebookTitle(String title) {
				this.title.setText(title);
			}

			@Override
			public void showNumOfNotes(int numOfNotes) {
				this.numNotes.setText(Integer.toString(numOfNotes));
			}

			@Override
			public void onClick(View view) {
				presenter.openNotebook(index);

			}
		}

		private Context mContext;
		private int mCount;

		public NotebooksListAdapter(Context context) {
			this.mContext = context;
		}

		public void update(int count){
			mCount = count;
		}

		@Override
		public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_notebook, parent, false);
			return new SimpleViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
			viewHolder.index = position;

			// every time this adapter is called to present a list item view,
			// just call presenter to present this list item as "subview" and
			// presenter will provide all necessary data to the viewHolder
			// (if it implements "subview" interface)
			presenter.presentSubView(viewHolder, position);
		}

		@Override
		public int getItemCount() {
			return mCount;
		}

	}
}
