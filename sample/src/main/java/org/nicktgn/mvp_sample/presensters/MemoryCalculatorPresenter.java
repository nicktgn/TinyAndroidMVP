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

package org.nicktgn.mvp_sample.presensters;

import android.os.Bundle;

import org.nicktgn.mvp.MvpBasePresenter;
import org.nicktgn.mvp.MvpView;
import org.nicktgn.mvp_sample.models.MemoryCalculatorModel;

/**
 * Created by nick on 11/30/15.
 */
public class MemoryCalculatorPresenter extends MvpBasePresenter<MemoryCalculatorPresenter.MemoryCalculatorView> {

	public interface MemoryCalculatorView extends MvpView{

	}

	public interface MemoryEntryView extends MvpView{

	}

	private MemoryCalculatorModel mModel;

	@Override
	public Bundle getModelData() {
		return null;
	}

	@Override
	protected void present(Bundle cachedData) {

	}

}
