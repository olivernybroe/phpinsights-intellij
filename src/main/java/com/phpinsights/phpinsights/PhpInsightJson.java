package com.phpinsights.phpinsights;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhpInsightJson {
    public Summary summary;

    @SerializedName("Code")
    public List<Insight> code;

    @SerializedName("Complexity")
    public List<Insight> complexity;

    @SerializedName("Architecture")
    public List<Insight> architecture;

    @SerializedName("Style")
    public List<Insight> style;

    @SerializedName("Security")
    public List<Insight> security;
}

class Summary {
    public double code;
    public double complexity;
    public double architecture;
    public double style;
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
