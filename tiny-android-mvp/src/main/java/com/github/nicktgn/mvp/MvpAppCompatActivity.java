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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import java.lang.reflect.InvocationTargetException;

/**
 * Abstract helper implementation of the View based on {@link AppCompatActivity}. Actual view
 * activities can extend from this class.
 * @author nicktgn
 */
public abstract class MvpAppCompatActivity<V extends MvpView, P extends MvpPresenter>
							extends AppCompatActivity
							implements MvpView {

	private static final Logger logger = LoggerManager.getLogger(MvpAppCompatActivity.class.getName());

	protected P presenter;

	private MvpBundle stateData;
	private MvpBundle argumentsData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = createPresenter();

		// ---- INPUT ARGUMENTS ---- //
		// try to get input model data from Intent (that started this Activity)
		try {
			Bundle bundle = getIntent().getExtras().getBundle(Constants.ARGUMENTS_DATA);
			if(bundle != null){
				argumentsData = new MvpBundle(bundle);
				logger.d("Got some arguments data");
			}
		} catch(NullPointerException e){
			logger.d("No intent data");
		}

		// ---- CACHING ---- //
		// if we have savedInstanceState (activity was re-created)
		if(savedInstanceState != null){
			Bundle bundle = savedInstanceState.getBundle(Constants.CACHED_STATE_DATA);
			if(bundle != null){
				stateData = new MvpBundle(bundle);
				logger.d("Got some cached state data");
			}
		}

	}

	/**
	 * Use this helper method to create an Intent for starting another View (Activity)
	 * with extras containing provided arguments indented for Presenter of this new View
	 * @param arguments arguments from this View's Presenter intended for Presenter of another View
	 * @return intent to another View (Activity)
	 */
	protected Intent getMvpIntent(Class targetView, MvpBundle arguments){
		Intent i = new Intent(this, targetView);
		Bundle bundle = new Bundle();
		bundle.putBundle(Constants.ARGUMENTS_DATA, arguments.getRealBundle());
		i.putExtras(bundle);
		return i;
	}

	/**
	 * Use this helper method to get another View (Fragment)
	 * with extras containing provided arguments indented for Presenter of this new View
	 * @param arguments arguments from this View's Presenter intended for Presenter of another View
	 * @return intent to another View (Activity) (or null if failed to instantiate)
	 */
	protected <T extends Fragment & IMvpFragment> T getMvpFragment(Class<T> targetView, MvpBundle arguments){
		try {
			T fragment = targetView.getConstructor().newInstance();
			Bundle bundle = new Bundle();
			bundle.putBundle(Constants.ARGUMENTS_DATA, arguments.getRealBundle());
			fragment.setArguments(bundle);
			return fragment;
		} catch (java.lang.InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * By default view attaches itself in onStart() and detaches itself in onStop().
	 * You can override this method to change this behaviour and instead to attach in
	 * onResume() and detach in onPause()
	 * @return true to attach in onResume() and detach in onPause()
	 */
	protected boolean attachOnResume(){
		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();
		logger.d("onStart");

		if(!attachOnResume()){
			// View is attached in onStart() or in onResume() because in attachView() we already need all the elements of the View
			// need to be prepared for use (usually done in onCreate()).
			presenter.attachView(this, argumentsData, stateData);
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
		logger.d("onResume");

		if(attachOnResume()){
			// View is attached in onStart() or in onResume() because in attachView() we already need all the elements of the View
			// need to be prepared for use (usually done in onCreate()).
			presenter.attachView(this, argumentsData, stateData);
		}
	}

	@Override
	protected void onPause(){
		logger.d("onPause");

		if(attachOnResume()){
			presenter.detachView(false);
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		logger.d("onStop");

		if(!attachOnResume()){
			presenter.detachView(false);
		}
		super.onStop();
	}

	/**
	 * Creates Presenter instance
	 * @return new Presenter instance
	 */
	abstract protected P createPresenter();

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		logger.d("onSaveInstanceState");
		MvpBundle savedData = presenter.saveState();
		if(savedData != null){
			outState.putBundle(Constants.CACHED_STATE_DATA, savedData.getRealBundle());
		}
		super.onSaveInstanceState(outState);
	}

}
