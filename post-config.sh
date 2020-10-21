#!/bin/bash
# This script changes default Jitsi logo to eXo logo and makes it not clickable.
# NOTE: requires env variables: CALL_APP_URL and CONFIG_FOLDER
echo "interfaceConfig.DEFAULT_LOGO_URL: '$CALL_APP_URL/images/logo.png'" >> "$CONFIG_FOLDER/web/interface_config.js"
echo "interfaceConfig.JITSI_WATERMARK_LINK: ''" >> "$CONFIG_FOLDER/web/interface_config.js"
