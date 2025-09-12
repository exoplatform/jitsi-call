#!/bin/bash
# This script changes default Jitsi logo to eXo logo and makes it not clickable.
# NOTE: requires env variables: CALL_APP_URL and CONFIG_FOLDER
# Optional: EXO_URL, the eXo base URL (e.g. https://exo.example.com). When set, the logo is the platform's
# branding logo, so an administrator's logo change shows in calls; otherwise the call app's static logo.
if [ -n "$EXO_URL" ]; then
  LOGO_URL="${EXO_URL%/}/portal/rest/v1/platform/branding/logo"
else
  LOGO_URL="$CALL_APP_URL/images/logo.png"
fi
echo "interfaceConfig['DEFAULT_LOGO_URL'] = '$LOGO_URL';" >> "$CONFIG_FOLDER/web/interface_config.js"
echo "interfaceConfig['JITSI_WATERMARK_LINK'] = '';" >> "$CONFIG_FOLDER/web/interface_config.js"
