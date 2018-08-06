# Hyperion-Core
The core Hyperion plugin. Required by all other plugins.

## Customization
To override the message that appears in the persistent notification, simply override the XML resource:
```xml
<string name="hype_notification_content_title">Custom title here</string>
<string name="hype_notification_content_text">Custom message here</string>
<string name="hype_notification_subtext">Tap to open the debug menu</string>
```
