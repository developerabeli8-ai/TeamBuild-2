package com.teambuild.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val Teal = Color(0xFF005C66)
private val TealDark = Color(0xFF00434A)
private val TealTint = Color(0xFFE0F2F1)
private val Canvas = Color(0xFFFBF9F9)
private val Ink = Color(0xFF1B1C1C)
private val Muted = Color(0xFF5E6869)
private val Border = Color(0xFFE5E7EB)
private val CardSurface = Color.White
private val Error = Color(0xFFBA1A1A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TeamBuildApp() }
    }
}

private enum class Destination(val label: String) {
    Chat("New chat"),
    Missions("Missions"),
    Files("Files & artifacts"),
    Approvals("Approvals"),
    Settings("Settings"),
    Providers("AI providers"),
    ConnectProvider("Connect provider")
}

private data class ChatLine(val text: String, val fromUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamBuildApp() {
    var destination by rememberSaveable { mutableStateOf(Destination.Chat.name) }
    var draft by rememberSaveable { mutableStateOf("") }
    var approvalStatus by rememberSaveable { mutableStateOf("Action required") }
    var providerConnected by rememberSaveable { mutableStateOf(false) }
    val messages = remember { mutableStateListOf<ChatLine>() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Teal,
            onPrimary = Color.White,
            primaryContainer = TealTint,
            onPrimaryContainer = TealDark,
            background = Canvas,
            surface = CardSurface,
            onSurface = Ink,
            onSurfaceVariant = Muted,
            error = Error
        )
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Canvas,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    DrawerContent(
                        current = Destination.valueOf(destination),
                        onSelect = { next ->
                            destination = next.name
                            scope.launch { drawerState.close() }
                            if (next == Destination.Chat) {
                                messages.clear()
                                approvalStatus = "Action required"
                            }
                        }
                    )
                }
            }
        ) {
            Scaffold(
                containerColor = Canvas,
                topBar = {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Hub, contentDescription = null, tint = Teal)
                                Spacer(Modifier.width(8.dp))
                                Text("TeamBuild", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Open navigation")
                            }
                        },
                        actions = {
                            IconButton(onClick = { destination = Destination.Approvals.name }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                            }
                            IconButton(onClick = { destination = Destination.Settings.name }) {
                                Icon(Icons.Default.Person, contentDescription = "Account")
                            }
                        }
                    )
                },
                bottomBar = {
                    if (Destination.valueOf(destination) == Destination.Chat) {
                        ChatComposer(
                            draft = draft,
                            onDraftChange = { draft = it },
                            onSend = {
                                val text = draft.trim()
                                if (text.isNotEmpty()) {
                                    messages.add(ChatLine(text, true))
                                    messages.add(
                                        ChatLine(
                                            "I've organized an autonomous squad to work on that. Here's the proposed execution plan.",
                                            false
                                        )
                                    )
                                    draft = ""
                                }
                            }
                        )
                    }
                }
            ) { padding ->
                when (Destination.valueOf(destination)) {
                    Destination.Chat -> ChatScreen(
                        messages = messages,
                        approvalStatus = approvalStatus,
                        onApproval = { approvalStatus = it },
                        onSuggestion = { draft = it },
                        modifier = Modifier.padding(padding)
                    )
                    Destination.Missions -> SimpleDestination(
                        "Missions",
                        "Your AI team's active and completed work will appear here.",
                        Icons.Default.AutoAwesome,
                        Modifier.padding(padding)
                    )
                    Destination.Files -> SimpleDestination(
                        "Files & artifacts",
                        "Generated reports, spreadsheets, and research files will be collected here.",
                        Icons.Default.FolderOpen,
                        Modifier.padding(padding)
                    )
                    Destination.Approvals -> ApprovalsScreen(
                        status = approvalStatus,
                        onDecision = { approvalStatus = it },
                        modifier = Modifier.padding(padding)
                    )
                    Destination.Settings -> SettingsScreen(
                        providerConnected = providerConnected,
                        onManageProvider = { destination = Destination.Providers.name },
                        modifier = Modifier.padding(padding)
                    )
                    Destination.Providers -> ProviderScreen(
                        connected = providerConnected,
                        onConnect = { destination = Destination.ConnectProvider.name },
                        onDisconnect = { providerConnected = false },
                        modifier = Modifier.padding(padding)
                    )
                    Destination.ConnectProvider -> ConnectProviderScreen(
                        onConnected = {
                            providerConnected = true
                            destination = Destination.Providers.name
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerContent(current: Destination, onSelect: (Destination) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
            Icon(Icons.Default.Hub, contentDescription = null, tint = Teal, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("TeamBuild", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Personal workspace", color = Muted, fontSize = 12.sp)
            }
        }
        Button(
            onClick = { onSelect(Destination.Chat) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Teal)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("New chat")
        }
        Text("WORKSPACE", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        DrawerItem("Missions", Icons.Default.AutoAwesome, current == Destination.Missions) { onSelect(Destination.Missions) }
        DrawerItem("Files & artifacts", Icons.Default.FolderOpen, current == Destination.Files) { onSelect(Destination.Files) }
        DrawerItem("Approvals", Icons.Default.Security, current == Destination.Approvals) { onSelect(Destination.Approvals) }
        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Border)
        Text("ACCOUNT", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
        DrawerItem("AI providers", Icons.Default.SmartToy, current == Destination.Providers) { onSelect(Destination.Providers) }
        DrawerItem("Settings", Icons.Default.Settings, current == Destination.Settings) { onSelect(Destination.Settings) }
        Spacer(Modifier.weight(1f))
        HorizontalDivider(color = Border)
        Row(Modifier.fillMaxWidth().padding(top = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(38.dp).background(TealTint, CircleShape), contentAlignment = Alignment.Center) {
                Text("A", color = Teal, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Abel Timtim", fontWeight = FontWeight.SemiBold)
                Text("Free plan", color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun DrawerItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 2.dp)
            .background(if (selected) TealTint else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = if (selected) Teal else Muted, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(label, color = if (selected) TealDark else Ink, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
    }
}

@Composable
private fun ChatScreen(
    messages: List<ChatLine>,
    approvalStatus: String,
    onApproval: (String) -> Unit,
    onSuggestion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (messages.isEmpty()) {
            item {
                Column(
                    Modifier.fillMaxWidth().padding(top = 46.dp, bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier.size(64.dp).background(TealTint, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Hub, contentDescription = null, tint = Teal, modifier = Modifier.size(32.dp))
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Hi, Abel", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = TealDark)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "What would you like your AI team to build?",
                        color = Muted,
                        fontSize = 15.sp
                    )
                    Spacer(Modifier.height(28.dp))
                    SuggestionCard("Create a business plan", "Research, financials, and a polished draft") {
                        onSuggestion("Create a business plan for a coffee-delivery startup in Nairobi")
                    }
                    Spacer(Modifier.height(10.dp))
                    SuggestionCard("Research a topic", "Gather sources and synthesize the findings") {
                        onSuggestion("Research a topic and summarize the findings")
                    }
                    Spacer(Modifier.height(10.dp))
                    SuggestionCard("Create a study plan", "Build a structured learning path") {
                        onSuggestion("Create a structured study plan")
                    }
                }
            }
        } else {
            items(messages) { message ->
                if (message.fromUser) UserMessage(message.text) else {
                    AssistantMessage(message.text)
                    AgentPlan()
                    ApprovalCard(approvalStatus, onApproval)
                }
            }
        }
    }
}

@Composable
private fun SuggestionCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Teal)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted)
        }
    }
}

@Composable
private fun UserMessage(text: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E8E7)),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
        ) {
            Text(text, Modifier.padding(14.dp), color = Ink)
        }
    }
}

@Composable
private fun AssistantMessage(text: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Hub, contentDescription = null, tint = Teal, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("TeamBuild AI", color = Teal, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text("  Execution squad", color = Muted, fontSize = 12.sp)
        }
        Spacer(Modifier.height(8.dp))
        Card(colors = CardDefaults.cardColors(containerColor = CardSurface)) {
            Text(text, Modifier.padding(14.dp), color = Ink, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun AgentPlan() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AgentCard("Researcher Agent", "Nairobi regional data", "Scraping competitor pricing across Westlands & Kilimani", "68%", Icons.Default.TravelExplore)
        AgentCard("Financial Analyst", "Modeler V2.4", "Calculating unit economics & fleet fuel cost", "42%", Icons.Default.Calculate)
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F3F3))) {
            Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EditNote, contentDescription = null, tint = Muted)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Writer Agent", fontWeight = FontWeight.SemiBold)
                    Text("Waiting for competitor analysis", fontSize = 12.sp, color = Muted)
                }
                StatusPill("QUEUED", TealTint, TealDark)
            }
        }
    }
}

@Composable
private fun AgentCard(name: String, model: String, task: String, progress: String, icon: ImageVector) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F3F3)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Teal)
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.SemiBold)
                    Text(model, fontSize = 11.sp, color = Muted)
                }
                StatusPill(if (progress == "68%") "RUNNING" else "PROCESSING", TealTint, TealDark)
            }
            Text(task, color = Muted, fontSize = 12.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("In progress", color = Muted, fontSize = 11.sp)
                Text(progress, color = Teal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            LinearProgressIndicator(
                progress = { progress.removeSuffix("%").toFloat() / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = Teal,
                trackColor = Color(0xFFD8E1E1)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Tag("Web Search")
                Tag("Maps API")
            }
        }
    }
}

@Composable
private fun StatusPill(label: String, background: Color, foreground: Color) {
    Text(label, Modifier.background(background, CircleShape).padding(horizontal = 9.dp, vertical = 5.dp), color = foreground, fontSize = 9.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun Tag(label: String) {
    Text(label, Modifier.background(Color.White, CircleShape).padding(horizontal = 9.dp, vertical = 5.dp), color = Muted, fontSize = 10.sp)
}

@Composable
private fun ApprovalCard(status: String, onDecision: (String) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F7)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFDAD6))
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = Teal)
                Spacer(Modifier.width(8.dp))
                Text("Approval request", Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                StatusPill(status.uppercase(), if (status == "Action required") Color(0xFFFFDAD6) else TealTint, if (status == "Action required") Error else TealDark)
            }
            Text(
                "Researcher squad requests KES 1,800 ($14.00) for live traffic-routing research.",
                color = Muted,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Review", "Approve", "Edit", "Reject").forEach { action ->
                    OutlinedButton(
                        onClick = { onDecision(if (action == "Approve") "Approved" else if (action == "Reject") "Rejected" else action) },
                        modifier = Modifier.weight(1f),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(action, fontSize = 10.sp, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatComposer(draft: String, onDraftChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { }) { Icon(Icons.Default.Add, contentDescription = "Attach") }
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Ask your AI team to build...", fontSize = 13.sp) },
            maxLines = 4,
            shape = RoundedCornerShape(14.dp)
        )
        IconButton(onClick = onSend, enabled = draft.isNotBlank()) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = if (draft.isBlank()) Muted else Teal)
        }
    }
}

@Composable
private fun SimpleDestination(title: String, subtitle: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = Teal, modifier = Modifier.size(44.dp))
        Spacer(Modifier.height(14.dp))
        Text(title, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, color = Muted)
    }
}

@Composable
private fun ApprovalsScreen(status: String, onDecision: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Approvals", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Review actions that need your approval.", color = Muted)
        ApprovalCard(status, onDecision)
    }
}

@Composable
private fun SettingsScreen(
    providerConnected: Boolean,
    onManageProvider: () -> Unit,
    modifier: Modifier = Modifier
) {
    var missionUpdates by rememberSaveable { mutableStateOf(true) }
    var approvalAlerts by rememberSaveable { mutableStateOf(true) }
    Column(modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        Card(colors = CardDefaults.cardColors(containerColor = CardSurface), border = androidx.compose.foundation.BorderStroke(1.dp, Border)) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(42.dp).background(TealTint, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Teal)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Abel Timtim", fontWeight = FontWeight.SemiBold)
                        Text("abel@example.com", color = Muted, fontSize = 12.sp)
                    }
                    StatusPill("ACTIVE", TealTint, TealDark)
                }
                TextButton(onClick = { }) { Text("Edit profile") }
            }
        }
        SettingsLink("AI provider", if (providerConnected) "OpenRouter connected" else "No provider connected", onManageProvider)
        SettingsLink("Default model", "Automatic", onManageProvider)
        Text("NOTIFICATIONS", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        SettingToggle("Mission updates", missionUpdates) { missionUpdates = it }
        SettingToggle("Approval requests", approvalAlerts) { approvalAlerts = it }
        SettingToggle("Completed missions", true) { }
        Text("PLAN & USAGE", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        Card(colors = CardDefaults.cardColors(containerColor = CardSurface), border = androidx.compose.foundation.BorderStroke(1.dp, Border)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Free plan", fontWeight = FontWeight.SemiBold)
                    Text("3 of 5 missions", color = Muted, fontSize = 12.sp)
                }
                LinearProgressIndicator(progress = { 0.6f }, modifier = Modifier.fillMaxWidth(), color = Teal)
                Text("Resets monthly", color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SettingsLink(title: String, detail: String, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(detail, color = Muted, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted)
        }
    }
}

@Composable
private fun SettingToggle(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun ProviderScreen(
    connected: Boolean,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("AI providers", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Choose how your AI team connects to models.", color = Muted)
        Card(colors = CardDefaults.cardColors(containerColor = TealTint)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(if (connected) "OpenRouter connected" else "No active provider", fontWeight = FontWeight.Bold)
                Text("Default model: Automatic", color = Muted, fontSize = 13.sp)
                Text("KES 450 / KES 2,000 monthly spend", color = Muted, fontSize = 12.sp)
                LinearProgressIndicator(progress = { 0.225f }, modifier = Modifier.fillMaxWidth(), color = Teal)
                Button(onClick = onConnect, colors = ButtonDefaults.buttonColors(containerColor = Teal)) {
                    Text(if (connected) "Manage provider" else "Connect a provider")
                }
                if (connected) TextButton(onClick = onDisconnect) { Text("Disconnect provider", color = Error) }
            }
        }
        listOf(
            "OpenRouter" to "One API for many AI models",
            "Google Gemini" to "Use Gemini models",
            "OpenAI" to "Use GPT models",
            "Anthropic Claude" to "Use Claude models",
            "Custom provider" to "Connect an OpenAI-compatible API"
        ).forEach { (name, detail) ->
            SettingsLink(name, detail, onConnect)
        }
        Text("API keys are write-only in this demo. Connect a backend vault before using real keys.", color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun ConnectProviderScreen(onConnected: () -> Unit, modifier: Modifier = Modifier) {
    var apiKey by rememberSaveable { mutableStateOf("") }
    var accepted by rememberSaveable { mutableStateOf(false) }
    var showKey by rememberSaveable { mutableStateOf(false) }
    Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Teal)
            Spacer(Modifier.width(8.dp))
            Text("Connect provider", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Text("Connect OpenRouter to choose from a range of AI models.", color = Muted)
        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("API key") },
            singleLine = true,
            visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { showKey = !showKey }) {
                    Icon(if (showKey) Icons.Default.Close else Icons.Default.Security, contentDescription = "Toggle key visibility")
                }
            }
        )
        OutlinedTextField(
            value = "KES 2,000",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Monthly spending limit") },
            readOnly = true,
            supportingText = { Text("Requests pause automatically when this limit is reached.") }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Text("I understand this key may be used for AI requests.", fontSize = 13.sp)
        }
        Button(
            onClick = onConnected,
            enabled = apiKey.isNotBlank() && accepted,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Teal)
        ) {
            Text("Connect provider")
        }
        Text(
            "Demo only: keys are not sent or stored. Add a secure backend vault before using real provider credentials.",
            color = Muted,
            fontSize = 12.sp
        )
    }
}
