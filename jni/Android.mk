LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := EffectiveJava
LOCAL_SRC_FILES := NativeHelper.c
include $(BUILD_SHARED_LIBRARY)
