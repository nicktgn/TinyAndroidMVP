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

package org.nicktgn.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.nicktgn.mvp.android.ContextWrapper;

/**
 * Abstract helper implementation of the View based on {@link AppCompatActivity}. Actual view
 * activities can extend from this class.
 * @author nicktgn
 */
public abstract class MvpAppCompatActivity<V extends MvpView, P extends MvpPresenter> extends AppCompatActivity implements MvpView {

	private static final String MODEL_DATA = "model_data";

	protected P presenter;

	private Bundle savedData;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = createPresenter();

		savedData = null;
		if(savedInstanceState != null){
			savedData = savedInstanceState.getBundle(MODEL_DATA);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// View is attached in onStart() because in attachView we already need all the elements of the View
		// need to be prepared for use (usually done in onCreate()).
		// Because we attach in onStart() but detach in onDestroy(), it is also useful to check if
		// the view has not been attached yet, as the activity can be stopped and started again without
		// destroying it in the process.
		if(!presenter.isViewAttached()) {
			presenter.attachView(this, savedData, new ContextWrapper(this));
		}
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		presenter.detachView(false);
	}

	/**
	 * Creates Presenter instance
	 * @return new Presenter instance
	 */
	abstract protected P createPresenter();

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Bundle savedData = presenter.getModelData();
		if(savedData != null){
			outState.putBundle(MODEL_DATA, savedData);
		}
		super.onSaveInstanceState(outState);
	}

}
