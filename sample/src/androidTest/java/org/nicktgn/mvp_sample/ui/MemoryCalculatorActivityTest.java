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

package org.nicktgn.mvp_sample.ui;


import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.nicktgn.mvp_sample.ui.MemoryCalculatorActivity;

public class MemoryCalculatorActivityTest extends ActivityInstrumentationTestCase2<MemoryCalculatorActivity>{

	private MemoryCalculatorActivity mActivity;

	public MemoryCalculatorActivityTest() {
		super(MemoryCalculatorActivity.class);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		injectInstrumentation(InstrumentationRegistry.getInstrumentation());
		mActivity = getActivity();
	}



}