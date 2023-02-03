## TODO

* [X]  CREATE HOME NAVIGATION
* [X]  LEARN APP FRAGMENTATION
* [X]  LEARN APP RECYCLER VIEW
* [X]  MVVP ACHITECTURE
* [X]  LEARN LOTTIE USE
* [ ]  ANIMATION
* [ ]  SOUND EFFECT
* [ ]  SOUND, ALARM, READ AND WRITE MEMORY PERMISSION
* [X]  THEME LIGHT AND DARK
* [X]  ROOM PERSISTENCE LIBRARY
* [ ]  WRITE THESIS
* [ ]  REVIEW
* [ ]  ADD SMALL FEATURE
* [ ]  FIX APP CONFIGURATION CHANGES PROBLEM
* [ ]  APPLICATION FOR MUTIPLE SCREEN SIZE LAYOUT, HDPI, DPI, MDPI, XHDPI, XXHDPI
* [X]  Custom Fragment Dialog
* [X]  Do the logic for keeping track of skip if the app is not open for over 1 day
* [ ]  Check job scheduler or foregound service android

## DEPENDENCIES

Room Persistence Library

Firebase Dependency

Material Component Bridge (For Dropdown List)

Recycler View

Data Binding and View Binding Enables

Navigation Components

Animated Bottom Bar

Swipe Reveal Layout

Mp Android Chart

Material Caendar View

Lottie Dependecy

GSON

Glide - not used by implemented

## To-Do: Configuration Saved State and Restore

#### HOME Configuration

**On Item Click:**

* [X]  Default
* [X]  Rotation,
* [X]  Change Tab,
* [X]  Restart

**Fab Dialog:**

* [X]  Rotation
* [X]  Restart

**On Add Default**

* [X]  Default
* [X]  Rotation
* [X]  Change Tab
* [X]  Restart

**On Add New**

* [X]  Default
* [X]  Rotation
* [X]  Change Tab
* [X]  Restart
* [X]  Dialog Defualt {No Cloud Check, Red Was Current Selected Color} is not on new instance of diag is dislayed

#### Subroutine Configuration

* [X]  Subroutine animation (keep track of expanded child adapter and not expanded)
* [X]  Subroutine Modfiy
  * [X]  Default
  * [X]  Rotation
  * [X]  Change Tab
  * [X]  Restart

#### Analytics Configuration

**Analtyic**

* [X]  Default
* [X]  Rotation
* [X]  Change Tab
* [X]  Restart

**On Item Click**

* [X]  Default
* [X]  Rotation
* [X]  Change Tab
* [X]  Restart

#### Journal Configuration

* [ ]  Default
* [ ]  Rotation
* [ ]  Change Tab
* [ ]  Restart

#### Habit display check if still on reform before restoring at on pause and resume state for change tab

#### FIX: APP Tracker for skips and completed subroutines not matching with total skips when app is not opened for more than a day

#### FIX NEGATIVE DAYS WHEN USER MODIFY THIER DEVICE SETTING DATE

#### Fix setting multi touching

* [X]  Fixed

#### Fix Color Match on Analytic Menu when a habit is selected in the view

#### Learn how to set a daily alarm manager that will be based on oboarding layout, and also add the a feature to control the daily reminder for mornign and noon

#### Urgent: Fix Double Entry of Habit and Subroutines

* [X]  Fixed

#### Update Lottie Animation on Empty Habit on Reform

* [ ]  Use more suitable lottie anim other than ghost

#### Handle Drop Habit causing recycler view items a ArrayIndexOutOfBoundsException

* [ ]  Possible Solution, Add Confirmation Dialog
* [ ]  F*** coding........................

Layouts:

* Splash Screen
* Onboarding
  * Initial
  * Eula
  * Get Usual Wake Up
  * Get Usual Sleep
  * Introduction of app prolly
  * Greetings, 21 day
  * Nickname
  * Identity (Confirmation Dialog)
  * Get Started
  * Personalization (Assessment Section), (Confirmation Dialog)
  * Analysis (Percentage of Identified bad habits based on the provided answer in assessment tool)
* Home
  * Home
  * Floating Action Button: Add Habit Dialog
    * Predefined
      * Add Default Habit
        * FAB (Modify DB, Mark Selected Habit on Reform)
    * Custom
      * Add New Habit
        * Insert | Add Subroutine Dialog (Limitation min of 2 subroutine)
  * Item Habit View (Home Item on Click)
  * Relapse Button
  * Drop Button (Drop Dialog, Set Habit DB on Default)
  * Modify Habit Dialog
* Subroutine
  * Subroutine
  * Modify Subroutines
    * Remove Subroutine Button
    * Insert Subroutine Dialog
* Analytic
  * Analytic (Habits)
    * Item on Click (Subroutines)
* Journal
* Setting
