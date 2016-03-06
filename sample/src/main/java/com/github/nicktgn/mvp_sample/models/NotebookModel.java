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

public class NotebookModel implements Parcelable {

	private String title;
	private ArrayList<NoteModel> notes;

	public String getTitle(){
		return title;
	}

	public NotebookModel setTitle(String title){
		this.title = title;
		return this;
	}

	public int getNumNotes(){
		return notes.size();
	}

	public NoteModel getNote(int index) throws IndexOutOfBoundsException{
		return notes.get(index);
	}

	public NotebookModel addNote(NoteModel note){
		notes.add(note);
		return this;
	}

	public NotebookModel removeNote(NoteModel note){
		notes.remove(note);
		return this;
	}

	public NotebookModel removeNote(int index) throws IndexOutOfBoundsException{
		notes.remove(index);
		return this;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeTypedList(notes);
	}

	public NotebookModel(String title) {
		this.title = title;
		notes = new ArrayList<NoteModel>();
	}

	protected NotebookModel(Parcel in) {
		this.title = in.readString();
		this.notes = in.createTypedArrayList(NoteModel.CREATOR);
	}

	public static final Creator<NotebookModel> CREATOR = new Creator<NotebookModel>() {
		public NotebookModel createFromParcel(Parcel source) {
			return new NotebookModel(source);
		}

		public NotebookModel[] newArray(int size) {
			return new NotebookModel[size];
		}
	};
}
