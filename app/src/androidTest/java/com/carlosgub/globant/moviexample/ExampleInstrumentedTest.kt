@file:OptIn(ExperimentalTestApi::class)

package com.carlosgub.globant.moviexample


import android.Manifest
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.kakao.common.utilities.getResourceString
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ComposeMainActivity(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ComposeMainActivity>(
        semanticsProvider = semanticsProvider,
    ) {
    val logInEmailTextField: KNode = child { hasTestTag("login_email") }
    val logInPasswordTextField: KNode = child { hasTestTag("login_password") }
    val loginButton: KNode = child { hasTestTag("login_button") }
    val loginError: KNode = child { hasTestTag("login_error") }
    val signInButton: KNode = child { hasTestTag("login_sign_in_button") }
    val homeSearchTextField: KNode = child { hasTestTag("home_search") }
    val homeList: KNode = child { hasTestTag("home_lazy_column") }
    val homeSignOut: KNode = child { hasTestTag("home_sign_out") }
    val loading: KNode = child { hasTestTag("loading") }
    val signUpNameTextField: KNode = child { hasTestTag("sign_up_name") }
    val signUpEmailTextField: KNode = child { hasTestTag("sign_up_email") }
    val signUpPasswordTextField: KNode = child { hasTestTag("sign_up_password") }
    val signUpSignUpButton: KNode = child { hasTestTag("sign_up_button") }
    val signUpError: KNode = child { hasTestTag("sign_up_error") }
}

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    private val username = "Carlos Gabriel Ugaz Burga"
    private val randomString = (1..5).map { ('a'..'z').random() }.joinToString("")
    private val badEmail = "$randomString@example"
    private val goodEmail = "$badEmail.com"
    private val goodPassword = "aA123123@"
    private val emailToLogin = "carlos@gmail.com"
    private val passwordToLogin = "aA123123@"
    private val badPassword1 = "123qweASD"

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.INTERNET
    )

    @Test
    fun loginWithUserAndPassword() = run {
        testSuccessLoginWithEmailAndPassword()
        testHomeScreen()
    }

    @Test
    fun loginWithWrongUserAndPassword() = run {
        testSuccessLoginWithWrongEmailAndPassword()
    }

    @Test
    fun registerNewUser() = run {
        testSuccessRegisterScreen()
        testHomeScreen()
    }

    @Test
    fun registerWrongNewUser() = run {
        testWrongRegisterScreen()
    }

    @Test
    fun registerNewUserThatExistCurrently() = run {
        testExistUserRegisterScreen()
    }

    private fun dismissKeyboard() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }

    private fun TestContext<Unit>.testSuccessLoginWithEmailAndPassword() {
        step("Open Login screen & fill fields to sign in") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                logInEmailTextField {
                    assertIsDisplayed()
                    performTextInput(emailToLogin)
                }
                logInPasswordTextField {
                    assertIsDisplayed()
                    performTextInput(passwordToLogin)
                }
                dismissKeyboard()
                loginButton {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(com.carlosgub.globant.resources.R.string.login_login))
                    performClick()
                }
            }
        }
    }

    private fun TestContext<Unit>.testSuccessLoginWithWrongEmailAndPassword() {
        step("Open Login screen & fill fields to sign in") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                logInEmailTextField {
                    assertIsDisplayed()
                    performTextInput(emailToLogin)
                }
                logInPasswordTextField {
                    assertIsDisplayed()
                    performTextInput(badPassword1)
                }
                dismissKeyboard()
                loginButton {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(com.carlosgub.globant.resources.R.string.login_login))
                    performClick()
                }
                loginError {
                    assertIsDisplayed()
                }
            }
        }
    }

    private fun TestContext<Unit>.testSuccessRegisterScreen() {
        step("Register new screen") {
            onComposeScreen<ComposeMainActivity>(
                composeTestRule
            ) {
                signInButton {
                    assertIsDisplayed()
                    performClick()
                }
                signUpNameTextField {
                    assertIsDisplayed()
                    performTextInput(username)
                }
                signUpEmailTextField {
                    assertIsDisplayed()
                    performTextInput(goodEmail)
                }
                signUpPasswordTextField {
                    assertIsDisplayed()
                    performTextInput(goodPassword)
                }
                signUpSignUpButton {
                    assertIsDisplayed()
                    assertIsEnabled()
                    assertTextEquals(getResourceString(com.carlosgub.globant.resources.R.string.sign_up_sign_up_button))
                    performClick()
                    dismissKeyboard()
                }
            }
        }
    }

    private fun TestContext<Unit>.testWrongRegisterScreen() {
        step("Register new screen") {
            onComposeScreen<ComposeMainActivity>(
                composeTestRule
            ) {
                signInButton {
                    assertIsDisplayed()
                    performClick()
                }
                signUpNameTextField {
                    assertIsDisplayed()
                    performTextInput(username)
                }
                signUpEmailTextField {
                    assertIsDisplayed()
                    performTextInput(goodEmail)
                }
                signUpPasswordTextField {
                    assertIsDisplayed()
                    performTextInput(badPassword1)
                }
                signUpSignUpButton {
                    assertIsDisplayed()
                    assertIsNotEnabled()
                    assertTextEquals(getResourceString(com.carlosgub.globant.resources.R.string.sign_up_sign_up_button))
                }
            }
        }
    }

    private fun TestContext<Unit>.testExistUserRegisterScreen() {
        step("Register new screen") {
            onComposeScreen<ComposeMainActivity>(
                composeTestRule
            ) {
                signInButton {
                    assertIsDisplayed()
                    performClick()
                }
                signUpNameTextField {
                    assertIsDisplayed()
                    performTextInput(username)
                }
                signUpEmailTextField {
                    assertIsDisplayed()
                    performTextInput(emailToLogin)
                }
                signUpPasswordTextField {
                    assertIsDisplayed()
                    performTextInput(passwordToLogin)
                }
                signUpSignUpButton {
                    assertIsDisplayed()
                    assertIsEnabled()
                    performClick()
                    assertTextEquals(getResourceString(com.carlosgub.globant.resources.R.string.sign_up_sign_up_button))
                }
                dismissKeyboard()
                signUpError {
                    assertIsDisplayed()
                }
            }
        }
    }

    private fun TestContext<Unit>.testHomeScreen() {
        step("Open Home screen") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                homeSearchTextField {
                    assertIsDisplayed()
                    performTextInput("marvel")
                    dismissKeyboard()
                }
                loading {
                    assertIsDisplayed()
                }
                homeList {
                    performScrollToIndex(8)
                    Thread.sleep(5000)
                    performScrollToIndex(0)
                }
                homeSignOut {
                    performClick()
                    Thread.sleep(5000)
                }
                loginButton {
                    assertIsDisplayed()
                    Thread.sleep(5000)
                }
            }
        }
    }
}