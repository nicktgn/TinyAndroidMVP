# Android Model-View-Presenter(MVP) helper library

Variation of Model-View-Presenter(MVP) architectural pattern which fits neatly into the Android app development model. This pattern is simple to understand and quick to implement. 

It aims at solving two big related drawbacks of "standard" Android app development aproach:
*  **Activities and Fragments usually tend to handle everything: views, routing, business logic and data manipulations.** MVP aproach lets Activities and Fragments to be simple Views the way they should be. All the bussiness logic is placed in the separate Presenter class which is your common POJO.
*  **Because of the previous reason the task of testing Android applications developed using "standard" approach becomes close to impossible.** This problem and the reasons behind it are described in detail in [this great series of articles](http://www.philosophicalhacker.com/2015/04/17/why-android-unit-testing-is-so-hard-pt-1/). Because MVP separates Views (essentially all Android app componnents classes) from the bussines logic, this bussines logic can be easily tested using JUnit or anything else even without all the Android dependencies.

This library provides a simple opt-in helper framework for apps based on MVP pattern.

Suggested architecture is loosely based on the following sources:
* ["How to Make Our Android Apps Unit Testable: The "Square Way""](http://www.philosophicalhacker.com/2015/05/01/how-to-make-our-android-apps-unit-testable-pt-1/)
* [Ted Mosby - Software Architect: MVP for Android](http://hannesdorfmann.com/android/mosby/)

### Usage:
***Gradle:***

Add the following to your `build.gradle` file:
```
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.nicktgn.mvp:tiny-android-mvp:0.4.0'
}
```

### License:
```
Copyright 2015 Nick Tsygankov (nicktgn@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
