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

import java.lang.ref.WeakReference;

/**
 * Abstract helper implementation of the Presenter. Actual Presenters can extend from this class.
 * Adds a helper {@link MvpBasePresenter#present(Bundle, Bundle)} method to hide the View attachment
 * implementation details from the actual Presenter
 * @author nicktgn
 */
public abstract class MvpBasePresenter<V extends MvpView>
						implements MvpPresenter<V>{

	WeakReference<V> viewRef;
	
	@Override
	public void attachView(V view, MvpBundle arguments, MvpBundle savedState) {
		if(view == null){
			throw new IllegalArgumentException("View should not be null");
		}

		if(!isViewAttached()){
			viewRef = new WeakReference<V>(view);
		}

		present(arguments, savedState);
	}

	/**
	 * Override this method if you need Presenter to read input arguments.
	 * Called after presenter is created, but before View has been attached
	 * View implementation will pass arguments {@link Bundle} from {@link android.content.Intent}
	 * that started this View (in case of Activity) or from arguments of {@link android.support.v4.app.Fragment}
	 * @param arguments Bundle with arguments intended for this Presenter
	 */
	//abstract protected void readArguments(Bundle arguments);

	/**
	 * Called after the View has been attached to this Presenter.
	 * In this method Presenter decides how to present Model data through View's interface.
	 * @param arguments input arguments (if any) provided to this Presenter
	 * @param savedState saved state of Presenter (if any), or input arguments intended for this Presenter (if any)
	 */
	abstract protected void present(MvpBundle arguments, MvpBundle savedState);

	/**
	 * Override this method if you need Presenter to present complex sub Views. Can be used with Adapters, in
	 * particular with {@link android.support.v7.widget.RecyclerView} and its
	 * {@link android.support.v7.widget.RecyclerView.ViewHolder}. See README for the sample on how
	 * to use sub Views with Adapters.
	 * @param view	interface of the sub View to present
	 * @param index index of the sub View
	 */
	public void presentSubView(MvpView view, int index){
		throw new UnsupportedOperationException("Method is not implemented. Override it in your Presenter subclass.");
	}

	/**
	 * Returns the reference to a View attached to this Presenter
	 * @return View
	 */
	protected V getView() {
		return viewRef == null ? null : viewRef.get();
	}

	@Override
	public boolean isViewAttached() {
		return viewRef != null && viewRef.get() != null;
	}

	/**
	 * Use this to get special marked bundle intended for presenter arguments
	 * This is used by Mvp*Activity, MvpFragment to correctly identify if Intent or
	 * fragment arguments contain input data that should be passed to presenter
	 * @return
	 */
	/*
	public static Bundle getPresenterArgumentsBundle(){
		Bundle bundle = new Bundle();
		bundle.putBoolean(HAS_ARGUMENT_MODEL_DATA, true);
		return bundle;
	}*/

	/**
	 *
	 * @param retainInstance indicates whether the View instance will be retained: can be useful in
	 */
	@Override
	public void detachView(boolean retainInstance) {
		if (viewRef != null) {
			viewRef.clear();
			viewRef = null;
		}
	}

}
