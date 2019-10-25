package com.phpinsights.phpinsights;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NonNls;

import java.util.List;

public class PhpInsightJson {
    public Summary summary;

    @NonNls
    @SerializedName("Code")
    public List<Insight> code;

    @NonNls
    @SerializedName("Complexity")
    public List<Insight> complexity;

    @NonNls
    @SerializedName("Architecture")
    public List<Insight> architecture;

    @NonNls
    @SerializedName("Style")
    public List<Insight> style;

    @NonNls
    @SerializedName("Security")
    public List<Insight> security;
}

class Summary {
    public double code;
    public double complexity;
    public double architecture;
    public double style;
    @NonNls
    @SerializedName("security issues")
    public int securityIssues;
}

class Insight {
    public String title;
    public String insightClass;
    public String file;
    public int line;
    public String message;
}
