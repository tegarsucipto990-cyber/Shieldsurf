# ShieldSurf — Android Privacy Browser MVP

A starter Android browser using Android System WebView. Features: URL/search bar, back/forward/home/reload, local bookmarks, a basic domain-based ad/tracker blocklist, blocked-request counter, and a dialog to clear cookies/cache/site data.

## Build with GitHub Actions
1. Create a new GitHub repository (for example `ShieldSurf`).
2. Upload the CONTENTS of this folder to the repository root.
3. Open Actions and run **Build ShieldSurf APK** (or push to `main`).
4. Download the `ShieldSurf-debug-apk` artifact from the completed workflow and extract `app-debug.apk`.

## Important privacy limitations
This is an MVP, not a hardened privacy browser. The bundled blocklist is intentionally small and is not equivalent to uBlock Origin, Brave Shields, EasyList, or EasyPrivacy. Some ads/trackers will not be blocked, and some sites may break when blocking is enabled. A website, ISP, or network operator may still observe browsing activity. The app does not contain API keys. For public release, add maintained filter lists, tests, tab management, a privacy policy, and a security review.
