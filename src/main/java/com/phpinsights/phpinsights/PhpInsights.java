package com.phpinsights.phpinsights;

public enum PhpInsights {
    TOOL_NAME {
        @Override
        public String toString() {
            return "PHP Insights";
        }
    },

    DISPLAY_NAME {
        @Override
        public String toString() {
            return TOOL_NAME.toString();
        }
    },

    HELP_TOPIC {
        @Override
        public String toString() {
            return "settings.phpinsights";
        }
    },

    LAUNCHER_NAME {
        @Override
        public String toString() {
            return "phpinsights";
        }
    }
}
