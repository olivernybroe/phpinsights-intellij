package com.phpinsights.phpinsights;

import org.jetbrains.annotations.NonNls;

public enum PhpInsights {
    TOOL_NAME {
        @Override
        public String toString() {
            return PhpInsightsBundle.message("TOOL_NAME");
        }
    },

    DISPLAY_NAME {
        @Override
        public String toString() {
            return TOOL_NAME.toString();
        }
    },
    COMPOSER_NAME {
        @NonNls
        @Override
        public String toString() {
            return "nunomaduro/phpinsights";
        }
    },
    NOTIFICATION_GROUP_DISPLAY_ID {
        @NonNls
        @Override
        public String toString() {
            return "PHP External Quality Tools";
        }
    }
}
