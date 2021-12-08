## テスト勉強用

### 概要

テストの勉強成果として作成した簡単なアプリです。<br>
行って見たい場所を記録することができます。<br>

### 各ブランチについて
- app : ベースとなるアプリになります。
- unitTest : ViewModelの単体テストを記述しました。
- integrationTest : ViewModelとFragmentの統合テストを記述しました。

### 使用したライブラリ
- assertion : Truth
- integrationTest : Espresso
- navigationTest : Mockito

### 参考
- 『Advanced Android in Kotlin 05.1:Testing Basics』
https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-basics?hl=ja#0
- 『Advanced Android in Kotlin 05.2:Introduction to Test Doubles and Dependency Injection』
https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-test-doubles?hl=ja#0
- 『Advanced Android in Kotlin 05.3: Testing Coroutines and Jetpack integrations』
https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-survey?hl=ja#1
- Testの時のLiveDataの扱い方について
https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04
- AnimationTestRuleについて
『Android端末のアニメーションを無効にするJUnitテストルール』
https://android.suzu-sd.com/2020/04/android_no_animation_wo_mukou_ni_suru_testrule/