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

package com.github.nicktgn.mvp_sample.models;

public class NotebooksStorageManager {

	private static NotebooksStorageManager mInstance = null;

	private NotebooksStorageManager(){

	}

	public static NotebooksStorageManager getInstance(){
		if(mInstance == null){
			mInstance = new NotebooksStorageManager();
		}
		return mInstance;
	}

	private NotebooksProvider mainModel;

	public NotebooksProvider getMainModel(){
		return mainModel;
	}

	public void addNotebook(){

	}

}
