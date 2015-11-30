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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by nick on 11/30/15.
 */
public class MemoryCalculatorModel implements Parcelable {
	private int size;
	private ArrayList<String> calculations;
	private ArrayList<String> results;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.size);
		dest.writeStringList(this.calculations);
		dest.writeStringList(this.results);
	}

	public MemoryCalculatorModel(int size) {
		this.size = size;
		calculations = new ArrayList<String>(size);
		results = new ArrayList<String>(size);
	}

	protected MemoryCalculatorModel(Parcel in) {
		this.size = in.readInt();
		this.calculations = in.createStringArrayList();
		this.results = in.createStringArrayList();
	}

	public static final Creator<MemoryCalculatorModel> CREATOR = new Creator<MemoryCalculatorModel>() {
		public MemoryCalculatorModel createFromParcel(Parcel source) {
			return new MemoryCalculatorModel(source);
		}

		public MemoryCalculatorModel[] newArray(int size) {
			return new MemoryCalculatorModel[size];
		}
	};
}
