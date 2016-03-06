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

package com.github.nicktgn.mvp;

import java.lang.ref.WeakReference;

/**
 * Created by nick on 1/22/16.
 */
public abstract class MvpBaseCtxPresenter<V extends MvpView, C extends MvpContext>
										 extends MvpBasePresenter<V> {

	WeakReference<C> contextRef;

	public MvpBaseCtxPresenter(C context){
		contextRef = new WeakReference<C>(context);
	}

	/**
	 * Returns the reference to the Context of the View
	 * @return Context interface reference
	 */
	protected C getContext(){
		return contextRef == null ? null : contextRef.get();
	}

}
