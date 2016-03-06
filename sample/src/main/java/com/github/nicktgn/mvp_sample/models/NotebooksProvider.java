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

package com.github.nicktgn.mvp_sample.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NotebooksProvider implements Parcelable {

	private static NotebooksProvider mInstance = null;

	public static NotebooksProvider getInstance(){
		if(mInstance == null){
			mInstance = new NotebooksProvider();
		}
		return mInstance;
	}

	private ArrayList<NotebookModel> notebooksList;
	private int totalNumNotes;

	public int getTotalNumNotes(){
		totalNumNotes = 0;
		for(NotebookModel n : notebooksList){
			totalNumNotes += n.getNumNotes();
		}
		return totalNumNotes;
	}

	public NotebookModel getNotebook(int index) throws IndexOutOfBoundsException{
		return notebooksList.get(index);
	}

	public int getNumNotebooks(){
		return notebooksList.size();
	}

	public NotebooksProvider addNotebook(NotebookModel notebook){
		notebooksList.add(notebook);
		return this;
	}

	public NotebooksProvider removeNotebook(int index) throws IndexOutOfBoundsException{
		notebooksList.remove(index);
		return this;
	}
	public NotebooksProvider removeNotebook(NotebookModel notebook){
		notebooksList.remove(notebook);
		return this;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(notebooksList);
		dest.writeInt(this.totalNumNotes);
	}

	private NotebooksProvider() {
		totalNumNotes = 0;
		notebooksList = new ArrayList<NotebookModel>();
	}

	protected NotebooksProvider(Parcel in) {
		this.notebooksList = in.createTypedArrayList(NotebookModel.CREATOR);
		this.totalNumNotes = in.readInt();
	}

	public static final Creator<NotebooksProvider> CREATOR = new Creator<NotebooksProvider>() {
		public NotebooksProvider createFromParcel(Parcel source) {
			return new NotebooksProvider(source);
		}

		public NotebooksProvider[] newArray(int size) {
			return new NotebooksProvider[size];
		}
	};
}
