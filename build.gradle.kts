// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    id("cash.bdo.scalroid") /*version '[1.6-gradle8,)'*/ apply false
}