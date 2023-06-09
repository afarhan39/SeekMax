<h1 align="center">SeekMaxAF</h1>

<p align="center">  
SeekMaxAF is a small demo application based on modern Android application tech-stacks and MVVM architecture.<br>This project is for focusing especially on the Seek task of creating Job Ads app based on GraphQL API.
</p>
</br>

<p align="center">
<img src="/misc/screenshotSeekMaxAF.png"/>
</p>

## Tech stack & Open-source libraries
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
- [Apollo](https://github.com/apollographql/apollo-kotlin) GraphQL client
- Coroutines
  - Flow
- JetPack
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository pattern
- [Glide](https://github.com/bumptech/glide) to load image
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components like ripple animation, cardView.
- [Chucker](https://github.com/ChuckerTeam/chucker) inspect HTTP traffic, for debugging

## Architecture
SeekMaxAF is based on MVVM architecture and a repository pattern.

Huge credits to __[skydoves](https://github.com/skydoves)__ for his nice template of Readme!

## Getting Started
- go to backend folder
- run command `docker-compose up -d` in Terminal
- edit server URL, located in `app/src/main/java/com/example/seek_max/hilt/ApiModule.kt`
- run and install the app

# License
```xml
Designed and developed by afarhan39 (Amir Farhan), 2023

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
