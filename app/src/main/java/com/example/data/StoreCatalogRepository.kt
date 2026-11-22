package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.model.AppScreenshotPreview
import com.example.model.ScreenshotRowPreview
import com.example.model.StoreApp
import com.example.model.StoreTab
import java.util.Calendar
import java.util.TimeZone

class StoreCatalogRepository {
  private fun utcMillis(year: Int, monthZeroBased: Int, day: Int, hour: Int, minute: Int): Long {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.set(year, monthZeroBased, day, hour, minute, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
  }

  fun getApps(): List<StoreApp> {
    val nov20Morning = utcMillis(2026, Calendar.NOVEMBER, 20, 8, 40)
    val nov20Latest = utcMillis(2026, Calendar.NOVEMBER, 20, 19, 20)
    val nov20Newest = utcMillis(2026, Calendar.NOVEMBER, 20, 19, 47)
    val nov21Newest = utcMillis(2026, Calendar.NOVEMBER, 21, 12, 8)
    val nov22Newest = utcMillis(2026, Calendar.NOVEMBER, 22, 12, 37)
    return listOf(
      StoreApp(
        id = 11,
        name = "NICH Employee Attendance 2",
        developer = "AS Plus Limited",
        category = "Business • Attendance",
        tab = StoreTab.Apps,
        rating = "2.5 ★",
        downloads = "500",
        reviews = "500 downloads",
        ageRating = "3+",
        sizeLabel = "28 MB",
        version = "v1.4",
        color = Color(0xFF0B57D0),
        iconText = "N2",
        logoResId = R.drawable.logo_nich_attendance_2,
        fillLogoBounds = true,
        heroTitle = "Track attendance with NICH Employee Attendance 2",
        promoTitle = "NICH EMPLOYEE ATTENDANCE 2 • AS PLUS LIMITED",
        promoText = "Official attendance portal release v1.4 ready for direct APK download.",
        about =
          "NICH Employee Attendance 2 by AS Plus Limited enables fast, reliable employee attendance check-in and workforce tracking.",
        downloadUrl =
          "https://www.mediafire.com/file/dq27ppr8z9wuc8v/NICH_Employee_Attendance_2_1.4.apk/file",
        addedAtEpochMillis = nov20Latest,
        isPlayStoreApp = false,
        sourceLabel = "Direct APK • v1.4",
        featured = true,
        screenshots =
          listOf(
            AppScreenshotPreview(
              caption = "NICH Employee Attendance 2 Launch Screen",
              imageResId = R.drawable.shot_nich_1,
            ),
            AppScreenshotPreview(
              caption = "VIP Access & Voice Attendance Terminal",
              imageResId = R.drawable.shot_nich_2,
            ),
            AppScreenshotPreview(
              caption = "Procedural Face Scan Biometric Mesh",
              imageResId = R.drawable.shot_nich_3,
            ),
            AppScreenshotPreview(
              caption = "Palm Scan Sensor Verification",
              imageResId = R.drawable.shot_nich_4,
            ),
          ),
      ),
      StoreApp(
        id = 12,
        name = "Rider Profit 1.0",
        developer = "AS Plus Limited",
        category = "Finance • Rider Tracker",
        tab = StoreTab.Apps,
        rating = "4.8 ★",
        downloads = "1M+",
        reviews = "1M+ downloads",
        ageRating = "3+",
        sizeLabel = "23 MB",
        version = "v1.0",
        color = Color(0xFFC81048),
        iconText = "RP",
        logoResId = R.drawable.logo_rider_profit_1_0,
        fillLogoBounds = true,
        heroTitle = "Track rider income & expenses with Rider Profit 1.0",
        promoTitle = "RIDER PROFIT 1.0 • AS PLUS LIMITED",
        promoText = "Track daily delivery trips, fuel costs, and net rider earnings effortlessly.",
        about =
          "Rider Profit 1.0 by AS Plus Limited helps delivery riders log daily rides, track expenses, and calculate clear profit margins in real time.",
        downloadUrl =
          "https://www.mediafire.com/file/klxzx12f9fjvs17/Rider_Profit_1.0.apk/file",
        addedAtEpochMillis = nov20Newest,
        isPlayStoreApp = false,
        sourceLabel = "Direct APK • v1.0",
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "Rider Profit 1.0",
              caption = "Daily Profit Summary",
              backgroundColor = Color(0xFFFFF5F7),
              textColor = Color(0xFF1F1218),
              topBarColor = Color(0xFFC81048),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview(
                    "Net Profit Tracker",
                    "Trips, Tips & Fuel Expenses",
                    Color(0xFFC81048),
                    Color.White,
                  ),
                  ScreenshotRowPreview(
                    "Earnings Overview",
                    "Daily & Weekly Breakdown",
                    Color.White,
                    Color(0xFF1F1218),
                    Color(0xFFFECDD3),
                  ),
                  ScreenshotRowPreview(
                    "AS Plus Limited",
                    "Official Release v1.0",
                    Color(0xFFFFFBEB),
                    Color(0xFF92400E),
                  ),
                ),
            ),
            AppScreenshotPreview(
              header = "Expense & Fuel Logs",
              caption = "Smart Rider Insights",
              backgroundColor = Color.White,
              textColor = Color(0xFF1F1218),
              topBarColor = Color(0xFF9F1239),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview(
                    "Profit Margin",
                    "Instant Net Income View",
                    Color(0xFFECFDF5),
                    Color(0xFF065F46),
                  ),
                  ScreenshotRowPreview(
                    "Ride History",
                    "Fast Trip & Cash Entry",
                    Color(0xFFF8FAFC),
                    Color(0xFF1F1218),
                    Color(0xFFE2E8F0),
                  ),
                  ScreenshotRowPreview(
                    "23 MB Package",
                    "Fast Direct APK Install",
                    Color(0xFFC81048),
                    Color.White,
                  ),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 61,
        name = "NICH Employee Attendance 3",
        developer = "NICH",
        category = "Business • Attendance",
        tab = StoreTab.Apps,
        rating = "4.8 ★",
        downloads = "1k",
        reviews = "1k downloads",
        ageRating = "3+",
        sizeLabel = "110 MB",
        version = "v2.0",
        color = Color(0xFF0B57D0),
        iconText = "N3",
        logoResId = R.drawable.logo_nich_attendance_3,
        fillLogoBounds = true,
        heroTitle = "Track attendance with NICH Employee Attendance 3",
        promoTitle = "NICH EMPLOYEE ATTENDANCE 3 • NICH",
        promoText =
          "Smart employee attendance app with face liveness check, CNIC scanning, PDF reports, and auto-updates.",
        about =
          "Smart employee attendance app with face liveness check, CNIC scanning, PDF reports, and auto-updates.",
        downloadUrl =
          "https://www.mediafire.com/file/7jngoot02aruyt3/NICH_Employee_Attendance_3_2.0.apk/file",
        addedAtEpochMillis = nov21Newest,
        isPlayStoreApp = false,
        sourceLabel = "Direct APK • v2.0",
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "NICH Attendance 3",
              caption = "Face Liveness & CNIC Scan",
              backgroundColor = Color(0xFFF4F8FF),
              textColor = Color(0xFF0F172A),
              topBarColor = Color(0xFF0B57D0),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview(
                    "Face Liveness Check",
                    "Anti-spoof biometric check-in",
                    Color(0xFF0B57D0),
                    Color.White,
                  ),
                  ScreenshotRowPreview(
                    "CNIC Scanning",
                    "Instant employee ID verification",
                    Color.White,
                    Color(0xFF0F172A),
                    Color(0xFFDBEAFE),
                  ),
                  ScreenshotRowPreview(
                    "Verified Attendance",
                    "Fast & accurate workforce log",
                    Color(0xFFECFDF5),
                    Color(0xFF065F46),
                  ),
                ),
            ),
            AppScreenshotPreview(
              header = "Reports & Updates",
              caption = "PDF Reports & Auto-Updates",
              backgroundColor = Color.White,
              textColor = Color(0xFF0F172A),
              topBarColor = Color(0xFF0842A0),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview(
                    "PDF Reports",
                    "Export attendance summaries",
                    Color(0xFFEFF6FF),
                    Color(0xFF1E3A8A),
                  ),
                  ScreenshotRowPreview(
                    "Auto-Updates",
                    "Seamless v2.0 release channel",
                    Color(0xFFF8FAFC),
                    Color(0xFF0F172A),
                    Color(0xFFE2E8F0),
                  ),
                  ScreenshotRowPreview(
                    "110 MB Package",
                    "Direct APK by NICH",
                    Color(0xFF0B57D0),
                    Color.White,
                  ),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 62,
        name = "ZEE 5",
        developer = "Zee Entertainment Enterprises Limited",
        category = "Entertainment • Streaming",
        tab = StoreTab.Apps,
        rating = "3.6 ★",
        downloads = "100M+",
        reviews = "100M+ downloads",
        ageRating = "12+",
        sizeLabel = "129 MB",
        version = "v39.64.4",
        color = Color(0xFF1A1926),
        iconText = "Z5",
        logoResId = R.drawable.logo_zee_5,
        fillLogoBounds = true,
        heroTitle = "Download ZEE 5 v39.64.4",
        promoTitle = "ZEE 5 • ZEE ENTERTAINMENT ENTERPRISES LIMITED",
        promoText =
          "Smart employee attendance app with face liveness check, CNIC scanning, PDF reports, and auto-updates.",
        about =
          "Smart employee attendance app with face liveness check, CNIC scanning, PDF reports, and auto-updates.",
        downloadUrl = "https://getmodsapk.com/zee5-mod-apk-2022/download/675320/",
        addedAtEpochMillis = nov22Newest,
        isPlayStoreApp = false,
        sourceLabel = "Direct APK • v39.64.4",
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "ZEE 5",
              caption = "ZEE 5 Overview",
              backgroundColor = Color(0xFFF6F6FA),
              textColor = Color(0xFF1A1926),
              topBarColor = Color(0xFF1A1926),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview(
                    "ZEE 5 v39.64.4",
                    "Zee Entertainment Enterprises Limited",
                    Color(0xFF1A1926),
                    Color.White,
                  ),
                  ScreenshotRowPreview(
                    "Rated 3.6 ★ • 100M+",
                    "Popular release on AS Plus Store",
                    Color.White,
                    Color(0xFF1A1926),
                    Color(0xFFE2E8F0),
                  ),
                  ScreenshotRowPreview(
                    "Direct APK Ready",
                    "129 MB quick download",
                    Color(0xFFEFF6FF),
                    Color(0xFF1E3A8A),
                  ),
                ),
            ),
            AppScreenshotPreview(
              header = "Release Details",
              caption = "Version 39.64.4 Features",
              backgroundColor = Color.White,
              textColor = Color(0xFF1A1926),
              topBarColor = Color(0xFF2B293D),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview(
                    "Core Features",
                    "Face liveness, CNIC scan & PDF reports",
                    Color(0xFFF8FAFC),
                    Color(0xFF1A1926),
                    Color(0xFFE2E8F0),
                  ),
                  ScreenshotRowPreview(
                    "Auto-Updates",
                    "Latest v39.64.4 build",
                    Color(0xFFECFDF5),
                    Color(0xFF065F46),
                  ),
                  ScreenshotRowPreview(
                    "129 MB Package",
                    "Direct APK Download",
                    Color(0xFF1A1926),
                    Color.White,
                  ),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 63,
        name = "Exion Hill Racing",
        developer = "Exion Games",
        category = "Games • Racing",
        tab = StoreTab.Apps,
        rating = "4.3 ★",
        downloads = "50M+",
        reviews = "Google Play",
        ageRating = "3+",
        sizeLabel = "76 MB",
        version = "Play Store",
        color = Color(0xFF0284C7),
        iconText = "EH",
        logoResId = R.drawable.logo_exionhillracing,
        fillLogoBounds = true,
        heroTitle = "Race rugged terrain in Exion Hill Racing",
        promoTitle = "EXION HILL RACING • EXION GAMES",
        promoText =
          "Physics-based hill racing across challenging terrain with upgradeable engine, suspension, and tires.",
        about =
          "Exion Hill Racing is a realistic physics-based speed racing game. Upgradeable parts are the engine, suspension and tires.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.exiongames.exionhillracing",
        addedAtEpochMillis = nov22Newest,
        isPlayStoreApp = true,
        isGame = true,
        screenshots =
          listOf(
            AppScreenshotPreview(
              caption = "Exion Hill Racing Gameplay 1",
              imageResId = R.drawable.shot_exionhillracing_1,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "Exion Hill Racing Gameplay 2",
              imageResId = R.drawable.shot_exionhillracing_2,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "Exion Hill Racing Gameplay 3",
              imageResId = R.drawable.shot_exionhillracing_3,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "Exion Hill Racing Gameplay 4",
              imageResId = R.drawable.shot_exionhillracing_4,
              isLandscape = true,
            ),
          ),
      ),
      StoreApp(
        id = 64,
        name = "Devices Tycoon",
        developer = "Roastery Games",
        category = "Games • Simulation",
        tab = StoreTab.Apps,
        rating = "3.8 ★",
        downloads = "1M+",
        reviews = "Google Play",
        ageRating = "3+",
        sizeLabel = "75 MB",
        version = "Play Store",
        color = Color(0xFF1D4ED8),
        iconText = "DT",
        logoResId = R.drawable.logo_devicestycoon,
        fillLogoBounds = true,
        heroTitle = "Build your tech empire in Devices Tycoon",
        promoTitle = "DEVICES TYCOON • ROASTERY GAMES",
        promoText =
          "Design custom smartphones, laptops, tablets, smartwatches, OS, and processors from your start-up.",
        about =
          "Welcome to Devices Tycoon! This is a unique business simulator that will allow you to feel like the owner of a company to create your own devices! Create your own smartphones, tablets, laptops, smart watches, headphones, operating systems and even your own processors, hire top engineers and designers, upgrade over 16 offices, and customize over 10,000 hardware combinations.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.roasterygames.devicestycoon",
        addedAtEpochMillis = nov22Newest,
        isPlayStoreApp = true,
        isGame = true,
        screenshots =
          listOf(
            AppScreenshotPreview(
              caption = "Create Laptops & Hardware",
              imageResId = R.drawable.shot_devicestycoon_1,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "Devices Tycoon Gameplay 2",
              imageResId = R.drawable.shot_devicestycoon_2,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "Devices Tycoon Gameplay 3",
              imageResId = R.drawable.shot_devicestycoon_3,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "Devices Tycoon Gameplay 4",
              imageResId = R.drawable.shot_devicestycoon_4,
              isLandscape = true,
            ),
          ),
      ),
      StoreApp(
        id = 65,
        name = "GTA: San Andreas - Definitive",
        developer = "Rockstar Games",
        category = "Games • Action",
        tab = StoreTab.Apps,
        rating = "4.2 ★",
        downloads = "50K+",
        reviews = "Google Play",
        ageRating = "17+",
        sizeLabel = "2.8 GB",
        version = "Play Store",
        color = Color(0xFF3F6212),
        iconText = "SA",
        logoResId = R.drawable.logo_gtasade,
        fillLogoBounds = true,
        heroTitle = "Return to Los Santos in GTA: San Andreas - Definitive",
        promoTitle = "GTA: SAN ANDREAS - DEFINITIVE • ROCKSTAR GAMES",
        promoText =
          "Updated open-world classic featuring high-resolution graphics, enhanced lighting, and modern mobile controls.",
        about =
          "Five years ago, Carl 'CJ' Johnson escaped the haze of Los Santos, San Andreas... a city tearing itself apart with gang trouble, drugs, and corruption. Now, it's the early 90s. CJ's got to go home — his mother has been murdered, his family has fallen apart, and his childhood friends are all heading towards disaster. Forced on a journey across the entire state of San Andreas to save his family and take control of the streets.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.rockstargames.gtasa.de",
        addedAtEpochMillis = nov22Newest,
        isPlayStoreApp = true,
        isGame = true,
        screenshots =
          listOf(
            AppScreenshotPreview(
              caption = "Los Santos Skyline",
              imageResId = R.drawable.shot_gtasade_1,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "GTA San Andreas Screenshot 2",
              imageResId = R.drawable.shot_gtasade_2,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "GTA San Andreas Screenshot 3",
              imageResId = R.drawable.shot_gtasade_3,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "GTA San Andreas Screenshot 4",
              imageResId = R.drawable.shot_gtasade_4,
              isLandscape = true,
            ),
          ),
      ),
      StoreApp(
        id = 66,
        name = "Grand Theft Auto: Vice City",
        developer = "Rockstar Games",
        category = "Games • Action",
        tab = StoreTab.Apps,
        rating = "3.3 ★",
        downloads = "1M+",
        reviews = "Google Play",
        ageRating = "17+",
        sizeLabel = "1.4 GB",
        version = "Play Store",
        color = Color(0xFFDB2777),
        iconText = "VC",
        logoResId = R.drawable.logo_gtavc,
        fillLogoBounds = true,
        heroTitle = "Explore 1980s Vice City in Grand Theft Auto: Vice City",
        promoTitle = "GRAND THEFT AUTO: VICE CITY • ROCKSTAR GAMES",
        promoText =
          "Iconic 1980s open-world adventure with updated graphics, character models, lighting effects, and custom controls.",
        about =
          "Welcome back to Vice City. Welcome back to the 1980s. From the decade of big hair, excess and pastel suits comes a story of one man's rise to the top of the criminal pile. Vice City is a huge urban sprawl ranging from the beach to the swamps and the glitz to the ghetto, combining open-world gameplay with a character-driven narrative, updated high-resolution graphics, and custom mobile controls.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.rockstargames.gtavc",
        addedAtEpochMillis = nov22Newest,
        isPlayStoreApp = true,
        isGame = true,
        screenshots =
          listOf(
            AppScreenshotPreview(
              caption = "Vice City Neon Streets",
              imageResId = R.drawable.shot_gtavc_1,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "GTA Vice City Screenshot 2",
              imageResId = R.drawable.shot_gtavc_2,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "GTA Vice City Screenshot 3",
              imageResId = R.drawable.shot_gtavc_3,
              isLandscape = true,
            ),
            AppScreenshotPreview(
              caption = "GTA Vice City Screenshot 4",
              imageResId = R.drawable.shot_gtavc_4,
              isLandscape = true,
            ),
          ),
      ),
      StoreApp(
        id = 1,
        name = "WhatsApp",
        developer = "WhatsApp LLC",
        category = "Communication • Messaging",
        tab = StoreTab.Apps,
        rating = "4.3 ★",
        downloads = "10B+",
        reviews = "Google Play",
        ageRating = "16+",
        sizeLabel = "68 MB",
        version = "Play Store",
        color = Color(0xFF25D366),
        iconText = "W",
        logoResId = R.drawable.ic_app_whatsapp,
        heroTitle = "Simple, reliable messaging with WhatsApp",
        promoTitle = "WHATSAPP • SIMPLE, RELIABLE MESSAGING",
        promoText = "End-to-end encrypted messaging, voice & video calls, and channels.",
        about =
          "WhatsApp from Meta is a FREE messaging and video calling app used by over 2B people in more than 180 countries.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.whatsapp",
        addedAtEpochMillis = nov20Morning,
        featured = false,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "WhatsApp",
              caption = "Chats & Voice Notes",
              backgroundColor = Color.White,
              textColor = Color(0xFF111B21),
              topBarColor = Color(0xFF075E54),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Family Group", "Photo • 9:40 AM", Color(0xFFE7FCE3), Color(0xFF111B21)),
                  ScreenshotRowPreview("Office Team", "Meeting at 11:00 AM", Color(0xFFF0F2F5), Color(0xFF111B21)),
                  ScreenshotRowPreview("Encrypted Calls", "HD Voice & Video", Color(0xFF25D366), Color.White),
                ),
            ),
            AppScreenshotPreview(
              header = "Updates",
              caption = "Status & Channels",
              backgroundColor = Color(0xFFEFEAE2),
              textColor = Color(0xFF111B21),
              topBarColor = Color(0xFF008069),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("My Status", "Tap to add status update", Color.White, Color(0xFF111B21)),
                  ScreenshotRowPreview("Channels", "Follow verified updates", Color.White, Color(0xFF111B21)),
                  ScreenshotRowPreview("Private & Secure", "End-to-end encrypted", Color(0xFFD9FDD3), Color(0xFF111B21)),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 2,
        name = "TikTok",
        developer = "TikTok Pte. Ltd.",
        category = "Social • Short Videos",
        tab = StoreTab.Apps,
        rating = "4.4 ★",
        downloads = "1B+",
        reviews = "Google Play",
        ageRating = "12+",
        sizeLabel = "138 MB",
        version = "Play Store",
        color = Color(0xFF0D0D0F),
        iconText = "T",
        logoResId = R.drawable.ic_app_tiktok,
        heroTitle = "Watch trending videos on TikTok",
        promoTitle = "TIKTOK • VIDEOS, TRENDS & CREATORS",
        promoText = "Watch personalized short videos, add music & effects, and go live.",
        about =
          "TikTok is the leading destination for mobile short-form videos—watch, create, and discover trending clips and creators.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.zhiliaoapp.musically",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "Following | For You",
              caption = "For You Video Feed",
              backgroundColor = Color(0xFF121216),
              textColor = Color.White,
              topBarColor = Color.Black,
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("@creative_hub", "Trending Reel • 240K Likes", Color(0xFF1F1F26), Color.White),
                  ScreenshotRowPreview("Original Sound", "Tap to use audio", Color(0xFF25F4EE), Color.Black),
                  ScreenshotRowPreview("Create Video", "Filters & Effects", Color(0xFFFE2C55), Color.White),
                ),
            ),
            AppScreenshotPreview(
              header = "Discover",
              caption = "Trending Hashtags",
              backgroundColor = Color.White,
              textColor = Color(0xFF161823),
              topBarColor = Color(0xFF161823),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("#TechTips", "1.4M videos today", Color(0xFFF1F1F2), Color(0xFF161823)),
                  ScreenshotRowPreview("#TravelVlogs", "Explore destinations", Color(0xFFF1F1F2), Color(0xFF161823)),
                  ScreenshotRowPreview("LIVE Streams", "Watch creators live", Color(0xFF161823), Color.White),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 3,
        name = "ChatGPT",
        developer = "OpenAI",
        category = "Productivity • AI Assistant",
        tab = StoreTab.Apps,
        rating = "4.7 ★",
        downloads = "100M+",
        reviews = "Google Play",
        ageRating = "12+",
        sizeLabel = "42 MB",
        version = "Play Store",
        color = Color(0xFF10A37F),
        iconText = "AI",
        logoResId = R.drawable.ic_app_chatgpt,
        heroTitle = "Get instant answers with ChatGPT",
        promoTitle = "CHATGPT • OFFICIAL OPENAI APP",
        promoText = "Get instant answers, creative inspiration, voice mode, and image help.",
        about =
          "The official ChatGPT app by OpenAI syncs your history across devices and brings the latest model improvements to your fingertips.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.openai.chatgpt",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "ChatGPT",
              caption = "Instant Answers",
              backgroundColor = Color.White,
              textColor = Color(0xFF111827),
              topBarColor = Color(0xFF10A37F),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("You", "Summarize today's notes", Color(0xFFF3F4F6), Color(0xFF111827)),
                  ScreenshotRowPreview(
                    "ChatGPT",
                    "Here are 3 key takeaways...",
                    Color(0xFFECFDF5),
                    Color(0xFF065F46),
                    Color(0xFFA7F3D0),
                  ),
                  ScreenshotRowPreview("Message ChatGPT", "Type, attach, or speak", Color(0xFF111827), Color.White),
                ),
            ),
            AppScreenshotPreview(
              header = "Voice Mode",
              caption = "Hands-Free Voice",
              backgroundColor = Color(0xFF0F172A),
              textColor = Color.White,
              topBarColor = Color(0xFF0F172A),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Listening...", "Natural real-time voice", Color(0xFF1E293B), Color.White),
                  ScreenshotRowPreview("Analyze Photos", "Snap & ask questions", Color(0xFF10A37F), Color.White),
                  ScreenshotRowPreview("Synced History", "Access chats anywhere", Color(0xFF1E293B), Color(0xFFCBD5E1)),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 4,
        name = "TikTok Lite",
        developer = "TikTok Pte. Ltd.",
        category = "Social • Lite Video",
        tab = StoreTab.Apps,
        rating = "4.3 ★",
        downloads = "1B+",
        reviews = "Google Play",
        ageRating = "12+",
        sizeLabel = "32 MB",
        version = "Play Store",
        color = Color(0xFF0D0D0F),
        iconText = "TL",
        logoResId = R.drawable.ic_app_tiktok_lite,
        heroTitle = "Fast & data-friendly TikTok Lite",
        promoTitle = "TIKTOK LITE • FAST & DATA-FRIENDLY",
        promoText = "Smooth video watching built for smaller storage and slower networks.",
        about =
          "TikTok Lite is a compact, fast version of TikTok designed to load videos quickly while saving mobile data and storage.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.zhiliaoapp.musically.go",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "TikTok Lite",
              caption = "Fast Video Playback",
              backgroundColor = Color(0xFF101014),
              textColor = Color.White,
              topBarColor = Color.Black,
              topBarTextColor = Color(0xFF25F4EE),
              rows =
                listOf(
                  ScreenshotRowPreview("Data Saver Mode", "Stream smoothly on 3G/4G", Color(0xFF1E1E24), Color.White),
                  ScreenshotRowPreview("Lightweight App", "Saves phone storage", Color(0xFF25F4EE), Color.Black),
                  ScreenshotRowPreview("For You Feed", "Curated short clips", Color(0xFFFE2C55), Color.White),
                ),
            ),
            AppScreenshotPreview(
              header = "Explore",
              caption = "Quick Search & Share",
              backgroundColor = Color.White,
              textColor = Color(0xFF161823),
              topBarColor = Color(0xFF161823),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Comedy & Sports", "Instant load previews", Color(0xFFF1F5F9), Color(0xFF161823)),
                  ScreenshotRowPreview("Music & Clips", "Share to friends", Color(0xFFF1F5F9), Color(0xFF161823)),
                  ScreenshotRowPreview("Low Battery Use", "Optimized performance", Color(0xFF161823), Color.White),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 5,
        name = "Facebook",
        developer = "Meta Platforms, Inc.",
        category = "Social • Networking",
        tab = StoreTab.Apps,
        rating = "4.3 ★",
        downloads = "5B+",
        reviews = "Google Play",
        ageRating = "12+",
        sizeLabel = "86 MB",
        version = "Play Store",
        color = Color(0xFF1877F2),
        iconText = "f",
        logoResId = R.drawable.ic_app_facebook,
        heroTitle = "Connect with friends on Facebook",
        promoTitle = "FACEBOOK • CONNECT WITH COMMUNITIES",
        promoText = "Share updates, explore Marketplace, watch Reels, and stay in touch.",
        about =
          "Keeping up with friends is faster and easier than ever. Share updates and photos, engage with friends and Pages, and stay connected to communities.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.facebook.katana",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "facebook",
              caption = "News Feed & Stories",
              backgroundColor = Color(0xFFF0F2F5),
              textColor = Color(0xFF1C1E21),
              topBarColor = Color(0xFF1877F2),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Stories", "Share moments with friends", Color.White, Color(0xFF1C1E21)),
                  ScreenshotRowPreview("Community Post", "Like • Comment • Share", Color.White, Color(0xFF1C1E21)),
                  ScreenshotRowPreview("Groups & Events", "Discover local meetups", Color(0xFFE7F3FF), Color(0xFF1877F2)),
                ),
            ),
            AppScreenshotPreview(
              header = "Reels & Marketplace",
              caption = "Reels & Marketplace",
              backgroundColor = Color.White,
              textColor = Color(0xFF1C1E21),
              topBarColor = Color(0xFF1877F2),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Watch Reels", "Short creative videos", Color(0xFFF0F2F5), Color(0xFF1C1E21)),
                  ScreenshotRowPreview("Marketplace", "Buy & sell nearby", Color(0xFFF0F2F5), Color(0xFF1C1E21)),
                  ScreenshotRowPreview("Notifications", "Real-time updates", Color(0xFF1877F2), Color.White),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 6,
        name = "Snapchat",
        developer = "Snap Inc",
        category = "Communication • Camera",
        tab = StoreTab.Apps,
        rating = "4.2 ★",
        downloads = "1B+",
        reviews = "Google Play",
        ageRating = "12+",
        sizeLabel = "78 MB",
        version = "Play Store",
        color = Color(0xFFFFFC00),
        iconText = "S",
        logoResId = R.drawable.ic_app_snapchat,
        heroTitle = "Share the moment with Snapchat",
        promoTitle = "SNAPCHAT • SHARE THE MOMENT",
        promoText = "Fast camera, AR Lenses, Spotlight videos, and Snap Map.",
        about =
          "Snapchat is a fast and fun way to share the moment with your friends and family using creative Lenses, Stories, and Chat.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.snapchat.android",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "Snapchat",
              caption = "Camera & AR Lenses",
              backgroundColor = Color(0xFF18181B),
              textColor = Color.White,
              topBarColor = Color(0xFFFFFC00),
              topBarTextColor = Color(0xFF111111),
              rows =
                listOf(
                  ScreenshotRowPreview("AR Filters", "Try trending face lenses", Color(0xFF27272A), Color.White),
                  ScreenshotRowPreview("Snap Camera", "Tap to capture & send", Color(0xFFFFFC00), Color(0xFF111111)),
                  ScreenshotRowPreview("Memories", "Save favorite Snaps", Color(0xFF27272A), Color.White),
                ),
            ),
            AppScreenshotPreview(
              header = "Chat & Stories",
              caption = "Streaks & Snap Map",
              backgroundColor = Color.White,
              textColor = Color(0xFF111111),
              topBarColor = Color(0xFF0FADFF),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Best Friends", "New Snap • Tap to view", Color(0xFFF4F4F5), Color(0xFF111111)),
                  ScreenshotRowPreview("Snap Map", "Explore live hotspots", Color(0xFFF4F4F5), Color(0xFF111111)),
                  ScreenshotRowPreview("Spotlight", "Fun community Snaps", Color(0xFF0FADFF), Color.White),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 7,
        name = "WhatsApp Business",
        developer = "WhatsApp LLC",
        category = "Business • Messaging",
        tab = StoreTab.Apps,
        rating = "4.4 ★",
        downloads = "1B+",
        reviews = "Google Play",
        ageRating = "16+",
        sizeLabel = "66 MB",
        version = "Play Store",
        color = Color(0xFF25D366),
        iconText = "WB",
        logoResId = R.drawable.ic_app_whatsapp_business,
        heroTitle = "Connect with customers on WhatsApp Business",
        promoTitle = "WHATSAPP BUSINESS • GROW YOUR STORE",
        promoText = "Showcase products with Catalogs, automated greetings, and quick replies.",
        about =
          "WhatsApp Business enables you to have a business presence on WhatsApp, communicate more efficiently with customers, and grow your business.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.whatsapp.w4b",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "Business Tools",
              caption = "Product Catalog",
              backgroundColor = Color.White,
              textColor = Color(0xFF111B21),
              topBarColor = Color(0xFF008069),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Catalog Manager", "Showcase items & prices", Color(0xFFE7FCE3), Color(0xFF111B21)),
                  ScreenshotRowPreview("Quick Replies", "Instant customer answers", Color(0xFFF0F2F5), Color(0xFF111B21)),
                  ScreenshotRowPreview("Business Profile", "Hours, address & website", Color(0xFF008069), Color.White),
                ),
            ),
            AppScreenshotPreview(
              header = "Customer Chats",
              caption = "Labels & Broadcasts",
              backgroundColor = Color(0xFFF8FAFC),
              textColor = Color(0xFF111B21),
              topBarColor = Color(0xFF075E54),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("New Order #104", "Label: Pending Payment", Color.White, Color(0xFF111B21)),
                  ScreenshotRowPreview("Greeting Message", "Auto-reply enabled", Color.White, Color(0xFF111B21)),
                  ScreenshotRowPreview("Order Updates", "Fast client support", Color(0xFF25D366), Color.White),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 8,
        name = "CapCut",
        developer = "Bytedance Pte. Ltd.",
        category = "Video Players & Editors",
        tab = StoreTab.Apps,
        rating = "4.4 ★",
        downloads = "1B+",
        reviews = "Google Play",
        ageRating = "12+",
        sizeLabel = "148 MB",
        version = "Play Store",
        color = Color(0xFF111111),
        iconText = "CC",
        logoResId = R.drawable.ic_app_capcut,
        heroTitle = "All-in-one video editor CapCut",
        promoTitle = "CAPCUT • ALL-IN-ONE VIDEO EDITOR",
        promoText = "Multi-track timeline, auto captions, templates, and 4K export.",
        about =
          "CapCut is a free, all-in-one video editor and graphic design app with smart templates, effects, filters, and smooth transitions.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.lemon.lvoverseas",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "New Project",
              caption = "Timeline Video Editor",
              backgroundColor = Color(0xFF121215),
              textColor = Color.White,
              topBarColor = Color(0xFF1B1B20),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("1080p • 60fps", "Split, trim & speed curve", Color(0xFF23232A), Color.White),
                  ScreenshotRowPreview("Auto Captions", "Instant styled subtitles", Color(0xFF00E5FF), Color.Black),
                  ScreenshotRowPreview("Audio & Beats", "Sync clips to music", Color(0xFF23232A), Color.White),
                ),
            ),
            AppScreenshotPreview(
              header = "Templates",
              caption = "Trending Templates",
              backgroundColor = Color.White,
              textColor = Color(0xFF111111),
              topBarColor = Color(0xFF111111),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("1-Tap Reels", "Replace clips & export", Color(0xFFF4F4F5), Color(0xFF111111)),
                  ScreenshotRowPreview("AI Effects", "Background removal & filters", Color(0xFFF4F4F5), Color(0xFF111111)),
                  ScreenshotRowPreview("Export HD", "Share directly to social", Color(0xFF111111), Color.White),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 9,
        name = "Easypaisa",
        developer = "Telenor Microfinance Bank",
        category = "Finance • Mobile Wallet",
        tab = StoreTab.Apps,
        rating = "4.3 ★",
        downloads = "50M+",
        reviews = "Google Play",
        ageRating = "3+",
        sizeLabel = "54 MB",
        version = "Play Store",
        color = Color(0xFF00A859),
        iconText = "ep",
        logoResId = R.drawable.ic_app_easypaisa,
        heroTitle = "Digital wallet & payments with Easypaisa",
        promoTitle = "EASYPAISA • PAKISTAN'S DIGITAL WALLET",
        promoText = "Send money, pay utility bills, mobile load, and Raast transfers.",
        about =
          "Easypaisa lets you send and receive money nationwide, pay utility bills, recharge mobile packages, and manage digital payments easily.",
        downloadUrl = "https://play.google.com/store/apps/details?id=pk.com.telenor.phoenix",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "easypaisa",
              caption = "Wallet & Balance",
              backgroundColor = Color(0xFFF4FBF7),
              textColor = Color(0xFF102A1D),
              topBarColor = Color(0xFF00A859),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Easypaisa Account", "Instant Raast & Bank Transfers", Color(0xFF00A859), Color.White),
                  ScreenshotRowPreview("Send Money", "Free wallet & IBAN transfers", Color.White, Color(0xFF102A1D), Color(0xFFD1FAE5)),
                  ScreenshotRowPreview("Bill Payments", "Electricity, Gas & Internet", Color.White, Color(0xFF102A1D), Color(0xFFD1FAE5)),
                ),
            ),
            AppScreenshotPreview(
              header = "Services",
              caption = "Mobile Load & QR Pay",
              backgroundColor = Color.White,
              textColor = Color(0xFF102A1D),
              topBarColor = Color(0xFF008748),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Easyload & Bundles", "All networks supported", Color(0xFFECFDF5), Color(0xFF065F46)),
                  ScreenshotRowPreview("Scan QR Code", "Pay at stores nationwide", Color(0xFFECFDF5), Color(0xFF065F46)),
                  ScreenshotRowPreview("Debit Card", "Manage card limits in-app", Color(0xFF00A859), Color.White),
                ),
            ),
          ),
      ),
      StoreApp(
        id = 10,
        name = "JazzCash",
        developer = "JazzCash",
        category = "Finance • Digital Payments",
        tab = StoreTab.Apps,
        rating = "4.2 ★",
        downloads = "50M+",
        reviews = "Google Play",
        ageRating = "3+",
        sizeLabel = "52 MB",
        version = "Play Store",
        color = Color(0xFFC8102E),
        iconText = "JC",
        logoResId = R.drawable.ic_app_jazzcash,
        heroTitle = "Fast transfers & bills with JazzCash",
        promoTitle = "JAZZCASH • FAST PAYMENTS & TRANSFERS",
        promoText = "Money transfers, mobile load, QR payments, and utility bill pay.",
        about =
          "JazzCash is Pakistan's digital wallet for instant money transfers, bank & Raast payments, mobile top-ups, and everyday bills.",
        downloadUrl = "https://play.google.com/store/apps/details?id=com.techlogix.mobilinkcustomer",
        addedAtEpochMillis = nov20Morning,
        screenshots =
          listOf(
            AppScreenshotPreview(
              header = "JazzCash",
              caption = "Account & Transfers",
              backgroundColor = Color(0xFFFFF8F8),
              textColor = Color(0xFF221114),
              topBarColor = Color(0xFFC8102E),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Mobile Account", "Send money to any bank or CNIC", Color(0xFFC8102E), Color.White),
                  ScreenshotRowPreview("Raast ID", "Zero-fee instant transfers", Color.White, Color(0xFF221114), Color(0xFFFECDD3)),
                  ScreenshotRowPreview("Pay Bills", "Electricity, Water & Challans", Color(0xFFFDB913), Color(0xFF111111)),
                ),
            ),
            AppScreenshotPreview(
              header = "Quick Actions",
              caption = "QR Scan & Mobile Load",
              backgroundColor = Color.White,
              textColor = Color(0xFF221114),
              topBarColor = Color(0xFF8E0B20),
              topBarTextColor = Color.White,
              rows =
                listOf(
                  ScreenshotRowPreview("Mobile Load", "Prepaid & Postpaid bundles", Color(0xFFFEF2F2), Color(0xFF991B1B)),
                  ScreenshotRowPreview("QR Payments", "Scan & pay seamlessly", Color(0xFFFEF2F2), Color(0xFF991B1B)),
                  ScreenshotRowPreview("JazzCash Debit Card", "Online & ATM access", Color(0xFFC8102E), Color.White),
                ),
            ),
          ),
      ),
    ) + GoogleAppsCatalog.getApps(utcMillis(2026, Calendar.NOVEMBER, 10, 10, 0))
  }
}
