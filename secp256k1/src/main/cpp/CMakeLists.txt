# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.11)

# Declares and names the project.
project("secp256k1-android" LANGUAGES CXX)

# Add https://github.com/peerbridge/secp256k1
SET(BUILD_TESTS OFF CACHE BOOL "Build unit tests")
add_subdirectory(secp256k1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        secp256k1-android

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)

# Include https://github.com/peerbridge/secp256k1
target_include_directories(secp256k1-android PUBLIC secp256k1)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        secp256k1-android

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}

        # Links the target library to the secp256k1 C++ library
        # provided via https://github.com/peerbridge/secp256k1
        secp256k1)
