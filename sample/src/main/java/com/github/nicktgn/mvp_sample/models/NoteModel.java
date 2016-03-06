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

public class NoteModel implements Parcelable {

	private String title;
	private String content;

	public String getTitle(){
		return title;
	}
	public String getContent(){
		return content;
	}

	public NoteModel setTitle(String title){
		this.title = title;
		return this;
	}

	public NoteModel setContent(String content){
		this.content = content;
		return this;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeString(this.content);
	}

	public NoteModel(String title) {
		this.title = title;
		this.content = "";
	}

	protected NoteModel(Parcel in) {
		this.title = in.readString();
		this.content = in.readString();
	}

	public static final Creator<NoteModel> CREATOR = new Creator<NoteModel>() {
		public NoteModel createFromParcel(Parcel source) {
			return new NoteModel(source);
		}

		public NoteModel[] newArray(int size) {
			return new NoteModel[size];
		}
	};
}
