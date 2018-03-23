/*
 * Copyright $year  Nick Tsygankov (nicktgn@gmail.com)
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

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by nick on 01/03/2018.
 */

public class MvpFragmentCompatHelper<FV extends Fragment & IMvpFragment, P extends MvpPresenter> {

    private static final Logger logger = LoggerManager.getLogger(MvpFragmentCompatHelper.class.getName());

    protected P presenter;

    MvpBundle stateData = null;
    MvpBundle argumentsData = null;

    /**
     * Use this helper method to get another View (Fragment)
     * with extras containing provided arguments indented for Presenter of this new View
     * @param arguments arguments from this View's Presenter intended for Presenter of another View
     * @return intent to another View (Activity) (or null if failed to instantiate)
     */
    public <T extends Fragment & IMvpFragment> T getMvpFragmentCompat(Class<T> targetView, MvpBundle arguments){
        try {
            T fragment = targetView.getConstructor().newInstance();
            Bundle bundle = new Bundle();
            bundle.putBundle(Constants.ARGUMENTS_DATA, arguments.getRealBundle());
            fragment.setArguments(bundle);
            return fragment;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    public <T extends android.app.Fragment & IMvpFragment> T getMvpFragment(Class<T> targetView, MvpBundle arguments){
        try {
            T fragment = targetView.getConstructor().newInstance();
            Bundle bundle = new Bundle();
            bundle.putBundle(Constants.ARGUMENTS_DATA, arguments.getRealBundle());
            fragment.setArguments(bundle);
            return fragment;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void onActivityCreated(FV fragmentView, Bundle savedInstanceState, P presenter) {
        logger.d("onActivityCreated");
        this.presenter = presenter;

        // ---- INPUT ARGUMENTS ---- //
        if(argumentsData != null){
            try {
                // first try to get input model data from fragment arguments
                // NOTE: you can still skip this convenience mechanism, and inject the data into presenter
                // through presenter's constructor for example

                Bundle bundle = fragmentView.getArguments().getBundle(Constants.ARGUMENTS_DATA);
                if(bundle != null) {
                    argumentsData = new MvpBundle(bundle);
                    logger.d("Got arguments data from fragment arguments");
                }

                // if noting found in arguments, try to get input model data from Intent
                // (that started Activity hosting this Fragment)
                // NOTE: you can still skip this convenience mechanism, and inject the data into presenter
                // through presenter's constructor for example
                else{
                    bundle = fragmentView.getActivity().getIntent().getExtras().getBundle(Constants.ARGUMENTS_DATA);
                    if(bundle != null) {
                        argumentsData = new MvpBundle(bundle);
                        logger.d("Got argumnets data from intent");
                    }
                }
            } catch(NullPointerException e){
                logger.d("No arguments data");
            }
        }


        // ---- CACHING ---- //
        // if we have savedInstanceState (activity was re-created)
        if(savedInstanceState != null){
            Bundle bundle = savedInstanceState.getBundle(Constants.CACHED_STATE_DATA);
            if(bundle != null){
                stateData = new MvpBundle(bundle);
                logger.d("Got cached state data from instance state");
            }
        }
        // else this fragment is just created or came back from back stack
        // try to restore presenter state from MvpState (cause fragment instance is still
        // the same and only the view was re-created)
        else{
            stateData = MvpState.restoreState(this);
            if(stateData != null){
                logger.d("Got cached data from mvp fragment state");
            }
        }

        this.presenter.attachView(fragmentView, argumentsData, stateData);
    }

    public void onDestroyView(FV fragmentView) {
        logger.d("onDestroyView");

        // in case the fragment is not destroyed (that is when its returned from back stack)
        //  and only view is recreated, save presenter state in MvpState (cause we retain fragment
        // instance reference)
        MvpBundle savedData = presenter.saveState();
        if(savedData != null){
            MvpState.saveState(this, savedData);
        }

        presenter.detachView(fragmentView.getRetainInstance());
    }


    public void onSaveInstanceState(Bundle outState) {
        MvpBundle savedData = presenter.saveState();
        if(savedData != null){
            outState.putBundle(Constants.CACHED_STATE_DATA, savedData.getRealBundle());
        }
    }
}
