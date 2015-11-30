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

package com.github.nicktgn.mvp.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.nicktgn.mvp.IContext;

import java.lang.ref.WeakReference;

/**
 * A Wrapper class to hide the real Context Object from Presenters.
 * The main reason of doing this is to reduce the Presenters' dependency on Android SDK
 * so that we can mock the Context during Unit Test
 * @author nicktgn
 */
public class ContextWrapper implements IContext {
	WeakReference<Context> contextRef;

	/**
	 * Construct a new ContextWrapper from Android Context
	 * @param context
	 */
	public ContextWrapper(Context context) {
		  contextRef = new WeakReference<Context>(context);
	}

	@Override
	public void startActivity(Class<?> dest, Bundle data) {
		Intent intent = new Intent(contextRef.get(), dest);
		if (data != null) {
			intent.putExtras(data);
		}

		if (!(contextRef.get() instanceof Activity)) {
			// System will throw an Exception if you try to call startActivity from outside an Activity without having the flag
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		contextRef.get().startActivity(intent);
	}
}
