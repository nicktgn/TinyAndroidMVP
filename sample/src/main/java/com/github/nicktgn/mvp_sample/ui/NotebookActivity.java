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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.nicktgn.mvp.MvpActivity;
import com.github.nicktgn.mvp_sample.R;
import com.github.nicktgn.mvp_sample.presensters.MainPresenter;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class NotebookActivity extends AppCompatActivity {

	private static final Logger logger = LoggerManager.getLogger(MainActivity.class.getName());

	@Bind(R.id.toolbar) Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notebook);

		ButterKnife.bind(this);

		// we need to register this object to EventBus here first
		// because NotebookFragment controlled by this activity is changing
		// ActionBar title as part of show*() calls @see {@link NotebookFragment#showTitle(String)}
		EventBus.getDefault().register(this);

		// Set a Toolbar to replace the ActionBar.
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Create new fragment and transaction
		Fragment notebookFragment = new NotebookFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.container, notebookFragment);

		// Commit the transaction
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				FragmentManager fm = getSupportFragmentManager();

				// no more fragments in back stack -> go to parent activity
				if(fm.getBackStackEntryCount() == 0){
					Intent upIntent = NavUtils.getParentActivityIntent(this);
					if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
						logger.d("Creating NEW TASK...");
						// This activity is NOT part of this app's task, so create a new task
						// when navigating up, with a synthesized back stack.
						TaskStackBuilder.create(this)
							// Add all of this activity's parents to the back stack
							.addNextIntentWithParentStack(upIntent)
								// Navigate up to the closest parent
							.startActivities();
					} else {
						logger.d("Using SAME TASK...");
						// This activity is part of this app's task, so simply
						// navigate up to the logical parent activity.
						//NavUtils.navigateUpTo(this, upIntent);
						onBackPressed();
					}
				}
				// still has fragments in back stack -> pop to previous fragment
				else{
					fm.popBackStack();
				}

				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// because we unregister this object in onStop() we should also
		// register it again in onStart() in case it is not registered yet
		if(!EventBus.getDefault().isRegistered(this)){
			EventBus.getDefault().register(this);
		}
	}

	@Override
	protected void onStop(){
		EventBus.getDefault().unregister(this);

		super.onStop();
	}

	public void onEvent(NotebookFragment.EventShowNotebookTitle evt){
		getSupportActionBar().setTitle(evt.title);
	}

	public void onEvent(NotebookFragment.EventOpenNote evt){
		// Create new fragment and transaction
		Fragment noteFragment = evt.noteView;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, noteFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
