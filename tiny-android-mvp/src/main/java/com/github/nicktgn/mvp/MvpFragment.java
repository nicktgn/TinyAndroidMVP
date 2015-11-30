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

package com.github.nicktgn.mvp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.nicktgn.mvp.android.ContextWrapper;

/**
 * Abstract helper implementation of the View based on {@link Fragment}. Actual view
 * fragments can extend from this class.
 * @author nicktgn
 */
public abstract class MvpFragment<V extends MvpView, P extends MvpPresenter> extends Fragment implements MvpView {

	private static final String MODEL_DATA = "model_data";

	protected P presenter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		presenter = createPresenter();

		Bundle savedData;
		// we have savedInstanceState (activity was re-created)
		if(savedInstanceState != null){
			savedData = savedInstanceState.getBundle(MODEL_DATA);
		}
		// try to restore presenter state from MvpState (cause fragment instance is still
		// the same and only the view was re-created)
		else{
			savedData = MvpState.restoreState(this);
		}

		presenter.attachView(this, savedData, new ContextWrapper(this.getActivity()));
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		// in case the fragment is not destroyed (that is when activity is destroyed or config change)
		//  and only view is recreated, save presenter state in MvpState (cause we retain fragment
		// instance reference)
		Bundle savedData = presenter.getModelData();
		if(savedData != null){
			MvpState.saveState(this, savedData);
		}

		presenter.detachView(getRetainInstance());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Bundle savedData = presenter.getModelData();
		if(savedData != null){
			// TODO: check if the outState bundle is the same instance as the one for activity or is it nested one
			outState.putBundle(MODEL_DATA, savedData);
		}
	}

	abstract protected P createPresenter();
}
