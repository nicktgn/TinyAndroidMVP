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
import android.util.Log;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

/**
 * Abstract helper implementation of the View based on {@link Fragment}. Actual view
 * fragments can extend from this class.
 * @author nicktgn
 */
public abstract class MvpFragment<V extends MvpView, P extends MvpPresenter> extends Fragment implements MvpView {
	private static final Logger logger = LoggerManager.getLogger(MvpAppCompatActivity.class.getName());
	
	protected P presenter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		logger.d("onActivityCreated");
		presenter = createPresenter();

		MvpBundle stateData = null;
		MvpBundle argumentsData = null;

		// ---- INPUT ARGUMENTS ---- //
		try {
			// first try to get input model data from fragment arguments
			// NOTE: you can still skip this convenience mechanism, and inject the data into presenter
			// through presenter's constructor for example

			Bundle bundle = getArguments().getBundle(Constants.ARGUMENTS_DATA);
			if(bundle != null) {
				argumentsData = new MvpBundle(bundle);
				logger.d("Got arguments data from fragment arguments");
			}

			// if noting found in arguments, try to get input model data from Intent
			// (that started Activity hosting this Fragment)
			// NOTE: you can still skip this convenience mechanism, and inject the data into presenter
			// through presenter's constructor for example
			else{
				bundle = getActivity().getIntent().getExtras().getBundle(Constants.ARGUMENTS_DATA);
				if(bundle != null) {
					argumentsData = new MvpBundle(bundle);
					logger.d("Got argumnets data from intent");
				}
			}
		} catch(NullPointerException e){
			logger.d("No arguments data");
		}

		// ---- CACHING ---- //
		// if we have savedInstanceState (activity was re-created)
		if(savedInstanceState != null){
			Bundle bundle = savedInstanceState.getBundle(Constants.CACHED_STATE_DATA);
			if(bundle != null){
				stateData = new MvpBundle(bundle);
				logger.d("Got cached state data from instance state");
			}
		}
		// else this fragment is just created or came back from back stack
		// try to restore presenter state from MvpState (cause fragment instance is still
		// the same and only the view was re-created)
		else{
			stateData = MvpState.restoreState(this);
			if(stateData != null){
				logger.d("Got cached data from mvp fragment state");
			}
		}

		presenter.attachView(this, argumentsData, stateData);
	}

	@Override
	public void onDestroyView() {
		logger.d("onDestroyView");

		// in case the fragment is not destroyed (that is when its returned from back stack)
		//  and only view is recreated, save presenter state in MvpState (cause we retain fragment
		// instance reference)
		MvpBundle savedData = presenter.saveState();
		if(savedData != null){
			MvpState.saveState(this, savedData);
		}

		presenter.detachView(getRetainInstance());

		super.onDestroyView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		MvpBundle savedData = presenter.saveState();
		if(savedData != null){
			outState.putBundle(Constants.CACHED_STATE_DATA, savedData.getRealBundle());
		}
	}

	abstract protected P createPresenter();
}
