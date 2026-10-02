package com.example

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SensorsOff
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = { TopNavBar() }
        ) { innerPadding ->
          MainContent(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar() {
  val context = LocalContext.current
  TopAppBar(
    title = {
      Column {
        Text(
          text = "NoWakeProximity",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = "LSPosed Hook for HyperOS Double-Tap Wake",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    },
    actions = {
      IconButton(
        onClick = {
          Toast.makeText(
            context,
            "Target: Redmi Note 13 4G (sapphire), HyperOS / KernelSU",
            Toast.LENGTH_LONG
          ).show()
        },
        modifier = Modifier.testTag("info_button")
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = "Target Device Info"
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  )
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Overview", "Sensor Test", "Setup Guide", "Technical")

  Column(modifier = modifier.fillMaxSize()) {
    TabRow(
      selectedTabIndex = selectedTab,
      modifier = Modifier.fillMaxWidth().testTag("tabs_row")
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = { Text(text = title, fontSize = 13.sp) },
          modifier = Modifier.testTag("tab_$index")
        )
      }
    }

    Box(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      when (selectedTab) {
        0 -> OverviewTab()
        1 -> SensorTestTab()
        2 -> SetupGuideTab()
        3 -> TechnicalTab()
      }
    }
  }
}

@Composable
fun OverviewTab() {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Card
    Card(
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
      ),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth().testTag("hero_card")
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.TouchApp,
              contentDescription = "Double Tap Wake",
              tint = MaterialTheme.colorScheme.onPrimary,
              modifier = Modifier.size(28.dp)
            )
          }

          Column {
            Text(
              text = "Bypass Proximity on Wake",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "Stops display sleeping on double-tap wake",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "On HyperOS, waking the phone with a double-tap runs BaseMiuiPhoneWindowManager.registerProximitySensor(). If the sensor reports \"too close\" (e.g. slight obstruction, screen protector, or pocket edge), the listener forces the device back to sleep immediately.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
            .padding(10.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LockOpen,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Hook: registerProximitySensor() -> returns null",
              fontFamily = FontFamily.Monospace,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // Target Profile Card
    OutlinedCard(
      modifier = Modifier.fillMaxWidth().testTag("target_profile_card"),
      shape = RoundedCornerShape(14.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.PhoneAndroid,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Target Device Profile",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        InfoRow(label = "Device", value = "Redmi Note 13 4G (sapphire)")
        InfoRow(label = "ROM", value = "Xiaomi HyperOS (Android 14/15)")
        InfoRow(label = "Root Provider", value = "KernelSU / APatch / Magisk")
        InfoRow(label = "Framework", value = "LSPosed (Zygisk Next)")
        InfoRow(label = "Scope", value = "System Framework (android)")
      }
    }

    // Side Effects Card
    Card(
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier.fillMaxWidth().testTag("side_effects_card")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = Color(0xFFF59E0B)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Side Effect Notice",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "With the proximity check bypassed, the phone may wake inside your pocket upon accidental screen contact. This behavior also affects fingerprint sensor and DPAD center wake because HyperOS delegates all of them to the same listener.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
fun SensorTestTab() {
  val context = LocalContext.current
  val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager }
  val proximitySensor = remember { sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY) }

  var currentDistance by remember { mutableFloatStateOf(-1f) }
  var maxDistance by remember { mutableFloatStateOf(proximitySensor?.maximumRange ?: 5f) }
  var hasSensor by remember { mutableStateOf(proximitySensor != null) }

  DisposableEffect(sensorManager, proximitySensor) {
    if (sensorManager != null && proximitySensor != null) {
      val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
          event?.let {
            if (it.values.isNotEmpty()) {
              currentDistance = it.values[0]
            }
          }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
      }

      sensorManager.registerListener(listener, proximitySensor, SensorManager.SENSOR_DELAY_UI)

      onDispose {
        sensorManager.unregisterListener(listener)
      }
    } else {
      onDispose { }
    }
  }

  val isNear = currentDistance >= 0f && currentDistance < maxDistance
  val statusColor by animateColorAsState(
    targetValue = if (isNear) Color(0xFFEF4444) else Color(0xFF10B981),
    label = "sensor_color"
  )

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Live Hardware Proximity Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth().testTag("sensor_live_card")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Live Proximity Sensor Test",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = if (hasSensor) "Cover the top bezel of your phone" else "No proximity sensor detected (Emulator)",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Visual radar / status indicator
        Box(
          modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(statusColor.copy(alpha = 0.15f))
            .border(3.dp, statusColor, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isNear) Icons.Default.SensorsOff else Icons.Default.Sensors,
            contentDescription = "Sensor Status",
            tint = statusColor,
            modifier = Modifier.size(54.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = if (!hasSensor) "EMULATOR / SIMULATED"
          else if (isNear) "NEAR (COVERED)"
          else "FAR (UNCOVERED)",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            color = statusColor
          )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = if (hasSensor && currentDistance >= 0f) "Distance: ${currentDistance} cm (Max: ${maxDistance} cm)"
          else "Distance: ${if (currentDistance >= 0f) currentDistance else "N/A"}",
          fontFamily = FontFamily.Monospace,
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    // Sensor Calibration Guide Card (*#*#6484#*#*)
    OutlinedCard(
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier.fillMaxWidth().testTag("sensor_stuck_card")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.HelpOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Check for \"Stuck on Near\"",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "As stated in the instructions: before installing the hook, make sure the hardware sensor isn't physically stuck on 'near' due to dust or a damaged screen protector.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        val clipboardManager = LocalClipboardManager.current
        FilledTonalButton(
          onClick = {
            clipboardManager.setText(AnnotatedString("*#*#6484#*#*"))
            Toast.makeText(context, "Copied *#*#6484#*#* to clipboard", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.fillMaxWidth().testTag("dialer_code_button")
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Copy Xiaomi Hardware CIT Code: *#*#6484#*#*")
        }
      }
    }
  }
}

@Composable
fun SetupGuideTab() {
  val checklist = remember {
    mutableStateMapOf(
      0 to false,
      1 to false,
      2 to false,
      3 to false,
      4 to false
    )
  }

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Text(
      text = "LSPosed Installation & Setup",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Text(
      text = "Follow these 5 steps to activate the module on HyperOS:",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    SetupStepItem(
      stepNumber = 1,
      title = "Install LSPosed & Zygisk",
      description = "Ensure LSPosed is installed and working with a Zygisk provider under KernelSU / Magisk / APatch (for example, Zygisk Next).",
      isChecked = checklist[0] ?: false,
      onCheckedChange = { checklist[0] = it }
    )

    SetupStepItem(
      stepNumber = 2,
      title = "Enable NoWakeProximity Module",
      description = "Open the LSPosed Manager app, go to the 'Modules' tab, and toggle NoWakeProximity ON.",
      isChecked = checklist[1] ?: false,
      onCheckedChange = { checklist[1] = it }
    )

    SetupStepItem(
      stepNumber = 3,
      title = "Tick 'System Framework' Scope",
      description = "CRITICAL: In the module's scope list, ensure 'System Framework' (android) is checked. The hook targets the OS system_server.",
      isChecked = checklist[2] ?: false,
      onCheckedChange = { checklist[2] = it },
      highlight = true
    )

    SetupStepItem(
      stepNumber = 4,
      title = "Reboot Device",
      description = "Reboot your smartphone to allow LSPosed to inject the hook into the system_server process at startup.",
      isChecked = checklist[3] ?: false,
      onCheckedChange = { checklist[3] = it }
    )

    SetupStepItem(
      stepNumber = 5,
      title = "Verify Double-Tap Wake",
      description = "Turn screen off, cover the top edge/proximity sensor with your palm or paper, and double-tap the screen. The display should turn on and stay on!",
      isChecked = checklist[4] ?: false,
      onCheckedChange = { checklist[4] = it }
    )
  }
}

@Composable
fun SetupStepItem(
  stepNumber: Int,
  title: String,
  description: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  highlight: Boolean = false
) {
  Card(
    colors = CardDefaults.cardColors(
      containerColor = if (highlight) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
      else MaterialTheme.colorScheme.surface
    ),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.fillMaxWidth().testTag("setup_step_$stepNumber")
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Checkbox(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
      )

      Spacer(modifier = Modifier.width(8.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "$stepNumber. $title",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
fun TechnicalTab() {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = "Hook Implementation Details",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )

    // Code Explanation Card
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Hook Mechanism",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "• Target Class: com.android.server.policy.BaseMiuiPhoneWindowManager\n" +
              "• Target Method: registerProximitySensor()\n" +
              "• Replacement: XC_MethodReplacement returning null\n" +
              "• Asset: assets/xposed_init -> com.shamim.nowakeprox.Hook",
          fontFamily = FontFamily.Monospace,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Java Hook Code Card
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
      modifier = Modifier.fillMaxWidth().testTag("code_viewer_card")
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Hook.java",
            color = Color(0xFF94A3B8),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
          )

          IconButton(
            onClick = {
              val code = """
                package com.shamim.nowakeprox;
                import de.robv.android.xposed.*;
                import de.robv.android.xposed.callbacks.XC_LoadPackage;

                public class Hook implements IXposedHookLoadPackage {
                    @Override
                    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
                        if (!"android".equals(lpparam.packageName)) return;
                        try {
                            Class<?> c = XposedHelpers.findClass(
                                "com.android.server.policy.BaseMiuiPhoneWindowManager",
                                lpparam.classLoader);
                            XposedBridge.hookAllMethods(c, "registerProximitySensor",
                                new XC_MethodReplacement() {
                                    @Override
                                    protected Object replaceHookedMethod(MethodHookParam param) {
                                        return null;
                                    }
                                });
                            XposedBridge.log("NoWakeProximity: hooked registerProximitySensor");
                        } catch (Throwable t) {
                            XposedBridge.log("NoWakeProximity: " + t);
                        }
                    }
                }
              """.trimIndent()
              clipboardManager.setText(AnnotatedString(code))
              Toast.makeText(context, "Hook source code copied!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.size(28.dp).testTag("copy_code_button")
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy Hook Code",
              tint = Color(0xFF38BDF8),
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = """
// Intercepts and replaces registerProximitySensor
XposedBridge.hookAllMethods(
    c,
    "registerProximitySensor",
    new XC_MethodReplacement() {
        @Override
        protected Object replaceHookedMethod(
            MethodHookParam param) {
            return null; // skips listener registration
        }
    }
);
          """.trimIndent(),
          fontFamily = FontFamily.Monospace,
          fontSize = 12.sp,
          color = Color(0xFF38BDF8)
        )
      }
    }

    // Logcat Diagnostic Card
    OutlinedCard(
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier.fillMaxWidth().testTag("logcat_card")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Terminal, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Verification & Logcat Filter",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Check LSPosed logs for this confirmation string:",
          style = MaterialTheme.typography.bodySmall
        )
        Text(
          text = "\"NoWakeProximity: hooked registerProximitySensor\"",
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        val logcatCmd = "adb logcat | grep -E \"NoWakeProximity|proximity_sensor\""
        Button(
          onClick = {
            clipboardManager.setText(AnnotatedString(logcatCmd))
            Toast.makeText(context, "Copied adb logcat command!", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.fillMaxWidth().testTag("copy_logcat_button")
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Copy Logcat Shell Filter")
        }
      }
    }
  }
}

@Composable
fun InfoRow(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}
