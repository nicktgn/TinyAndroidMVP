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

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MvpBundle {

	private Bundle bundle = null;

	private Map<String, Object> stub = null;

	public MvpBundle(){
		bundle = new Bundle();
		try{
			bundle.putInt("test", 1);
		} catch (Exception e){
			bundle = null;
			stub = new HashMap<String, Object>();
		}
	}

	public MvpBundle(Bundle bundle){
		if(bundle == null){
			throw new IllegalArgumentException("Input Bundle can not be null. Consider using default constructor instead.");
		}
		this.bundle = bundle;
		try{
			this.bundle.putInt("test", 1);
		} catch (Exception e){
			this.bundle = null;
			stub = new HashMap<String, Object>();
		}
	}

	public Bundle getRealBundle(){
		return bundle;
	}

	public int describeContents(){
		return 0;
	}

	@TargetApi(18)
	public IBinder getBinder(String key){
		if(bundle != null){
			return bundle.getBinder(key);
		}
		try{
			return (IBinder) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public Bundle getBundle(String key){
		if(bundle != null){
			return bundle.getBundle(key);
		}
		try{
			return (Bundle) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public byte getByte(String key){
		if(bundle != null){
			return bundle.getByte(key);
		}
		try{
			return (byte) stub.get(key);
		} catch(Exception e) {
			return 0;
		}
	}

	public Byte getByte(String key, byte defaultValue){
		if(bundle != null){
			return bundle.getByte(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (Byte) (tmp != null ? tmp : defaultValue);
		} catch(Exception e) {
			return defaultValue;
		}
	}

	public byte[] getByteArray(String key){
		if(bundle != null){
			return bundle.getByteArray(key);
		}
		try{
			return (byte[]) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public char getChar(String key){
		if(bundle != null){
			return bundle.getChar(key);
		}
		try{
			return (char) stub.get(key);
		} catch(Exception e) {
			return 0;
		}
	}

	public char getChar(String key, char defaultValue){
		if(bundle != null){
			return bundle.getChar(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (char) (tmp != null ? tmp : defaultValue);
		} catch(Exception e) {
			return defaultValue;
		}
	}

	public char[] getCharArray(String key){
		if(bundle != null){
			return bundle.getCharArray(key);
		}
		try{
			return (char[]) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public CharSequence getCharSequence(String key, CharSequence defaultValue){
		if(bundle != null){
			return bundle.getCharSequence(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (CharSequence) (tmp != null ? tmp : defaultValue);
		} catch(Exception e) {
			return defaultValue;
		}
	}

	public CharSequence getCharSequence(String key){
		if(bundle != null){
			return bundle.getCharSequence(key);
		}
		try{
			return (CharSequence) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public CharSequence[] getCharSequenceArray(String key){
		if(bundle != null){
			return bundle.getCharSequenceArray(key);
		}
		try{
			return (CharSequence[]) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public ArrayList<CharSequence> getCharSequenceArrayList(String key){
		if(bundle != null){
			return bundle.getCharSequenceArrayList(key);
		}
		try{
			return (ArrayList<CharSequence>) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public ClassLoader getClassLoader(){
		return this.getClassLoader();
	}

	public float getFloat(String key){
		if(bundle != null){
			return bundle.getFloat(key);
		}
		try{
			return (float) stub.get(key);
		} catch(Exception e) {
			return 0.0f;
		}
	}

	float	getFloat(String key, float defaultValue){
		if(bundle != null){
			return bundle.getFloat(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (float) (tmp != null ? tmp : defaultValue);
		} catch(Exception e) {
			return defaultValue;
		}
	}

	public float[] getFloatArray(String key){
		if(bundle != null){
			return bundle.getFloatArray(key);
		}
		try{
			return (float[]) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public ArrayList<Integer> getIntegerArrayList(String key){
		if(bundle != null){
			return bundle.getIntegerArrayList(key);
		}
		try{
			return (ArrayList<Integer>) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public <T extends Parcelable> T getParcelable(String key){
		if(bundle != null){
			return bundle.getParcelable(key);
		}
		try{
			return (T) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public Parcelable[] getParcelableArray(String key){
		if(bundle != null){
			return bundle.getParcelableArray(key);
		}
		try{
			return (Parcelable[]) stub.get(key);
		} catch(Exception e) {
			return null;
		}
	}

	public <T extends Parcelable> ArrayList<T> getParcelableArrayList(String key){
		if(bundle != null){
			return bundle.getParcelableArrayList(key);
		}
		try{
			return (ArrayList<T>) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	public Serializable getSerializable(String key){
		if(bundle != null){
			return bundle.getSerializable(key);
		}
		try{
			return (Serializable) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	public short getShort(String key){
		if(bundle != null){
			return bundle.getShort(key);
		}
		try{
			return (short) stub.get(key);
		} catch(Exception e){
			return 0;
		}
	}

	public short getShort(String key, short defaultValue){
		if(bundle != null){
			return bundle.getShort(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (short) (tmp != null ? tmp : defaultValue);
		} catch(Exception e){
			return defaultValue;
		}
	}

	public short[] getShortArray(String key){
		if(bundle != null){
			return bundle.getShortArray(key);
		}
		try{
			return (short[]) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	@TargetApi(21)
	public Size	getSize(String key){
		if(bundle != null){
			return bundle.getSize(key);
		}
		try{
			return (Size) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	@TargetApi(21)
	public SizeF getSizeF(String key){
		if(bundle != null){
			return bundle.getSizeF(key);
		}
		try{
			return (SizeF) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	public <T extends Parcelable> SparseArray<T>	getSparseParcelableArray(String key){
		if(bundle != null){
			return bundle.getSparseParcelableArray(key);
		}
		try{
			return (SparseArray<T>) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	public ArrayList<String> getStringArrayList(String key){
		if(bundle != null){
			return bundle.getStringArrayList(key);
		}
		try{
			return (ArrayList<String>) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	public boolean hasFileDescriptors(){
		//TODO:
		return false;
	}

	public void putAll(MvpBundle bundle){
		if(this.bundle != null) {
			this.bundle.putAll(bundle.bundle);
		} else {
			stub.putAll(bundle.stub);
		}
	}

	public void putAll(Bundle bundle){
		if(bundle != null){
			bundle.putAll(bundle);
		}
	}

	@TargetApi(18)
	public void putBinder(String key, IBinder value){
		if(bundle != null) {
			bundle.putBinder(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putBundle(String key, Bundle value){
		if(bundle != null) {
			bundle.putBundle(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putByte(String key, byte value){
		if(bundle != null) {
			bundle.putByte(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putByteArray(String key, byte[] value){
		if(bundle != null) {
			bundle.putByteArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putChar(String key, char value){
		if(bundle != null) {
			bundle.putChar(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putCharArray(String key, char[] value){
		if(bundle != null) {
			bundle.putCharArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putCharSequence(String key, CharSequence value){
		if(bundle != null) {
			bundle.putCharSequence(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putCharSequenceArray(String key, CharSequence[] value){
		if(bundle != null) {
			bundle.putCharSequenceArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putCharSequenceArrayList(String key, ArrayList<CharSequence> value){
		if(bundle != null) {
			bundle.putCharSequenceArrayList(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putFloat(String key, float value){
		if(bundle != null) {
			bundle.putFloat(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putFloatArray(String key, float[] value){
		if(bundle != null) {
			bundle.putFloatArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putIntegerArrayList(String key, ArrayList<Integer> value){
		if(bundle != null) {
			bundle.putIntegerArrayList(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putParcelable(String key, Parcelable value){
		if(bundle != null) {
			bundle.putParcelable(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putParcelableArray(String key, Parcelable[] value){
		if(bundle != null) {
			bundle.putParcelableArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putParcelableArrayList(String key, ArrayList<? extends Parcelable> value){
		if(bundle != null) {
			bundle.putParcelableArrayList(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putSerializable(String key, Serializable value){
		if(bundle != null) {
			bundle.putSerializable(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putShort(String key, short value){
		if(bundle != null) {
			bundle.putShort(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putShortArray(String key, short[] value){
		if(bundle != null) {
			bundle.putShortArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void putSize(String key, Size value){
		if(bundle != null) {
			bundle.putSize(key, value);
		} else {
			stub.put(key, value);
		}
	}


	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void putSizeF(String key, SizeF value){
		if(bundle != null) {
			bundle.putSizeF(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putSparseParcelableArray(String key, SparseArray<? extends Parcelable> value){
		if(bundle != null) {
			bundle.putSparseParcelableArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void putStringArrayList(String key, ArrayList<String> value){
		if(bundle != null) {
			bundle.putStringArrayList(key, value);
		} else {
			stub.put(key, value);
		}
	}

	public void readFromParcel(Parcel parcel){
		if(bundle != null) {
			bundle.readFromParcel(parcel);
		}
	}

	public void setClassLoader(ClassLoader loader){
		if(bundle != null) {
			bundle.setClassLoader(loader);
		} else{
			this.setClassLoader(loader);
		}
	}

	public String toString(){
		return "MvpBundle";
	}

	public void writeToParcel(Parcel parcel, int flags){
		if(bundle != null){
			bundle.writeToParcel(parcel, flags);
		}
	}

	/**
	 * Returns the number of mappings contained in this Bundle.
	 *
	 * @return the number of mappings as an int.
	 */
	public int size() {
		if(bundle != null){
			return bundle.size();
		}
		return stub.size();
	}

	/**
	 * Returns true if the mapping of this Bundle is empty, false otherwise.
	 */
	public boolean isEmpty() {
		if(bundle != null){
			return bundle.isEmpty();
		}
		return stub.isEmpty();
	}

	/**
	 * Removes all elements from the mapping of this Bundle.
	 */
	public void clear() {
		if(bundle != null){
			bundle.clear();
		} else{
			stub.clear();
		}
	}

	/**
	 * Returns true if the given key is contained in the mapping
	 * of this Bundle.
	 *
	 * @param key a String key
	 * @return true if the key is part of the mapping, false otherwise
	 */
	public boolean containsKey(String key) {
		if(bundle != null){
			return bundle.containsKey(key);
		}
		return stub.containsKey(key);
	}

	/**
	 * Returns the entry with the given key as an object.
	 *
	 * @param key a String key
	 * @return an Object, or null
	 */
	@Nullable
	public Object get(String key) {
		if(bundle != null){
			return bundle.get(key);
		}
		return stub.get(key);
	}

	/**
	 * Removes any entry with the given key from the mapping of this Bundle.
	 *
	 * @param key a String key
	 */
	public void remove(String key) {
		if(bundle != null){
			bundle.remove(key);
		} else{
			stub.containsKey(key);
		}
	}

	/**
	 * Inserts all mappings from the given PersistableBundle into this BaseBundle.
	 *
	 * @param bundle a PersistableBundle
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void putAll(PersistableBundle bundle) {
		if(bundle != null){
			bundle.putAll(bundle);
		}
	}

	/**
	 * Returns a Set containing the Strings used as keys in this Bundle.
	 *
	 * @return a Set of String keys
	 */
	public Set<String> keySet() {
		if(bundle != null){
			return bundle.keySet();
		}
		return stub.keySet();
	}

	/**
	 * Inserts a Boolean value into the mapping of this Bundle, replacing
	 * any existing value for the given key.  Either key or value may be null.
	 *
	 * @param key a String, or null
	 * @param value a boolean
	 */
	public void putBoolean(@Nullable String key, boolean value) {
		if(bundle != null){
			bundle.putBoolean(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Inserts an int value into the mapping of this Bundle, replacing
	 * any existing value for the given key.
	 *
	 * @param key a String, or null
	 * @param value an int
	 */
	public void putInt(@Nullable String key, int value) {
		if(bundle != null){
			bundle.putInt(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Inserts a long value into the mapping of this Bundle, replacing
	 * any existing value for the given key.
	 *
	 * @param key a String, or null
	 * @param value a long
	 */
	public void putLong(@Nullable String key, long value) {
		if(bundle != null){
			bundle.putLong(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Inserts a double value into the mapping of this Bundle, replacing
	 * any existing value for the given key.
	 *
	 * @param key a String, or null
	 * @param value a double
	 */
	public void putDouble(@Nullable String key, double value) {
		if(bundle != null){
			bundle.putDouble(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Inserts a String value into the mapping of this Bundle, replacing
	 * any existing value for the given key.  Either key or value may be null.
	 *
	 * @param key a String, or null
	 * @param value a String, or null
	 */
	public void putString(@Nullable String key, @Nullable String value) {
		if(bundle != null){
			bundle.putString(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Inserts a boolean array value into the mapping of this Bundle, replacing
	 * any existing value for the given key.  Either key or value may be null.
	 *
	 * @param key a String, or null
	 * @param value a boolean array object, or null
	 */
	public void putBooleanArray(@Nullable String key, @Nullable boolean[] value) {
		if(bundle != null){
			bundle.putBooleanArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Inserts an int array value into the mapping of this Bundle, replacing
	 * any existing value for the given key.  Either key or value may be null.
	 *
	 * @param key a String, or null
	 * @param value an int array object, or null
	 */
	public void putIntArray(@Nullable String key, @Nullable int[] value) {
		if(bundle != null){
			bundle.putIntArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Inserts a long array value into the mapping of this Bundle, replacing
	 * any existing value for the given key.  Either key or value may be null.
	 *
	 * @param key a String, or null
	 * @param value a long array object, or null
	 */
	public void putLongArray(@Nullable String key, @Nullable long[] value) {
		if(bundle != null){
			bundle.putLongArray(key, value);
		} else {
			stub.put(key, value);
		}
	}


	/**
	 * Inserts a double array value into the mapping of this Bundle, replacing
	 * any existing value for the given key.  Either key or value may be null.
	 *
	 * @param key a String, or null
	 * @param value a double array object, or null
	 */
	public void putDoubleArray(@Nullable String key, @Nullable double[] value) {
		if(bundle != null){
			bundle.putDoubleArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Inserts a String array value into the mapping of this Bundle, replacing
	 * any existing value for the given key.  Either key or value may be null.
	 *
	 * @param key a String, or null
	 * @param value a String array object, or null
	 */
	public void putStringArray(@Nullable String key, @Nullable String[] value) {
		if(bundle != null){
			bundle.putStringArray(key, value);
		} else {
			stub.put(key, value);
		}
	}

	/**
	 * Returns the value associated with the given key, or false if
	 * no mapping of the desired type exists for the given key.
	 *
	 * @param key a String
	 * @return a boolean value
	 */
	public boolean getBoolean(String key) {
		if(bundle != null){
			return bundle.getBoolean(key);
		}
		try{
			return (boolean) stub.get(key);
		} catch(Exception e){
			return false;
		}
	}


	/**
	 * Returns the value associated with the given key, or defaultValue if
	 * no mapping of the desired type exists for the given key.
	 *
	 * @param key a String
	 * @param defaultValue Value to return if key does not exist
	 * @return a boolean value
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		if(bundle != null){
			return bundle.getBoolean(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (boolean) (tmp != null ? tmp : defaultValue);
		} catch(Exception e){
			return defaultValue;
		}
	}

	/**
	 * Returns the value associated with the given key, or 0 if
	 * no mapping of the desired type exists for the given key.
	 *
	 * @param key a String
	 * @return an int value
	 */
	public int getInt(String key) {
		if(bundle != null){
			return bundle.getInt(key);
		}
		try{
			return (int) stub.get(key);
		} catch(Exception e) {
			return 0;
		}
	}

	/**
	 * Returns the value associated with the given key, or defaultValue if
	 * no mapping of the desired type exists for the given key.
	 *
	 * @param key a String
	 * @param defaultValue Value to return if key does not exist
	 * @return an int value
	 */
	public int getInt(String key, int defaultValue) {
		if(bundle != null){
			return bundle.getInt(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (int) (tmp != null ? tmp : defaultValue);
		} catch(Exception e){
			return defaultValue;
		}
	}

	/**
	 * Returns the value associated with the given key, or 0L if
	 * no mapping of the desired type exists for the given key.
	 *
	 * @param key a String
	 * @return a long value
	 */
	public long getLong(String key) {
		if(bundle != null){
			return bundle.getLong(key);
		}
		try{
			return (byte) stub.get(key);
		} catch(Exception e){
			return 0;
		}
	}

	/**
	 * Returns the value associated with the given key, or defaultValue if
	 * no mapping of the desired type exists for the given key.
	 *
	 * @param key a String
	 * @param defaultValue Value to return if key does not exist
	 * @return a long value
	 */
	public long getLong(String key, long defaultValue) {
		if(bundle != null){
			return bundle.getLong(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (long) (tmp != null ? tmp : defaultValue);
		} catch(Exception e){
			return defaultValue;
		}
	}

	/**
	 * Returns the value associated with the given key, or 0.0 if
	 * no mapping of the desired type exists for the given key.
	 *
	 * @param key a String
	 * @return a double value
	 */
	public double getDouble(String key) {
		if(bundle != null){
			return bundle.getDouble(key);
		}
		try{
			return (double) stub.get(key);
		} catch(Exception e){
			return 0.0;
		}
	}

	/**
	 * Returns the value associated with the given key, or defaultValue if
	 * no mapping of the desired type exists for the given key.
	 *
	 * @param key a String
	 * @param defaultValue Value to return if key does not exist
	 * @return a double value
	 */
	public double getDouble(String key, double defaultValue) {
		if(bundle != null){
			return bundle.getDouble(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (double) (tmp != null ? tmp : defaultValue);
		} catch(Exception e){
			return defaultValue;
		}
	}

	/**
	 * Returns the value associated with the given key, or null if
	 * no mapping of the desired type exists for the given key or a null
	 * value is explicitly associated with the key.
	 *
	 * @param key a String, or null
	 * @return a String value, or null
	 */
	@Nullable
	public String getString(@Nullable String key) {
		if(bundle != null){
			return bundle.getString(key);
		}
		try{
			return (String) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	/**
	 * Returns the value associated with the given key, or defaultValue if
	 * no mapping of the desired type exists for the given key or if a null
	 * value is explicitly associated with the given key.
	 *
	 * @param key a String, or null
	 * @param defaultValue Value to return if key does not exist or if a null
	 *     value is associated with the given key.
	 * @return the String value associated with the given key, or defaultValue
	 *     if no valid String object is currently mapped to that key.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public String getString(@Nullable String key, String defaultValue) {
		if(bundle != null){
			return bundle.getString(key, defaultValue);
		}
		try{
			Object tmp = stub.get(key);
			return (String) (tmp != null ? tmp : defaultValue);
		} catch(Exception e){
			return defaultValue;
		}
	}

	/**
	 * Returns the value associated with the given key, or null if
	 * no mapping of the desired type exists for the given key or a null
	 * value is explicitly associated with the key.
	 *
	 * @param key a String, or null
	 * @return a boolean[] value, or null
	 */
	@Nullable
	public boolean[] getBooleanArray(@Nullable String key) {
		if(bundle != null){
			return bundle.getBooleanArray(key);
		}
		try{
			return (boolean []) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	/**
	 * Returns the value associated with the given key, or null if
	 * no mapping of the desired type exists for the given key or a null
	 * value is explicitly associated with the key.
	 *
	 * @param key a String, or null
	 * @return an int[] value, or null
	 */
	@Nullable
	public int[] getIntArray(@Nullable String key) {
		if(bundle != null){
			return bundle.getIntArray(key);
		}
		try{
			return (int[]) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	/**
	 * Returns the value associated with the given key, or null if
	 * no mapping of the desired type exists for the given key or a null
	 * value is explicitly associated with the key.
	 *
	 * @param key a String, or null
	 * @return a long[] value, or null
	 */
	@Nullable
	public long[] getLongArray(@Nullable String key) {
		if(bundle != null){
			return bundle.getLongArray(key);
		}
		try{
			return (long[]) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	/**
	 * Returns the value associated with the given key, or null if
	 * no mapping of the desired type exists for the given key or a null
	 * value is explicitly associated with the key.
	 *
	 * @param key a String, or null
	 * @return a double[] value, or null
	 */
	@Nullable
	public double[] getDoubleArray(@Nullable String key) {
		if(bundle != null){
			return bundle.getDoubleArray(key);
		}
		try{
			return (double[]) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}

	/**
	 * Returns the value associated with the given key, or null if
	 * no mapping of the desired type exists for the given key or a null
	 * value is explicitly associated with the key.
	 *
	 * @param key a String, or null
	 * @return a String[] value, or null
	 */
	@Nullable
	public String[] getStringArray(@Nullable String key) {
		if(bundle != null){
			return bundle.getStringArray(key);
		}
		try{
			return (String[]) stub.get(key);
		} catch(Exception e){
			return null;
		}
	}
}
