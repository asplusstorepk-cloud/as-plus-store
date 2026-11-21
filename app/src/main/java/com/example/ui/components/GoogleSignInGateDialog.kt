package com.example.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.example.BuildConfig
import com.example.R
import com.example.model.GoogleUserProfile
import com.example.model.StoreApp
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GoogleSignInGateScreen(
  pendingApp: StoreApp?,
  onDismiss: () -> Unit,
  onSignInSuccess: (GoogleUserProfile) -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler(onBack = onDismiss)

  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember(context) { CredentialManager.create(context) }
  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Surface(
    modifier = modifier.fillMaxSize().testTag("google_sign_in_screen"),
    color = MaterialTheme.colorScheme.background,
  ) {
    Column(
      modifier =
        Modifier
          .fillMaxSize()
          .statusBarsPadding()
          .navigationBarsPadding(),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("google_sign_in_back_button"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
          )
        }
        Text(
          text = stringResource(R.string.sign_in_google_title),
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
        )
      }

      Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center,
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().widthIn(max = 460.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Box(
            modifier =
              Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Rounded.Lock,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(32.dp),
            )
          }

          Text(
            text = "Google Sign-In required to download",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
          )

          Text(
            text =
              if (pendingApp != null) {
                "Sign in with your Google account to unlock and start downloading ${pendingApp.name}."
              } else {
                "Sign in with your Google account to download apps from AS Plus Store."
              },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
          )

          if (pendingApp != null) {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
              Row(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
              ) {
                AppIconTile(
                  iconText = pendingApp.iconText,
                  backgroundColor = pendingApp.color,
                  logoResId = pendingApp.logoResId,
                  iconVector = pendingApp.iconVector,
                  fillLogoBounds = pendingApp.fillLogoBounds,
                  contentDescription = "${pendingApp.name} logo",
                  size = 48.dp,
                  cornerRadius = 12.dp,
                )
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = pendingApp.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                  )
                  Text(
                    text = "${pendingApp.developer} • ${pendingApp.sizeLabel}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                  )
                }
              }
            }
          }

          if (errorMessage != null) {
            Card(
              modifier = Modifier.fillMaxWidth().testTag("google_sign_in_error"),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            ) {
              Text(
                text = errorMessage!!,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(14.dp),
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          Button(
            onClick = {
              errorMessage = null
              isLoading = true
              launchGoogleSignIn(
                context = context,
                credentialManager = credentialManager,
                scope = scope,
                onSuccess = { profile ->
                  isLoading = false
                  onSignInSuccess(profile)
                },
                onCancelled = {
                  isLoading = false
                  errorMessage = "Google Sign-In was cancelled. Please select a Google account to download."
                },
                onError = { msg ->
                  isLoading = false
                  errorMessage = msg
                },
              )
            },
            enabled = !isLoading,
            shape = RoundedCornerShape(999.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("google_sign_in_button"),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
              ),
          ) {
            if (isLoading) {
              CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.5.dp,
              )
            } else {
              Text(
                text = stringResource(R.string.continue_with_google),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.5.sp),
              )
            }
          }

          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(999.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("google_sign_in_cancel_button"),
          ) {
            Text(
              text = "Keep browsing",
              style = MaterialTheme.typography.labelLarge,
            )
          }
        }
      }
    }
  }
}

private tailrec fun Context.findActivity(): Activity? =
  when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
  }

fun launchGoogleSignIn(
  context: Context,
  credentialManager: CredentialManager,
  scope: CoroutineScope,
  onSuccess: (GoogleUserProfile) -> Unit,
  onCancelled: () -> Unit,
  onError: (String) -> Unit,
) {
  val clientId = BuildConfig.GOOGLE_WEB_CLIENT_ID.trim()
  if (clientId.isEmpty() || clientId == "YOUR_GOOGLE_WEB_CLIENT_ID") {
    onError(
      "Google OAuth Web Client ID is not configured yet. Add GOOGLE_WEB_CLIENT_ID in the Secrets panel in AI Studio to activate Google Sign-In.",
    )
    return
  }

  val activity = context.findActivity()
  if (activity == null) {
    onError("Unable to open Google Sign-In from this screen context.")
    return
  }

  val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
  val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

  scope.launch {
    try {
      val result = credentialManager.getCredential(activity, request)
      val cred = result.credential
      if (cred is CustomCredential && cred.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val googleCred = GoogleIdTokenCredential.createFrom(cred.data)
        val email = googleCred.id
        val displayName =
          googleCred.displayName?.takeIf { it.isNotBlank() }
            ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
        onSuccess(
          GoogleUserProfile(
            email = email,
            displayName = displayName,
            photoUrl = googleCred.profilePictureUri?.toString(),
          ),
        )
      } else {
        onError("Unexpected credential type returned by Google Sign-In.")
      }
    } catch (e: GetCredentialCancellationException) {
      Log.w("Auth", "Google Sign-In flow cancelled or dismissed: ${e.message}", e)
      onCancelled()
    } catch (e: NoCredentialException) {
      Log.w("Auth", "No Google account credential available: ${e.message}", e)
      onError("No Google account found on this device. Please add a Google account and try again.")
    } catch (e: Exception) {
      Log.e("Auth", "Google Sign-In failed", e)
      onError(e.localizedMessage ?: "Google Sign-In failed. Please try again.")
    }
  }
}

fun signOutGoogleAccount(
  credentialManager: CredentialManager,
  scope: CoroutineScope,
  onComplete: () -> Unit,
) {
  scope.launch {
    withContext(NonCancellable) {
      try {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
      } catch (e: Exception) {
        Log.w("Auth", "Failed to clear Credential Manager state: ${e.message}")
      } finally {
        onComplete()
      }
    }
  }
}
