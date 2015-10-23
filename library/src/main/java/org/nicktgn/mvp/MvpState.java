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

import java.util.HashMap;

/**
 * Helper class to save the state of the Presenter in case when we retain the reference to the View
 * instance.
 * @author nicktgn
 */
public class MvpState {

	private static final HashMap<Object, Bundle> mStates = new HashMap<Object, Bundle>();

	/**
	 * Saves state of the Presenter
	 * @param view View instance
	 * @param state state of the Presenter obtained from {@link MvpPresenter#getModelData()}
	 */
	public static void saveState(Object view, Bundle state){
		mStates.put(view, state);
	}

	/**
	 * Returns saved state of the Presenter.
	 * @param view View instance
	 * @return state of the Presenter that can be provided to
	 *         {@link MvpPresenter#attachView(MvpView, Bundle, IContext)} to restore the Presenter's
	 *         state
	 */
	public static Bundle restoreState(Object view){
		return mStates.get(view);
	}

}
