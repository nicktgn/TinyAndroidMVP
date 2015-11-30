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
 * Adds a helper {@link MvpBasePresenter#present(Bundle, IContext)} method to hide the View attachment
 * implementation details from the actual Presenter
 * @author nicktgn
 */
public abstract class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V>{

	private WeakReference<V> viewRef;
	private WeakReference<IContext> contextRef;

	@Override
	abstract public Bundle getModelData();

	@Override
	public void attachView(V view, Bundle cachedData, IContext context) {
		viewRef = new WeakReference<V>(view);
		contextRef = new WeakReference<IContext>(context);

		present(cachedData);
	}

	/**
	 * Called after the View has been attached to this Presenter.
	 * In this method Presenter decides how to present Model data through View's interface.
	 * @param cachedData saved state of Presenter
	 */
	abstract protected void present(Bundle cachedData);

	/**
	 * Override this if you need Presenter to present complex sub Views. Can be used with Adapters, in
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
	 * Returns the reference to the Context of the View
	 * @return Context interface reference
	 */
	protected IContext getContext(){
		return contextRef == null ? null : contextRef.get();
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

	@Override
	public void detachView(boolean retainInstance) {
		if (viewRef != null) {
			viewRef.clear();
			viewRef = null;
		}
		if (contextRef != null){
			contextRef.clear();
			contextRef = null;
		}
	}
}
