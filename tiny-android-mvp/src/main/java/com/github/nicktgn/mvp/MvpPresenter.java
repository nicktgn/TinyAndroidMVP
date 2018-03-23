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

import com.github.nicktgn.mvp.annotations.MVPFragmentCompat;

/**
 * Basic Presenter interface
 * @author nicktgn
 */
public interface MvpPresenter<V extends MvpView> {

	/**
	 * Read arguments intended for Presenter. These arguments for example can be passed
	 * between Presenters in different Views through {@link android.content.Intent} that starts
	 * another View (in case if View is {@link android.app.Activity}) or through arguments of
	 * {@link android.support.v4.app.Fragment}, etc. <br/>
	 * Common places to use this:<br/>
	 * <ul>
	 *    <li>In Activities:
	 *    	<ul>
	 *    		<li>{@link android.app.Activity#onCreate(Bundle)}</li>
	 *    	</ul>
	 *    </li>
	 *    <li>In Fragments:
	 *    	<ul>
	 *    	 	<li>{@link android.app.Fragment#onActivityCreated(Bundle)}</li>
	 *    	</ul>
	 *    </li>
	 * </ul
	 * @param arguments {@link Bundle} with arguments
	 */
	//void readArguments(Bundle arguments);


	/**
	 * Get the {@link Bundle} with state data operated by Presenter.
	 * This {@link Bundle} can be used to save/restore Model data between destruction and re-creation of
	 * the Views. <br/>
	 * See source of {@link MvpAppCompatActivity} and {@link MVPFragmentCompat} for
	 * an example of how to save/restore Presenter state. <br/>
	 * Common places to use this:<br/>
	 * <ul>
	 *    <li>In Activities:
	 *    	<ul>
	 *    		<li>{@link android.app.Activity#onSaveInstanceState(Bundle)}</li>
	 *    	</ul>
	 *    </li>
	 *    <li>In Fragments:
	 *    	<ul>
	 *    	 	<li>{@link android.app.Fragment#onDestroyView()} when only Fragment's view is
	 *    	 	destroyed/re-created; use helper class {@link MvpState} to save the state in this case</li>
	 *    		<li>{@link android.app.Fragment#onSaveInstanceState(Bundle)} when the Activity
	 *    		containing the fragment also gets destroyed/re-created</li>
	 *    	</ul>
	 *    </li>
	 * </ul
	 * @return {@link MvpBundle} with cached state data
	 */
	MvpBundle saveState();

	/**
	 * Usually is called after the View has been created and it is ready to be attached to Presenter
	 * to be populated with Model data.<br/>
	 * In this method Presenter decides how to present Model data through View's interface.
	 * Common places to invoke this:<br/>
	 * <ul>
	 *    <li>{@link android.app.Activity#onStart()}; make sure to call
	 *    {@link MvpPresenter#isViewAttached()} first to check that it has not been attached yet.</li>
	 *    <li>{@link android.app.Fragment#onActivityCreated(Bundle)}; make sure to call
	 *    {@link MvpPresenter#isViewAttached()} first to check that it has not been attached yet.</li>
	 * </ul>
	 * @param view View related to this Presenter
	 * @param arguments input arguments (if any)  provided to this Presenter
	 * @param savedState saved state of this Presenter (if any), or input arguments intended for this Presenter (if any)
	 */
	void attachView(V view, MvpBundle arguments, MvpBundle savedState);

	/**
	 * Usually is called before the View is going to be destroyed.<br/>
	 * Common places to invoke this:<br/>
	 * <ul>
	 *    <li>{@link android.app.Activity#onStop()};
	 *    <li>{@link android.app.Fragment#onDestroy()};
	 * </ul>
	 * @param retainInstance indicates whether the View instance will be retained: can be useful in
	 *                       case of Fragments ({@link android.app.Fragment#setRetainInstance(boolean)})
	 */
	void detachView(boolean retainInstance);

	/**
	 * Checks if the View is already attached to this Presenter
	 * @return true if View is attached, false otherwise
	 */
	boolean isViewAttached();
}
