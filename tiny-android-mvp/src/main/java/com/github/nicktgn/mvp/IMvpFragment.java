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


import android.support.v4.app.Fragment;

public interface IMvpFragment extends MvpView {

	<T extends Fragment & IMvpFragment> T getMvpFragmentCompat(Class<T> targetView, MvpBundle arguments);
	<T extends android.app.Fragment & IMvpFragment> T getMvpFragment(Class<T> targetView, MvpBundle arguments);

	<P extends MvpPresenter> P getPresenter();
}
